package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.getExternalItemsValue;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.setExternalItemsValue;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.SiemacMetadataStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.web.client.base.utils.SiemacMetadataExternalField;
import org.siemac.metamac.statistical.resources.web.client.base.view.handlers.StatisticalResourceUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.model.ds.SiemacMetadataDS;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.web.common.client.utils.CustomRequiredValidator;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomDateItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchSrmListItemWithSchemeFilterItem;
import org.siemac.metamac.web.common.shared.criteria.SrmExternalResourceRestCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

public class SiemacMetadataPublicationDescriptorsEditionForm extends GroupDynamicForm {

    protected ProcStatusEnum                      procStatus;

    private StatisticalResourceUiHandlers         uiHandlers;

    private SearchSrmListItemWithSchemeFilterItem publisherItem;
    private SearchSrmListItemWithSchemeFilterItem publisherContributorItem;
    private SearchSrmListItemWithSchemeFilterItem mediatorItem;

    public SiemacMetadataPublicationDescriptorsEditionForm() {
        super(getConstants().formPublicationDescriptors());

        publisherItem = createPublisherItem();
        publisherItem.setValidators(new CustomRequiredValidator() {

            @Override
            protected boolean condition(Object value) {
                List<ExternalItemDto> values = getExternalItemsValue(getItem(SiemacMetadataDS.PUBLISHER));
                return CommonUtils.isResourceInProductionValidationOrGreaterProcStatus(procStatus) ? (values != null && values.size() > 0) : true;
            }
        });

        publisherContributorItem = createPublisherContributorItem();
        mediatorItem = createMediatorItem();
        CustomDateItem newnessUntilDate = new CustomDateItem(SiemacMetadataDS.NEWNESS_UNTIL_DATE, getConstants().siemacMetadataStatisticalResourceNewnessUntilDate());
        CustomDateItem featuredUntilDate = new CustomDateItem(SiemacMetadataDS.FEATURED_UNTIL_DATE, getConstants().siemacMetadataStatisticalResourceFeaturedUntilDate());

        setFields(publisherItem, publisherContributorItem, mediatorItem, newnessUntilDate, featuredUntilDate);
    }

    public void setSiemacMetadataStatisticalResourceDto(SiemacMetadataStatisticalResourceDto dto) {
        this.procStatus = dto.getProcStatus();

        setExternalItemsValue(getItem(SiemacMetadataDS.PUBLISHER), dto.getPublisher());
        setExternalItemsValue(getItem(SiemacMetadataDS.PUBLISHER_CONTRIBUTOR), dto.getPublisherContributor());
        setExternalItemsValue(getItem(SiemacMetadataDS.MEDIATOR), dto.getMediator());
        setValue(SiemacMetadataDS.NEWNESS_UNTIL_DATE, dto.getNewnessUntilDate());
        setValue(SiemacMetadataDS.FEATURED_UNTIL_DATE, dto.getFeaturedUntilDate());
    }

    public SiemacMetadataStatisticalResourceDto getSiemacMetadataStatisticalResourceDto(SiemacMetadataStatisticalResourceDto dto) {
        dto.getPublisher().clear();
        dto.getPublisher().addAll(getExternalItemsValue(getItem(SiemacMetadataDS.PUBLISHER)));

        dto.getPublisherContributor().clear();
        dto.getPublisherContributor().addAll(getExternalItemsValue(getItem(SiemacMetadataDS.PUBLISHER_CONTRIBUTOR)));

        dto.getMediator().clear();
        dto.getMediator().addAll(getExternalItemsValue(getItem(SiemacMetadataDS.MEDIATOR)));

        dto.setNewnessUntilDate(((CustomDateItem) getItem(SiemacMetadataDS.NEWNESS_UNTIL_DATE)).getValueAsDate());
        dto.setFeaturedUntilDate(((CustomDateItem) getItem(SiemacMetadataDS.FEATURED_UNTIL_DATE)).getValueAsDate());
        return dto;
    }

    // *************************************************
    // PUBLISHER
    // *************************************************

    public void setOrganisationUnitSchemesForPublisher(List<ExternalItemDto> externalItemsDtos, int firstResult, int totalResults) {
        publisherItem.setFilterResources(externalItemsDtos, firstResult, totalResults);
    }

