package com.wolasoft.maplenou;

import android.content.Context;
import android.content.pm.PackageInfo;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class ApplicationTest {

    private Context appContext;
    private PackageInfo info;

    @Before
    public void setUp() throws Exception{
        appContext = InstrumentationRegistry.getTargetContext();
        info = appContext.getPackageManager().getPackageInfo(appContext.getPackageName(), 0);
    }

    @Test
    public void testVersion() {
        assertNotNull(appContext);
        assertNotNull(info);
        assertEquals(info.versionName, "1.0");
    }

    @Test
    public void testAppName() {
        assertNotNull(appContext);
        assertNotNull(info);
        assertEquals("Maplenou", appContext.getString(R.string.app_name));
    }
}