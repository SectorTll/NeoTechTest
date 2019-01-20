package com.deniss.neotech;

import com.deniss.neotech.db.StoredTime;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;
// CHECKSTYLE:OFF


/**************************************************************************
 *
 *
 * ------------------------------------------------------------------------
 *
 *************************************************************************/
// CHECKSTYLE:ON


public class DirectorTest {
    @Test
    public void testDrainTo_takeAll_NoException() throws Exception {
        Director director = new Director();
        director.put(new StoredTime());
        director.put(new StoredTime());
        director.put(new StoredTime());
        List<StoredTime> result = director.drainTo(4,1000);
        Assert.assertEquals(result.size(),3);
    }

    @Test
    public void testDrainTo_takeTwo_NoException() throws Exception {
        Director director = new Director();
        director.put(new StoredTime());
        director.put(new StoredTime());
        director.put(new StoredTime());
        List<StoredTime> result = director.drainTo(2,1000);
        Assert.assertEquals(result.size(),2);
    }



}
