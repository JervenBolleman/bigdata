/**
 * 
 */
package com.github.ansell.bigdata.sesameloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.sail.SailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bigdata.rdf.sail.BigdataSail;
import com.bigdata.rdf.sail.BigdataSail.BigdataSailConnection;
import com.bigdata.rdf.sail.BigdataSailRepository;
import com.bigdata.rdf.sail.BigdataSailRepositoryConnection;
import com.github.sesameloader.RepositoryManager;

/**
 * @author Peter Ansell p_ansell@yahoo.com
 * 
 */
public class BigDataRepositoryManagerUnisolated implements RepositoryManager
{
    private final Logger log = LoggerFactory.getLogger(BigDataRepositoryManagerUnisolated.class);
    
    private BigdataSailRepository repository;

    private BigdataSailRepositoryConnection unisolatedConnection;
    
    /**
     * Load a Properties object from a file.
     * 
     * @param resource
     * @return
     * @throws IOException
     */
    private static Properties loadProperties(String resource) throws IOException
    {
        Properties p = new Properties();
        InputStream is = BigDataRepositoryManagerUnisolated.class.getResourceAsStream(resource);
        p.load(new InputStreamReader(new BufferedInputStream(is), Charset.forName("UTF-8")));
        return p;
    }
    
    public BigDataRepositoryManagerUnisolated(String propertiesResourceLocation) throws RepositoryException, IOException
    {
        this(loadProperties(propertiesResourceLocation));
    }
    
    public BigDataRepositoryManagerUnisolated(Properties properties) throws RepositoryException, IOException
    {
        this(null, properties);
    }
    
    /**
     * @throws RepositoryException
     * @throws IOException
     * 
     */
    public BigDataRepositoryManagerUnisolated(File dataLocation, String propertiesResourceLocation) throws RepositoryException, IOException
    {
        this(dataLocation, loadProperties(propertiesResourceLocation));
    }
    
    public BigDataRepositoryManagerUnisolated(File dataLocation, Properties properties)
        throws RepositoryException, IOException
    {
        if(properties == null)
        {
            // default to using fastload.properties if none is specified before here
            properties = loadProperties("fastload.properties");
        }
        
        // if the file property was not set in the properties file, then use the one that was given
        if(properties.getProperty(BigdataSail.Options.FILE) == null)
        {
            if(dataLocation == null)
            {
                throw new RuntimeException("Could not determine the file location for the bigdata repository");
            }
            
            properties.setProperty(BigdataSail.Options.FILE, dataLocation.getAbsolutePath());
        }
        
        BigdataSail sail = new BigdataSail(properties);
        
        repository = new BigdataSailRepository(sail);
        repository.initialize();
    }
    
    @Override
    public RepositoryConnection getConnection() throws RepositoryException
    {
        if(unisolatedConnection == null)
        {
            synchronized(this)
            {
                if(unisolatedConnection == null)
                {
                    unisolatedConnection = repository.getUnisolatedConnection();
                    
                    unisolatedConnection.setAutoCommit(false);
                }
            }
        }
        
        return unisolatedConnection;
    }
    
    @Override
    public void shutDown() throws SailException, RepositoryException
    {
        log.info("Computing closure on bigdata repository before shutting down");
        
        BigdataSailConnection sailCxn = null;
        BigdataSailRepositoryConnection repoCxn = null;
        try
        {
            // by default we are in fastmode, so we need to compute closure before shutting down
            repoCxn = (BigdataSailRepositoryConnection)getConnection();
            sailCxn = (BigdataSailConnection)repoCxn.getSailConnection();
            sailCxn.computeClosure();
            sailCxn.getTripleStore().commit();
            
        }
        finally
        {
            if(sailCxn != null)
            {
                try
                {
                    sailCxn.close();
                }
                catch(SailException sx)
                {
                    log.error("Found SailException while trying to shutdown repository", sx);
                }
            }
            if(repoCxn != null)
            {
                try
                {
                    repoCxn.close();
                }
                catch(RepositoryException rx)
                {
                    log.error("Found RepositoryException while trying to shutdown repository", rx);
                }
            }

            try
            {
                repository.shutDown();
            }
            finally
            {
                log.info("Shutdown complete");
            }
        }
    }
    
    @Override
    public ValueFactory getValueFactory()
    {
        return repository.getValueFactory();
    }
    
    @Override
    public Integer getMaximumThreads()
    {
        // getUnisolatedConnection only supports a single connection
        return 1;
    }
    
}
