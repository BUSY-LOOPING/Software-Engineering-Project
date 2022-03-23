package com.busy.looping.seproject;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginDatabaseTest {


    @Test
    public void testSQLCreate() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        LoginDatabase database = LoginDatabase.getInstance(appContext);
        database.getWritableDatabase();
        Assert.assertEquals(database.getErrorMessage(), "");
    }
}