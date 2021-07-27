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
import com.example.amplify.lib.IEnvironment;
import com.example.amplify.lib.tracking.rules.HuaweiAppGalleryRule;
import org.junit.Test;
import org.mockito.Mock;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * HuaweiAppGalleryRuleTest extends BaseTest
 */
public class HuaweiAppGalleryRuleTest extends BaseTest {

    private HuaweiAppGalleryRule huaweiAppGalleryRule;

    @Mock
    private IEnvironment environment;

    @Override
    public void localSetUp() {
        huaweiAppGalleryRule = new HuaweiAppGalleryRule();
    }

    @Test
    public void testThatEnvironmentRuleIsSatisfiedIfHuaweiAppGalleryIsInstalledOnDevice() {
        // Arrange
        when(environment.isHuaweiAppGalleryInstalled()).thenReturn(true);

        // Act
        final boolean isEnvironmentRuleSatisfied = huaweiAppGalleryRule.shouldAllowFeedbackPrompt(environment);

        // Assert
        assertTrue("Environment based rule should be satisfied", isEnvironmentRuleSatisfied);
    }

    @Test
    public void testThatEnvironmentRuleIsNotSatisfiedIfHuaweiAppGalleryIsNotInstalledOnDevice() {
        // Arrange
        when(environment.isHuaweiAppGalleryInstalled()).thenReturn(false);

        // Act
        final boolean isEnvironmentRuleSatisfied = huaweiAppGalleryRule.shouldAllowFeedbackPrompt(environment);

        // Assert
        assertFalse("Environment based rule should not be satisfied", isEnvironmentRuleSatisfied);
    }

}
