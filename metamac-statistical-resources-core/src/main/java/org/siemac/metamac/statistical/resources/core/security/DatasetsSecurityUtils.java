package org.siemac.metamac.statistical.resources.core.security;

import java.util.Date;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.sso.utils.SecurityUtils;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.security.shared.SharedDatasetsSecurityUtils;

public class DatasetsSecurityUtils extends SecurityUtils {

    // ------------------------------------------------------------------------
    // DATASOURCES
    // ------------------------------------------------------------------------

    public static void canCreateDatasource(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canCreateDatasource(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdateDatasource(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canUpdateDatasource(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveDatasourceByUrn(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canRetrieveDatasourceByUrn(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canDeleteDatasource(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canDeleteDatasource(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveDatasourcesByDatasetVersion(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canRetrieveDatasourcesByDatasetVersion(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveDatasourceDimensionRepresentationMappings(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canRetrieveDatasourceDimensionRepresentationMappings(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    // ------------------------------------------------------------------------
    // DATASETS
    // ------------------------------------------------------------------------

    public static void canFindDatasetsByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canFindDatasetsByCondition(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    // ------------------------------------------------------------------------
    // DATASETS VERSIONS
    // ------------------------------------------------------------------------

    public static void canCreateDataset(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canCreateDataset(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdateDatasetVersion(ServiceContext ctx, DatasetVersionDto datasetVersionDto) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canUpdateDatasetVersion(getMetamacPrincipal(ctx), datasetVersionDto.getStatisticalOperation().getCode(), datasetVersionDto.getProcStatus())) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canDeleteDatasetVersion(ServiceContext ctx, DatasetVersionDto datasetVersionDto) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canDeleteDatasetVersion(getMetamacPrincipal(ctx), datasetVersionDto.getStatisticalOperation().getCode(), datasetVersionDto.getProcStatus())) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canFindDatasetsVersionsByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canFindDatasetsVersionsByCondition(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveDatasetVersionByUrn(ServiceContext ctx, String operationCode, ProcStatusEnum procStatus) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canRetrieveDatasetVersionByUrn(getMetamacPrincipal(ctx), operationCode, procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveDatasetVersions(ServiceContext ctx) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canRetrieveDatasetVersions(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canPublishDatasetVersion(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canPublishDataset(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canResendPublishedDatasetVersionStreamMessage(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canPublishDataset(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canResendPublishedQueryVersionXMessage(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canPublishDataset(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canVersionDataset(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canVersionDataset(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canSendDatasetVersionToProductionValidation(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canSendDatasetVersionToProductionValidation(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canSendDatasetVersionToDiffusionValidation(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canSendDatasetVersionToDiffusionValidation(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canSendDatasetVersionToValidationRejected(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canSendDatasetVersionToValidationRejected(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveLatestDatasetVersion(ServiceContext ctx, String operationCode, ProcStatusEnum procStatus) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canRetrieveLatestDatasetVersion(getMetamacPrincipal(ctx), operationCode, procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveLatestPublishedDatasetVersion(ServiceContext ctx) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canRetrieveLatestPublishedDatasetVersion(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveDatasetVersionDimensionsIds(ServiceContext ctx) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canRetrieveDatasetVersionDimensionsIds(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveCoverageForDatasetVersionDimension(ServiceContext ctx) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canRetrieveCoverageForDatasetVersionDimension(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canFilterCoverageForDatasetVersionDimension(ServiceContext ctx) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canFilterCoverageForDatasetVersionDimension(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canFindStatisticOfficialities(ServiceContext ctx) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canFindStatisticOfficialities(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canFindPurposes(ServiceContext ctx) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canFindPurposes(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canImportDatasourcesInDatasetVersion(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canImportDatasourcesInDatasetVersion(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canImportDatasourcesInStatisticalOperation(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canImportDatasourcesInStatisticalOperation(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveDatasetVersionMainCoverages(ServiceContext ctx) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canRetrieveDatasetVersionMainCoverages(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCreateAttributeInstance(ServiceContext ctx, DatasetVersionDto datasetVersionDto) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canCreateAttributeInstance(getMetamacPrincipal(ctx), datasetVersionDto.getStatisticalOperation().getCode(), datasetVersionDto.getProcStatus())) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdateAttributeInstance(ServiceContext ctx, DatasetVersionDto datasetVersionDto) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canUpdateAttributeInstance(getMetamacPrincipal(ctx), datasetVersionDto.getStatisticalOperation().getCode(), datasetVersionDto.getProcStatus())) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canDeleteAttributeInstance(ServiceContext ctx, DatasetVersionDto datasetVersionDto) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canDeleteAttributeInstance(getMetamacPrincipal(ctx), datasetVersionDto.getStatisticalOperation().getCode(), datasetVersionDto.getProcStatus())) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveAttributeInstances(ServiceContext ctx, DatasetVersionDto datasetVersionDto) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canRetrieveAttributeInstances(getMetamacPrincipal(ctx), datasetVersionDto.getStatisticalOperation().getCode(), datasetVersionDto.getProcStatus())) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    // ------------------------------------------------------------------------
    // CATEGORISATIONS
    // ------------------------------------------------------------------------

    public static void canCreateCategorisation(ServiceContext ctx, DatasetVersionDto datasetVersionDto) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canCreateCategorisation(getMetamacPrincipal(ctx), datasetVersionDto.getStatisticalOperation().getCode(), datasetVersionDto.getProcStatus())) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveCategorisationByUrn(ServiceContext ctx, Categorisation categorisation) throws MetamacException {
        String operationCode = categorisation.getDatasetVersion().getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        ProcStatusEnum procStatus = categorisation.getDatasetVersion().getSiemacMetadataStatisticalResource().getEffectiveProcStatus();

        if (!SharedDatasetsSecurityUtils.canRetrieveCategorisationByUrn(getMetamacPrincipal(ctx), operationCode, procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canDeleteCategorisation(ServiceContext ctx, Categorisation categorisation) throws MetamacException {
        String operationCode = categorisation.getDatasetVersion().getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        ProcStatusEnum procStatus = categorisation.getDatasetVersion().getSiemacMetadataStatisticalResource().getEffectiveProcStatus();

        Date validFrom = categorisation != null && categorisation.getValidFromEffective() != null ? categorisation.getValidFromEffective().toDate() : null;
        if (!SharedDatasetsSecurityUtils.canDeleteDatasetCategorisation(getMetamacPrincipal(ctx), validFrom, operationCode, procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveCategorisationsByDatasetVersion(ServiceContext ctx, DatasetVersionDto datasetVersionDto) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canRetrieveCategorisationsByDatasetVersion(getMetamacPrincipal(ctx), datasetVersionDto.getStatisticalOperation().getCode(),
                datasetVersionDto.getProcStatus())) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canEndCategorisationValidity(ServiceContext ctx, Categorisation categorisation) throws MetamacException {
        String operationCode = categorisation.getDatasetVersion().getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        ProcStatusEnum procStatus = categorisation.getDatasetVersion().getSiemacMetadataStatisticalResource().getEffectiveProcStatus();

        Date validFrom = categorisation != null && categorisation.getValidFromEffective() != null ? categorisation.getValidFromEffective().toDate() : null;
        Date validTo = categorisation != null && categorisation.getValidToEffective() != null ? categorisation.getValidToEffective().toDate() : null;
        if (!SharedDatasetsSecurityUtils.canEndCategorisationValidity(getMetamacPrincipal(ctx), validFrom, validTo, operationCode, procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canExportDatasourcesTsv(ServiceContext ctx) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canExportDatasourcesTsv(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canExportAttributesTsv(ServiceContext ctx) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canExportAttributesTsv(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    // ------------------------------------------------------------------------
    // ADMIN MANAGEMENT
    // ------------------------------------------------------------------------

    public static void canUpdateGeographicCoverageVariableElementsCache(ServiceContext ctx) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canUpdateGeographicCoverageVariableElementsCache(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canResendAllKafkaMessages(ServiceContext ctx) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canResendAllKafkaMessages(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdateGeoCacheRelatedResource(ServiceContext ctx) throws MetamacException {
        if (!SharedDatasetsSecurityUtils.canUpdateGeoCacheRelatedResource(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

}
