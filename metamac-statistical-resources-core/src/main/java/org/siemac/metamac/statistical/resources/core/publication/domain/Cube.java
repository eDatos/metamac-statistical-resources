package org.siemac.metamac.statistical.resources.core.publication.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.RandomStringUtils;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;

/**
 * Publication cube
 */
@Entity
@Table(name = "TB_CUBES")
public class Cube extends CubeBase {

    private static final long serialVersionUID = 1L;

    private static int        CODE_MAX_LENGTH  = 10;

    public Cube() {
    }

    public void fillCodeAndUrn() {
        String code = RandomStringUtils.randomAlphanumeric(CODE_MAX_LENGTH);
        this.getNameableStatisticalResource().setCode(code);
        this.getNameableStatisticalResource().setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceCollectionCubeUrn(code));
    }

    public String getDatasetUrn() {
        return super.getDataset() != null ? super.getDataset().getIdentifiableStatisticalResource().getUrn() : null;
    }

    public String getQueryUrn() {
        return super.getQuery() != null ? super.getQuery().getIdentifiableStatisticalResource().getUrn() : null;
    }

    public String getMultidatasetUrn() {
        return super.getMultidataset() != null ? super.getMultidataset().getIdentifiableStatisticalResource().getUrn() : null;
    }

    public String getPublicationUrn() {
        return super.getPublication() != null ? super.getPublication().getIdentifiableStatisticalResource().getUrn() : null;
    }
}
