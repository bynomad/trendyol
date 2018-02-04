package com.trendyol.configservice.model.rest;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class ConfigResponse implements Serializable {
    private UUID id;
    private String applicationName;
    private String name;
    private String type;
    private String value;
    private byte isActive;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConfigResponse configResponse = (ConfigResponse) o;

        return this.id.equals(configResponse.id);
    }

    @Override
    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }
}
