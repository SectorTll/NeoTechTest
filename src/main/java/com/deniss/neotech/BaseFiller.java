package com.deniss.neotech;



import org.apache.log4j.Logger;

import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


/**
 * Main class for inserting new time stamp in db every second
 */
class BaseFiller {

    private final Logger logger = Logger.getLogger(BaseFiller.class);
    private final Director director = new Director();
    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private final Timer timer = new Timer();


    BaseFiller() {
    }


    /**
     * Start the endless process of filling the database, create one TimeProducer, which add to queue time every seconds,
     *
     */
    void populate() {
        timer.scheduleAtFixedRate(new TimeProducer(director), 0, 1000);

        do {
            FutureTask<Boolean> futureTask = createNextFutureTask(director);
            if (futureTask != null) {
                threadPool.execute(futureTask);
                logger.debug("Added new TimeConsumer");
            }
        } while (true);//TODO:Cancellation flag needed.
    }

    private FutureTask<Boolean> createNextFutureTask(Director director) {
        final TimeSaver callable;
        try {
            callable = TimeSaver.create(director);
        } catch (Exception e) {
            logger.error("Exception during creation of next task", e);
            return null;
        }
        return new FutureTask<Boolean>(callable)
        {
            @Override
            protected void done(){
                logger.info("TimeSaver is finished status: " + callable.result );

            }
        };

    }
}
