package org.siemac.metamac.statistical.resources.web.client.publication.view;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionBaseDto;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesDefaults;
import org.siemac.metamac.statistical.resources.web.client.base.view.StatisticalResourceBaseListViewImpl;
import org.siemac.metamac.statistical.resources.web.client.base.widgets.NewStatisticalResourceWindow;
import org.siemac.metamac.statistical.resources.web.client.publication.model.ds.PublicationDS;
import org.siemac.metamac.statistical.resources.web.client.publication.model.record.PublicationRecord;
import org.siemac.metamac.statistical.resources.web.client.publication.presenter.PublicationListPresenter;
import org.siemac.metamac.statistical.resources.web.client.publication.utils.PublicationClientSecurityUtils;
import org.siemac.metamac.statistical.resources.web.client.publication.view.handlers.PublicationListUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.publication.widgets.NewPublicationWindow;
import org.siemac.metamac.statistical.resources.web.client.publication.widgets.PublicationVersionSearchSectionStack;
import org.siemac.metamac.statistical.resources.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.StatisticalResourcesRecordUtils;
import org.siemac.metamac.statistical.resources.web.client.widgets.windows.ValidationRejectionWindow;
import org.siemac.metamac.statistical.resources.web.shared.criteria.PublicationVersionWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListResult;
import org.siemac.metamac.web.common.client.widgets.BaseAdvancedSearchSectionStack;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

public class PublicationListViewImpl extends StatisticalResourceBaseListViewImpl<PublicationListUiHandlers> implements PublicationListPresenter.PublicationListView {

    private PublicationVersionSearchSectionStack searchSectionStack;
    private NewPublicationWindow                 newPublicationWindow;

