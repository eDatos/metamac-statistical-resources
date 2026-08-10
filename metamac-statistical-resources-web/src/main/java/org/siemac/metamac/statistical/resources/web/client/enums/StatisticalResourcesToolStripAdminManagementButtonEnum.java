package org.siemac.metamac.statistical.resources.web.client.enums;

import com.smartgwt.client.types.ValueEnum;

public enum StatisticalResourcesToolStripAdminManagementButtonEnum implements ValueEnum {

    UPDATE_GEOCOV_VARELEM_CACHE("update_geocov_varelem_cache_button"),
    RELOAD_KAFKA_TOPICS("reload_kafka_topics_button");

    private String value;

    StatisticalResourcesToolStripAdminManagementButtonEnum(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
