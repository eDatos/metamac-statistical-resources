package org.siemac.metamac.statistical.resources.web.server.dtos;

import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.web.client.enums.LifeCycleActionEnum;

public class ResourceNotificationDto extends BaseResourceNotificationDto<LifeCycleStatisticalResourceDto> {

    private static final long serialVersionUID = -6177937293345892429L;

    public static class Builder {

        private final LifeCycleActionEnum             lifeCycleAction;
        private LifeCycleStatisticalResourceDto       updatedResource;
        private final LifeCycleStatisticalResourceDto previousResource;
        private String                                reasonOfRejection;

        public Builder(LifeCycleStatisticalResourceDto previousResource, LifeCycleActionEnum lifeCycleAction) {
            this.previousResource = previousResource;
            this.lifeCycleAction = lifeCycleAction;
        }

        public Builder updatedResource(LifeCycleStatisticalResourceDto updatedResource) {
            this.updatedResource = updatedResource;
            return this;
        }

        public Builder reasonOfRejection(String reasonOfRejection) {
            this.reasonOfRejection = reasonOfRejection;
            return this;
        }

        public ResourceNotificationDto build() {
            return new ResourceNotificationDto(this);
        };
    }

    public ResourceNotificationDto(Builder builder) {
        lifeCycleAction = builder.lifeCycleAction;
        statisticalResourceType = builder.previousResource.getType();
        updatedResource = builder.updatedResource;
        previousResource = builder.previousResource;
        reasonOfRejection = builder.reasonOfRejection;
    }
}
