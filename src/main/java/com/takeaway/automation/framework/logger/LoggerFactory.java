package com.takeaway.automation.framework.logger;

import com.takeaway.automation.framework.conf.EnvConf;
import com.takeaway.automation.framework.utils.FileUtil;
import org.apache.log4j.PropertyConfigurator;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

public class LoggerFactory {

    private static final Properties log4jProperties;
    public static final LoggerFormat LOG = new LoggerFormat();
    private static final AtomicReference<LogLevel> logLevel = new AtomicReference<>(LogLevel.UNKNOWN);

    static {
        String log4jPath = EnvConf.getProperty("conf.log4j");
        if(log4jPath == null){
            log4jProperties = null;
            System.err.println("log4j.properties file don't exist!");
        }else{
            Properties properties = FileUtil.createPropertiesFromResource(LoggerFactory.class , log4jPath);
            if(properties == null){
                log4jProperties = null;
                throw new IllegalStateException("Failed to load '" + log4jPath + "' file");
            }else{
                log4jProperties = properties;
                PropertyConfigurator.configure(log4jProperties);
            }
        }
    }

    public static boolean isDebug(){
        return getLogLevel() == LogLevel.DEBUG;
    }

    private static LogLevel getLogLevel(){
        if(log4jProperties != null &&
                logLevel.get() == LogLevel.UNKNOWN){
            String rootLoggerLine = log4jProperties.getProperty("log4j.rootLogger");
            String level = rootLoggerLine.split(",")[0].trim();
            logLevel.set(LogLevel.valueOf(level));
        }
        return logLevel.get();
    }
    private LoggerFactory() {
    }


}


