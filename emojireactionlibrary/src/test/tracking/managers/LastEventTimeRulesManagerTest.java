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

package amplify.tracking.managers;

import amplify.helpers.BaseTest;
import amplify.helpers.FakeSettings;
import com.example.amplify.lib.tracking.interfaces.IEvent;
import com.example.amplify.lib.tracking.interfaces.IEventBasedRule;
import com.example.amplify.lib.tracking.managers.LastEventTimeRulesManager;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.mockito.Mock;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * LastEventTimeRulesManagerTest extends BaseTest
 */

public class LastEventTimeRulesManagerTest extends BaseTest {

    private LastEventTimeRulesManager lastEventTimeRulesManager;

    private FakeSettings<Long> fakeSettings;

    @Mock
    private IEvent mockEvent;
    @Mock
    private IEventBasedRule<Long> mockEventBasedRule;

    @Override
    public void localSetUp() {
        fakeSettings = new FakeSettings<>();

        lastEventTimeRulesManager = new LastEventTimeRulesManager(fakeSettings);

        when(mockEvent.getTrackingKey()).thenReturn(DEFAULT_MOCK_EVENT_TRACKING_KEY);
        lastEventTimeRulesManager.addEventBasedRule(mockEvent, mockEventBasedRule);
    }

    @Test
    public void testThatEventsAreSavedWithCorrectTrackingKey() {

        final String expectedTrackingKey = getExpectedTrackingKeyForEvent(mockEvent);
        assert fakeSettings.readTrackingValue(expectedTrackingKey) == null;

        lastEventTimeRulesManager.notifyEventTriggered(mockEvent);

        final Long trackedEventTime = fakeSettings.readTrackingValue(expectedTrackingKey);

        assertNotNull(
                "The event time should have been saved using the correct tracking key",
                trackedEventTime);
    }

    @Test
    public void testThatFirstEventTimeIsRecorded() {

        final long fakeEventTime = MARCH_18_2014_838PM_UTC;


        triggerEventAtTime(mockEvent, fakeEventTime);

        final Long trackedEventTime
                = fakeSettings.readTrackingValue(getExpectedTrackingKeyForEvent(mockEvent));

        assertEquals(
                "The correct time should have been recorded for this event",
                Long.valueOf(fakeEventTime),
                trackedEventTime);
    }

    @Test
    public void testThatSecondEventTimeIsRecorded() {

        final long fakeEventTimeEarlier = MARCH_18_2014_838PM_UTC;
        final long fakeEventTimeLater = fakeEventTimeEarlier + TimeUnit.DAYS.toMillis(1);
        assert fakeEventTimeEarlier < fakeEventTimeLater;

        triggerEventAtTime(mockEvent, fakeEventTimeEarlier);
        triggerEventAtTime(mockEvent, fakeEventTimeLater);

        final Long trackedEventTime
                = fakeSettings.readTrackingValue(getExpectedTrackingKeyForEvent(mockEvent));

        assertEquals(
                "The correct (latest) time should have been recorded for this event",
                Long.valueOf(fakeEventTimeLater),
                trackedEventTime);
    }

    private String getExpectedTrackingKeyForEvent(@NotNull final IEvent event) {
        return "AMPLIFY_" + event.getTrackingKey() + "_LAST_TIME";
    }

    private void triggerEventAtTime(@NotNull final IEvent event, final long time) {
        setFakeCurrentTimeMillis(time);
        lastEventTimeRulesManager.notifyEventTriggered(event);
    }

}
