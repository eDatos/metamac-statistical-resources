package org.siemac.metamac.statistical.resources.web.client.multidataset.view;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionDto;
import org.siemac.metamac.statistical.resources.web.client.base.utils.RequiredFieldUtils;
import org.siemac.metamac.statistical.resources.web.client.base.view.StatisticalResourceMetadataBaseViewImpl;
import org.siemac.metamac.statistical.resources.web.client.multidataset.presenter.MultidatasetMetadataTabPresenter.MultidatasetMetadataTabView;
import org.siemac.metamac.statistical.resources.web.client.multidataset.view.handlers.MultidatasetMetadataTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.multidataset.widgets.MultidatasetMainFormLayout;
import org.siemac.metamac.statistical.resources.web.client.multidataset.widgets.forms.MultidatasetClassDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.multidataset.widgets.forms.MultidatasetClassDescriptorsForm;
import org.siemac.metamac.statistical.resources.web.client.multidataset.widgets.forms.MultidatasetContentDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.multidataset.widgets.forms.MultidatasetContentDescriptorsForm;
import org.siemac.metamac.statistical.resources.web.client.multidataset.widgets.forms.MultidatasetIdentifiersForm;
import org.siemac.metamac.statistical.resources.web.client.multidataset.widgets.forms.MultidatasetResourceRelationDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.multidataset.widgets.forms.MultidatasetResourceRelationDescriptorsForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.VersionWindow;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.LifeCycleResourceLifeCycleForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.LifeCycleResourceVersionEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.LifeCycleResourceVersionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.NameableResourceIdentifiersEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataCommonMetadataEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataCommonMetadataForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataIntellectualPropertyDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataIntellectualPropertyDescriptorsForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataLanguageEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataLanguageForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataProductionDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataProductionDescriptorsForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataPublicationDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataPublicationDescriptorsForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataThematicContentClassifiersEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataThematicContentClassifiersForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.windows.ValidationRejectionWindow;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.GetMultidatasetVersionsResult;
import org.siemac.metamac.statistical.resources.web.shared.utils.RelatedResourceUtils;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class MultidatasetMetadataTabViewImpl extends StatisticalResourceMetadataBaseViewImpl<MultidatasetMetadataTabUiHandlers> implements MultidatasetMetadataTabView {

    private VLayout                                                  panel;

    private MultidatasetMainFormLayout                               mainFormLayout;

    private MultidatasetIdentifiersForm                              identifiersForm;
    private MultidatasetContentDescriptorsForm                       contentDescriptorsForm;
    private SiemacMetadataCommonMetadataForm                         commonMetadataForm;
    private SiemacMetadataThematicContentClassifiersForm             thematicContentClassifiersForm;
    private SiemacMetadataLanguageForm                               languageForm;
    private SiemacMetadataProductionDescriptorsForm                  productionDescriptorsForm;
    private MultidatasetClassDescriptorsForm                         classDescriptorsForm;
    private MultidatasetResourceRelationDescriptorsForm              resourceRelationDescriptorsForm;
    private SiemacMetadataPublicationDescriptorsForm                 publicationDescriptorsForm;
    private LifeCycleResourceLifeCycleForm                           lifeCycleForm;
    private LifeCycleResourceVersionForm                             versionForm;
    private SiemacMetadataIntellectualPropertyDescriptorsForm        intellectualPropertyDescriptorsForm;

    private NameableResourceIdentifiersEditionForm                   identifiersEditionForm;
    private MultidatasetContentDescriptorsEditionForm                contentDescriptorsEditionForm;
    private SiemacMetadataCommonMetadataEditionForm                  commonMetadataEditionForm;
    private SiemacMetadataThematicContentClassifiersEditionForm      thematicContentClassifiersEditionForm;
    private SiemacMetadataLanguageEditionForm                        languageEditionForm;
    private SiemacMetadataProductionDescriptorsEditionForm           productionDescriptorsEditionForm;
    private MultidatasetClassDescriptorsEditionForm                  classDescriptorsEditionForm;
    private MultidatasetResourceRelationDescriptorsEditionForm       resourceRelationDescriptorsEditionForm;
    private SiemacMetadataPublicationDescriptorsEditionForm          publicationDescriptorsEditionForm;
    private LifeCycleResourceLifeCycleForm                           lifeCycleEditionForm;
    private LifeCycleResourceVersionEditionForm                      versionEditionForm;

    private SiemacMetadataIntellectualPropertyDescriptorsEditionForm intellectualPropertyDescriptorsEditionForm;

    private MultidatasetVersionDto                                   multidatasetVersionDto;

    @Inject
    public MultidatasetMetadataTabViewImpl() {
        panel = new VLayout();

        mainFormLayout = new MultidatasetMainFormLayout();
        bindMainFormLayoutEvents();
        createViewForm();
        createEditionForm();

        VLayout subPanel = new VLayout();
        subPanel.addMember(mainFormLayout);

        panel.addMember(subPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(MultidatasetMetadataTabUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        resourceRelationDescriptorsEditionForm.setUiHandlers(uiHandlers);
        resourceRelationDescriptorsForm.setUiHandlers(uiHandlers);
        lifeCycleForm.setUiHandlers(uiHandlers);
        lifeCycleEditionForm.setUiHandlers(uiHandlers);
        commonMetadataForm.setBaseUiHandlers(uiHandlers);
        commonMetadataEditionForm.setUiHandlers(uiHandlers);
        thematicContentClassifiersEditionForm.setUiHandlers(uiHandlers);
        productionDescriptorsEditionForm.setUiHandlers(uiHandlers);
        publicationDescriptorsEditionForm.setUiHandlers(uiHandlers);
        languageEditionForm.setUiHandlers(uiHandlers);
    }

    private void bindMainFormLayoutEvents() {
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                identifiersForm.setTranslationsShowed(translationsShowed);
                identifiersEditionForm.setTranslationsShowed(translationsShowed);

                contentDescriptorsForm.setTranslationsShowed(translationsShowed);
                contentDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                commonMetadataForm.setTranslationsShowed(translationsShowed);
                commonMetadataEditionForm.setTranslationsShowed(translationsShowed);

                thematicContentClassifiersForm.setTranslationsShowed(translationsShowed);
                thematicContentClassifiersEditionForm.setTranslationsShowed(translationsShowed);

                languageForm.setTranslationsShowed(translationsShowed);
                languageEditionForm.setTranslationsShowed(translationsShowed);

                productionDescriptorsForm.setTranslationsShowed(translationsShowed);
                productionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                classDescriptorsForm.setTranslationsShowed(translationsShowed);
                classDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                resourceRelationDescriptorsForm.setTranslationsShowed(translationsShowed);
                resourceRelationDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                publicationDescriptorsForm.setTranslationsShowed(translationsShowed);
                publicationDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                lifeCycleForm.setTranslationsShowed(translationsShowed);
                lifeCycleEditionForm.setTranslationsShowed(translationsShowed);

                versionForm.setTranslationsShowed(translationsShowed);
                versionEditionForm.setTranslationsShowed(translationsShowed);

                intellectualPropertyDescriptorsForm.setTranslationsShowed(translationsShowed);
                intellectualPropertyDescriptorsEditionForm.setTranslationsShowed(translationsShowed);
            }
        });

        // Save

        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false) && commonMetadataEditionForm.validate(false)
                        && productionDescriptorsEditionForm.validate(false) && classDescriptorsEditionForm.validate(false) && versionEditionForm.validate(false)
                        && resourceRelationDescriptorsEditionForm.validate(false) && publicationDescriptorsEditionForm.validate(false) && thematicContentClassifiersEditionForm.validate(false)
                        && languageEditionForm.validate(false) && intellectualPropertyDescriptorsEditionForm.validate(false)) {
                    // See: METAMAC-2516
                    // Two invokes to getXXXDto() is needed for Chrome, please don't remove this two call fix.
                    getMultidatasetDto();
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                        @Override
                        public void execute() {
                            getUiHandlers().saveMultidataset(getMultidatasetDto());
                        }
                    });

                }
            }
        });

        // Delete

        mainFormLayout.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteMultidataset(multidatasetVersionDto.getUrn());
            }
        });

        // Life cycle
        mainFormLayout.getProductionValidationButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().sendToProductionValidation(multidatasetVersionDto);
            }
        });
        mainFormLayout.getDiffusionValidationButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().sendToDiffusionValidation(multidatasetVersionDto);
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
                        getUiHandlers().rejectValidation(multidatasetVersionDto, reasonOfRejection);
                    }
                });
            }
        });
        mainFormLayout.getPublishButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().publish(multidatasetVersionDto);
            }
        });

        // TODO METAMAC-2715 - Realizar la notificación a Kafka de los recursos Multidataset
        // mainFormLayout.getResendStreamMessageButton().addClickHandler(new ClickHandler() {
        //
        // @Override
        // public void onClick(ClickEvent event) {
        // getUiHandlers().resendStreamMessage(multidatasetVersionDto);
        // }
        // });

        mainFormLayout.getVersioningButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                final VersionWindow versionWindow = new VersionWindow(getConstants().lifeCycleVersioning());
                versionWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (versionWindow.validateForm()) {
                            getUiHandlers().version(multidatasetVersionDto, versionWindow.getSelectedVersion());
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
                getUiHandlers().previewData(multidatasetVersionDto);
            }
        });
    }

    private void createViewForm() {
        // Identifiers form
        identifiersForm = new MultidatasetIdentifiersForm();
        mainFormLayout.addViewCanvas(identifiersForm);

        // Content descriptors form
        contentDescriptorsForm = new MultidatasetContentDescriptorsForm();
        mainFormLayout.addViewCanvas(contentDescriptorsForm);

        // Common metadata form
        commonMetadataForm = new SiemacMetadataCommonMetadataForm();
        mainFormLayout.addViewCanvas(commonMetadataForm);

        // Thematic content classifiers
        thematicContentClassifiersForm = new SiemacMetadataThematicContentClassifiersForm();
        mainFormLayout.addViewCanvas(thematicContentClassifiersForm);

        // Languages
        languageForm = new SiemacMetadataLanguageForm();
        mainFormLayout.addViewCanvas(languageForm);

        // Production descriptors
        productionDescriptorsForm = new SiemacMetadataProductionDescriptorsForm();
        mainFormLayout.addViewCanvas(productionDescriptorsForm);

        // Class descriptors
        classDescriptorsForm = new MultidatasetClassDescriptorsForm();
        mainFormLayout.addViewCanvas(classDescriptorsForm);

        // Resource relation descriptors
        resourceRelationDescriptorsForm = new MultidatasetResourceRelationDescriptorsForm();
        mainFormLayout.addViewCanvas(resourceRelationDescriptorsForm);

        // Multidataset descriptors
        publicationDescriptorsForm = new SiemacMetadataPublicationDescriptorsForm();
        mainFormLayout.addViewCanvas(publicationDescriptorsForm);

        // Life cycle
        lifeCycleForm = new LifeCycleResourceLifeCycleForm();
        mainFormLayout.addViewCanvas(lifeCycleForm);

        // Version
        versionForm = new LifeCycleResourceVersionForm();
        mainFormLayout.addViewCanvas(versionForm);

        // Intellectual property descriptors
        intellectualPropertyDescriptorsForm = new SiemacMetadataIntellectualPropertyDescriptorsForm();
        mainFormLayout.addViewCanvas(intellectualPropertyDescriptorsForm);
    }

    private void createEditionForm() {
        // Identifiers form
        identifiersEditionForm = new NameableResourceIdentifiersEditionForm();
        mainFormLayout.addEditionCanvas(identifiersEditionForm);

        // Content descriptors form
        contentDescriptorsEditionForm = new MultidatasetContentDescriptorsEditionForm();
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);

        // Common metadata
        commonMetadataEditionForm = new SiemacMetadataCommonMetadataEditionForm();
        mainFormLayout.addEditionCanvas(commonMetadataEditionForm);

        // Thematic content classifiers
        thematicContentClassifiersEditionForm = new SiemacMetadataThematicContentClassifiersEditionForm();
        mainFormLayout.addEditionCanvas(thematicContentClassifiersEditionForm);

        // Languages
        languageEditionForm = new SiemacMetadataLanguageEditionForm();
        mainFormLayout.addEditionCanvas(languageEditionForm);

        // Production descriptors
        productionDescriptorsEditionForm = new SiemacMetadataProductionDescriptorsEditionForm();
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);

        // Class descriptors
        classDescriptorsEditionForm = new MultidatasetClassDescriptorsEditionForm();
        mainFormLayout.addEditionCanvas(classDescriptorsEditionForm);

        // Resource relation descriptors
        resourceRelationDescriptorsEditionForm = new MultidatasetResourceRelationDescriptorsEditionForm();
        mainFormLayout.addEditionCanvas(resourceRelationDescriptorsEditionForm);

        // Multidataset descriptors
        publicationDescriptorsEditionForm = new SiemacMetadataPublicationDescriptorsEditionForm();
        mainFormLayout.addEditionCanvas(publicationDescriptorsEditionForm);

        // Life cycle
        lifeCycleEditionForm = new LifeCycleResourceLifeCycleForm();
        mainFormLayout.addEditionCanvas(lifeCycleEditionForm);

        // Version
        versionEditionForm = new LifeCycleResourceVersionEditionForm();
        mainFormLayout.addEditionCanvas(versionEditionForm);

        // Intellectual property descriptors
        intellectualPropertyDescriptorsEditionForm = new SiemacMetadataIntellectualPropertyDescriptorsEditionForm();
        mainFormLayout.addEditionCanvas(intellectualPropertyDescriptorsEditionForm);
    }

    private void setMultidatasetViewMode(MultidatasetVersionDto multidatasetDto) {
        // Identifiers form
        identifiersForm.setMultidatasetVersionDto(multidatasetDto);

        // Content descriptors form
        contentDescriptorsForm.setMultidatasetVersionDto(multidatasetDto);

        // Common metadata form
        commonMetadataForm.setSiemacMetadataStatisticalResourceDto(multidatasetDto);

        // Thematic content classifiers
        thematicContentClassifiersForm.setSiemacMetadataStatisticalResourceDto(multidatasetDto);

        // Languages
        languageForm.setSiemacMetadataStatisticalResourceDto(multidatasetDto);

        // Class descriptors
        classDescriptorsForm.setMultidatasetDto(multidatasetDto);

        // Resource relation descriptors
        resourceRelationDescriptorsForm.setSiemacMetadataStatisticalResourceDto(multidatasetDto);

        // Production descriptors
        productionDescriptorsForm.setSiemacMetadataStatisticalResourceDto(multidatasetDto);

        // Multidataset descriptors
        publicationDescriptorsForm.setSiemacMetadataStatisticalResourceDto(multidatasetDto);

        // Life cycle
        lifeCycleForm.setLifeCycleStatisticalResourceDto(multidatasetDto);

        // Version
        versionForm.setLifeCycleStatisticalResourceDto(multidatasetDto);

        // Intellectual property descriptors
        intellectualPropertyDescriptorsForm.setSiemacMetadataStatisticalResourceDto(multidatasetDto);
    }

    private void setMultidatasetEditionMode(MultidatasetVersionDto multidatasetDto) {

        String[] requiredFieldsToNextProcStatus = RequiredFieldUtils.getMultidatasetRequiredFieldsToNextProcStatus(multidatasetDto.getProcStatus());

        // Identifiers form
        identifiersEditionForm.setNameableStatisticalResourceDto(multidatasetDto);
        identifiersEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Content descriptors form
        contentDescriptorsEditionForm.setMultidatasetDto(multidatasetDto);
        contentDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Common metadata
        commonMetadataEditionForm.setSiemacMetadataStatisticalResourceDto(multidatasetDto);
        commonMetadataEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Thematic content classifiers
        thematicContentClassifiersEditionForm.setSiemacMetadataStatisticalResourceDto(multidatasetDto);
        thematicContentClassifiersEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Languages
        languageEditionForm.setSiemacMetadataStatisticalResourceDto(multidatasetDto);
        languageEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Production descriptors
        productionDescriptorsEditionForm.setSiemacMetadataStatisticalResourceDto(multidatasetDto);
        productionDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Class descriptors
        classDescriptorsEditionForm.setMultidatasetDto(multidatasetDto);
        classDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Resource relation descriptors
        resourceRelationDescriptorsEditionForm.setSiemacMetadataStatisticalResourceDto(multidatasetDto);
        resourceRelationDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Multidataset descriptors
        publicationDescriptorsEditionForm.setSiemacMetadataStatisticalResourceDto(multidatasetDto);
        publicationDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Life cycle
        lifeCycleEditionForm.setLifeCycleStatisticalResourceDto(multidatasetDto);
        lifeCycleEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Version
        versionEditionForm.setLifeCycleStatisticalResourceDto(multidatasetDto);
        versionEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Intellectual property descriptors
        intellectualPropertyDescriptorsEditionForm.setSiemacMetadataStatisticalResourceDto(multidatasetDto);
        intellectualPropertyDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);
    }

    @Override
    public void setMultidataset(MultidatasetVersionDto multidatasetDto) {
        multidatasetVersionDto = multidatasetDto;

        mainFormLayout.setMultidatasetVersion(multidatasetDto);
        mainFormLayout.setViewMode();

        setMultidatasetViewMode(multidatasetDto);
        setMultidatasetEditionMode(multidatasetDto);
    }

    private MultidatasetVersionDto getMultidatasetDto() {
        // Identifiers
        multidatasetVersionDto = (MultidatasetVersionDto) identifiersEditionForm.getNameableStatisticalResourceDto(multidatasetVersionDto);

        // Content descriptors
        multidatasetVersionDto = (MultidatasetVersionDto) contentDescriptorsEditionForm.getMultidatasetDto(multidatasetVersionDto);

        // Content descriptors
        multidatasetVersionDto = (MultidatasetVersionDto) commonMetadataEditionForm.getSiemacMetadataStatisticalResourceDto(multidatasetVersionDto);

        // Thematic content classifiers
        multidatasetVersionDto = (MultidatasetVersionDto) thematicContentClassifiersEditionForm.getSiemacMetadataStatisticalResourceDto(multidatasetVersionDto);

        // Language
        multidatasetVersionDto = (MultidatasetVersionDto) languageEditionForm.getSiemacMetadataStatisticalResourceDto(multidatasetVersionDto);

        // Production descriptors
        multidatasetVersionDto = (MultidatasetVersionDto) productionDescriptorsEditionForm.getSiemacMetadataStatisticalResourceDto(multidatasetVersionDto);

        // Class descriptors
        multidatasetVersionDto = classDescriptorsEditionForm.getMultidatasetDto(multidatasetVersionDto);

        // Resource relation descriptors
        multidatasetVersionDto = (MultidatasetVersionDto) resourceRelationDescriptorsEditionForm.getSiemacMetadataStatisticalResourceDto(multidatasetVersionDto);

        // Multidataset descriptors
        multidatasetVersionDto = (MultidatasetVersionDto) publicationDescriptorsEditionForm.getSiemacMetadataStatisticalResourceDto(multidatasetVersionDto);

        // Version
        multidatasetVersionDto = (MultidatasetVersionDto) versionEditionForm.getLifeCycleStatisticalResourceDto(multidatasetVersionDto);

        // Intellectual property descriptors
        multidatasetVersionDto = (MultidatasetVersionDto) intellectualPropertyDescriptorsEditionForm.getSiemacMetadataStatisticalResourceDto(multidatasetVersionDto);

        return multidatasetVersionDto;
    }

    @Override
    public void setMultidatasetsForReplaces(GetMultidatasetVersionsResult result) {
        List<RelatedResourceDto> relatedResourceDtos = RelatedResourceUtils.getMultidatasetVersionBaseDtosAsRelatedResourceDtos(result.getMultidatasetBaseDtos());
        resourceRelationDescriptorsEditionForm.setRelatedResourcesForReplaces(relatedResourceDtos, result.getFirstResultOut(), relatedResourceDtos.size(), result.getTotalResults());
    }

    @Override
    public void setStatisticalOperationsForReplacesSelection(List<ExternalItemDto> results, ExternalItemDto defaultSelected) {
        resourceRelationDescriptorsEditionForm.setStatisticalOperationsForReplacesSelection(results, defaultSelected);
    }

    @Override
    protected SiemacMetadataCommonMetadataEditionForm getCommonMetadataEditionForm() {
        return commonMetadataEditionForm;
    }

    @Override
    protected SiemacMetadataProductionDescriptorsEditionForm getProductionDescriptorsEditionForm() {
        return productionDescriptorsEditionForm;
    }

    @Override
    protected SiemacMetadataPublicationDescriptorsEditionForm getPublicationDescriptorsEditionForm() {
        return publicationDescriptorsEditionForm;
    }

    @Override
    protected SiemacMetadataThematicContentClassifiersEditionForm getThematicContentClassifiersEditionForm() {
        return thematicContentClassifiersEditionForm;
    }

    @Override
    protected SiemacMetadataLanguageEditionForm getLanguageEditionForm() {
        return languageEditionForm;
    }
}
