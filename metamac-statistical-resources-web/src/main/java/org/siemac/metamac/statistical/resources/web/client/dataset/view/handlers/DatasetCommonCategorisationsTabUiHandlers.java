package org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers;

import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

public interface DatasetCommonCategorisationsTabUiHandlers extends BaseUiHandlers {

    void retrieveCategorySchemesForCategorisations(int firstResult, int maxResults, MetamacWebCriteria categorySchemeWebCriteria);
    void retrieveCategoriesForCategorisations(int firstResult, int maxResults, SrmItemRestCriteria categoryWebCriteria);
}
