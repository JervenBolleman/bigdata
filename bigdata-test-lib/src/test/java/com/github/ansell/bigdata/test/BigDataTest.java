/**
 * 
 */
package com.github.ansell.bigdata.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bigdata.rdf.sail.BigdataSail;
import com.bigdata.rdf.sail.BigdataSailRepository;

/**
 * @author Peter Ansell p_ansell@yahoo.com
 *
 */
public class BigDataTest
{
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        BigdataSail sail = new BigdataSail();
        
        BigdataSailRepository testRepo = new BigdataSailRepository(sail);
    }
    
    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }
    
    @Test
    public void test()
    {
    }
    
}
