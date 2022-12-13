package org.siemac.metamac.statistical.resources.web.client.publication.view;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionDto;
import org.siemac.metamac.statistical.resources.web.client.base.utils.RequiredFieldUtils;
import org.siemac.metamac.statistical.resources.web.client.base.view.StatisticalResourceMetadataBaseViewImpl;
import org.siemac.metamac.statistical.resources.web.client.publication.presenter.PublicationMetadataTabPresenter.PublicationMetadataTabView;
import org.siemac.metamac.statistical.resources.web.client.publication.view.handlers.PublicationMetadataTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.publication.widgets.PublicationMainFormLayout;
import org.siemac.metamac.statistical.resources.web.client.publication.widgets.forms.PublicationClassDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.publication.widgets.forms.PublicationClassDescriptorsForm;
import org.siemac.metamac.statistical.resources.web.client.publication.widgets.forms.PublicationIdentifiersForm;
import org.siemac.metamac.statistical.resources.web.client.publication.widgets.forms.PublicationResourceRelationDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.publication.widgets.forms.PublicationResourceRelationDescriptorsForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.VersionWindow;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.LifeCycleResourceLifeCycleForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.LifeCycleResourceVersionEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.LifeCycleResourceVersionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.NameableResourceIdentifiersEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataCommonMetadataEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataCommonMetadataForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataContentDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataContentDescriptorsForm;
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
import org.siemac.metamac.statistical.resources.web.shared.publication.GetPublicationVersionsResult;
import org.siemac.metamac.statistical.resources.web.shared.utils.RelatedResourceUtils;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class PublicationMetadataTabViewImpl extends StatisticalResourceMetadataBaseViewImpl<PublicationMetadataTabUiHandlers> implements PublicationMetadataTabView {

    private VLayout                                                  panel;

    private PublicationMainFormLayout                                mainFormLayout;

    private PublicationIdentifiersForm                               identifiersForm;
    private SiemacMetadataContentDescriptorsForm                     contentDescriptorsForm;
    private SiemacMetadataCommonMetadataForm                         commonMetadataForm;
    private SiemacMetadataThematicContentClassifiersForm             thematicContentClassifiersForm;
    private SiemacMetadataLanguageForm                               languageForm;
    private SiemacMetadataProductionDescriptorsForm                  productionDescriptorsForm;
    private PublicationClassDescriptorsForm                          classDescriptorsForm;
    private PublicationResourceRelationDescriptorsForm               resourceRelationDescriptorsForm;
    private SiemacMetadataPublicationDescriptorsForm                 publicationDescriptorsForm;
    private LifeCycleResourceLifeCycleForm                           lifeCycleForm;
    private LifeCycleResourceVersionForm                             versionForm;
    private SiemacMetadataIntellectualPropertyDescriptorsForm        intellectualPropertyDescriptorsForm;

    private NameableResourceIdentifiersEditionForm                   identifiersEditionForm;
    private SiemacMetadataContentDescriptorsEditionForm              contentDescriptorsEditionForm;
    private SiemacMetadataCommonMetadataEditionForm                  commonMetadataEditionForm;
    private SiemacMetadataThematicContentClassifiersEditionForm      thematicContentClassifiersEditionForm;
    private SiemacMetadataLanguageEditionForm                        languageEditionForm;
    private SiemacMetadataProductionDescriptorsEditionForm           productionDescriptorsEditionForm;
    private PublicationClassDescriptorsEditionForm                   classDescriptorsEditionForm;
    private PublicationResourceRelationDescriptorsEditionForm        resourceRelationDescriptorsEditionForm;
    private SiemacMetadataPublicationDescriptorsEditionForm          publicationDescriptorsEditionForm;
    private LifeCycleResourceLifeCycleForm                           lifeCycleEditionForm;
    private LifeCycleResourceVersionEditionForm                      versionEditionForm;

    private SiemacMetadataIntellectualPropertyDescriptorsEditionForm intellectualPropertyDescriptorsEditionForm;

    private PublicationVersionDto                                    publicationVersionDto;

    @Inject
    public PublicationMetadataTabViewImpl() {
        panel = new VLayout();

        mainFormLayout = new PublicationMainFormLayout();
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
    public void setUiHandlers(PublicationMetadataTabUiHandlers uiHandlers) {
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
                    getPublicationDto();
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                        @Override
                        public void execute() {
                            getUiHandlers().savePublication(getPublicationDto());
                        }
                    });

                }
            }
        });

        // Delete

        mainFormLayout.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deletePublication(publicationVersionDto.getUrn());
            }
        });

        // Life cycle
        mainFormLayout.getProductionValidationButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().sendToProductionValidation(publicationVersionDto);
            }
        });
        mainFormLayout.getDiffusionValidationButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().sendToDiffusionValidation(publicationVersionDto);
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
                        getUiHandlers().rejectValidation(publicationVersionDto, reasonOfRejection);
                    }
                });
            }
        });
        mainFormLayout.getPublishButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().publish(publicationVersionDto);
            }
        });

        mainFormLayout.getResendStreamMessageButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().resendStreamMessage(publicationVersionDto);
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
                            getUiHandlers().version(publicationVersionDto, versionWindow.getSelectedVersion());
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
                getUiHandlers().previewData(publicationVersionDto);
            }
        });
    }

    private void createViewForm() {
        // Identifiers form
        identifiersForm = new PublicationIdentifiersForm();
        mainFormLayout.addViewCanvas(identifiersForm);

        // Content descriptors form
        contentDescriptorsForm = new SiemacMetadataContentDescriptorsForm();
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
        classDescriptorsForm = new PublicationClassDescriptorsForm();
        mainFormLayout.addViewCanvas(classDescriptorsForm);

        // Resource relation descriptors
        resourceRelationDescriptorsForm = new PublicationResourceRelationDescriptorsForm();
        mainFormLayout.addViewCanvas(resourceRelationDescriptorsForm);

        // Publication descriptors
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
        contentDescriptorsEditionForm = new SiemacMetadataContentDescriptorsEditionForm();
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
        classDescriptorsEditionForm = new PublicationClassDescriptorsEditionForm();
        mainFormLayout.addEditionCanvas(classDescriptorsEditionForm);

        // Resource relation descriptors
        resourceRelationDescriptorsEditionForm = new PublicationResourceRelationDescriptorsEditionForm();
        mainFormLayout.addEditionCanvas(resourceRelationDescriptorsEditionForm);

        // Publication descriptors
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

    private void setPublicationViewMode(PublicationVersionDto publicationDto) {
        // Identifiers form
        identifiersForm.setPublicationVersionDto(publicationDto);

        // Content descriptors form
        contentDescriptorsForm.setSiemacMetadataStatisticalResourceDto(publicationDto);

        // Common metadata form
        commonMetadataForm.setSiemacMetadataStatisticalResourceDto(publicationDto);

        // Thematic content classifiers
        thematicContentClassifiersForm.setSiemacMetadataStatisticalResourceDto(publicationDto);

        // Languages
        languageForm.setSiemacMetadataStatisticalResourceDto(publicationDto);

        // Class descriptors
        classDescriptorsForm.setPublicationDto(publicationDto);

        // Resource relation descriptors
        resourceRelationDescriptorsForm.setPublicationVersionDto(publicationDto);

        // Production descriptors
        productionDescriptorsForm.setSiemacMetadataStatisticalResourceDto(publicationDto);

        // Publication descriptors
        publicationDescriptorsForm.setSiemacMetadataStatisticalResourceDto(publicationDto);

        // Life cycle
        lifeCycleForm.setLifeCycleStatisticalResourceDto(publicationDto);

        // Version
        versionForm.setLifeCycleStatisticalResourceDto(publicationDto);

        // Intellectual property descriptors
        intellectualPropertyDescriptorsForm.setSiemacMetadataStatisticalResourceDto(publicationDto);
    }

    private void setPublicationEditionMode(PublicationVersionDto publicationDto) {

        String[] requiredFieldsToNextProcStatus = RequiredFieldUtils.getPublicationRequiredFieldsToNextProcStatus(publicationDto.getProcStatus());

        // Identifiers form
        identifiersEditionForm.setNameableStatisticalResourceDto(publicationDto);
        identifiersEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Content descriptors form
        contentDescriptorsEditionForm.setSiemacMetadataStatisticalResourceDto(publicationDto);
        contentDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Common metadata
        commonMetadataEditionForm.setSiemacMetadataStatisticalResourceDto(publicationDto);
        commonMetadataEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Thematic content classifiers
        thematicContentClassifiersEditionForm.setSiemacMetadataStatisticalResourceDto(publicationDto);
        thematicContentClassifiersEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Languages
        languageEditionForm.setSiemacMetadataStatisticalResourceDto(publicationDto);
        languageEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Production descriptors
        productionDescriptorsEditionForm.setSiemacMetadataStatisticalResourceDto(publicationDto);
        productionDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Class descriptors
        classDescriptorsEditionForm.setPublicationDto(publicationDto);
        classDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Resource relation descriptors
        resourceRelationDescriptorsEditionForm.setSiemacMetadataStatisticalResourceDto(publicationDto);
        resourceRelationDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Publication descriptors
        publicationDescriptorsEditionForm.setSiemacMetadataStatisticalResourceDto(publicationDto);
        publicationDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Life cycle
        lifeCycleEditionForm.setLifeCycleStatisticalResourceDto(publicationDto);
        lifeCycleEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Version
        versionEditionForm.setLifeCycleStatisticalResourceDto(publicationDto);
        versionEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Intellectual property descriptors
        intellectualPropertyDescriptorsEditionForm.setSiemacMetadataStatisticalResourceDto(publicationDto);
        intellectualPropertyDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);
    }

    @Override
    public void setPublication(PublicationVersionDto publicationDto) {
        publicationVersionDto = publicationDto;

        mainFormLayout.setPublicationVersion(publicationDto);
        mainFormLayout.setViewMode();

        setPublicationViewMode(publicationDto);
        setPublicationEditionMode(publicationDto);
    }

    private PublicationVersionDto getPublicationDto() {
        // Identifiers
        publicationVersionDto = (PublicationVersionDto) identifiersEditionForm.getNameableStatisticalResourceDto(publicationVersionDto);

        // Content descriptors
        publicationVersionDto = (PublicationVersionDto) contentDescriptorsEditionForm.getSiemacMetadataStatisticalResourceDto(publicationVersionDto);

        // Content descriptors
        publicationVersionDto = (PublicationVersionDto) commonMetadataEditionForm.getSiemacMetadataStatisticalResourceDto(publicationVersionDto);

        // Thematic content classifiers
        publicationVersionDto = (PublicationVersionDto) thematicContentClassifiersEditionForm.getSiemacMetadataStatisticalResourceDto(publicationVersionDto);

        // Language
        publicationVersionDto = (PublicationVersionDto) languageEditionForm.getSiemacMetadataStatisticalResourceDto(publicationVersionDto);

        // Production descriptors
        publicationVersionDto = (PublicationVersionDto) productionDescriptorsEditionForm.getSiemacMetadataStatisticalResourceDto(publicationVersionDto);

        // Class descriptors
        publicationVersionDto = classDescriptorsEditionForm.getPublicationDto(publicationVersionDto);

        // Resource relation descriptors
        publicationVersionDto = (PublicationVersionDto) resourceRelationDescriptorsEditionForm.getSiemacMetadataStatisticalResourceDto(publicationVersionDto);

        // Publication descriptors
        publicationVersionDto = (PublicationVersionDto) publicationDescriptorsEditionForm.getSiemacMetadataStatisticalResourceDto(publicationVersionDto);

        // Version
        publicationVersionDto = (PublicationVersionDto) versionEditionForm.getLifeCycleStatisticalResourceDto(publicationVersionDto);

        // Intellectual property descriptors
        publicationVersionDto = (PublicationVersionDto) intellectualPropertyDescriptorsEditionForm.getSiemacMetadataStatisticalResourceDto(publicationVersionDto);

        return publicationVersionDto;
    }

    @Override
    public void setPublicationsForReplaces(GetPublicationVersionsResult result) {
        List<RelatedResourceDto> relatedResourceDtos = RelatedResourceUtils.getPublicationVersionBaseDtosAsRelatedResourceDtos(result.getPublicationBaseDtos());
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
