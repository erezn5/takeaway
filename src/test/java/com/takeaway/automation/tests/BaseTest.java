package com.takeaway.automation.tests;

import com.takeaway.automation.framework.conf.EnvConf;
import com.takeaway.automation.framework.utils.FileUtil;
import com.takeaway.automation.tests.components.TestNGListener;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import java.io.File;
import java.util.TimeZone;

@Listeners(TestNGListener.class)
public class BaseTest {
    private static final File DOWNLOADS_FOLDER = new File(EnvConf.getProperty("test_output.files.folder"));
    protected final File testTempFolder;

    @BeforeClass
    public void setUp() {
        FileUtil.createFolder(DOWNLOADS_FOLDER, false);
        TimeZone.setDefault(TimeZone.getTimeZone(EnvConf.getProperty("env.timezone.id")));
    }

    public BaseTest() {
        this.testTempFolder = new File(DOWNLOADS_FOLDER, randSuffix(getClass().getSimpleName()));
        FileUtil.createFolder(testTempFolder, false);
        testTempFolder.deleteOnExit();
    }

    protected static String randSuffix(String prefix){
        return prefix + "-" + String.valueOf(System.nanoTime()).substring(9);
    }

}
