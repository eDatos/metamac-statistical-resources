package org.siemac.metamac.statistical.resources.core.publication.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.joda.time.DateTime;
import org.siemac.metamac.rest.common.domain.CacheableResource;
import org.siemac.metamac.statistical.resources.core.base.domain.HasSiemacMetadata;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;

/**
 * Entity representing PublicationVersion.
 * <p>
 * This class is responsible for the domain object related
 * business logic for PublicationVersion. Properties and associations are
 * implemented in the generated base class {@link org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersionBase}.
 */
@Entity
@Table(name = "TB_PUBLICATIONS_VERSIONS")
public class PublicationVersion extends PublicationVersionBase implements HasSiemacMetadata, CacheableResource {
    private static final long serialVersionUID = 1L;

    public PublicationVersion() {
    }

    @Override
    public LifeCycleStatisticalResource getLifeCycleStatisticalResource() {
        return getSiemacMetadataStatisticalResource();
    }

    public DateTime getLastModifiedDate() {
        return getLifeCycleStatisticalResource().getLastUpdated();
    }
}