    @Inject
    public PublicationListViewImpl() {
        super();

        // Publication list

        listGrid.getListGrid().setDataSource(new PublicationDS());
        listGrid.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String urn = ((PublicationRecord) event.getRecord()).getAttribute(PublicationDS.URN);
                    getUiHandlers().goToPublication(urn);
                }
            }
        });
        listGrid.getListGrid().setFields(ResourceFieldUtils.getPublicationListGridFields());

        // Delete confirmation window

        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deletePublication(getSelectedResourcesUrns());
                deleteConfirmationWindow.hide();
            }
        });
    }

    @Override
    public void setUiHandlers(PublicationListUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        searchSectionStack.setUiHandlers(uiHandlers);
    }

    @Override
    public void setPublicationPaginatedList(List<PublicationVersionBaseDto> publicationVersionBaseDtos, int firstResult, int totalResults) {
        PublicationRecord[] records = StatisticalResourcesRecordUtils.getPublicationRecords(publicationVersionBaseDtos);
        listGrid.getListGrid().setData(records);
        listGrid.refreshPaginationInfo(firstResult, publicationVersionBaseDtos.size(), totalResults);
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.clearSearchSection();
    }

    @Override
    protected NewStatisticalResourceWindow getNewStatisticalResourceWindow() {
        return newPublicationWindow;
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
        getUiHandlers().retrievePublications(firstResult, maxResults, getPublicationVersionWebCriteria());
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
                newPublicationWindow = new NewPublicationWindow(getConstants().publicationCreate());
                newPublicationWindow.setUiHandlers(getUiHandlers());
                newPublicationWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newPublicationWindow.validateForm()) {
                            getUiHandlers().createPublication(newPublicationWindow.getNewPublicationDto());
                            newPublicationWindow.destroy();
                        }
                    }
                });
                newPublicationWindow.setDefaultLanguage(StatisticalResourcesDefaults.defaultLanguage);
                newPublicationWindow.setDefaultMaintainer(StatisticalResourcesDefaults.defaultAgency);
            }
        };
    }

    // Sent to production validation

    @Override
    protected ClickHandler getSendToProductionValidationClickHandler() {
        return new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                List<PublicationVersionBaseDto> publicationVersionDtos = StatisticalResourcesRecordUtils.getPublicationVersionBaseDtosFromListGridRecords(listGrid.getListGrid().getSelectedRecords());
                getUiHandlers().sendToProductionValidation(publicationVersionDtos);
            }
        };
    }

    // Send to diffusion validation

    @Override
    protected ClickHandler getSendToDiffusionValidationClickHandler() {
        return new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                List<PublicationVersionBaseDto> publicationVersionDtos = StatisticalResourcesRecordUtils.getPublicationVersionBaseDtosFromListGridRecords(listGrid.getListGrid().getSelectedRecords());
                getUiHandlers().sendToDiffusionValidation(publicationVersionDtos);
            }
        };
    }

    // Reject validation

    @Override
    protected ClickHandler getRejectValidationClickHandler() {
        return new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                final List<PublicationVersionBaseDto> publicationVersionDtos = StatisticalResourcesRecordUtils
                        .getPublicationVersionBaseDtosFromListGridRecords(listGrid.getListGrid().getSelectedRecords());
                final ValidationRejectionWindow window = new ValidationRejectionWindow(getConstants().lifeCycleRejectValidation());
                window.show();
                window.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        String reasonOfRejection = window.getReasonOfRejection();
                        window.markForDestroy();
                        getUiHandlers().rejectValidation(publicationVersionDtos, reasonOfRejection);
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
                List<PublicationVersionBaseDto> publicationVersionDtos = StatisticalResourcesRecordUtils.getPublicationVersionBaseDtosFromListGridRecords(listGrid.getListGrid().getSelectedRecords());
                getUiHandlers().publish(publicationVersionDtos);
            }
        };
    }

    // Version

    @Override
    protected void version(VersionTypeEnum versionType) {
        List<PublicationVersionBaseDto> publicationVersionBaseDtos = StatisticalResourcesRecordUtils.getPublicationVersionBaseDtosFromListGridRecords(listGrid.getListGrid().getSelectedRecords());
        getUiHandlers().version(publicationVersionBaseDtos, versionType);
    }

    //
    // LISTGRID ACTIONS
    //

    @Override
    protected boolean canCreate() {
        return PublicationClientSecurityUtils.canCreatePublication();
    }

    @Override
    protected boolean canDelete(ListGridRecord record) {
        return PublicationClientSecurityUtils.canDeletePublicationVersion(getDtoFromRecord(record));
    }

    @Override
    protected boolean canSendToProductionValidation(ListGridRecord record) {
        return PublicationClientSecurityUtils.canSendPublicationVersionToProductionValidation(getDtoFromRecord(record));
    }

    @Override
    protected boolean canSendToDiffusionValidation(ListGridRecord record) {
        return PublicationClientSecurityUtils.canSendPublicationVersionToDiffusionValidation(getDtoFromRecord(record));
    }

    @Override
    protected boolean canRejectValidation(ListGridRecord record) {
        return PublicationClientSecurityUtils.canSendPublicationVersionToValidationRejected(getDtoFromRecord(record));
    }

    @Override
    protected boolean canPublish(ListGridRecord record) {
        return PublicationClientSecurityUtils.canPublishPublicationVersion(getDtoFromRecord(record));
    }

    @Override
    protected boolean canVersion(ListGridRecord record) {
        return PublicationClientSecurityUtils.canVersionPublication(getDtoFromRecord(record));
    }

    //
    // SEARCH
    //

    @Override
    protected BaseAdvancedSearchSectionStack createAdvacedSearchSectionStack() {
        searchSectionStack = new PublicationVersionSearchSectionStack();
        return searchSectionStack;
    }

    @Override
    public PublicationVersionWebCriteria getPublicationVersionWebCriteria() {
        return searchSectionStack.getPublicationVersionWebCriteria();
    }

    @Override
    public void setStatisticalOperationsForSearchSection(GetStatisticalOperationsPaginatedListResult result) {
        searchSectionStack.setStatisticalOperations(result);
    }

    private PublicationVersionBaseDto getDtoFromRecord(ListGridRecord record) {
        PublicationRecord pubRecord = (PublicationRecord) record;
        return pubRecord.getPublicationVersionBaseDto();
    }
}
