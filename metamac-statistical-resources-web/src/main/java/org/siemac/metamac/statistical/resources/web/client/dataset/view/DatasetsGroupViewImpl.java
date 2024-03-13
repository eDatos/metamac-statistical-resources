package org.siemac.metamac.statistical.resources.web.client.dataset.view;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.CategorisationDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.statistical.resources.web.client.base.widgets.CustomTabSet;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetInGroupCategorisationsTabPresenter.DatasetInGroupCategorisationsTabView;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetInGroupMetadataTabPresenter.DatasetInGroupMetadataTabView;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetsGroupPresenter;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetsGroupUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.DatasetVersionsSectionStack;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.widgets.windows.search.SearchMultipleStatisticalRelatedResourcePaginatedWindow;
import org.siemac.metamac.statistical.resources.web.shared.criteria.VersionableStatisticalResourceWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionsResult;
import org.siemac.metamac.statistical.resources.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.WarningLabel;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class DatasetsGroupViewImpl extends ViewWithUiHandlers<DatasetsGroupUiHandlers> implements DatasetsGroupPresenter.DatasetsGroupView {

    private VLayout                                                 panel;

    private TitleLabel                                              titleLabel;
    private InformationLabel                                        informationLabel;
    private WarningLabel                                            warningLabel;

    private DatasetVersionsSectionStack                             versionsSectionStack;

    private CustomTabSet                                            tabSet;
    private Tab                                                     datasetInGroupMetadataTab;
    private Tab                                                     datasetInGroupCategorisationsTab;

    protected ToolStrip                                             toolStrip;

    // button
    protected CustomToolStripButton                                 saveButton;
    protected CustomToolStripButton                                 addDatasetButton;
    protected CustomToolStripButton                                 cleanDatasetButton;
    private DatasetInGroupMetadataTabView                           datasetInGroupMetadataTabView;
    private DatasetInGroupCategorisationsTabView                    datasetInGroupCategorisationsTabView;
    private ResultDatasetMetadata                                   resultDatasetMetadata = new ResultDatasetMetadata();

    private SearchMultipleStatisticalRelatedResourcePaginatedWindow searchDatasetVersionsWindow;
    private ProcStatusEnum                                          statusDatasets;

    @Inject
    public DatasetsGroupViewImpl(DatasetInGroupMetadataTabView datasetInGroupMetadataTabView, DatasetInGroupCategorisationsTabView datasetInGroupCategorisationsTabView) {
        panel = new VLayout();
        this.datasetInGroupMetadataTabView = datasetInGroupMetadataTabView;
        this.datasetInGroupCategorisationsTabView = datasetInGroupCategorisationsTabView;
        this.resultDatasetMetadata = new ResultDatasetMetadata();

        titleLabel = new TitleLabel(new String());
        titleLabel.setVisible(false);

        informationLabel = new InformationLabel();
        informationLabel.setVisible(false);

        warningLabel = new WarningLabel();
        warningLabel.setVisible(false);
        warningLabel.setAlign(Alignment.CENTER);
        warningLabel.setMargin(50);
        warningLabel.setIconSize(24);

        //
        // DATASET VERSIONS
        //

        versionsSectionStack = new DatasetVersionsSectionStack(getConstants().datasetVersionsSelected(), getConstants().updateDatasetVersionsInGroupStatus(), true);

        // TABS

        tabSet = new CustomTabSet();

        datasetInGroupMetadataTab = new Tab(getConstants().datasetMetadata());
        datasetInGroupMetadataTab.setPane((Canvas) datasetInGroupMetadataTabView.asWidget());

        datasetInGroupCategorisationsTab = new Tab(getConstants().datasetCategorisations());
        datasetInGroupCategorisationsTab.setPane((Canvas) datasetInGroupCategorisationsTabView.asWidget());

        tabSet.setTabs(datasetInGroupMetadataTab, datasetInGroupCategorisationsTab);

        //
        // PANEL LAYOUT
        //

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.setMembersMargin(5);
        subPanel.addMember(versionsSectionStack);

        VLayout tabSubPanel = new VLayout();
        createToolTrip();
        tabSubPanel.addMember(toolStrip);

        tabSubPanel.addMember(tabSet);
        tabSubPanel.setMargin(15);
        subPanel.addMember(tabSubPanel);

        panel.addMember(subPanel);

        bindEvents();
    }

    private void createToolTrip() {
        toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        saveButton = createSaveButton();
        toolStrip.addButton(saveButton);

        addDatasetButton = createAddDatasetButton();
        toolStrip.addButton(addDatasetButton);

        cleanDatasetButton = createCleanDatasetButton();
        toolStrip.addButton(cleanDatasetButton);
    }

    private CustomToolStripButton createSaveButton() {
        CustomToolStripButton button = new CustomToolStripButton(getConstants().actionSave(), RESOURCE.saveListGrid().getURL());
        button.addClickHandler(getSaveButtonClickHandler());
        return button;
    }

    private ClickHandler getSaveButtonClickHandler() {
        return new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (checkCanSave()) {
                    versionsSectionStack.resetAllStatusDatasetVersion();
                    resultDatasetMetadata = new ResultDatasetMetadata();
                    resultDatasetMetadata.setDatasetsUrnsUpdate(versionsSectionStack.getAllDatasetUrns());
                    resultDatasetMetadata.setDatasetDto(datasetInGroupMetadataTabView.getDatasetVersionMetadata());
                    if (resultDatasetMetadata.getDatasetDto() != null) {
                        resultDatasetMetadata.setCategorisations(datasetInGroupCategorisationsTabView.getCategorisations());
                        getUiHandlers().updateDatasets(resultDatasetMetadata.getNextUrn(), resultDatasetMetadata.getDatasetDto(), resultDatasetMetadata.getCategorisations());
                    }
                }
            }
        };
    }

    private boolean checkCanSave() {
        if (versionsSectionStack.getNumberSelectedDatasets() == 0) {
            getUiHandlers().showMessageMaxDatasetsExceeded(getMessages().datasetNoEntriesUpdateInGroup());
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * @see org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetsGroupPresenter.DatasetsGroupView#initChildViews()
     * It is necessary the form are cleaned (each tab form) when the user updates a dataset group and he leaves the actual screen and does this operation again. In this case, the forms must be clean
     */
    @Override
    public void initChildViews() {
        resultDatasetMetadata.clear();
        datasetInGroupCategorisationsTabView.clearSelectedCategorisations();
        datasetInGroupMetadataTabView.initMetadataInGroupForm();
    }

    private void bindEvents() {
        datasetInGroupMetadataTab.addTabSelectedHandler(new TabSelectedHandler() {

            @Override
            public void onTabSelected(TabSelectedEvent event) {
                getUiHandlers().goToDatasetInGroupMetadata();
            }
        });

        datasetInGroupCategorisationsTab.addTabSelectedHandler(new TabSelectedHandler() {

            @Override
            public void onTabSelected(TabSelectedEvent event) {
                getUiHandlers().goToDatasetCategorisations();
            }
        });
    }

    @Override
    public void setDataset(DatasetVersionDto datasetVersionDto) {
        tabSet.show();
    }

    @Override
    public void setDatasetVersionsSelected(List<DatasetVersionBaseDto> datasetVersionBaseDtos) {
        setStatusDatataset(datasetVersionBaseDtos);
        versionsSectionStack.setDatasetVersions(datasetVersionBaseDtos, DatasetDS.CODE);
    }

    @Override
    public void selectMetadataTab() {
        tabSet.selectTab(datasetInGroupMetadataTab);
    }

    @Override
    public void selectCategorisationsTab() {
        tabSet.selectTab(datasetInGroupCategorisationsTab);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void refreshStatusUpdateDatasetVersionInProgress(String datasetUrn, StreamMessageStatusEnum status, MetamacWebException notificationException) {
        try {
            if (notificationException != null) {
                status = StreamMessageStatusEnum.FAILED;
                resultDatasetMetadata.addNotificationExceptions(notificationException);

            }

            versionsSectionStack.refreshStatusDatasetVersion(datasetUrn, status);
            callSynchronouslyUpdateNextDatasetVersion();

        } catch (Exception e) {
            if (resultDatasetMetadata != null) {
                getUiHandlers().showUpdateResults(resultDatasetMetadata.getNotificationException());
                resultDatasetMetadata.clear();
            }
        }
    }

    private void setStatusDatataset(List<DatasetVersionBaseDto> datasetVersionBaseDtos) {
        if (datasetVersionBaseDtos != null && !datasetVersionBaseDtos.isEmpty()) {
            statusDatasets = datasetVersionBaseDtos.get(0).getProcStatus();
        } else {
            statusDatasets = ProcStatusEnum.DRAFT;
        }
    }

    private void callSynchronouslyUpdateNextDatasetVersion() {
        String nextUrn = resultDatasetMetadata.getNextUrn();
        if (nextUrn != null) {
            getUiHandlers().updateDatasets(nextUrn, resultDatasetMetadata.getDatasetDto(), resultDatasetMetadata.getCategorisations());
        } else {
            getUiHandlers().showUpdateResults(resultDatasetMetadata.getNotificationException());
            resultDatasetMetadata.clear();
        }
    }

    protected class ResultDatasetMetadata {

        List<String>              datasetsUrnsUpdate      = null;
        int                       posInDatasetsUrnsUpdate = 0;
        DatasetVersionDto         datasetDto              = null;
        List<CategorisationDto>   categorisations         = null;
        List<MetamacWebException> notificationExceptions  = new ArrayList<MetamacWebException>();

        public void setNotificationExceptions(List<MetamacWebException> notificationExceptions) {
            this.notificationExceptions = notificationExceptions;
        }

        public void addNotificationExceptions(MetamacWebException metamacWebException) {
            this.notificationExceptions.add(metamacWebException);
        }

        public List<String> getDatasetsUrnsUpdate() {
            return datasetsUrnsUpdate;
        }
        public void setDatasetsUrnsUpdate(List<String> datasetsUrnsUpdate) {
            this.datasetsUrnsUpdate = datasetsUrnsUpdate;
        }
        public DatasetVersionDto getDatasetDto() {
            return datasetDto;
        }
        public void setDatasetDto(DatasetVersionDto datasetDto) {
            this.datasetDto = datasetDto;
        }
        public List<CategorisationDto> getCategorisations() {
            return categorisations;
        }
        public void setCategorisations(List<CategorisationDto> categorisations) {
            this.categorisations = categorisations;
        }
        public int getPosInDatasetsUrnsUpdate() {
            return posInDatasetsUrnsUpdate;
        }
        public void setPosInDatasetsUrnsUpdate(int posInDatasetsUrnsUpdate) {
            this.posInDatasetsUrnsUpdate = posInDatasetsUrnsUpdate;
        }

        protected String getNextUrn() {
            if (posInDatasetsUrnsUpdate < datasetsUrnsUpdate.size()) {
                return datasetsUrnsUpdate.get(posInDatasetsUrnsUpdate++);
            } else {
                return null;
            }
        }

        protected void clear() {
            posInDatasetsUrnsUpdate = 0;
            datasetsUrnsUpdate = null;
            datasetDto = null;
            categorisations = null;
            notificationExceptions.clear();
        }

        protected MetamacWebException getNotificationException() {

            if (notificationExceptions.isEmpty()) {
                return null;
            }

            MetamacWebException metamacWebException = new MetamacWebException();
            for (MetamacWebException exception : notificationExceptions) {
                metamacWebException.getWebExceptionItems().addAll(exception.getWebExceptionItems());
            }
            return metamacWebException;
        }
    }

    private CustomToolStripButton createAddDatasetButton() {
        CustomToolStripButton button = new CustomToolStripButton(getConstants().actionAdd(),
                org.siemac.metamac.statistical.resources.web.client.resources.GlobalResources.RESOURCE.newListGrid().getURL());
        button.addClickHandler(getAddDatasetButtonClickHandler());
        return button;
    }

    private ClickHandler getAddDatasetButtonClickHandler() {
        return new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                searchDatasetVersionsWindow = new SearchMultipleStatisticalRelatedResourcePaginatedWindow(getConstants().resourceSelection(), StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS,
                        new SearchPaginatedAction<VersionableStatisticalResourceWebCriteria>() {

                            @Override
                            public void retrieveResultSet(int firstResult, int maxResults, VersionableStatisticalResourceWebCriteria webCriteria) {
                                getUiHandlers().retrieveDatasets(firstResult, maxResults, webCriteria, statusDatasets);
                            }

                        });

                // Load resources (to populate the selection window)
                getUiHandlers().retrieveDatasets(0, StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS, searchDatasetVersionsWindow.getSearchCriteria(), statusDatasets);

                searchDatasetVersionsWindow.setSaveAction(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        List<RelatedResourceDto> selectedResource = searchDatasetVersionsWindow.getSelectedResources();
                        List<DatasetVersionBaseDto> newSelectedDataset = RelatedResourceUtils.getRelatedResourceDtosAsDatasetVersionBaseDtos(selectedResource, statusDatasets);
                        if (!newSelectedDataset.isEmpty()) {
                            Integer totalSelectedDataset = versionsSectionStack.getNumberSelectedDatasets() + newSelectedDataset.size();
                            if (totalSelectedDataset <= CommonUtils.getMaxNumberOfUpdatedDatasetInGroup()) {
                                versionsSectionStack.addDatasetVersions(newSelectedDataset);
                            } else {
                                getUiHandlers().showMessageMaxDatasetsExceeded(getMessages().datasetMaxNumberUpdateInGroupExceeded(String.valueOf(CommonUtils.getMaxNumberOfUpdatedDatasetInGroup())));
                            }
                        }
                        searchDatasetVersionsWindow.markForDestroy();
                    }
                });
            }
        };
    }

    private CustomToolStripButton createCleanDatasetButton() {
        CustomToolStripButton button = new CustomToolStripButton(getConstants().actionClearDataset(), RESOURCE.clear().getURL());
        button.addClickHandler(getCleanDatasetButtonClickHandler());
        return button;
    }

    private ClickHandler getCleanDatasetButtonClickHandler() {
        return new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                initChildViews();
            }
        };

    }

    public void setDatasetsForAdd(GetDatasetVersionsResult result) {
        List<RelatedResourceDto> relatedResourceDtos = RelatedResourceUtils.getDatasetVersionBaseDtosAsRelatedResourceDtos(result.getDatasetVersionBaseDtos());
        if (searchDatasetVersionsWindow != null) {
            searchDatasetVersionsWindow.setResources(relatedResourceDtos);
            searchDatasetVersionsWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), relatedResourceDtos.size(), result.getTotalResults());
        }
    }

    @Override
    public void showUnauthorizedResourceWarningMessage() {
        // Without impl

    }
}
