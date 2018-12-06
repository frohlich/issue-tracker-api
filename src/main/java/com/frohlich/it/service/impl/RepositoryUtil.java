package com.frohlich.it.service.impl;

import com.google.common.base.CaseFormat;

public class RepositoryUtil {

    public static String generateRepositoryName(String name) {
        name = name.replaceAll("[^a-zA-Z0-9]+","");
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name.replaceAll(" ", "_").toUpperCase());
    }
}
