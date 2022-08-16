package org.siemac.metamac.statistical.resources.core.base.validators;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.constants.ProcStatusForActionsConstants;
import org.siemac.metamac.statistical.resources.core.base.domain.HasLifecycle;
import org.siemac.metamac.statistical.resources.core.dto.BasicVersionableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.enume.utils.ProcStatusEnumUtils;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;

public abstract class ProcStatusValidator extends ProcStatusForActionsConstants {

    // --------------------------------------------------------------------
    // StatisticalResources
    // --------------------------------------------------------------------

    public static void checkStatisticalResourceCanBeDeleted(HasLifecycle resource) throws MetamacException {
        ProcStatusEnumUtils.checkPossibleProcStatus(resource, procStatusForDeleteResource);
    }

    public static void checkStatisticalResourceCanBeEdited(HasLifecycle resource) throws MetamacException {
        ProcStatusEnumUtils.checkPossibleProcStatus(resource, procStatusForEditResource);
    }

    public static void checkStatisticalResourceCanSendToProductionValidation(HasLifecycle resource) throws MetamacException {
        ProcStatusEnumUtils.checkPossibleProcStatus(resource, procStatusForSendResourceToProductionValidation);
    }

    public static void checkStatisticalResourceCanSendToDiffusionValidation(HasLifecycle resource) throws MetamacException {
        ProcStatusEnumUtils.checkPossibleProcStatus(resource, procStatusForSendResourceToDiffusionValidation);
    }

    public static void checkStatisticalResourceCanSendToValidationRejected(HasLifecycle resource) throws MetamacException {
        ProcStatusEnumUtils.checkPossibleProcStatus(resource, procStatusForSendResourceToValidationRejected);
    }

    public static void checkStatisticalResourceCanSendToPublish(HasLifecycle resource) throws MetamacException {
        ProcStatusEnumUtils.checkPossibleProcStatus(resource, procStatusForSendResourceToPublish);
    }

    public static void checkStatisticalResourceCanSendToVersion(HasLifecycle resource) throws MetamacException {
        ProcStatusEnumUtils.checkPossibleProcStatus(resource, procStatusForSendResourceToVersion);
    }

    // --------------------------------------------------------------------
    // QueryVersion
    // --------------------------------------------------------------------
    public static void checkQueryVersionCanBeEdited(HasLifecycle resource) throws MetamacException {
        ProcStatusEnumUtils.checkPossibleProcStatus(resource, procStatusForEditQueryVersion);
    }

    // --------------------------------------------------------------------
    // DatasetVersion
    // --------------------------------------------------------------------
    public static void checkDatasetVersionCanImportDatasources(HasLifecycle resource, BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto) throws MetamacException {
        if (basicVersionableStatisticalResourceDto == null || !Boolean.TRUE.equals(basicVersionableStatisticalResourceDto.getAutomaticLifeCicle())) {
            ProcStatusEnumUtils.checkPossibleProcStatus(resource, procStatusForImportDatasourcesInDatasetVersion);
        }
    }

    // --------------------------------------------------------------------
    // PublicationVersion
    // --------------------------------------------------------------------
    public static void checkStatisticalResourceStructureCanBeEdited(PublicationVersion resource) throws MetamacException {
        ProcStatusEnumUtils.checkPossibleProcStatus(resource, procStatusForEditPublicationStructure);
    }

    // --------------------------------------------------------------------
    // MultidatasetVersion
    // --------------------------------------------------------------------
    public static void checkStatisticalResourceStructureCanBeEdited(MultidatasetVersion resource) throws MetamacException {
        ProcStatusEnumUtils.checkPossibleProcStatus(resource, procStatusForEditMultidatasetStructure);
    }
}
