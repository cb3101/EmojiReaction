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
import com.example.amplify.lib.tracking.managers.TotalEventCountRulesManager;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * TotalEventCountRulesManagerTest extends BaseTest
 */
public class TotalEventCountRulesManagerTest extends BaseTest {

    private TotalEventCountRulesManager totalEventCountRulesManager;

    private FakeSettings<Integer> fakeSettings;

    @Mock
    private IEvent mockEvent;
    @Mock
    private IEventBasedRule<Integer> mockRule;

    @Override
    public void localSetUp() {
        fakeSettings = new FakeSettings<>();

        totalEventCountRulesManager = new TotalEventCountRulesManager(fakeSettings);

        when(mockEvent.getTrackingKey()).thenReturn(DEFAULT_MOCK_EVENT_TRACKING_KEY);
        totalEventCountRulesManager.addEventBasedRule(mockEvent, mockRule);
    }

    @Test
    public void testThatEventsAreSavedWithCorrectTrackingKey() {

        final String expectedTrackingKey = getExpectedTrackingKeyForEvent(mockEvent);
        assert fakeSettings.readTrackingValue(expectedTrackingKey) == null;


        totalEventCountRulesManager.notifyEventTriggered(mockEvent);

        final Integer trackedTotalEventCount = fakeSettings.readTrackingValue(expectedTrackingKey);

        assertNotNull(
                "The total event count should have been saved using the correct tracking key",
                trackedTotalEventCount);
    }


    @Test
    public void testThatCorrectNumberOfEventsIsRecorded() {

        totalEventCountRulesManager.addEventBasedRule(mockEvent, mockRule);

        final Integer expectedEventCount = 7;
        assert expectedEventCount > 0;

        for (int i = 0; i < expectedEventCount; i++) {
            totalEventCountRulesManager.notifyEventTriggered(mockEvent);
        }


        final Integer actualEventCount
                = fakeSettings.readTrackingValue(getExpectedTrackingKeyForEvent(mockEvent));

        assertEquals("The correct number of events should have been recorded", expectedEventCount,
                actualEventCount);
    }

    private String getExpectedTrackingKeyForEvent(@NotNull final IEvent event) {
        return "AMPLIFY_" + event.getTrackingKey() + "_TOTAL_COUNT";
    }

}
