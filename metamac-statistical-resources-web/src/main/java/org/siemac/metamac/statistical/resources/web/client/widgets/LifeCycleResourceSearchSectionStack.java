package org.siemac.metamac.statistical.resources.web.client.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesDefaults.getSelectedStatisticalOperation;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.CustomDatePickerItem;
import org.siemac.metamac.statistical.resources.web.client.model.ds.LifeCycleResourceDS;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.shared.criteria.LifeCycleStatisticalResourceWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListResult;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.BaseAdvancedSearchSectionStack;
import org.siemac.metamac.web.common.client.widgets.SearchExternalItemWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomDateItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchExternalItemLinkItem;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public abstract class LifeCycleResourceSearchSectionStack extends BaseAdvancedSearchSectionStack {

    private SearchExternalItemWindow searchOperationWindow;

    public LifeCycleResourceSearchSectionStack() {
    }

    @Override
    protected void clearAdvancedSearchSection() {
        super.clearAdvancedSearchSection();
        // Search last versions by default
        ((CustomCheckboxItem) advancedSearchForm.getItem(LifeCycleResourceDS.LAST_VERSION)).setValue(true);
        // Search by selected operation by default
        advancedSearchForm.setValue(LifeCycleResourceDS.STATISTICAL_OPERATION, RecordUtils.getExternalItemRecord(getSelectedStatisticalOperation()));
    }

    @Override
    protected void createAdvancedSearchForm() {
        advancedSearchForm = new GroupDynamicForm(StringUtils.EMPTY);
        advancedSearchForm.setPadding(5);
        advancedSearchForm.setMargin(5);
        advancedSearchForm.setVisible(false);

        SearchExternalItemLinkItem searchOperationItem = createSearchStatisticalOperationItem(LifeCycleResourceDS.STATISTICAL_OPERATION, getConstants()
                .siemacMetadataStatisticalResourceStatisticalOperation());

        TextItem code = new TextItem(LifeCycleResourceDS.CODE, getConstants().identifiableStatisticalResourceCode());
        TextItem name = new TextItem(LifeCycleResourceDS.TITLE, getConstants().nameableStatisticalResourceTitle());
        TextItem urn = new TextItem(LifeCycleResourceDS.URN, getConstants().identifiableStatisticalResourceURN());
        TextItem description = new TextItem(LifeCycleResourceDS.DESCRIPTION, getConstants().nameableStatisticalResourceDescription());

        SelectItem nextVersionType = new SelectItem(LifeCycleResourceDS.NEXT_VERSION, getConstants().versionableStatisticalResourceNextVersion());
        nextVersionType.setValueMap(CommonUtils.getStatisticalResourceNextVersionHashMap());

        CustomDatePickerItem nextVersionDate = createFieldDateNextVersion();

        SelectItem procStatus = new SelectItem(LifeCycleResourceDS.PROC_STATUS, getConstants().lifeCycleStatisticalResourceProcStatus());
        procStatus.setValueMap(CommonUtils.getProcStatusHashMap());

        CustomCheckboxItem isLastVersion = new CustomCheckboxItem(LifeCycleResourceDS.LAST_VERSION, MetamacWebCommon.getConstants().isLastVersion());
        isLastVersion.setValue(true);

        CustomButtonItem searchItem = new CustomButtonItem(ADVANCED_SEARCH_ITEM_NAME, MetamacWebCommon.getConstants().search());
        searchItem.setColSpan(4);
        searchItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                retrieveResources();
            }
        });
        FormItem[] advancedSearchFormItems = new FormItem[]{searchOperationItem, code, name, urn, description, nextVersionType, nextVersionDate, procStatus, isLastVersion, searchItem};
        setFormItemsInAdvancedSearchForm(advancedSearchFormItems);
    }

    private CustomDatePickerItem createFieldDateNextVersion() {
        CustomDatePickerItem customDatePickerItem = new CustomDatePickerItem(LifeCycleResourceDS.DATE_NEXT_VERSION, getConstants().versionableStatisticalResourceNextVersionDate(), false, false);
        customDatePickerItem.defaultDateType();
        return customDatePickerItem;
    }
    
    public LifeCycleStatisticalResourceWebCriteria getLifeCycleResourceWebCriteria(LifeCycleStatisticalResourceWebCriteria lifecycleStatisticalResourceWebCriteria) {

        lifecycleStatisticalResourceWebCriteria.setCriteria(searchForm.getValueAsString(SEARCH_ITEM_NAME));

        ExternalItemDto operation = ((SearchExternalItemLinkItem) advancedSearchForm.getItem(LifeCycleResourceDS.STATISTICAL_OPERATION)).getExternalItemDto();
        if (operation != null) {
            lifecycleStatisticalResourceWebCriteria.setStatisticalOperationUrn(operation.getUrn());
        }

        lifecycleStatisticalResourceWebCriteria.setCode(advancedSearchForm.getValueAsString(LifeCycleResourceDS.CODE));
        lifecycleStatisticalResourceWebCriteria.setTitle(advancedSearchForm.getValueAsString(LifeCycleResourceDS.TITLE));
        lifecycleStatisticalResourceWebCriteria.setUrn(advancedSearchForm.getValueAsString(LifeCycleResourceDS.URN));
        lifecycleStatisticalResourceWebCriteria.setDescription(advancedSearchForm.getValueAsString(LifeCycleResourceDS.DESCRIPTION));
        lifecycleStatisticalResourceWebCriteria.setNextVersionType(CommonUtils.getNextVersionTypeEnum(advancedSearchForm.getValueAsString(LifeCycleResourceDS.NEXT_VERSION)));
        lifecycleStatisticalResourceWebCriteria.setNextVersionDate(((CustomDatePickerItem) advancedSearchForm.getItem(LifeCycleResourceDS.DATE_NEXT_VERSION)).getValue());
        lifecycleStatisticalResourceWebCriteria.setProcStatus(CommonUtils.getProcStatusEnum(advancedSearchForm.getValueAsString(LifeCycleResourceDS.PROC_STATUS)));
        lifecycleStatisticalResourceWebCriteria.setOnlyLastVersion(((CustomCheckboxItem) advancedSearchForm.getItem(LifeCycleResourceDS.LAST_VERSION)).getValueAsBoolean());
        return lifecycleStatisticalResourceWebCriteria;
    }

    //
    // RELATED RESOURCES
    //

    private SearchExternalItemLinkItem createSearchStatisticalOperationItem(String name, String title) {
        SearchExternalItemLinkItem operationItem = new SearchExternalItemLinkItem(name, title) {

            @Override
            public void onSearch() {
                searchOperationWindow = new SearchExternalItemWindow(getConstants().statisticalOperation(), StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        retrieveStatisticalOperations(firstResult, maxResults, new MetamacWebCriteria(searchOperationWindow.getSearchCriteria()));
                    }
                });
                retrieveStatisticalOperations(0, StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS, new MetamacWebCriteria());
                searchOperationWindow.getListGrid().setSelectionType(SelectionStyle.SINGLE); // Only one statistical operation can be selected
                searchOperationWindow.getExternalListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        retrieveStatisticalOperations(firstResult, maxResults, new MetamacWebCriteria(criteria));
                    }
                });
                searchOperationWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        ExternalItemDto selectedOperation = searchOperationWindow.getSelectedExternalItem();
                        searchOperationWindow.destroy();
                        getForm().setValue(getName(), RecordUtils.getExternalItemRecord(selectedOperation));
                        getForm().validate(false);
                    }
                });
            }
        };
        return operationItem;
    }

    public void setStatisticalOperations(GetStatisticalOperationsPaginatedListResult result) {
        if (searchOperationWindow != null) {
            searchOperationWindow.setExternalItems(result.getOperationsList());
            searchOperationWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getOperationsList().size(), result.getTotalResults());
        }
    }

    //
    // ABSTRACT METHODS
    //

    public abstract void retrieveStatisticalOperations(int firstResult, int maxResults, MetamacWebCriteria criteria);
}
