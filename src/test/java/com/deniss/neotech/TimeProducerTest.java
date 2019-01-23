package com.deniss.neotech;


import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Timer;
import java.util.concurrent.TimeUnit;


public class TimeProducerTest {


    @Test
    public void test_ShouldPass() throws InterruptedException {

        Director director = new Director();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimeProducer(director), 0, 1000);
        TimeUnit.MILLISECONDS.sleep(2500);
        Assert.assertEquals(director.size(),3);

    }
}
