package org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers;

import java.util.Date;
import java.util.List;

public interface DatasetCategorisationsTabUiHandlers extends DatasetCommonCategorisationsTabUiHandlers {

    void createCategorisations(String datasetVersionUrn, List<String> categoryUrns);
    void deleteCategorisations(String datasetVersionUrn, List<String> urns);
    void endCategorisationsValidity(String datasetVersionUrn, List<String> urn, Date validTo);
}
