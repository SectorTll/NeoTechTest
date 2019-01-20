package com.deniss.neotech;

import com.deniss.neotech.db.HibernateUtil;
import com.deniss.neotech.db.StoredTime;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Class for saving data to database, as possible solution for slow database, every single TimeSaver is responsible
 * for atomic single save operation. Overhead expenses of opening/saving e.t.c db operation should be shared between multiple TimeSavers.
 * Instead of one class which responsible for DB operation, timesaver economy time for pre-processing operation with db if needed.
 */
class TimeSaver implements Callable<Boolean> {
    private Director director = null;
    private final static int TIMEOUT = 5000;
    private final Logger logger = Logger.getLogger(TimeSaver.class);
    private List<StoredTime> localCache ;
    boolean result;

    TimeSaver(Director director) {
        this.director = director;
    }

    /**
     * Directly create instance with constructor is not good, because due register TimeSaver in queue
     */
    static TimeSaver create(Director director) throws InterruptedException {
        if (director == null)
            throw new IllegalArgumentException("Queue could not be null");//TODO: We could use an annotation Contract here
        TimeSaver result = new TimeSaver(director);
        result.register();
        return result;
    }

    public Boolean call() throws InterruptedException {

        result = processQueue();
            if (!result)
                for (StoredTime storedTime:localCache) {
                    logger.warn("Following data lost: " + storedTime.getTheTime());
                }
        return result;
    }

    /**
     * Idea of method to take all data in queue and try to save it as bunch data for cases where db save process is slow enough
     *
     * @return true if save successful
     * @throws InterruptedException
     */
    private synchronized boolean processQueue() throws InterruptedException {
        boolean saveResult;
        try {
            localCache = director.drainTo(5, TIMEOUT);
            long startTime = System.currentTimeMillis();
            do {
                saveResult = saveData(localCache);
                if (System.currentTimeMillis() - startTime > TIMEOUT) {
                    logger.warn("Canceled due timeout, status: " + saveResult);

                    break;
                }
                TimeUnit.MILLISECONDS.sleep(TIMEOUT/10);
            } while (!saveResult);

        } catch (InterruptedException e) {
            logger.error("Unexpected exception during take StoredTime");
            throw e;
        } finally {
            unregister();
        }
        return saveResult;
    }

     boolean saveData(List<StoredTime> localCache) {
        Transaction transaction = null;

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            for (StoredTime storedTime : localCache) {
                session.save(storedTime);
            }
            session.getTransaction().commit();

        } catch (Exception e) {
            logger.warn("Unable to save StoredTimes to DB. Elements count: " + localCache.size(), e);
            if (transaction != null) {
                transaction.rollback();
            }

            return false;
        }
        return true;
    }

    private void unregister() {
        director.take(this);
        logger.debug("unregistred");
    }

    private void register() throws InterruptedException {
        director.put(this);
    }

}
