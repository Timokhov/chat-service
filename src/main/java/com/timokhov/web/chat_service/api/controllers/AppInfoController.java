package com.timokhov.web.chat_service.api.controllers;

import com.timokhov.web.chat_service.api.services.AppInfoService;
import com.timokhov.web.chat_service.dto.http.DataResponse;

import com.timokhov.web.chat_service.api.services.dto.AppInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(AppInfoController.URL)
public class AppInfoController {

    static final String URL = "/app-info";

    @Resource
    private AppInfoService appInfoService;

    @RequestMapping(method = RequestMethod.GET)
    public DataResponse<AppInfo> getAppInfo() {
        return new DataResponse<>(appInfoService.getAppInfo());
    }

}
