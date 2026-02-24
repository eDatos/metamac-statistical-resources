package org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.dataset.domain.DecimalSeparatorTypeEnum;
import org.siemac.metamac.web.common.shared.criteria.SrmExternalResourceRestCriteria;

import com.gwtplatform.mvp.client.UiHandlers;

public interface DatasetDatasourcesTabUiHandlers extends UiHandlers {

    void deleteDatasources(List<String> datasourcesUrns, boolean deleteAttributes);
    void deleteAllDatasourcesNotUsed(String datasetUrn, boolean deleteAttributes);

    // Importation

    void datasourcesImportationFailed(String errorMessage);
    void datasourcesImportationSucceed(String fileName);
    void retrieveAlternativeCodelistsForVariable(String dimensionId, String variableUrn, int firstResult, int maxResults, SrmExternalResourceRestCriteria criteria);
    void retrieveDimensionVariablesForDataset(String urn);

    void showWaitPopup();

    void saveDataset(DatasetVersionDto datasetVersionDto);
    void createDatabaseDatasource(String urn, String tablename);

    void exportDatasources(String urn, DecimalSeparatorTypeEnum decimalSeparator);
}
