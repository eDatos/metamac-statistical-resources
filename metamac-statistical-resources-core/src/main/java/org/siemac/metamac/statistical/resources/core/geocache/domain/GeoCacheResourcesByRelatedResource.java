package org.siemac.metamac.statistical.resources.core.geocache.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity representing GeoCacheResourcesByRelatedResource.
 * <p>
 * This class is responsible for the domain object related
 * business logic for GeoCacheResourcesByRelatedResource. Properties and associations are
 * implemented in the generated base class {@link org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResourcesByRelatedResourceBase}.
 */
@Entity
@Table(name = "TB_GEO_CACHE_RESOURCE_BY_RELATED_RESOURCE")
public class GeoCacheResourcesByRelatedResource
    extends GeoCacheResourcesByRelatedResourceBase {
    private static final long serialVersionUID = 1L;

    public GeoCacheResourcesByRelatedResource() {
    }
}
