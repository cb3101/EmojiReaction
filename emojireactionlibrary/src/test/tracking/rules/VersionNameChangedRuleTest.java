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
import com.example.amplify.lib.tracking.rules.VersionNameChangedRule;
import ohos.rpc.RemoteException;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * VersionNameChangedRuleTest extends BaseTest
 */
public class VersionNameChangedRuleTest extends BaseTest {

    static final String TESTSTRING = "any string";

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatRuleAllowsPromptIfEventHasNeverOccurred() throws RemoteException {

        final String fakeCurrentVersionName = testString;
        final VersionNameChangedRule versionNameChangedRule = new VersionNameChangedRule(fakeCurrentVersionName);


        final boolean ruleShouldAllowFeedbackPrompt = versionNameChangedRule.shouldAllowFeedbackPromptByDefault();


        assertTrue(
                "Feedback prompt should be allowed if the associated event has never occurred.",
                ruleShouldAllowFeedbackPrompt);
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Test
    public void testThatRuleBlocksPromptIfAppVersionNameHasNotChanged() {

        final String fakeCachedVersionName = testString;
        final String fakeCurrentVersionName = fakeCachedVersionName;
        final VersionNameChangedRule versionNameChangedRule = new VersionNameChangedRule(fakeCurrentVersionName);


        final boolean ruleShouldAllowFeedbackPrompt =
                versionNameChangedRule.shouldAllowFeedbackPrompt(fakeCachedVersionName);


        assertFalse(
                "Feedback prompt should be blocked if the app version name has not changed",
                ruleShouldAllowFeedbackPrompt);
    }

    @Test
    public void testThatRuleAllowsPromptIfAppVersionNameHasChanged() {

        final String fakeCachedVersionName = testString;
        final String fakeCurrentVersionName = "any other string";
        assert !fakeCachedVersionName.equals(fakeCurrentVersionName);

        final VersionNameChangedRule versionNameChangedRule = new VersionNameChangedRule(fakeCurrentVersionName);


        final boolean ruleShouldAllowFeedbackPrompt =
                versionNameChangedRule.shouldAllowFeedbackPrompt(fakeCachedVersionName);


        assertTrue(
                "Feedback prompt should be allowed if the app version name has changed",
                ruleShouldAllowFeedbackPrompt);
    }

}
