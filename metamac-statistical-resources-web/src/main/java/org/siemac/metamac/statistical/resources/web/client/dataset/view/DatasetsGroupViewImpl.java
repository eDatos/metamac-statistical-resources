package org.siemac.metamac.statistical.resources.web.client.dataset.view;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.datasets.CategorisationDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.base.widgets.CustomTabSet;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetInGroupCategorisationsTabPresenter.DatasetInGroupCategorisationsTabView;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetInGroupMetadataTabPresenter.DatasetInGroupMetadataTabView;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetsGroupPresenter;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetsGroupUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.DatasetVersionsSectionStack;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.WarningLabel;

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

    private VLayout                              panel;

    private TitleLabel                           titleLabel;
    private InformationLabel                     informationLabel;
    private WarningLabel                         warningLabel;

    private DatasetVersionsSectionStack          versionsSectionStack;

    private CustomTabSet                         tabSet;
    private Tab                                  datasetInGroupMetadataTab;
    private Tab                                  datasetInGroupCategorisationsTab;

    protected ToolStrip                          toolStrip;

    // button
    protected CustomToolStripButton              saveButton;
    private DatasetInGroupMetadataTabView        datasetInGroupMetadataTabView;
    private DatasetInGroupCategorisationsTabView datasetInGroupCategorisationsTabView;

    @Inject
    public DatasetsGroupViewImpl(DatasetInGroupMetadataTabView datasetInGroupMetadataTabView, DatasetInGroupCategorisationsTabView datasetInGroupCategorisationsTabView) {
        panel = new VLayout();
        this.datasetInGroupMetadataTabView = datasetInGroupMetadataTabView;
        this.datasetInGroupCategorisationsTabView = datasetInGroupCategorisationsTabView;

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

        versionsSectionStack = new DatasetVersionsSectionStack(getConstants().datasetVersionsSelected(), getConstants().updateDatasetVersionsInGroupStatus());

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
                List<String> datasetsUrnsUpdate = versionsSectionStack.getAllDatasetUrns();
                DatasetVersionDto datasetDto = datasetInGroupMetadataTabView.getDatasetVersionMetadata();
                if (datasetDto != null) {
                    List<CategorisationDto> categorisations = datasetInGroupCategorisationsTabView.getCategorisations();
                    getUiHandlers().updateDatasets(datasetsUrnsUpdate, datasetDto, categorisations);
                }
            }
        };

    }

    /*
     * (non-Javadoc)
     * @see org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetsGroupPresenter.DatasetsGroupView#initChildViews()
     * It is necessary the form are cleaned (each tab form) when the user updates a dataset group and he leaves the actual screen and does this operation again. In this case, the forms must be clean
     */
    @Override
    public void initChildViews() {
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
        clearWarningLabel();
        setTitleLabelContents(datasetVersionDto);
        setInformationLabelContents(datasetVersionDto);
        tabSet.show();
    }

    @Override
    public void setDatasetVersionsSelected(List<DatasetVersionBaseDto> datasetVersionBaseDtos) {
        versionsSectionStack.setDatasetVersions(datasetVersionBaseDtos);
    }

    @Override
    public void showUnauthorizedResourceWarningMessage() {
        clearTitleLabel();
        clearInformationLabel();
        tabSet.hide();
        setWarningLabelContents(getMessages().lifeCycleResourceRetrieveOperationNotAllowed(StatisticalResourcesWeb.getCurrentUser().getUserId()));
    }

    private void setTitleLabelContents(DatasetVersionDto datasetVersionDto) {
        titleLabel.setContents(InternationalStringUtils.getLocalisedString(datasetVersionDto.getTitle()));
        titleLabel.show();
    }

    private void setWarningLabelContents(String message) {
        warningLabel.setContents(message);
        warningLabel.show();
    }

    private void setInformationLabelContents(DatasetVersionDto datasetVersionDto) {
        if (BooleanUtils.isTrue(datasetVersionDto.getIsTaskInBackground())) {
            String message = getMessages().datasetVersionInProcessInBackground();
            informationLabel.setContents(message);
            informationLabel.show();
        } else {
            clearInformationLabel();
        }
    }

    private void clearInformationLabel() {
        informationLabel.setContents(StringUtils.EMPTY);
        informationLabel.hide();
    }

    private void clearWarningLabel() {
        warningLabel.setContents(StringUtils.EMPTY);
        warningLabel.hide();
    }

    private void clearTitleLabel() {
        titleLabel.setContents(StringUtils.EMPTY);
        titleLabel.hide();
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

    // TODO EDATOS-4385 QUITAR SI NO SE VA A USAR
    @Override
    public void refreshStatusUpdateDatasetVersionInProgress(String datasetUrn, StreamMessageStatusEnum status) {
        versionsSectionStack.refreshStatusDatasetVersion(datasetUrn, status);

    }
}
