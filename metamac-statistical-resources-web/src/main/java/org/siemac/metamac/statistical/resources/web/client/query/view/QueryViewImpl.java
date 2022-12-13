package org.siemac.metamac.statistical.resources.web.client.query.view;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesDefaults;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.base.utils.RequiredFieldUtils;
import org.siemac.metamac.statistical.resources.web.client.query.model.record.QueryRecord;
import org.siemac.metamac.statistical.resources.web.client.query.presenter.QueryPresenter;
import org.siemac.metamac.statistical.resources.web.client.query.view.handlers.QueryUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.query.view.widgets.QueryMainFormLayout;
import org.siemac.metamac.statistical.resources.web.client.query.view.widgets.QueryVersionsSectionStack;
import org.siemac.metamac.statistical.resources.web.client.query.view.widgets.forms.QueryIdentifiersCreationForm;
import org.siemac.metamac.statistical.resources.web.client.query.view.widgets.forms.QueryProductionDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.query.view.widgets.forms.QueryProductionDescriptorsForm;
import org.siemac.metamac.statistical.resources.web.client.query.view.widgets.forms.QueryResourceRelationDescriptorsForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.VersionWindow;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.LifeCycleResourceContentDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.LifeCycleResourceContentDescriptorsForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.LifeCycleResourceLifeCycleForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.LifeCycleResourceVersionEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.LifeCycleResourceVersionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.NameableResourceIdentifiersEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.NameableResourceIdentifiersForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourceThematicContentClassifiersEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourceThematicContentClassifiersForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.windows.ValidationRejectionWindow;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionsResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetAgenciesPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetAgencySchemesPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.widgets.WarningLabel;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class QueryViewImpl extends ViewWithUiHandlers<QueryUiHandlers> implements QueryPresenter.QueryView {

    private VLayout                   panel;

    private QueryVersionsSectionStack versionsSectionStack;
    private QueryFormPanel            queryFormPanel;

    private WarningLabel              warningLabel;

    @Inject
    public QueryViewImpl() {
        super();

        // QUERY VERSIONS

        versionsSectionStack = new QueryVersionsSectionStack(getConstants().queryVersions());
        versionsSectionStack.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                String urn = ((QueryRecord) event.getRecord()).getUrn();
                getUiHandlers().retrieveQuery(urn);
            }
        });

        // QUERY

        panel = new VLayout();
        panel.setHeight100();

        warningLabel = new WarningLabel();
        warningLabel.setVisible(false);
        warningLabel.setAlign(Alignment.CENTER);
        warningLabel.setMargin(50);
        warningLabel.setIconSize(24);

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.setMembersMargin(5);
        subPanel.addMember(versionsSectionStack);
        subPanel.addMember(warningLabel);

        queryFormPanel = new QueryFormPanel();
        queryFormPanel.setMargin(15);
        queryFormPanel.setWidth("99%");
        subPanel.addMember(queryFormPanel);

        panel.addMember(subPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(QueryUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        queryFormPanel.productionDescriptorsEditionForm.setUiHandlers(uiHandlers);
        queryFormPanel.productionDescriptorsForm.setUiHandlers(uiHandlers);
        queryFormPanel.lifeCycleForm.setUiHandlers(uiHandlers);
        queryFormPanel.lifeCycleEditionForm.setUiHandlers(uiHandlers);

        queryFormPanel.resourceRelationDescriptorsEditionForm.setUiHandlers(uiHandlers);
        queryFormPanel.resourceRelationDescriptorsForm.setUiHandlers(uiHandlers);
    }

    @Override
    public void showUnauthorizedResourceWarningMessage() {
        queryFormPanel.hide();
        setWarningLabelContents(getMessages().lifeCycleResourceRetrieveOperationNotAllowed(StatisticalResourcesWeb.getCurrentUser().getUserId()));
    }

    @Override
    public void setQueryDto(QueryVersionDto queryDto) {
        clearWarningLabel();
        queryFormPanel.setQuery(queryDto);
        queryFormPanel.show();
    }

    @Override
    public void newQueryDto() {
        clearWarningLabel();
        clearQueryVersions();
        queryFormPanel.createQuery();
        queryFormPanel.show();
    }

    @Override
    public void setQueryVersionsAndSelectCurrent(String currentQueryUrn, List<QueryVersionBaseDto> queryVersionBaseDtos) {
        versionsSectionStack.setQueryVersions(queryVersionBaseDtos);
        versionsSectionStack.selectQueryVersion(currentQueryUrn);
    }

    @Override
    public void setDatasetsForQuery(GetDatasetVersionsResult result) {
        List<RelatedResourceDto> relatedResourceDtos = RelatedResourceUtils.getDatasetVersionBaseDtosAsRelatedResourceDtos(result.getDatasetVersionBaseDtos());
        queryFormPanel.productionDescriptorsEditionForm.setDatasetsForQuery(relatedResourceDtos, result.getFirstResultOut(), relatedResourceDtos.size(), result.getTotalResults());
    }

    @Override
    public void setStatisticalOperationsForDatasetSelection(GetStatisticalOperationsPaginatedListResult result) {
        queryFormPanel.productionDescriptorsEditionForm.setStatisticalOperationsForDatasetSelection(result.getOperationsList());
    }

    @Override
    public void setDatasetDimensionsIds(List<String> datasetDimensionsIds) {
        queryFormPanel.productionDescriptorsEditionForm.setDatasetDimensionsIds(datasetDimensionsIds);
    }

    @Override
    public void setDatasetDimensionCodes(String dimensionId, List<CodeItemDto> codesDimension) {
        queryFormPanel.productionDescriptorsEditionForm.setDatasetDimensionCodes(dimensionId, codesDimension);
    }

    @Override
    public void setAgencySchemesForMaintainer(GetAgencySchemesPaginatedListResult result) {
        queryFormPanel.productionDescriptorsEditionForm.setAgencySchemesForMaintainer(result.getAgencySchemes(), result.getFirstResultOut(), result.getAgencySchemes().size(),
                result.getTotalResults());
    }

    @Override
    public void setAgenciesForMaintainer(GetAgenciesPaginatedListResult result) {
        queryFormPanel.productionDescriptorsEditionForm.setExternalItemsForMaintainer(result.getAgencies(), result.getFirstResultOut(), result.getAgencies().size(), result.getTotalResults());
    }

    private void setWarningLabelContents(String message) {
        warningLabel.setContents(message);
        warningLabel.show();
    }

    private void clearWarningLabel() {
        warningLabel.setContents(StringUtils.EMPTY);
        warningLabel.hide();
    }

    private void clearQueryVersions() {
        versionsSectionStack.setQueryVersions(new ArrayList<QueryVersionBaseDto>());
    }

    private class QueryFormPanel extends VLayout {

        private QueryMainFormLayout                                      mainFormLayout;

        private NameableResourceIdentifiersForm                          identifiersForm;
        private LifeCycleResourceContentDescriptorsForm                  contentDescriptorsForm;
        private StatisticalResourceThematicContentClassifiersForm        thematicContentClassifiersForm;
        private QueryProductionDescriptorsForm                           productionDescriptorsForm;
        private QueryResourceRelationDescriptorsForm                     resourceRelationDescriptorsForm;
        private LifeCycleResourceLifeCycleForm                           lifeCycleForm;
        private LifeCycleResourceVersionForm                             versionForm;

        // only creation
        private QueryIdentifiersCreationForm                             identifiersCreationForm;
        // Only edition
        private NameableResourceIdentifiersEditionForm                   identifiersEditionForm;

        private LifeCycleResourceContentDescriptorsEditionForm           contentDescriptorsEditionForm;
        private StatisticalResourceThematicContentClassifiersEditionForm thematicContentClassifiersEditionForm;
        private QueryProductionDescriptorsEditionForm                    productionDescriptorsEditionForm;
        private QueryResourceRelationDescriptorsForm                     resourceRelationDescriptorsEditionForm;
        private LifeCycleResourceLifeCycleForm                           lifeCycleEditionForm;
        private LifeCycleResourceVersionEditionForm                      versionEditionForm;

        private QueryVersionDto                                          queryVersionDto;

        public QueryFormPanel() {
            super();
            setWidth("99%");

            mainFormLayout = new QueryMainFormLayout();
            mainFormLayout.setMargin(0);

            this.addMember(mainFormLayout);
            createViewForm();
            createEditionForm();

            bindMainFormLayoutEvents();
            hide();
        }

        private void bindMainFormLayoutEvents() {
            mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                    identifiersForm.setTranslationsShowed(translationsShowed);
                    identifiersCreationForm.setTranslationsShowed(translationsShowed);
                    identifiersEditionForm.setTranslationsShowed(translationsShowed);

                    contentDescriptorsForm.setTranslationsShowed(translationsShowed);
                    contentDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                    thematicContentClassifiersForm.setTranslationsShowed(translationsShowed);
                    thematicContentClassifiersEditionForm.setTranslationsShowed(translationsShowed);

                    productionDescriptorsForm.setTranslationsShowed(translationsShowed);
                    productionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                    resourceRelationDescriptorsForm.setTranslationsShowed(translationsShowed);
                    resourceRelationDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                    lifeCycleForm.setTranslationsShowed(translationsShowed);
                    lifeCycleEditionForm.setTranslationsShowed(translationsShowed);

                    versionForm.setTranslationsShowed(translationsShowed);
                    versionEditionForm.setTranslationsShowed(translationsShowed);
                }
            });

            // Save

            mainFormLayout.getSave().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (isCreationMode()) {
                        if (identifiersCreationForm.validate(false) && productionDescriptorsEditionForm.validate(false) && versionEditionForm.validate(false)
                                && contentDescriptorsEditionForm.validate(false)) {
                            // See: METAMAC-2516
                            // Two invokes to getXXXDto() is needed for Chrome, please don't remove this two call fix.
                            getQuery();
                            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                                @Override
                                public void execute() {
                                    getUiHandlers().saveQuery(getQuery());
                                }
                            });

                        }
                    } else {
                        if (identifiersEditionForm.validate(false) && productionDescriptorsEditionForm.validate(false) && versionEditionForm.validate(false)
                                && contentDescriptorsEditionForm.validate(false)) {
                            // See: METAMAC-2516
                            // Two invokes to getXXXDto() is needed for Chrome, please don't remove this two call fix.
                            getQuery();
                            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                                @Override
                                public void execute() {
                                    getUiHandlers().saveQuery(getQuery());
                                }
                            });

                        }
                    }
                }
            });

            // Delete

            mainFormLayout.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    getUiHandlers().deleteQuery(queryVersionDto.getUrn());
                }
            });

            // Cancel

            mainFormLayout.getCancelToolStripButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (isCreationMode()) {
                        QueryFormPanel.this.hide();
                        getUiHandlers().goToQueries();
                    }
                }
            });

            // Life cycle

            mainFormLayout.getProductionValidationButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    getUiHandlers().sendToProductionValidation(queryVersionDto);
                }
            });
            mainFormLayout.getDiffusionValidationButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    getUiHandlers().sendToDiffusionValidation(queryVersionDto);
                }
            });
            mainFormLayout.getRejectValidationButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {

                    final ValidationRejectionWindow window = new ValidationRejectionWindow(getConstants().lifeCycleRejectValidation());
                    window.show();
                    window.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                        @Override
                        public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                            String reasonOfRejection = window.getReasonOfRejection();
                            window.markForDestroy();
                            getUiHandlers().rejectValidation(queryVersionDto, reasonOfRejection);
                        }
                    });
                }
            });

            mainFormLayout.getPublishButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    getUiHandlers().publish(queryVersionDto);
                }
            });

            mainFormLayout.getResendStreamMessageButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    getUiHandlers().resendStreamMessage(queryVersionDto);
                }
            });

            mainFormLayout.getVersioningButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    final VersionWindow versionWindow = new VersionWindow(getConstants().lifeCycleVersioning());
                    versionWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                        @Override
                        public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                            if (versionWindow.validateForm()) {
                                getUiHandlers().version(queryVersionDto, versionWindow.getSelectedVersion());
                                versionWindow.destroy();
                            }
                        }
                    });
                }
            });

            // Preview data
            mainFormLayout.getPreviewButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    getUiHandlers().previewData(queryVersionDto);
                }
            });
        }

        private void createViewForm() {
            identifiersForm = new NameableResourceIdentifiersForm();
            thematicContentClassifiersForm = new StatisticalResourceThematicContentClassifiersForm();
            contentDescriptorsForm = new LifeCycleResourceContentDescriptorsForm();
            productionDescriptorsForm = new QueryProductionDescriptorsForm();
            resourceRelationDescriptorsForm = new QueryResourceRelationDescriptorsForm();
            lifeCycleForm = new LifeCycleResourceLifeCycleForm();
            versionForm = new LifeCycleResourceVersionForm();
            mainFormLayout.addViewCanvas(identifiersForm);
            mainFormLayout.addViewCanvas(contentDescriptorsForm);
            mainFormLayout.addViewCanvas(thematicContentClassifiersForm);
            mainFormLayout.addViewCanvas(productionDescriptorsForm);
            mainFormLayout.addViewCanvas(resourceRelationDescriptorsForm);
            mainFormLayout.addViewCanvas(lifeCycleForm);
            mainFormLayout.addViewCanvas(versionForm);
        }

        private void createEditionForm() {
            identifiersEditionForm = new NameableResourceIdentifiersEditionForm();
            identifiersCreationForm = new QueryIdentifiersCreationForm();
            contentDescriptorsEditionForm = new LifeCycleResourceContentDescriptorsEditionForm();
            thematicContentClassifiersEditionForm = new StatisticalResourceThematicContentClassifiersEditionForm();
            productionDescriptorsEditionForm = new QueryProductionDescriptorsEditionForm();
            resourceRelationDescriptorsEditionForm = new QueryResourceRelationDescriptorsForm();
            lifeCycleEditionForm = new LifeCycleResourceLifeCycleForm();
            versionEditionForm = new LifeCycleResourceVersionEditionForm();

            mainFormLayout.addEditionCanvas(identifiersEditionForm);
            mainFormLayout.addEditionCanvas(identifiersCreationForm);
            mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
            mainFormLayout.addEditionCanvas(thematicContentClassifiersEditionForm);
            mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
            mainFormLayout.addEditionCanvas(resourceRelationDescriptorsEditionForm);
            mainFormLayout.addEditionCanvas(lifeCycleEditionForm);
            mainFormLayout.addEditionCanvas(versionEditionForm);
        }

        private void createQuery() {
            queryVersionDto = new QueryVersionDto();
            queryVersionDto.setMaintainer(StatisticalResourcesDefaults.defaultAgency);

            mainFormLayout.setTitleLabelContents(getConstants().queryNew());
            mainFormLayout.setEditionMode();
            fillViewForm(queryVersionDto);
            fillEditionForm(queryVersionDto);

            mainFormLayout.redraw();
            show();
        }

        private void setQuery(QueryVersionDto queryVersionDto) {
            this.queryVersionDto = queryVersionDto;

            mainFormLayout.setQueryVersion(queryVersionDto);
            mainFormLayout.setViewMode();

            fillViewForm(queryVersionDto);
            fillEditionForm(queryVersionDto);
            mainFormLayout.redraw();
            show();
        }

        private void fillViewForm(QueryVersionDto queryDto) {
            identifiersForm.setNameableStatisticalResourceDto(queryDto);
            contentDescriptorsForm.setLifeCycleResource(queryDto);
            thematicContentClassifiersForm.setStatisticalResourceDto(queryDto);

            // EDATOS-3113 WORKAROUND, this form is continuously rebuilt
            mainFormLayout.removeViewCanvas(productionDescriptorsForm);
            productionDescriptorsForm = new QueryProductionDescriptorsForm();
            productionDescriptorsForm.setUiHandlers(getUiHandlers());
            productionDescriptorsForm.setQueryDto(queryDto);
            mainFormLayout.addViewCanvas(productionDescriptorsForm, 4);
            // EDATOS-3113 WORKAROUND

            resourceRelationDescriptorsForm.setQueryDto(queryDto);
            lifeCycleForm.setLifeCycleStatisticalResourceDto(queryDto);
            versionForm.setLifeCycleStatisticalResourceDto(queryDto);
        }

        private void fillEditionForm(QueryVersionDto queryVersionDto) {

            String[] requiredFieldsToNextProcStatus = RequiredFieldUtils.getQueryRequiredFieldsToNextProcStatus(queryVersionDto.getProcStatus());

            identifiersEditionForm.setNameableStatisticalResourceDto(queryVersionDto);
            identifiersEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

            identifiersCreationForm.setNameableStatisticalResourceDto(queryVersionDto);
            identifiersCreationForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

            contentDescriptorsEditionForm.setLifeCycleStatisticalResourceDto(queryVersionDto);
            contentDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

            thematicContentClassifiersEditionForm.setStatisticalResourceDto(queryVersionDto);
            thematicContentClassifiersEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

            // WORKAROUND, this form is continuously rebuilt
            mainFormLayout.removeEditionCanvas(productionDescriptorsEditionForm);
            productionDescriptorsEditionForm = new QueryProductionDescriptorsEditionForm();
            productionDescriptorsEditionForm.setUiHandlers(getUiHandlers());
            productionDescriptorsEditionForm.setQueryDto(queryVersionDto);
            productionDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);
            mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm, 4);
            // WORKAROUND

            resourceRelationDescriptorsEditionForm.setQueryDto(queryVersionDto);
            resourceRelationDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

            lifeCycleEditionForm.setLifeCycleStatisticalResourceDto(queryVersionDto);
            lifeCycleEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

            versionEditionForm.setLifeCycleStatisticalResourceDto(queryVersionDto);
            versionEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

            if (isCreationMode()) {
                identifiersEditionForm.hide();
                lifeCycleEditionForm.hide();
                identifiersCreationForm.show();
                versionEditionForm.hide();
                resourceRelationDescriptorsEditionForm.hide();
                thematicContentClassifiersEditionForm.hide();
            } else {
                identifiersEditionForm.show();
                lifeCycleEditionForm.show();
                identifiersCreationForm.hide();
                versionEditionForm.show();
                resourceRelationDescriptorsEditionForm.show();
                thematicContentClassifiersEditionForm.show();
            }

        }
        private QueryVersionDto getQuery() {
            if (isCreationMode()) {
                queryVersionDto = (QueryVersionDto) identifiersCreationForm.getNameableStatisticalResourceDto(queryVersionDto);
            } else {
                queryVersionDto = (QueryVersionDto) identifiersEditionForm.getNameableStatisticalResourceDto(queryVersionDto);
            }
            queryVersionDto = (QueryVersionDto) contentDescriptorsEditionForm.getLifeCycleStatisticalResourceDto(queryVersionDto);
            queryVersionDto = productionDescriptorsEditionForm.getQueryDto(queryVersionDto);
            queryVersionDto = (QueryVersionDto) versionEditionForm.getLifeCycleStatisticalResourceDto(queryVersionDto);
            return queryVersionDto;
        }

        private boolean isCreationMode() {
            return StringUtils.isEmpty(queryVersionDto.getUrn());
        }
    }
}
