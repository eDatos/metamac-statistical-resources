package org.siemac.metamac.statistical.resources.core.multidataset.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.joda.time.DateTime;
import org.siemac.metamac.rest.common.domain.CacheableResource;
import org.siemac.metamac.statistical.resources.core.base.domain.HasSiemacMetadata;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;

/**
 * Entity representing MultidatasetVersion.
 * <p>
 * This class is responsible for the domain object related
 * business logic for MultidatasetVersion. Properties and associations are
 * implemented in the generated base class {@link org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersionBase}.
 */
@Entity
@Table(name = "TB_MULTIDATASETS_VERSIONS")
public class MultidatasetVersion extends MultidatasetVersionBase implements HasSiemacMetadata, CacheableResource {

    private static final long serialVersionUID = 1L;

    public MultidatasetVersion() {
    }

    @Override
    public LifeCycleStatisticalResource getLifeCycleStatisticalResource() {
        return getSiemacMetadataStatisticalResource();
    }

    public DateTime getLastModifiedDate() {
        return getLifeCycleStatisticalResource().getLastUpdated();
    }
}
