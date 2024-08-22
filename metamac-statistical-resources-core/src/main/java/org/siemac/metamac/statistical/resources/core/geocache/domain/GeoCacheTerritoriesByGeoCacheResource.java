package org.siemac.metamac.statistical.resources.core.geocache.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity representing GeoCacheTerritoriesByGeoCacheResource.
 * <p>
 * This class is responsible for the domain object related
 * business logic for GeoCacheTerritoriesByGeoCacheResource. Properties and associations are
 * implemented in the generated base class {@link org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheTerritoriesByGeoCacheResourceBase}.
 */
@Entity
@Table(name = "TB_TERRITORIES_BY_GEO_CACHE_RESOURCE")
public class GeoCacheTerritoriesByGeoCacheResource
    extends GeoCacheTerritoriesByGeoCacheResourceBase {
    private static final long serialVersionUID = 1L;

    public GeoCacheTerritoriesByGeoCacheResource() {
    }
}
