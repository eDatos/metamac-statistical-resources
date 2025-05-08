package org.siemac.metamac.statistical.resources.web.client.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.CategorisationDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.model.ds.CategorisationDS;
import org.siemac.metamac.statistical.resources.web.client.model.record.CategorisationRecord;
import org.siemac.metamac.statistical.resources.web.client.utils.StatisticalResourcesRecordUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.ListGridUtils;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.client.widgets.CustomLinkListGridField;
import org.siemac.metamac.web.common.client.widgets.CustomListGridField;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.windows.search.SearchMultipleSrmItemWithSchemeFilterPaginatedWindow;
import org.siemac.metamac.web.common.shared.criteria.SrmExternalResourceRestCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public abstract class CategorisationsPanel extends VLayout {

    protected ToolStripButton                                      newCategorisationButton;
    protected ToolStripButton                                      deleteCategorisationButton;
    protected ToolStripButton                                      cancelCategorisationValidityButton;
    protected NavigableListGrid                                    categorisationListGrid;

    private DeleteConfirmationWindow                               deleteConfirmationWindow;

    protected ProcStatusEnum                                       categorisedArtefactProcStatus;

    protected SearchMultipleSrmItemWithSchemeFilterPaginatedWindow categoriesSelectionWindow;

    protected DateWindow                                           dateWindow;

    public CategorisationsPanel() {
        initCategorisationsPanel();
    }

    private void initCategorisationsPanel() {
        setMargin(15);

        // ToolStrip

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        newCategorisationButton = new ToolStripButton(getTitleActionNewButton(), RESOURCE.newListGrid().getURL());
        newCategorisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                showSearchCategoriesWindow();
            }
        });

        deleteCategorisationButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteCategorisationButton.setVisible(false);
        deleteCategorisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        cancelCategorisationValidityButton = new ToolStripButton(getConstants().categoryCancelValidity(), RESOURCE.disable().getURL());
        cancelCategorisationValidityButton.setVisible(false);
        cancelCategorisationValidityButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                dateWindow = new DateWindow(getConstants().categoryCancelValidityDate());
                dateWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (dateWindow.validateForm()) {
                            Date endValidityDate = dateWindow.getSelectedDate();
                            dateWindow.markForDestroy();
                            endCategorisationsValidity(getSelectedCategorisationUrns(), endValidityDate);
                        }
                    }
                });
            }
        });

        toolStrip.addButton(newCategorisationButton);
        toolStrip.addButton(deleteCategorisationButton);
        toolStrip.addButton(cancelCategorisationValidityButton);

        // Deletion window

        deleteConfirmationWindow = new DeleteConfirmationWindow(MetamacWebCommon.getConstants().deleteConfirmationTitle(), MetamacWebCommon.getConstants().deleteConfirmationMessage());
        deleteConfirmationWindow.setVisible(false);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteSelectedCategorisations();
                deleteConfirmationWindow.hide();
            }
        });

        // ListGrid

        categorisationListGrid = new NavigableListGrid();
        categorisationListGrid.setOverflow(Overflow.VISIBLE);
        categorisationListGrid.setAutoFitData(Autofit.VERTICAL);
        ListGridUtils.setCheckBoxSelectionType(categorisationListGrid);
        CustomListGridField codeField = new CustomListGridField(CategorisationDS.CODE, getConstants().identifiableStatisticalResourceCode());
        CustomLinkListGridField categoryField = new CustomLinkListGridField(CategorisationDS.CATEGORY, getConstants().category());
        CustomListGridField validFrom = new CustomListGridField(CategorisationDS.VALID_FROM, getConstants().versionableStatisticalResourceValidFrom());
        CustomListGridField validTo = new CustomListGridField(CategorisationDS.VALID_TO, getConstants().versionableStatisticalResourceValidTo());
        categorisationListGrid.setFields(codeField, categoryField, validFrom, validTo);

        categorisationListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (categorisationListGrid.getSelectedRecords().length > 0) {
                    showDeleteCategorisationButton(categorisationListGrid.getSelectedRecords());
                    showCancelCategorisationValidity(categorisationListGrid.getSelectedRecords());
                } else {
                    deleteCategorisationButton.hide();
                    cancelCategorisationValidityButton.hide();
                }
            }
        });

        addMember(toolStrip);
        addMember(categorisationListGrid);
    }

    protected String getTitleActionNewButton() {
        return getConstants().actionNew();
    }

    protected void deleteSelectedCategorisations() {
        deleteCategorisations(getSelectedCategorisationUrns());
    }

    public void setCategorisations(List<CategorisationDto> categorisationDtos) {
        categorisationListGrid.setAutoFitMaxRecords(categorisationDtos.size());
        categorisationListGrid.setData(StatisticalResourcesRecordUtils.getCategorisationRecords(categorisationDtos));
    }

    public void addCategorisations(List<CategorisationDto> categorisationDtos) {
        if (!categorisationDtos.isEmpty()) {
            categorisationListGrid.setAutoFitMaxRecords(categorisationDtos.size() + (categorisationListGrid.getRecords() != null ? categorisationListGrid.getRecords().length : 0));
            CategorisationRecord[] categorisations = StatisticalResourcesRecordUtils.getCategorisationRecords(categorisationDtos);
            for (int i = 0; i < categorisations.length; i++) {
                categorisationListGrid.addData(categorisations[i]);
            }
        }
        categorisationListGrid.sort();
    }

    public boolean checkExistCategorisationInListGrid(String urn) {
        for (ListGridRecord rawRecord : categorisationListGrid.getRecords()) {
            CategorisationRecord categorisationRecord = (CategorisationRecord) rawRecord;
            if (urn.equals(categorisationRecord.getUrn())) {
                return true;
            }
        }
        return false;
    }

    public void setUiHandlers(BaseUiHandlers uiHandlers) {
        categorisationListGrid.setUiHandlers(uiHandlers);
    }

    public ProcStatusEnum getCategorisedArtefactProcStatus() {
        return categorisedArtefactProcStatus;
    }

    public void setCategorisedArtefactProcStatus(ProcStatusEnum procStatus) {
        this.categorisedArtefactProcStatus = procStatus;
    }

    private List<String> getSelectedCategorisationUrns() {
        List<String> urns = new ArrayList<String>();
        for (ListGridRecord record : categorisationListGrid.getSelectedRecords()) {
            CategorisationRecord categorisationRecord = (CategorisationRecord) record;
            urns.add(categorisationRecord.getUrn());
        }
        return urns;
    }

    protected void showSearchCategoriesWindow() {
        SearchPaginatedAction<SrmExternalResourceRestCriteria> filterAction = new SearchPaginatedAction<SrmExternalResourceRestCriteria>() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults, SrmExternalResourceRestCriteria webCriteria) {
                webCriteria.setOnlyLastVersion(categoriesSelectionWindow.getFilter().getSearchCriteria().isItemSchemeLastVersion());
                retrieveCategorySchemesForCategorisations(firstResult, maxResults, webCriteria);
            }
        };

        categoriesSelectionWindow = new SearchMultipleSrmItemWithSchemeFilterPaginatedWindow(getConstants().resourceSelection(), StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS, filterAction,
                new SearchPaginatedAction<SrmItemRestCriteria>() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, SrmItemRestCriteria webCriteria) {
                        retrieveCategoriesForCategorisations(firstResult, maxResults, webCriteria);
                    }

                });

        retrieveCategoriesForCategorisations(0, StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS, categoriesSelectionWindow.getFilter().getSearchCriteria());

        categoriesSelectionWindow.setSaveAction(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                retrieveSelectedCategories(categoriesSelectionWindow.getSelectedResources());
            }
        });
    }

    protected void retrieveSelectedCategories(List<ExternalItemDto> selectedCategories) {
        List<String> urns = new ArrayList<String>();
        for (ExternalItemDto item : selectedCategories) {
            urns.add(item.getUrn());
        }
        createCategorisations(urns);
        categoriesSelectionWindow.markForDestroy();
    }

    public void setCategoriesForCategorisations(List<ExternalItemDto> categories, Integer firstResultOut, Integer totalResults) {
        if (categoriesSelectionWindow != null) {
            categoriesSelectionWindow.setResources(categories);
            categoriesSelectionWindow.refreshSourcePaginationInfo(firstResultOut, categories.size(), totalResults);
        }
    }

    public void setCategorySchemesForCategorisations(List<ExternalItemDto> categorySchemes, Integer firstResultOut, Integer totalResults) {
        if (categoriesSelectionWindow != null) {
            categoriesSelectionWindow.setFilterResources(categorySchemes);
            categoriesSelectionWindow.refreshFilterSourcePaginationInfo(firstResultOut, categorySchemes.size(), totalResults);
        }
    }

    private void showDeleteCategorisationButton(ListGridRecord[] selectedRecords) {
        if (canAllCategorisationsBeDeleted(selectedRecords)) {
            deleteCategorisationButton.show();
        } else {
            deleteCategorisationButton.hide();
        }
    }

    private void showCancelCategorisationValidity(ListGridRecord[] selectedRecords) {
        if (canCancelAllCategorisationsValidity(selectedRecords)) {
            cancelCategorisationValidityButton.show();
        } else {
            cancelCategorisationValidityButton.hide();
        }
    }

    public abstract void updateNewButtonVisibility();
    public abstract boolean canAllCategorisationsBeDeleted(ListGridRecord[] records);
    public abstract boolean canCancelAllCategorisationsValidity(ListGridRecord[] records);

    // ACTIONS

    protected abstract void retrieveCategorySchemesForCategorisations(int firstResult, int maxResults, SrmExternalResourceRestCriteria webCriteria);
    protected abstract void retrieveCategoriesForCategorisations(int firstResult, int maxResults, SrmItemRestCriteria webCriteria);
    protected abstract void createCategorisations(List<String> selectedResourcesUrns);
    protected abstract void deleteCategorisations(List<String> selectedCategorisationUrns);
    protected abstract void endCategorisationsValidity(List<String> selectedCategorisationUrns, Date endValidityDate);
}
