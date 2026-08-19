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

    /**
     * Checks if this version is the last published version of the resource: it is published and it has not been superseded by a later published version. It applies the same criteria as
     * DatasetService.retrievePublishedLastVersionDatasets and QueryService.retrievePublishedLastVersionQueries.
     * <p>
     * Note that the lastVersion flag can not be used for this purpose: it is set to false as soon as a later version is created in DRAFT status, even though the previous one is still the published
     * one.
     * </p>
     */
    public boolean isLastPublishedVersion() {
        return isPublishedVisible() && getValidTo() == null;
    }
}
