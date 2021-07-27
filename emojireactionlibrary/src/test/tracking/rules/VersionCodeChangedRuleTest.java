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

package amplify.tracking.rules;

import amplify.helpers.BaseTest;
import com.example.amplify.lib.tracking.rules.VersionCodeChangedRule;
import ohos.rpc.RemoteException;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * VersionCodeChangedRuleTest extends BaseTest
 */
public class VersionCodeChangedRuleTest extends BaseTest {

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatRuleAllowsPromptIfEventHasNeverOccurred() throws RemoteException {

        final int fakeCurrentVersionCode = 42;
        final VersionCodeChangedRule versionCodeChangedRule = new VersionCodeChangedRule(fakeCurrentVersionCode);


        final boolean ruleShouldAllowFeedbackPrompt = versionCodeChangedRule.shouldAllowFeedbackPromptByDefault();


        assertTrue(
                "Feedback prompt should be allowed if the associated event has never occurred.",
                ruleShouldAllowFeedbackPrompt);
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Test
    public void testThatRuleBlocksPromptIfAppVersionCodeHasNotChanged() {

        final int fakeCachedVersionCode = 42;
        final int fakeCurrentVersionCode = fakeCachedVersionCode;
        final VersionCodeChangedRule versionCodeChangedRule = new VersionCodeChangedRule(fakeCurrentVersionCode);


        final boolean ruleShouldAllowFeedbackPrompt =
                versionCodeChangedRule.shouldAllowFeedbackPrompt(fakeCurrentVersionCode);


        assertFalse(
                "Feedback prompt should be blocked if the app version code has not changed",
                ruleShouldAllowFeedbackPrompt);
    }


    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatRuleAllowsPromptIfAppVersionCodeHasIncreased() throws RemoteException {

        final int fakeCachedVersionCode = 42;
        final int fakeCurrentVersionCode = 69;
        assert fakeCachedVersionCode < fakeCurrentVersionCode;

        final VersionCodeChangedRule versionCodeChangedRule = new VersionCodeChangedRule(
                fakeCurrentVersionCode);


        final boolean ruleShouldAllowFeedbackPrompt =
                versionCodeChangedRule.shouldAllowFeedbackPrompt(fakeCachedVersionCode);


        assertTrue(
                "Feedback prompt should be allowed if the app version code has changed",
                ruleShouldAllowFeedbackPrompt);
    }

}
