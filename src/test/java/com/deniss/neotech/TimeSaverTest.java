package com.deniss.neotech;

import com.deniss.neotech.db.HibernateUtil;
import com.deniss.neotech.db.StoredTime;
import mockit.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.powermock.api.mockito.PowerMockito;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyObject;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


/**************************************************************************
 *An important class need to be tested
 * -
 *
 *************************************************************************/
public class TimeSaverTest {

    @Tested
    private TimeSaver timeSaver;

    @Injectable
    private Director director;


    @Mocked
    private Director director2;

    @Mocked
    private Session session;

    @Mocked
    SessionFactory sessionFactory;

    @Mocked
    Transaction transaction;

    @Mocked
    HibernateUtil hybernateUtil;




    @Test public void testCreate_notException() throws Exception {
        final TimeSaver[] t = new TimeSaver[1];
        new Verifications() {
            {
                t[0] = TimeSaver.create(director);
                assertNotNull(t[0]);
            }
        };

    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCreate_Exception() throws Exception {
        final TimeSaver[] t = new TimeSaver[1];
        t[0] = TimeSaver.create(null);

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



    @Test public void testSaveSuccessNoException() throws Exception {

        new Expectations() {
            {
                hybernateUtil.getSessionFactory().openSession();
                result = session;
            }
        };

            TimeSaver t =TimeSaver.create(director);
            List<StoredTime> lst =new ArrayList<StoredTime>();
            lst.add(new StoredTime());
            lst.add(new StoredTime());
            assertEquals(t.saveData(lst),true);
    }

    @Test public void testSaveFailNoException() throws Exception {

        new Expectations() {
            {
                hybernateUtil.getSessionFactory().openSession();
                result = session;
            }
        };

        TimeSaver t =TimeSaver.create(director);

        assertEquals(t.saveData(null),false);
    }


}
