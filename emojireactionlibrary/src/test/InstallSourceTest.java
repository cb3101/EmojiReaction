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

package amplify;

import com.example.amplify.lib.InstallSource;
import org.junit.Test;
import amplify.helpers.BaseTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * InstallSourceTest extends BaseTest
 */
public class InstallSourceTest extends BaseTest {

    @Test
    public void testGooglePlayStoreInstallSourceIsParsedCorrectly() {
        // Act
        final InstallSource installSource = InstallSource.fromInstallerPackageName("com.android.vending");

        // Assert
        assertTrue(
                "Package name is correctly identified as belonging to the Google Play Store",
                installSource instanceof InstallSource.GooglePlayStoreInstallSource);

        assertEquals(
                "Google Play Store install source has correct description",
                "Google Play Store",
                installSource.toString());
    }

    @Test
    public void testHuaweiAppGalleryInstallSourceIsParsedCorrectly() {
        // Act
        final InstallSource installSource = InstallSource.fromInstallerPackageName("com.huawei.appmarket");

        // Assert
        assertTrue(
                "Package name is correctly identified as belonging to the Huawei App Gallery",
                installSource instanceof InstallSource.HuaweiAppGalleryInstallSource);

        assertEquals(
                "Huawei App Gallery install source has correct description",
                "Huawei App Gallery",
                installSource.toString());
    }

    @Test
    public void testAmazonAppStoreInstallSourceIsParsedCorrectly() {
        // Act
        InstallSource installSource = InstallSource.fromInstallerPackageName("com.amazon.venezia");

        // Assert
        assertTrue(
                "Package name is correctly identified as belonging to the Amazon Appstore",
                installSource instanceof InstallSource.AmazonAppStoreInstallSource);

        assertEquals(
                "Amazon Appstore install source has correct description",
                "Amazon Appstore",
                installSource.toString());
    }

    @Test
    public void testAmazonUndergroundInstallSourceIsParsedCorrectly() {
        // Act
        final InstallSource installSource = InstallSource.fromInstallerPackageName("com.amazon.mshop.android");

        // Assert
        assertTrue(
                "Package name is correctly identified as belonging to Amazon Underground",
                installSource instanceof InstallSource.AmazonUndergroundInstallSource);

        assertEquals(
                "Amazon Underground install source has correct description",
                "Amazon Underground",
                installSource.toString());
    }

    @Test
    public void testPackageInstallerInstallSourceIsParsedCorrectly() {
        // Act
        final InstallSource installSource = InstallSource.fromInstallerPackageName("com.google.android.packageinstaller");

        // Assert
        assertTrue(
                "Package name is correctly identified as belonging to the Android Package Installer",
                installSource instanceof InstallSource.PackageInstallerInstallSource);

        assertEquals(
                "Package Installer install source has correct description",
                "Package Installer",
                installSource.toString());
    }

    @Test
    public void testUnrecognizedInstallSourceIsParsedCorrectly() {
        // Arrange
        final String unrecognizedInstallerPackageName = "com.unrecognized";

        // Act
        final InstallSource installSource = InstallSource.fromInstallerPackageName(unrecognizedInstallerPackageName);

        // Assert
        assertTrue(
                "Non-null package name is correctly identified as an unrecognized install source",
                installSource instanceof InstallSource.UnrecognizedInstallSource);

        assertEquals(
                "Unrecognized install source returns raw package name as description",
                unrecognizedInstallerPackageName,
                installSource.toString());
    }


    @Test
    public void testUnknownInstallSourceIsParsedCorrectly() {
        // Act
        final InstallSource installSource = InstallSource.fromInstallerPackageName(null);

        // Assert
        assertTrue(
                "null package name is correctly identified as an unknown install source",
                installSource instanceof InstallSource.UnknownInstallSource);

        assertEquals(
                "Unknown install source has correct description",
                "Unknown",
                installSource.toString());
    }

}
