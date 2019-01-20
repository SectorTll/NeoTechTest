package com.deniss.neotech;

import com.deniss.neotech.db.StoredTime;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.TimerTask;

import static java.util.Calendar.getInstance;



/**
 * Producer class, should populate StoredTime data to queue.
 */
class TimeProducer extends TimerTask {

    private final Logger logger = Logger.getLogger(TimeProducer.class);

    private Director director;

    /**
     * Constructor with shared data  between producer and consumers
     *
     * @param director contains
     */
    TimeProducer(Director director) {
        if (director == null) {
            throw new IllegalArgumentException("Director could not be null");
        }
        this.director = director;
    }

    /**
     * Constructor with shared data  between producer and consumers
     *
     *
     */
    @Override public void run() {
        try {
            StoredTime storedTime = new StoredTime();
            storedTime.setTheTime(new Timestamp(getInstance().getTime().getTime()));
            if (director.isQueueFull()) {
                logger.error("Queue overflow, buffer is full: " + storedTime.getTheTime() + "will be lost"); //Test task: the maximum base non-operation time of DB is five seconds. we did everything we could, so drop data :(
            } else {
                director.put(storedTime);
                logger.debug("Time added to queue:  " + storedTime.getTheTime());
            }

        } catch (InterruptedException e) {
            logger.error("Unexpected exception during stored task populating ", e);
            Thread.currentThread().interrupt();
        }
    }
}

