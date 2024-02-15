package org.siemac.metamac.statistical.resources.web.client.dataset.view;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.web.client.base.utils.RequiredFieldUtils;
import org.siemac.metamac.statistical.resources.web.client.base.view.StatisticalResourceMetadataBaseViewImpl;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetInGroupMetadataTabPresenter.DatasetInGroupMetadataTabView;
import org.siemac.metamac.statistical.resources.web.client.dataset.utils.DatasetMetadataExternalField;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetInGroupMetadataTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.DatasetInGroupMainFormLayout;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.DatasetContentDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.DatasetContentDescriptorsForm;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.DatasetProductionDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.DatasetProductionDescriptorsForm;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.DatasetPublicationDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.DatasetPublicationDescriptorsForm;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.DatasetResourceRelationDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.DatasetResourceRelationDescriptorsForm;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.DatasetVersionEditionForm;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms.DatasetVersionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataCommonMetadataEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataCommonMetadataForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataIntellectualPropertyDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataIntellectualPropertyDescriptorsForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataLanguageEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataLanguageForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataProductionDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataPublicationDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataThematicContentClassifiersEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataThematicContentClassifiersForm;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionMainCoveragesResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionsResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetConceptSchemesPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetConceptsPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetDsdsPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetGeographicalGranularitiesListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetTemporalGranularitiesListResult;
import org.siemac.metamac.statistical.resources.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class DatasetInGroupMetadataTabViewImpl extends StatisticalResourceMetadataBaseViewImpl<DatasetInGroupMetadataTabUiHandlers> implements DatasetInGroupMetadataTabView {

    private VLayout                                                  panel;
    private DatasetInGroupMainFormLayout                             mainFormLayout;

    private DatasetContentDescriptorsForm                            contentDescriptorsForm;
    private SiemacMetadataCommonMetadataForm                         commonMetadataForm;
    private SiemacMetadataThematicContentClassifiersForm             thematicContentClassifiersForm;
    private SiemacMetadataLanguageForm                               languageForm;
    private DatasetProductionDescriptorsForm                         productionDescriptorsForm;
    private DatasetResourceRelationDescriptorsForm                   resourceRelationDescriptorsForm;
    private DatasetPublicationDescriptorsForm                        publicationDescriptorsForm;
    private DatasetVersionForm                                       versionForm;
    private SiemacMetadataIntellectualPropertyDescriptorsForm        intellectualPropertyDescriptorsForm;

    private DatasetContentDescriptorsEditionForm                     contentDescriptorsEditionForm;
    private SiemacMetadataCommonMetadataEditionForm                  commonMetadataEditionForm;
    private SiemacMetadataThematicContentClassifiersEditionForm      thematicContentClassifiersEditionForm;
    private SiemacMetadataLanguageEditionForm                        languageEditionForm;
    private DatasetProductionDescriptorsEditionForm                  productionDescriptorsEditionForm;
    private DatasetResourceRelationDescriptorsEditionForm            resourceRelationDescriptorsEditionForm;
    private DatasetPublicationDescriptorsEditionForm                 publicationDescriptorsEditionForm;
    private DatasetVersionEditionForm                                versionEditionForm;
    private SiemacMetadataIntellectualPropertyDescriptorsEditionForm intellectualPropertyDescriptorsEditionForm;

    private DatasetVersionDto                                        datasetVersionDto;

    public DatasetInGroupMetadataTabViewImpl() {
        panel = new VLayout();

        mainFormLayout = new DatasetInGroupMainFormLayout();

        bindMainFormLayoutEvents();
        createViewForm();
        createEditionForm();

        panel.addMember(mainFormLayout);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(DatasetInGroupMetadataTabUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        resourceRelationDescriptorsEditionForm.setUiHandlers(uiHandlers);
        resourceRelationDescriptorsForm.setUiHandlers(uiHandlers);
        commonMetadataForm.setBaseUiHandlers(uiHandlers);
        contentDescriptorsEditionForm.setUiHandlers(uiHandlers);
        commonMetadataEditionForm.setUiHandlers(uiHandlers);
        thematicContentClassifiersEditionForm.setUiHandlers(uiHandlers);
        productionDescriptorsEditionForm.setUiHandlers(uiHandlers);
        publicationDescriptorsEditionForm.setUiHandlers(uiHandlers);
        versionEditionForm.setUiHandlers(uiHandlers);
        languageEditionForm.setUiHandlers(uiHandlers);
    }

    private void bindMainFormLayoutEvents() {
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();

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

                resourceRelationDescriptorsForm.setTranslationsShowed(translationsShowed);
                resourceRelationDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                publicationDescriptorsForm.setTranslationsShowed(translationsShowed);
                publicationDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                versionForm.setTranslationsShowed(translationsShowed);
                versionEditionForm.setTranslationsShowed(translationsShowed);

                intellectualPropertyDescriptorsForm.setTranslationsShowed(translationsShowed);
                intellectualPropertyDescriptorsEditionForm.setTranslationsShowed(translationsShowed);
            }
        });

        mainFormLayout.getEditToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                mainFormLayout.setEditionMode();
            }
        });
    }

    private void createViewForm() {

        // Content descriptors form
        contentDescriptorsForm = new DatasetContentDescriptorsForm(true);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);

        // Common metadata form
        commonMetadataForm = new SiemacMetadataCommonMetadataForm();
        mainFormLayout.addViewCanvas(commonMetadataForm);

        // Thematic content classifiers
        thematicContentClassifiersForm = new SiemacMetadataThematicContentClassifiersForm(true);
        mainFormLayout.addViewCanvas(thematicContentClassifiersForm);

        // Languages
        languageForm = new SiemacMetadataLanguageForm(true);
        mainFormLayout.addViewCanvas(languageForm);

        // Production descriptors
        productionDescriptorsForm = new DatasetProductionDescriptorsForm(true);
        mainFormLayout.addViewCanvas(productionDescriptorsForm);

        // Resource relation descriptors
        resourceRelationDescriptorsForm = new DatasetResourceRelationDescriptorsForm(true);
        mainFormLayout.addViewCanvas(resourceRelationDescriptorsForm);

        // Publication descriptors
        publicationDescriptorsForm = new DatasetPublicationDescriptorsForm(true);
        mainFormLayout.addViewCanvas(publicationDescriptorsForm);

        // Version
        versionForm = new DatasetVersionForm(true);
        mainFormLayout.addViewCanvas(versionForm);

        // Intellectual property descriptors
        intellectualPropertyDescriptorsForm = new SiemacMetadataIntellectualPropertyDescriptorsForm(true);
        mainFormLayout.addViewCanvas(intellectualPropertyDescriptorsForm);
    }

    private void createEditionForm() {

        // Content descriptors form
        contentDescriptorsEditionForm = new DatasetContentDescriptorsEditionForm(true);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);

        // Common metadata
        commonMetadataEditionForm = new SiemacMetadataCommonMetadataEditionForm();
        mainFormLayout.addEditionCanvas(commonMetadataEditionForm);

        // Thematic content classifiers
        thematicContentClassifiersEditionForm = new SiemacMetadataThematicContentClassifiersEditionForm(true);
        mainFormLayout.addEditionCanvas(thematicContentClassifiersEditionForm);

        // Languages
        languageEditionForm = new SiemacMetadataLanguageEditionForm(true);
        mainFormLayout.addEditionCanvas(languageEditionForm);

        // Production descriptors
        productionDescriptorsEditionForm = new DatasetProductionDescriptorsEditionForm(true);
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);

        // Resource relation descriptors
        resourceRelationDescriptorsEditionForm = new DatasetResourceRelationDescriptorsEditionForm(true);
        mainFormLayout.addEditionCanvas(resourceRelationDescriptorsEditionForm);

        // Publication descriptors
        publicationDescriptorsEditionForm = new DatasetPublicationDescriptorsEditionForm(true);
        mainFormLayout.addEditionCanvas(publicationDescriptorsEditionForm);

        // Version
        versionEditionForm = new DatasetVersionEditionForm(true);
        mainFormLayout.addEditionCanvas(versionEditionForm);

        // Intellectual property descriptors
        intellectualPropertyDescriptorsEditionForm = new SiemacMetadataIntellectualPropertyDescriptorsEditionForm(true);
        mainFormLayout.addEditionCanvas(intellectualPropertyDescriptorsEditionForm);
    }

    @Override
    public void setDataset(DatasetVersionDto datasetVersionDto) {
        this.datasetVersionDto = datasetVersionDto;

        mainFormLayout.setDatasetVersion(datasetVersionDto);
        mainFormLayout.setViewMode();

        setDatasetViewMode(datasetVersionDto);
        setDatasetEditionMode(datasetVersionDto);

        if (ProcStatusEnum.PUBLISHED.equals(datasetVersionDto.getProcStatus())) {
            getUiHandlers().retrieveMainCoveragesForDatasetVersion(datasetVersionDto.getUrn());
        }

        mainFormLayout.markForRedraw();
    }

    @Override
    public void initDatasetForUpdateInGroup(DatasetVersionDto datasetDto) {
        this.datasetVersionDto = datasetDto;

        mainFormLayout.setDatasetVersion(datasetVersionDto);
        mainFormLayout.setViewMode();

        setDatasetViewMode(datasetVersionDto);
        setDatasetEditionMode(datasetVersionDto);

        mainFormLayout.markForRedraw();

    }

    private void setDatasetViewMode(DatasetVersionDto datasetDto) {

        // Content descriptors
        contentDescriptorsForm.setDatasetVersionDto(datasetDto);

        // Common metadata
        commonMetadataForm.setSiemacMetadataStatisticalResourceDto(datasetDto);

        // Thematic content classifiers
        thematicContentClassifiersForm.setSiemacMetadataStatisticalResourceDto(datasetDto);

        // Languages
        languageForm.setSiemacMetadataStatisticalResourceDto(datasetDto);

        // Production descriptors
        productionDescriptorsForm.setDatasetVersionDto(datasetDto);

        // Resource relation descriptors
        resourceRelationDescriptorsForm.setDatasetVersionDto(datasetDto);

        // Publication descriptors
        publicationDescriptorsForm.setDatasetVersionDto(datasetDto);

        // Version
        versionForm.setDatasetVersionDto(datasetDto);

        // Intellectual property descriptors
        intellectualPropertyDescriptorsForm.setSiemacMetadataStatisticalResourceDto(datasetDto);

    }

    private void setDatasetEditionMode(DatasetVersionDto datasetDto) {

        String[] requiredFieldsToNextProcStatus = RequiredFieldUtils.getDatasetRequiredFieldsToNextProcStatus(datasetDto.getProcStatus());

        // Content Descriptors
        contentDescriptorsEditionForm.setDatasetVersionDto(datasetDto);
        contentDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Common metadata
        commonMetadataEditionForm.setSiemacMetadataStatisticalResourceDto(datasetDto);
        commonMetadataEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Thematic content classifiers
        thematicContentClassifiersEditionForm.setSiemacMetadataStatisticalResourceDto(datasetDto);
        thematicContentClassifiersEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Languages
        languageEditionForm.setSiemacMetadataStatisticalResourceDto(datasetDto);
        languageEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Production descriptors
        productionDescriptorsEditionForm.setDatasetVersionDto(datasetDto);
        productionDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Resource relation descriptors
        resourceRelationDescriptorsEditionForm.setSiemacMetadataStatisticalResourceDto(datasetDto);
        resourceRelationDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Publication descriptors
        publicationDescriptorsEditionForm.setDatasetVersionDto(datasetDto);
        publicationDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Version
        versionEditionForm.setDatasetVersionDto(datasetDto);
        versionEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Intellectual property descriptors
        intellectualPropertyDescriptorsEditionForm.setSiemacMetadataStatisticalResourceDto(datasetDto);
        intellectualPropertyDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);
    }

    public DatasetVersionDto getDatasetVersionDto() {

        // Content descriptors form
        datasetVersionDto = contentDescriptorsEditionForm.getDatasetVersionDto(datasetVersionDto);

        // Common metadata
        datasetVersionDto = (DatasetVersionDto) commonMetadataEditionForm.getSiemacMetadataStatisticalResourceDto(datasetVersionDto);

        // Thematic content classifiers
        datasetVersionDto = (DatasetVersionDto) thematicContentClassifiersEditionForm.getSiemacMetadataStatisticalResourceDto(datasetVersionDto);

        // Language
        datasetVersionDto = (DatasetVersionDto) languageEditionForm.getSiemacMetadataStatisticalResourceDto(datasetVersionDto);

        // Production descriptors
        datasetVersionDto = productionDescriptorsEditionForm.getDatasetVersionDto(datasetVersionDto);

        // Resource relation descriptors
        datasetVersionDto = (DatasetVersionDto) resourceRelationDescriptorsEditionForm.getSiemacMetadataStatisticalResourceDto(datasetVersionDto);

        // Publication descriptors
        datasetVersionDto = publicationDescriptorsEditionForm.getDatasetVersionDto(datasetVersionDto);

        // Version
        datasetVersionDto = versionEditionForm.getDatasetVersionDto(datasetVersionDto);

        // Intellectual property descriptors
        datasetVersionDto = (DatasetVersionDto) intellectualPropertyDescriptorsEditionForm.getSiemacMetadataStatisticalResourceDto(datasetVersionDto);

        return datasetVersionDto;
    }

    @Override
    public void setDatasetsForReplaces(GetDatasetVersionsResult result) {
        List<RelatedResourceDto> relatedResourceDtos = RelatedResourceUtils.getDatasetVersionBaseDtosAsRelatedResourceDtos(result.getDatasetVersionBaseDtos());
        resourceRelationDescriptorsEditionForm.setRelatedResourcesForReplaces(relatedResourceDtos, result.getFirstResultOut(), relatedResourceDtos.size(), result.getTotalResults());
    }

    @Override
    public void setStatisticalOperationsForReplacesSelection(List<ExternalItemDto> results, ExternalItemDto defaultSelected) {
        resourceRelationDescriptorsEditionForm.setStatisticalOperationsForReplacesSelection(results, defaultSelected);
    }

    @Override
    public void setDatasetsMainCoverages(GetDatasetVersionMainCoveragesResult result) {
        contentDescriptorsForm.setCoverages(result.getGeographicCoverage(), result.getTemporalCoverage(), result.getMeasureCoverage());
        contentDescriptorsEditionForm.setCoverages(result.getGeographicCoverage(), result.getTemporalCoverage(), result.getMeasureCoverage());
    }

    @Override
    public void setStatisticalOperationsForDsdSelection(List<ExternalItemDto> results, ExternalItemDto defaultSelected) {
        productionDescriptorsEditionForm.setStatisticalOperationsForRelatedDsd(results, defaultSelected, null);
    }

    @Override
    public void setDsdsForRelatedDsd(GetDsdsPaginatedListResult result) {
        List<ExternalItemDto> externalItemsDtos = result.getDsdsList();
        productionDescriptorsEditionForm.setExternalItemsForRelatedDsd(externalItemsDtos, result.getFirstResultOut(), result.getTotalResults());
    }

    @Override
    public void setCodesForGeographicalGranularities(GetGeographicalGranularitiesListResult result) {
        List<ExternalItemDto> externalItemsDtos = result.getGeographicalGranularities();
        contentDescriptorsEditionForm.setCodesForGeographicalGranularities(externalItemsDtos, result.getFirstResultOut(), result.getTotalResults());
    }

    @Override
    public void setTemporalCodesForField(GetTemporalGranularitiesListResult result, DatasetMetadataExternalField field) {
        List<ExternalItemDto> externalItemsDtos = result.getTemporalGranularities();
        switch (field) {
            case TEMPORAL_GRANULARITY:
                contentDescriptorsEditionForm.setCodesForTemporalGranularities(externalItemsDtos, result.getFirstResultOut(), result.getTotalResults());
                break;
            case UPDATE_FREQUENCY:
                versionEditionForm.setCodesForUpdateFrequency(externalItemsDtos, result.getFirstResultOut(), result.getTotalResults());
                break;
        }
    }

    @Override
    public void setConceptSchemesForStatisticalUnit(GetConceptSchemesPaginatedListResult result) {
        List<ExternalItemDto> externalItemsDtos = result.getConceptSchemes();
        contentDescriptorsEditionForm.setConceptSchemesForStatisticalUnit(externalItemsDtos, result.getFirstResultOut(), result.getTotalResults());
    }

    @Override
    public void setConceptsForStatisticalUnit(GetConceptsPaginatedListResult result) {
        List<ExternalItemDto> externalItemsDtos = result.getConcepts();
        contentDescriptorsEditionForm.setConceptsForStatisticalUnit(externalItemsDtos, result.getFirstResultOut(), result.getTotalResults());
    }

    @Override
    public void showInformationMessage(String title, String message) {
        InformationWindow informationWindow = new InformationWindow(title, message);
        informationWindow.show();
    }

    // Generic forms for parent
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
