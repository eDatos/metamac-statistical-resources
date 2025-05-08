package org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers;

import java.util.List;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.web.client.base.utils.SiemacMetadataExternalField;
import org.siemac.metamac.statistical.resources.web.client.base.view.handlers.NewStatisticalResourceUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.dataset.utils.DatasetMetadataExternalField;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DatasetVersionWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DsdWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

public interface DatasetListUiHandlers extends NewStatisticalResourceUiHandlers {

    void goToDataset(String code);
    void goToDatasetsInGroup(String selectedDatasetIdentifiers);
    void createDataset(DatasetVersionDto datasetDto);
    void deleteDatasets(List<String> urnsFromSelected);
    void retrieveDatasets(int firstResult, int maxResults, DatasetVersionWebCriteria criteria);
    public void showMessageMaxDatasetsExceeded();

    // LifeCycle

    void sendToProductionValidation(List<DatasetVersionBaseDto> datasetVersionBaseDtos);
    void sendToDiffusionValidation(List<DatasetVersionBaseDto> datasetVersionBaseDtos);
    void rejectValidation(List<DatasetVersionBaseDto> datasetVersionBaseDtos, String reasonOfRejection);
    void publish(List<DatasetVersionBaseDto> datasetVersionBaseDtos);

    void version(List<DatasetVersionBaseDto> datasetVersionBaseDtos, VersionTypeEnum versionType);

    // DSD related actions

    void retrieveDsdsForRelatedDsd(int firstResult, int maxResults, DsdWebCriteria criteria);
    void retrieveStatisticalOperationsForDsdSelection();

    // Importation

    void datasourcesImportationFailed(String errorMessage);
    void createNewDatasetFailed(String errorMessage);
    void datasourcesImportationSucceed(String fileName);

    // Related resources

    void retrieveStatisticalOperationsForSearchSection(int firstResult, int maxResults, MetamacWebCriteria criteria);
    void retrieveGeographicGranularitiesForSearchSection(int firstResult, int maxResults, MetamacWebCriteria criteria);
    void retrieveTemporalGranularitiesForSearchSection(int firstResult, int maxResults, MetamacWebCriteria criteria);
    void retrieveStatisticalOperationsForDsdSelectionInSearchSection();
    void retrieveDsdsForSearchSection(int firstResult, int maxResults, DsdWebCriteria criteria);

    // DATA_PROVIDERS and SCHEMES
    void retrieveDataProviderSchemes(int firstResult, int maxResults, MetamacWebCriteria webCriteria, SiemacMetadataExternalField field);
    void retrieveDataProviderUnits(int firstResult, int maxResults, SrmItemRestCriteria webCriteria, SiemacMetadataExternalField field);

    // Time codes
    void retrieveTemporalCodesForField(int firstResult, int maxResults, MetamacWebCriteria webCriteria, DatasetMetadataExternalField updateFrequency);

}
