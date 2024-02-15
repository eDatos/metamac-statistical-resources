package org.siemac.metamac.statistical.resources.web.client.dataset.view;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.base.widgets.CustomTabSet;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetCategorisationsTabPresenter.DatasetCategorisationsTabView;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetInGroupMetadataTabPresenter.DatasetInGroupMetadataTabView;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetsGroupPresenter;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetsGroupUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.DatasetVersionsSectionStack;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.WarningLabel;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class DatasetsGroupViewImpl extends ViewWithUiHandlers<DatasetsGroupUiHandlers> implements DatasetsGroupPresenter.DatasetsGroupView {

    private VLayout                     panel;

    private TitleLabel                  titleLabel;
    private InformationLabel            informationLabel;
    private WarningLabel                warningLabel;

    private DatasetVersionsSectionStack versionsSectionStack;

    private CustomTabSet                tabSet;
    private Tab                         datasetInGroupMetadataTab;
    private Tab                         datasetCategorisationsTab;

    @Inject
    public DatasetsGroupViewImpl(DatasetInGroupMetadataTabView datasetInGroupMetadataTabView, DatasetCategorisationsTabView datasetCategorisationsTabView) {
        panel = new VLayout();

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

        versionsSectionStack = new DatasetVersionsSectionStack(getConstants().datasetVersions());

        // TABS

        tabSet = new CustomTabSet();

        datasetInGroupMetadataTab = new Tab(getConstants().datasetMetadata());
        datasetInGroupMetadataTab.setPane((Canvas) datasetInGroupMetadataTabView.asWidget());

        datasetCategorisationsTab = new Tab(getConstants().datasetCategorisations());
        datasetCategorisationsTab.setPane((Canvas) datasetCategorisationsTabView.asWidget());

        tabSet.setTabs(datasetInGroupMetadataTab, datasetCategorisationsTab);

        //
        // PANEL LAYOUT
        //

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.setMembersMargin(5);
        subPanel.addMember(versionsSectionStack);

        VLayout tabSubPanel = new VLayout();
        tabSubPanel.addMember(titleLabel);
        tabSubPanel.addMember(informationLabel);
        tabSubPanel.addMember(warningLabel);
        tabSubPanel.addMember(tabSet);
        tabSubPanel.setMargin(15);
        subPanel.addMember(tabSubPanel);

        panel.addMember(subPanel);

        bindEvents();
    }

    private void bindEvents() {
        datasetInGroupMetadataTab.addTabSelectedHandler(new TabSelectedHandler() {

            @Override
            public void onTabSelected(TabSelectedEvent event) {
                getUiHandlers().goToDatasetInGroupMetadata();
            }
        });

        datasetCategorisationsTab.addTabSelectedHandler(new TabSelectedHandler() {

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
        tabSet.selectTab(datasetCategorisationsTab);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }
}
