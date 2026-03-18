package org.siemac.metamac.statistical.resources.web.client.multidataset.view.handlers;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionDto;
import org.siemac.metamac.statistical.resources.web.client.base.view.handlers.StatisticalResourceUiHandlers;
import org.siemac.metamac.statistical.resources.web.shared.criteria.VersionableStatisticalResourceWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

public interface MultidatasetMetadataTabUiHandlers extends StatisticalResourceUiHandlers, BaseUiHandlers {

    void saveMultidataset(MultidatasetVersionDto multidatasetDto);
    void deleteMultidataset(String urn);

    void previewData(MultidatasetVersionDto multidatasetDto);

    // RELATED PUBLICATIONS

    void retrieveMultidatasetsForReplaces(int firstResult, int maxResults, VersionableStatisticalResourceWebCriteria criteria);
    void retrieveStatisticalOperationsForReplacesSelection();

    // LIFECYCLE

    void sendToProductionValidation(MultidatasetVersionDto multidataset);
    void sendToDiffusionValidation(MultidatasetVersionDto multidataset);
    void rejectValidation(MultidatasetVersionDto multidataset, String reasonOfRejection);

    void publish(MultidatasetVersionDto multidataset);
    void version(MultidatasetVersionDto multidataset, VersionTypeEnum versionType);

    void updateGeoCacheRelatedResources(MultidatasetVersionDto multidataset);

    // TODO METAMAC-2715 - Realizar la notificación a Kafka de los recursos Multidataset
    // void resendStreamMessage(MultidatasetVersionDto multidatasetVersionDto);
}
