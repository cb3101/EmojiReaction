/*
 * Copyright 2015 Stuart Kent
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package amplify.tracking;

import amplify.helpers.BaseTest;
import com.example.amplify.lib.tracking.AmplifyExceptionHandler;
import com.example.amplify.lib.tracking.interfaces.IAppLevelEventRulesManager;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;

/**
 * AmplifyExceptionHandlerTest extends BaseTest
 */

public class AmplifyExceptionHandlerTest extends BaseTest {

    @Mock
    private IAppLevelEventRulesManager mockAppLevelEventRulesManager;
    @Mock
    private Thread.UncaughtExceptionHandler mockDefaultExceptionHandler;

    @Captor
    private ArgumentCaptor<Throwable> throwableCaptor;

    @Test
    public void testThatDefaultExceptionHandlerIsAlwaysCalled() {
        // Arrange
        final AmplifyExceptionHandler amplifyExceptionHandler
                = new AmplifyExceptionHandler(mockAppLevelEventRulesManager, mockDefaultExceptionHandler);

        final Throwable expectedThrowable = new Throwable("any string");

        // Act
        amplifyExceptionHandler.uncaughtException(new Thread(), expectedThrowable);

        // Assert
        verify(mockDefaultExceptionHandler, times(1))
                .uncaughtException(any(Thread.class), throwableCaptor.capture());

        final Throwable actualThrowable = throwableCaptor.getValue();

        assertEquals(
                "The original throwable should have been passed to the default exception handler",
                expectedThrowable,
                actualThrowable);
    }

}
