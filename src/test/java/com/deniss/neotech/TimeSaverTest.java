package com.deniss.neotech;

import com.deniss.neotech.db.HibernateUtil;
import com.deniss.neotech.db.StoredTime;
import mockit.*;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.engine.transaction.spi.LocalStatus;
import org.powermock.api.mockito.PowerMockito;
import org.testng.annotations.Test;


import javax.transaction.Synchronization;

import java.lang.reflect.Method;

import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.testng.Assert.*;

public class TimeSaverTest {

    @Tested
    private TimeSaver timeSaver;

    @Injectable
    private Director director;

    @Mocked
    private Session session;

    @Mocked
    SessionFactory sessionFactory;

    @Mocked
    Transaction transaction;



    @Test public void testCreate_notException() throws Exception {
        final TimeSaver[] t = new TimeSaver[1];
        new Expectations() {
            {
                t[0] = TimeSaver.create(director);
            }
        };

    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCreate_Exception() throws Exception {
        final TimeSaver[] t = new TimeSaver[1];
        new Expectations() {
            {
                t[0] = TimeSaver.create(null);
                result = new Exception();
            }
        };

    }

    @Test public void testCall() throws Exception {
        TimeSaver t =TimeSaver.create(director);
        t = PowerMockito.spy(t);
        doReturn(true).when(t, "processQueue");
        boolean result = t.call();
        assertEquals(result,true);
    }

    @Test public void testProcessQueue() throws Exception {

        TimeSaver t =TimeSaver.create(director);
        t = PowerMockito.spy(t);
        doReturn(true).when(t, "saveData", anyObject());
        boolean result = t.call();
        assertEquals(result,true);
    }

}
