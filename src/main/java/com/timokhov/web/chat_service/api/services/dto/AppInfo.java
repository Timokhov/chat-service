/*
 * Copyright (c) 2019 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.timokhov.web.chat_service.api.services.dto;

import com.timokhov.web.chat_service.config.logger.annotations.Loggable;

@Loggable
public class AppInfo extends AppVersionInfo {

    private String serverName;

    public AppInfo(String version, String buildDate, String buildNumber, String serverName) {
        super(version, buildDate, buildNumber);
        this.serverName = serverName;
    }

    @SuppressWarnings("unused")
    public String getServerName() {
        return serverName;
    }

}