    public void setOrganisationUnitsForPublisher(List<ExternalItemDto> externalItemsDtos, int firstResult, int totalResults) {
        publisherItem.setResources(externalItemsDtos, firstResult, totalResults);
    }

    private SearchSrmListItemWithSchemeFilterItem createPublisherItem() {
        return new SearchSrmListItemWithSchemeFilterItem(SiemacMetadataDS.PUBLISHER, getConstants().siemacMetadataStatisticalResourcePublisher(), StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS) {

            @Override
            protected void retrieveItems(int firstResult, int maxResults, SrmItemRestCriteria webCriteria) {
                uiHandlers.retrieveOrganisationUnits(firstResult, maxResults, webCriteria, SiemacMetadataExternalField.PUBLISHER);
            }

            @Override
            protected void retrieveItemSchemes(int firstResult, int maxResults, SrmExternalResourceRestCriteria webCriteria) {
                uiHandlers.retrieveOrganisationUnitSchemes(firstResult, maxResults, webCriteria, SiemacMetadataExternalField.PUBLISHER);
            }
        };
    }

    // *************************************************
    // PUBLISHER CONTRIBUTOR
    // *************************************************

    public void setOrganisationUnitSchemesForPublisherContributor(List<ExternalItemDto> externalItemsDtos, int firstResult, int totalResults) {
        publisherContributorItem.setFilterResources(externalItemsDtos, firstResult, totalResults);
    }

    public void setOrganisationUnitsForPublisherContributor(List<ExternalItemDto> externalItemsDtos, int firstResult, int totalResults) {
        publisherContributorItem.setResources(externalItemsDtos, firstResult, totalResults);
    }

    private SearchSrmListItemWithSchemeFilterItem createPublisherContributorItem() {
        return new SearchSrmListItemWithSchemeFilterItem(SiemacMetadataDS.PUBLISHER_CONTRIBUTOR, getConstants().siemacMetadataStatisticalResourcePublisherContributor(),
                StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS) {

            @Override
            protected void retrieveItems(int firstResult, int maxResults, SrmItemRestCriteria webCriteria) {
                uiHandlers.retrieveOrganisationUnits(firstResult, maxResults, webCriteria, SiemacMetadataExternalField.PUBLISHER_CONTRIBUTOR);
            }

            @Override
            protected void retrieveItemSchemes(int firstResult, int maxResults, SrmExternalResourceRestCriteria webCriteria) {
                uiHandlers.retrieveOrganisationUnitSchemes(firstResult, maxResults, webCriteria, SiemacMetadataExternalField.PUBLISHER_CONTRIBUTOR);
            }
        };
    }

    // *************************************************
    // MEDIATOR
    // *************************************************

    public void setOrganisationUnitSchemesForMediator(List<ExternalItemDto> externalItemsDtos, int firstResult, int totalResults) {
        mediatorItem.setFilterResources(externalItemsDtos, firstResult, totalResults);
    }

    public void setOrganisationUnitsForMediator(List<ExternalItemDto> externalItemsDtos, int firstResult, int totalResults) {
        mediatorItem.setResources(externalItemsDtos, firstResult, totalResults);
    }

    private SearchSrmListItemWithSchemeFilterItem createMediatorItem() {
        return new SearchSrmListItemWithSchemeFilterItem(SiemacMetadataDS.MEDIATOR, getConstants().siemacMetadataStatisticalResourceMediator(), StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS) {

            @Override
            protected void retrieveItems(int firstResult, int maxResults, SrmItemRestCriteria webCriteria) {
                uiHandlers.retrieveOrganisationUnits(firstResult, maxResults, webCriteria, SiemacMetadataExternalField.MEDIATOR);
            }

            @Override
            protected void retrieveItemSchemes(int firstResult, int maxResults, SrmExternalResourceRestCriteria webCriteria) {
                uiHandlers.retrieveOrganisationUnitSchemes(firstResult, maxResults, webCriteria, SiemacMetadataExternalField.MEDIATOR);
            }
        };
    }

    public void setUiHandlers(StatisticalResourceUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
}
