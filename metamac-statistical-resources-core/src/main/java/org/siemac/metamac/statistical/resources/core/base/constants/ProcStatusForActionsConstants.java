package org.siemac.metamac.statistical.resources.core.base.constants;

import org.apache.commons.lang3.ArrayUtils;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.utils.BaseEnumUtils;

public abstract class ProcStatusForActionsConstants {

    // ----------------------------------------------------------------------
    // ARRAYS
    // ----------------------------------------------------------------------

    // @formatter:off

    protected static final ProcStatusEnum[] procStatusForDeleteResource                       = {ProcStatusEnum.DRAFT, ProcStatusEnum.VALIDATION_REJECTED};
    protected static final ProcStatusEnum[] procStatusForEditResource                         = {ProcStatusEnum.DRAFT, ProcStatusEnum.VALIDATION_REJECTED, ProcStatusEnum.PRODUCTION_VALIDATION, ProcStatusEnum.DIFFUSION_VALIDATION};

    protected static final ProcStatusEnum[] procStatusForSendResourceToProductionValidation   = {ProcStatusEnum.DRAFT, ProcStatusEnum.VALIDATION_REJECTED};
    protected static final ProcStatusEnum[] procStatusForSendResourceToDiffusionValidation    = {ProcStatusEnum.PRODUCTION_VALIDATION};
    protected static final ProcStatusEnum[] procStatusForSendResourceToValidationRejected     = {ProcStatusEnum.PRODUCTION_VALIDATION, ProcStatusEnum.DIFFUSION_VALIDATION};
    protected static final ProcStatusEnum[] procStatusForSendResourceToPublish                = {ProcStatusEnum.DIFFUSION_VALIDATION};
    protected static final ProcStatusEnum[] procStatusForSendResourceToVersion                = {ProcStatusEnum.PUBLISHED};

    // Query
    protected static final ProcStatusEnum[] procStatusForEditQueryVersion                     = ArrayUtils.addAll(procStatusForEditResource, ProcStatusEnum.PUBLISHED);

    // DatasetVersion
    protected static final ProcStatusEnum[] procStatusForImportDatasourcesInDatasetVersion    = {ProcStatusEnum.DRAFT, ProcStatusEnum.VALIDATION_REJECTED};
    protected static final ProcStatusEnum[] procStatusForImportZipDatasources                 = {ProcStatusEnum.DRAFT, ProcStatusEnum.VALIDATION_REJECTED, ProcStatusEnum.PUBLISHED};
    
    // Publication
    protected static final ProcStatusEnum[] procStatusForEditPublicationStructure             = {ProcStatusEnum.DRAFT, ProcStatusEnum.VALIDATION_REJECTED, ProcStatusEnum.PRODUCTION_VALIDATION, ProcStatusEnum.DIFFUSION_VALIDATION};

    // Multidataset
    protected static final ProcStatusEnum[] procStatusForEditMultidatasetStructure             = {ProcStatusEnum.DRAFT, ProcStatusEnum.VALIDATION_REJECTED, ProcStatusEnum.PRODUCTION_VALIDATION, ProcStatusEnum.DIFFUSION_VALIDATION};

    // @formatter:on

    // ----------------------------------------------------------------------
    // STRINGS
    // ----------------------------------------------------------------------

    // Statistical Resources
    public static final String              PROC_STATUS_FOR_DELETE_RESOURCE                        = BaseEnumUtils.enumToString(procStatusForDeleteResource);

    public static final String              PROC_STATUS_FOR_EDIT_RESOURCE                          = BaseEnumUtils.enumToString(procStatusForEditResource);

    public static final String              PROC_STATUS_FOR_SEND_RESOURCE_TO_PRODUCTION_VALIDATION = BaseEnumUtils.enumToString(procStatusForSendResourceToProductionValidation);
    public static final String              PROC_STATUS_FOR_SEND_RESOURCE_TO_DIFFUSION_VALIDATION  = BaseEnumUtils.enumToString(procStatusForSendResourceToDiffusionValidation);
    public static final String              PROC_STATUS_FOR_SEND_RESOURCE_TO_VALIDATION_REJECTED   = BaseEnumUtils.enumToString(procStatusForSendResourceToValidationRejected);
    public static final String              PROC_STATUS_FOR_SEND_RESOURCE_TO_PUBLISH               = BaseEnumUtils.enumToString(procStatusForSendResourceToPublish);
    public static final String              PROC_STATUS_FOR_SEND_RESOURCE_TO_VERSION               = BaseEnumUtils.enumToString(procStatusForSendResourceToVersion);

    // Query
    public static final String              PROC_STATUS_FOR_EDIT_QUERY_VERSION                     = BaseEnumUtils.enumToString(procStatusForEditQueryVersion);

    // DatasetVersion
    public static final String              PROC_STATUS_FOR_IMPORT_DATASOURCES_IN_DATASET_VERSION  = BaseEnumUtils.enumToString(procStatusForImportDatasourcesInDatasetVersion);
    public static final String              PROC_STATUS_FOR_IMPORT_ZIP_DATASOURCES                 = BaseEnumUtils.enumToString(procStatusForImportZipDatasources);

    
    
    // Publication
    public static final String              PROC_STATUS_FOR_EDIT_PUBLICATION_STRUCTURE             = BaseEnumUtils.enumToString(procStatusForEditPublicationStructure);

    // Multidataset
    public static final String              PROC_STATUS_FOR_EDIT_MULTIDATASET_STRUCTURE            = BaseEnumUtils.enumToString(procStatusForEditMultidatasetStructure);
}
