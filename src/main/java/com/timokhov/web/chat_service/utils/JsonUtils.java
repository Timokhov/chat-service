/*
 * Copyright (c) 2018 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.timokhov.web.chat_service.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import java.io.IOException;

public class JsonUtils {

    public static String toStringJson(Object object) {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        mapper.registerModule(new JodaModule());
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            return "toString failed " + e.getMessage();
        }
    }
}
