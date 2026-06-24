package org.siemac.metamac.statistical.resources.web.server.dtos;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.web.client.enums.LifeCycleActionEnum;

public class ResourceNotificationBaseDto extends BaseResourceNotificationDto<LifeCycleStatisticalResourceBaseDto> {

    private static final long serialVersionUID = 4597719959970093399L;

    public static class Builder {

        private final LifeCycleActionEnum                 lifeCycleAction;
        private LifeCycleStatisticalResourceBaseDto       updatedResource;
        private final LifeCycleStatisticalResourceBaseDto previousResource;
        private String                                    reasonOfRejection;

        public Builder(LifeCycleStatisticalResourceBaseDto previousResource, LifeCycleActionEnum lifeCycleAction) {
            this.previousResource = previousResource;
            this.lifeCycleAction = lifeCycleAction;
        }

        public Builder updatedResource(LifeCycleStatisticalResourceBaseDto updatedResource) {
            this.updatedResource = updatedResource;
            return this;
        }

        public Builder reasonOfRejection(String reasonOfRejection) {
            this.reasonOfRejection = reasonOfRejection;
            return this;
        }

        public ResourceNotificationBaseDto build() {
            return new ResourceNotificationBaseDto(this);
        };
    }

    public ResourceNotificationBaseDto(Builder builder) {
        lifeCycleAction = builder.lifeCycleAction;
        statisticalResourceType = builder.previousResource.getType();
        updatedResource = builder.updatedResource;
        previousResource = builder.previousResource;
        reasonOfRejection = builder.reasonOfRejection;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(previousResource).append(updatedResource).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ResourceNotificationBaseDto))
            return false;
        if (obj == this)
            return true;

        ResourceNotificationBaseDto r = (ResourceNotificationBaseDto) obj;
        return new EqualsBuilder().append(previousResource, r.previousResource).append(updatedResource, r.updatedResource).isEquals();
    }
}
