package com.deniss.neotech;

import com.deniss.neotech.db.StoredTime;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;
// CHECKSTYLE:OFF


/**
 * ClAnother important class for tests, but tests are direct and simply to involve any kind of mocks
 * Contains main queue with storedTime data,
 * And consumers queue intended to store consumer instances ready for pull data from main queue
 */
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

    @Test
    public void testDrainTo_takeTwo_NoException2() throws Exception {
        final Director director = new Director();

        Runnable runnable = new Runnable() {
            public void run() {

                for (int i=0; i<5; i++) {

                    List<StoredTime> result;
                    try {
                        result = director.drainTo(2, 1000);
                        assertEquals(result.size(), 2);
                        System.out.println("drain 2");
                    } catch (InterruptedException e) {

                    }
                }

            }
        };
        Thread t = new Thread(runnable);
        t.start();


        for (int i=0; i<10; i++)
        {
            director.put(new StoredTime());
            System.out.println("put 1");
        }

        Thread.sleep(1000); //to be sure for last drain

        List<StoredTime> result = director.drainTo(2,1000);
        Assert.assertEquals(result.size(),0);
    }


}
