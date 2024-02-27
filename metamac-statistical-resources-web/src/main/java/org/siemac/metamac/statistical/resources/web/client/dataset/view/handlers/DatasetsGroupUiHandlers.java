package org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.dto.datasets.CategorisationDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.web.shared.criteria.VersionableStatisticalResourceWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.gwtplatform.mvp.client.UiHandlers;

public interface DatasetsGroupUiHandlers extends BaseUiHandlers, UiHandlers {

    public void goToDatasetInGroupMetadata();
    public void goToDatasetCategorisations();
    public void updateDatasets(String urn, DatasetVersionDto datasetChangedMetadataDto, List<CategorisationDto> categorisations);
    public void retrieveDatasets(int firstResult, int maxResults, VersionableStatisticalResourceWebCriteria criteria, ProcStatusEnum status);
    public void retrieveStatisticalOperationsForDatasetSelection();

    public void showWaitPopup();
    public void hideWaitPopup();
    public void showUpdateResults(MetamacWebException notificationException);
}
