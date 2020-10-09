/*
 * Copyright (c) 2019 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.timokhov.web.chat_service.api.services.dto;

import com.timokhov.web.chat_service.config.logger.annotations.Loggable;

@Loggable
public class AppVersionInfo {

    private String version;

    private String buildDate;

    private String buildNumber;

    public AppVersionInfo(String version, String buildDate, String buildNumber) {
        this.version = version;
        this.buildDate = buildDate;
        this.buildNumber = buildNumber;
    }

    @SuppressWarnings("unused")
    public String getVersion() {
        return version;
    }

    @SuppressWarnings("unused")
    public String getBuildDate() {
        return buildDate;
    }

    @SuppressWarnings("unused")
    public String getBuildNumber() {
        return buildNumber;
    }
}
