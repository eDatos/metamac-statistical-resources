package org.siemac.metamac.statistical.resources.web.client.publication.view.handlers;

import org.siemac.metamac.statistical.resources.core.dto.NameableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.web.shared.criteria.StatisticalResourceWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

public interface PublicationStructureTabUiHandlers extends BaseUiHandlers {

    void saveElement(String publicationVersionUrn, NameableStatisticalResourceDto element);
    void deleteElement(String publicationVersionUrn, String elementUrn);
    void updateElementLocation(String publicationVersionUrn, String elementUrn, String parentTargetUrn, Long orderInLevel);
    void goToLastVersion(String urn);

    void retrieveDatasetsForCubes(int firstResult, int maxResults, StatisticalResourceWebCriteria criteria);
    void retrieveStatisticalOperationsForDatasetSelection();

    void retrieveQueriesForCubes(int firstResult, int maxResults, StatisticalResourceWebCriteria criteria);
    void retrieveCollectionsForCubes(int firstResult, int maxResults, StatisticalResourceWebCriteria criteria);
    void retrieveStatisticalOperationsForQuerySelection();
    void retrieveStatisticalOperationsForCollectionSelection();

    void retrieveMultidatasetsForCubes(int firstResult, int maxResults, StatisticalResourceWebCriteria criteria);
    void retrieveStatisticalOperationsForMultidatasetSelection();

    // Importation

    void resourceImportationFailed(String errorMessage);
    void resourceImportationSucceed(String fileName, String publicationVersionUrn);

    void showWaitPopup();


    void goToRelatedDatasetQuery(String urn);
}
