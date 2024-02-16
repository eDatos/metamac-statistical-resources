package org.siemac.metamac.statistical.resources.web.client.multidataset.view;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesDefaults;
import org.siemac.metamac.statistical.resources.web.client.base.view.StatisticalResourceBaseListViewImpl;
import org.siemac.metamac.statistical.resources.web.client.base.widgets.NewStatisticalResourceWindow;
import org.siemac.metamac.statistical.resources.web.client.multidataset.model.ds.MultidatasetDS;
import org.siemac.metamac.statistical.resources.web.client.multidataset.model.record.MultidatasetRecord;
import org.siemac.metamac.statistical.resources.web.client.multidataset.presenter.MultidatasetListPresenter;
import org.siemac.metamac.statistical.resources.web.client.multidataset.utils.MultidatasetClientSecurityUtils;
import org.siemac.metamac.statistical.resources.web.client.multidataset.view.handlers.MultidatasetListUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.multidataset.widgets.MultidatasetVersionSearchSectionStack;
import org.siemac.metamac.statistical.resources.web.client.multidataset.widgets.NewMultidatasetWindow;
import org.siemac.metamac.statistical.resources.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.StatisticalResourcesRecordUtils;
import org.siemac.metamac.statistical.resources.web.client.widgets.windows.ValidationRejectionWindow;
import org.siemac.metamac.statistical.resources.web.shared.criteria.MultidatasetVersionWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListResult;
import org.siemac.metamac.web.common.client.widgets.BaseAdvancedSearchSectionStack;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

public class MultidatasetListViewImpl extends StatisticalResourceBaseListViewImpl<MultidatasetListUiHandlers> implements MultidatasetListPresenter.MultidatasetListView {

    private MultidatasetVersionSearchSectionStack searchSectionStack;
    private NewMultidatasetWindow                 newMultidatasetWindow;

