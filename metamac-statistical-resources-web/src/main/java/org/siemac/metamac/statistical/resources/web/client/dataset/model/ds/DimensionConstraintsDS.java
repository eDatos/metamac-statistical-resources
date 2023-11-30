package org.siemac.metamac.statistical.resources.web.client.dataset.model.ds;

import com.smartgwt.client.data.DataSource;

public class DimensionConstraintsDS extends DataSource {

    public static final String DIMENSION_ID                 = "dim-cons-id";
    public static final String INCLUSION_TYPE               = "dim-cons-in-type";
    public static final String VALUES                       = "dim-cons-values";
    public static final String TIME_RANGE                   = "dim-time-range";
    public static final String KEY_PART_TYPE                = "dim-key-part-type";
    public static final String DSD_DIMENSION_DTO            = "dim-cons-dto";
    public static final String SRM_RESOURCE_RESTRICTION     = "dim-cons-srm-res-restriction";
    public static final String ACTION_APPLY_SRM_RESTRICTION = "button-apply-srm_restriction";

    public DimensionConstraintsDS() {
        super();
    }
}
