package org.siemac.metamac.statistical.resources.web.client.multidataset.view.handlers;

import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetCubeDto;
import org.siemac.metamac.statistical.resources.web.shared.criteria.StatisticalResourceWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

public interface MultidatasetStructureTabUiHandlers extends BaseUiHandlers {

    void goToLastVersion(String urn);

    void retrieveDatasetsForCubes(int firstResult, int maxResults, StatisticalResourceWebCriteria criteria);
    void retrieveStatisticalOperationsForDatasetSelection();

    void retrieveQueriesForCubes(int firstResult, int maxResults, StatisticalResourceWebCriteria criteria);
    void retrieveStatisticalOperationsForQuerySelection();

    void saveMultidatasetCube(String multidatasetVersionUrn, MultidatasetCubeDto cube);
    void deleteCube(String urn, String multidatasetCubeUrn);
    void updateCubeLocation(String multidatasetVersionUrn, String multidatasetCubeUrn, Long orderInMultidataset);

    void goToRelatedDatasetQuery(String urn);
}
