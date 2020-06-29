package com.takeaway.automation.framework.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.takeaway.automation.framework.logger.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileUtil {

    private static final Gson GSON = new GsonBuilder().create();

    private FileUtil() { }

    public static Properties createPropertiesFromResource(Class clazz , String relativePath) {
        try(InputStream ips = clazz.getClassLoader().getResourceAsStream(relativePath)){
            Properties properties = new Properties();
            properties.load(ips);
            return properties;
        }catch (IOException e){
            System.err.println("Failed to convert resource'" + relativePath + "'stream to properties, cause: " + e.getMessage());
            return null;
        }
    }

    public static void createFolder(File folder , boolean recursive){
        if(folder.exists() && folder.isDirectory()){
            LoggerFactory.LOG.i(folder.getName() + " directory already exist");
        }else if((recursive ? folder.mkdirs() : folder.mkdir())){
            LoggerFactory.LOG.i(folder.getName() + " directory created successfully");
        }else{
            LoggerFactory.LOG.error("failed to create '" + folder.getName() + "' directory");
        }
    }

}

