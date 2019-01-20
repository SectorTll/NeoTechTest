package com.deniss.neotech;

import com.deniss.neotech.db.StoredTime;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Class with shared information between TimeSaver classes and TimeProducer,
 * Contains main queue with storedTime data,
 * And consumers queue intended to store consumer instances ready for pull data from main queue
 */
class Director {
    private final BlockingQueue<StoredTime> queue = new ArrayBlockingQueue<StoredTime>(5, true);
    private final BlockingQueue<TimeSaver> consumers = new ArrayBlockingQueue<TimeSaver>(3, true);
    private final Logger logger = Logger.getLogger(Director.class);

    boolean isQueueFull() {
        return queue.remainingCapacity() == 0;
    }

    void put(StoredTime storedTime) throws InterruptedException {
        queue.put(storedTime);
    }


    /**
     * Blockin realization drainTo methods, wait for numElements, if queue is empty
     * @param numElements size of elements should be returned
     * @param timeoutInMs timeout in milleseconds
     * @return return list of storedTime
     * @throws InterruptedException
     */
    synchronized List<StoredTime> drainTo(int numElements, long timeoutInMs) throws InterruptedException {
        List<StoredTime> lst =new ArrayList<StoredTime>();

        long timeout = System.currentTimeMillis() + timeoutInMs;
        int added = 0;
        while (added < numElements) {

            added += queue.drainTo(lst, numElements - added);
            if (added < numElements) {
                StoredTime storedTime = queue.poll(timeout - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
                if (storedTime == null) {
                    break;
                }
                lst.add(storedTime);
                added++;
            }
        }
        return lst;
    }

    void put(TimeSaver timeSaver) throws InterruptedException {
        consumers.put(timeSaver);
    }

    void take(TimeSaver timeSaver) {
        if (!consumers.remove(timeSaver)) {
            logger.error("Unable to remove TimeSaver form consumer queue");
        }
    }

    int size() {
        return queue.size();
    }
}
