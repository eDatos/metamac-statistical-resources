package org.siemac.metamac.statistical.resources.web.shared.utils;

import com.gwtplatform.dispatch.shared.SecurityCookie;

public class StatisticalResourcesSharedTokens extends org.siemac.metamac.web.common.shared.utils.SharedTokens {

    //
    // SECURITY
    //

    @SecurityCookie
    public static final String securityCookieName                   = "securityCookieName";

    //
    // FILE UPLOAD PARAMETERS
    //

    public static final String UPLOAD_PARAM_OPERATION_CODE          = "operation-urn";
    public static final String UPLOAD_PARAM_DATASET_VERSION_URN     = "dataset-version-urn";
    public static final String UPLOAD_PARAM_PUBLICATION_VERSION_URN = "publication-version-urn";
    public static final String UPLOAD_PARAM_LANGUAGE                = "importation-language";
    public static final String UPLOAD_PARAM_DIM_PREFIX              = "dim-";
    public static final String UPLOAD_MUST_BE_ZIP_FILE              = "importation-zip-file";
    public static final String UPLOAD_RESOURCE_TYPE                 = "import-resource-type";
    public static final String UPLOAD_DATA_PROVIDER                 = "import-data-provider";
    public static final String UPLOAD_VERSION_RATIONALE_TYPES       = "import-ver-rat-type";
    public static final String UPLOAD_NEXT_VERSION                  = "import-ver-next-ver";
    public static final String UPLOAD_DATE_NEXT_VERSION             = "import-ver-date-next-ver";
    public static final String UPLOAD_DATE_NEXT_UPDATE              = "import-ver-date-next-update";
    public static final String UPLOAD_HAS_EXTRA_FIELDS              = "import-has-extra-fields";
    public static final String UPLOAD_PROC_STATUS                   = "import-proc-status";
    public static final String UPLOAD_ZIP_INFORMATION_LABEL         = "zip-information-label";
    
}
