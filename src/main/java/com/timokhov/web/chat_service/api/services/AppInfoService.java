package com.timokhov.web.chat_service.api.services;

import com.timokhov.web.chat_service.api.services.dto.AppInfo;
import com.timokhov.web.chat_service.api.services.dto.AppVersionInfo;
import com.timokhov.web.chat_service.utils.AppVersionInfoUtils;
import com.timokhov.web.chat_service.utils.ServerUtils;
import org.springframework.stereotype.Component;

@Component
public class AppInfoService {

    private String versionFilePath;

    public AppInfoService(String versionFilePath) {
        this.versionFilePath = versionFilePath;
    }

    public AppInfo getAppInfo() {
        AppVersionInfo appVersionInfo = AppVersionInfoUtils.getAppVersionInfo(versionFilePath);

        return new AppInfo(
                appVersionInfo.getVersion(),
                appVersionInfo.getBuildDate(),
                appVersionInfo.getBuildNumber(),
                ServerUtils.getServerName()
        );
    }
}
