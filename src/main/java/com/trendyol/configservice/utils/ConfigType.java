package com.trendyol.configservice.utils;

public enum ConfigType {
    INTEGER("Integer"),
    STRING("String"),
    DOUBLE("Double"),
    BOOLEAN("Boolean");

    private String type;

    ConfigType(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}
