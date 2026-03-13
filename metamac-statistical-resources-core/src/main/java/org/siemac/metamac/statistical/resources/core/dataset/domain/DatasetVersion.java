package org.siemac.metamac.statistical.resources.core.dataset.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.cache.domain.CacheableResource;
import org.siemac.metamac.statistical.resources.core.base.domain.HasSiemacMetadata;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;

/**
 * Entity representing DatasetVersion.
 * <p>
 * This class is responsible for the domain object related
 * business logic for DatasetVersion. Properties and associations are
 * implemented in the generated base class {@link org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionBase}.
 */
@Entity
@Table(name = "TB_DATASETS_VERSIONS")
public class DatasetVersion extends DatasetVersionBase implements HasSiemacMetadata, CacheableResource {
    private static final long serialVersionUID = 1L;
    
    public DatasetVersion() {
    }

    @Override
    public LifeCycleStatisticalResource getLifeCycleStatisticalResource() {
        return getSiemacMetadataStatisticalResource();
    }

    public DateTime getLastModifiedDate() {
        return getLifeCycleStatisticalResource().getLastUpdated();
    }
}
