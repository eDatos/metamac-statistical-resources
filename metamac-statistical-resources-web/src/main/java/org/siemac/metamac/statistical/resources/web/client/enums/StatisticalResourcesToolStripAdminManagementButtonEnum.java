package org.siemac.metamac.statistical.resources.web.client.enums;

import com.smartgwt.client.types.ValueEnum;

public enum StatisticalResourcesToolStripAdminManagementButtonEnum implements ValueEnum {

    UPDATE_TERRITORIES_CACHE("update_territory_cache_button");

    private String value;

    StatisticalResourcesToolStripAdminManagementButtonEnum(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