    @Inject
    public MultidatasetListViewImpl() {
        super();

        // Multidataset list

        listGrid.getListGrid().setDataSource(new MultidatasetDS());
        listGrid.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String urn = ((MultidatasetRecord) event.getRecord()).getAttribute(MultidatasetDS.URN);
                    getUiHandlers().goToMultidataset(urn);
                }
            }
        });
        listGrid.getListGrid().setFields(ResourceFieldUtils.getMultidatasetListGridFields());

        // Delete confirmation window

        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteMultidataset(getSelectedResourcesUrns());
                deleteConfirmationWindow.hide();
            }
        });
    }

    @Override
    public void setUiHandlers(MultidatasetListUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        searchSectionStack.setUiHandlers(uiHandlers);
    }

    @Override
    public void setMultidatasetPaginatedList(List<MultidatasetVersionBaseDto> multidatasetVersionBaseDtos, int firstResult, int totalResults) {
        MultidatasetRecord[] records = StatisticalResourcesRecordUtils.getMultidatasetRecords(multidatasetVersionBaseDtos);
        listGrid.getListGrid().setData(records);
        listGrid.refreshPaginationInfo(firstResult, multidatasetVersionBaseDtos.size(), totalResults);
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.clearSearchSection();
    }

    @Override
    protected NewStatisticalResourceWindow getNewStatisticalResourceWindow() {
        return newMultidatasetWindow;
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    //
    // LISTGRID
    //

    @Override
    public void retrieveResultSet(int firstResult, int maxResults) {
        getUiHandlers().retrieveMultidatasets(firstResult, maxResults, getMultidatasetVersionWebCriteria());
    }

    //
    // LISTGRID BUTTONS
    //

    // Create

    @Override
    public ClickHandler getNewButtonClickHandler() {
        return new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                newMultidatasetWindow = new NewMultidatasetWindow(getConstants().multidatasetCreate());
                newMultidatasetWindow.setUiHandlers(getUiHandlers());
                newMultidatasetWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newMultidatasetWindow.validateForm()) {
                            getUiHandlers().createMultidataset(newMultidatasetWindow.getNewMultidatasetDto());
                            newMultidatasetWindow.destroy();
                        }
                    }
                });
                newMultidatasetWindow.setDefaultLanguage(StatisticalResourcesDefaults.defaultLanguage);
                newMultidatasetWindow.setDefaultMaintainer(StatisticalResourcesDefaults.defaultAgency);
            }
        };
    }

    // Sent to production validation

    @Override
    protected ClickHandler getSendToProductionValidationClickHandler() {
        return new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                List<MultidatasetVersionBaseDto> multidatasetVersionDtos = StatisticalResourcesRecordUtils
                        .getMultidatasetVersionBaseDtosFromListGridRecords(listGrid.getListGrid().getSelectedRecords());
                getUiHandlers().sendToProductionValidation(multidatasetVersionDtos);
            }
        };
    }

    // Send to diffusion validation

    @Override
    protected ClickHandler getSendToDiffusionValidationClickHandler() {
        return new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                List<MultidatasetVersionBaseDto> multidatasetVersionDtos = StatisticalResourcesRecordUtils
                        .getMultidatasetVersionBaseDtosFromListGridRecords(listGrid.getListGrid().getSelectedRecords());
                getUiHandlers().sendToDiffusionValidation(multidatasetVersionDtos);
            }
        };
    }

    // Reject validation

    @Override
    protected ClickHandler getRejectValidationClickHandler() {
        return new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                final List<MultidatasetVersionBaseDto> multidatasetVersionDtos = StatisticalResourcesRecordUtils
                        .getMultidatasetVersionBaseDtosFromListGridRecords(listGrid.getListGrid().getSelectedRecords());
                final ValidationRejectionWindow window = new ValidationRejectionWindow(getConstants().lifeCycleRejectValidation());
                window.show();
                window.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        String reasonOfRejection = window.getReasonOfRejection();
                        window.markForDestroy();
                        getUiHandlers().rejectValidation(multidatasetVersionDtos, reasonOfRejection);
                    }
                });

            }
        };
    }

    // Publish

    @Override
    protected ClickHandler getPublishClickHandler() {
        return new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                List<MultidatasetVersionBaseDto> multidatasetVersionDtos = StatisticalResourcesRecordUtils
                        .getMultidatasetVersionBaseDtosFromListGridRecords(listGrid.getListGrid().getSelectedRecords());
                getUiHandlers().publish(multidatasetVersionDtos);
            }
        };
    }

    // Version

    @Override
    protected void version(VersionTypeEnum versionType) {
        List<MultidatasetVersionBaseDto> multidatasetVersionBaseDtos = StatisticalResourcesRecordUtils.getMultidatasetVersionBaseDtosFromListGridRecords(listGrid.getListGrid().getSelectedRecords());
        getUiHandlers().version(multidatasetVersionBaseDtos, versionType);
    }

    //
    // LISTGRID ACTIONS
    //

    @Override
    protected boolean canCreate() {
        return MultidatasetClientSecurityUtils.canCreateMultidataset();
    }

    @Override
    protected boolean canDelete(ListGridRecord record) {
        return MultidatasetClientSecurityUtils.canDeleteMultidatasetVersion(getDtoFromRecord(record));
    }

    @Override
    protected boolean canSendToProductionValidation(ListGridRecord record) {
        return MultidatasetClientSecurityUtils.canSendMultidatasetVersionToProductionValidation(getDtoFromRecord(record));
    }

    @Override
    protected boolean canSendToDiffusionValidation(ListGridRecord record) {
        return MultidatasetClientSecurityUtils.canSendMultidatasetVersionToDiffusionValidation(getDtoFromRecord(record));
    }

    @Override
    protected boolean canRejectValidation(ListGridRecord record) {
        return MultidatasetClientSecurityUtils.canSendMultidatasetVersionToValidationRejected(getDtoFromRecord(record));
    }

    @Override
    protected boolean canPublish(ListGridRecord record) {
        return MultidatasetClientSecurityUtils.canPublishMultidatasetVersion(getDtoFromRecord(record));
    }

    @Override
    protected boolean canVersion(ListGridRecord record) {
        return MultidatasetClientSecurityUtils.canVersionMultidataset(getDtoFromRecord(record));
    }

    //
    // SEARCH
    //

    @Override
    protected BaseAdvancedSearchSectionStack createAdvacedSearchSectionStack() {
        searchSectionStack = new MultidatasetVersionSearchSectionStack();
        return searchSectionStack;
    }

    @Override
    public MultidatasetVersionWebCriteria getMultidatasetVersionWebCriteria() {
        return searchSectionStack.getMultidatasetVersionWebCriteria();
    }

    @Override
    public void setStatisticalOperationsForSearchSection(GetStatisticalOperationsPaginatedListResult result) {
        searchSectionStack.setStatisticalOperations(result);
    }

    private MultidatasetVersionBaseDto getDtoFromRecord(ListGridRecord record) {
        MultidatasetRecord pubRecord = (MultidatasetRecord) record;
        return pubRecord.getMultidatasetVersionBaseDto();
    }
}
