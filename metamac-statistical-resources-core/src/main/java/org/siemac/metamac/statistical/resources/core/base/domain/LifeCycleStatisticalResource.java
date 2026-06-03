package org.siemac.metamac.statistical.resources.core.base.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;

/**
 * Entity representing LifeCycleStatisticalResource.
 * <p>
 * This class is responsible for the domain object related business logic for LifeCycleStatisticalResource. Properties and associations are implemented in the generated base class
 * {@link org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResourceBase}.
 */
@Entity
@DiscriminatorValue("LIFE_CYCLE_RESOURCE")
public class LifeCycleStatisticalResource extends LifeCycleStatisticalResourceBase implements HasLifecycle {

    private static final long serialVersionUID = 1L;

    public LifeCycleStatisticalResource() {
    }

    @Transient
    @Override
    public LifeCycleStatisticalResource getLifeCycleStatisticalResource() {
        return this;
    }

    public ProcStatusEnum getEffectiveProcStatus() {
        return this.getProcStatus();
    }

    public boolean isPublishedVisible() {
        return ProcStatusEnum.PUBLISHED.equals(getEffectiveProcStatus());
    }
}
