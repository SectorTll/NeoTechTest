package com.deniss.neotech;

import org.junit.runner.RunWith;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.concurrent.FutureTask;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.testng.Assert.*;
// CHECKSTYLE:OFF


/**************************************************************************
 *
 *
 * Date Created:
 *
 * ------------------------------------------------------------------------
 *
 *************************************************************************/
// CHECKSTYLE:ON

@RunWith(PowerMockRunner.class)
@PrepareForTest({ TimeSaver.class })
public class BaseFillerTest {





    @Test public void testPopulate() throws Exception {

    }

    @Test
    public void createNextFutureTask_noDirector_NoException() throws Exception {
        BaseFiller baseFiller = new BaseFiller();

        TimeSaver spy = PowerMockito.spy(new TimeSaver(null));

        when(spy, method(TimeSaver.class, "create", Director.class))
                .withArguments(null)
                .thenReturn(new TimeSaver(null));




        baseFiller.populate();
    }

}
