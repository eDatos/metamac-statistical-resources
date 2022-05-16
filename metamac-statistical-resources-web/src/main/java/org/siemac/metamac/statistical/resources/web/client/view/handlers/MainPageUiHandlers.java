package org.siemac.metamac.statistical.resources.web.client.view.handlers;

import com.gwtplatform.mvp.client.UiHandlers;

public interface MainPageUiHandlers extends UiHandlers {

    void closeSession();

    void onNavigationPaneSectionHeaderClicked(String name);
    void onNavigationPaneSectionClicked(String name);

    void goToDatasets();

    void goToPublications();

    void goToQueries();

    void goToMultidatasets();

    void openHelpUrl();

    void updateTerritoriesCache();
}
