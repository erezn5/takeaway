package com.takeaway.automation.tests.components;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import static com.takeaway.automation.framework.logger.LoggerFactory.LOG;


public class TestNGListener implements ITestListener {

    public void onTestStart(ITestResult result) {
        LOG.info(result.getName()+" test case started");
    }

    public void onTestSuccess(ITestResult result) {
        LOG.info("[PASSED] name of the testcase passed is :" + result.getName());
    }

    public void onTestFailure(ITestResult result) {
        LOG.info("[FAILED] name of the testcase failed is :" + result.getName());
    }

    public void onTestSkipped(ITestResult result) {

    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    public void onStart(ITestContext context) {

    }

    public void onFinish(ITestContext context) {
        context.getPassedTests().getAllResults()
                .forEach(result -> LOG.info(result.getName()));
    }
}
