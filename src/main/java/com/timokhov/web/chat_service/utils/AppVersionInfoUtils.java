package com.timokhov.web.chat_service.utils;

import com.timokhov.web.chat_service.api.services.dto.AppVersionInfo;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public class AppVersionInfoUtils {

    public static AppVersionInfo getAppVersionInfo(String versionFilePath) {
        try {
            Properties versionProperties = PropertiesLoaderUtils.loadProperties(new EncodedResource(new ClassPathResource(
                    versionFilePath
            )));

            return new AppVersionInfo(
                    versionProperties.getProperty("version"),
                    versionProperties.getProperty("buildDate"),
                    versionProperties.getProperty("buildNumber")
            );
        } catch (IOException e) {
            return new AppVersionInfo("", "", "");
        }
    }
}
