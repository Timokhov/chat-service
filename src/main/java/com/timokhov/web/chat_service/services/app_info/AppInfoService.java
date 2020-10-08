package com.timokhov.web.chat_service.services.app_info;

import com.timokhov.web.chat_service.services.app_info.dto.AppInfo;
import com.timokhov.web.chat_service.services.app_info.dto.AppVersionInfo;
import com.timokhov.web.chat_service.utils.AppVersionInfoUtils;
import com.timokhov.web.chat_service.utils.ServerUtils;

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
