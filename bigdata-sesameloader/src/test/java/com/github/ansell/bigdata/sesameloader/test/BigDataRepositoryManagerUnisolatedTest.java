/**
 * 
 */
package com.github.ansell.bigdata.sesameloader.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.openrdf.repository.RepositoryException;

import com.github.ansell.bigdata.sesameloader.BigDataRepositoryManagerUnisolated;

/**
 * @author Peter Ansell p_ansell@yahoo.com
 *
 */
public class BigDataRepositoryManagerUnisolatedTest
{
    
    /**
     * JUnit creates this temporary folder before each test and cleans up after each test.
     * 
     * All of the test files are located inside of subfolders within folder, so they should all be
     * cleaned up by JUnit.
     */
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    private File repositoryFolder;
    
    private File testDataFolder;
    
    private File testDataFileRdf;
    
    private File testDataFileN3;

    private File repositoryFile;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        // create a temporary folder for our test data, which is populated based on resources in the
        // test jar file
        this.testDataFolder = this.folder.newFolder();
        
        // create a randomly named temporary file in N3 format
        this.testDataFileN3 = File.createTempFile("bigdataloadtest-1-", ".n3", this.testDataFolder);
        final FileOutputStream testOutputStreamN3 = new FileOutputStream(this.testDataFileN3);
        final InputStream testResource2 = this.getClass().getResourceAsStream("bigdataloadtest-1.n3");
        
        Assert.assertNotNull("Test resource not found", testResource2);
        
        IOUtils.copy(testResource2, testOutputStreamN3);
        
        // create a separate folder for the repository data
        this.repositoryFolder = this.folder.newFolder();
        
        this.repositoryFile = File.createTempFile("bigdata-", ".jnl", this.repositoryFolder);
        
    }
    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }
    
    /**
     * Test method for {@link com.github.ansell.bigdata.sesameloader.BigDataRepositoryManagerUnisolated#BigDataRepositoryManagerUnisolated(java.io.File)}.
     */
    @Test
    public void testBigDataRepositoryManagerUnisolatedFile()
    {
        fail("Not yet implemented"); // TODO
    }
    
    /**
     * Test method for {@link com.github.ansell.bigdata.sesameloader.BigDataRepositoryManagerUnisolated#BigDataRepositoryManagerUnisolated(java.io.File, java.util.Properties)}.
     */
    @Test
    public void testBigDataRepositoryManagerUnisolatedFileProperties()
    {
        fail("Not yet implemented"); // TODO
    }
    
    /**
     * Test method for {@link com.github.ansell.bigdata.sesameloader.BigDataRepositoryManagerUnisolated#getConnection()}.
     */
    @Test
    public void testGetConnection()
    {
        fail("Not yet implemented"); // TODO
    }
    
    /**
     * Test method for {@link com.github.ansell.bigdata.sesameloader.BigDataRepositoryManagerUnisolated#shutDown()}.
     */
    @Test
    public void testShutDown()
    {
        fail("Not yet implemented"); // TODO
    }
    
    /**
     * Test method for {@link com.github.ansell.bigdata.sesameloader.BigDataRepositoryManagerUnisolated#getValueFactory()}.
     */
    @Test
    public void testGetValueFactory()
    {
        fail("Not yet implemented"); // TODO
    }
    
    /**
     * Test method for {@link com.github.ansell.bigdata.sesameloader.BigDataRepositoryManagerUnisolated#getMaximumThreads()}.
     * @throws IOException 
     * @throws RepositoryException 
     */
    @Test
    public void testGetMaximumThreads() throws RepositoryException, IOException
    {
        BigDataRepositoryManagerUnisolated repoManager = new BigDataRepositoryManagerUnisolated(repositoryFile, "fastload.properties");
        
        Assert.assertEquals(new Integer(1), repoManager.getMaximumThreads());
    }
    
}
