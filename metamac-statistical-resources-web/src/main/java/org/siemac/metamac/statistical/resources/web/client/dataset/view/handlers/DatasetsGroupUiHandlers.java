package org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.dto.datasets.CategorisationDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface DatasetsGroupUiHandlers extends UiHandlers {

    public void goToDatasetInGroupMetadata();
    public void goToDatasetCategorisations();
    public void updateDatasets(String urn, DatasetVersionDto datasetChangedMetadataDto, List<CategorisationDto> categorisations);

    public void showWaitPopup();
    public void hideWaitPopup();
}
