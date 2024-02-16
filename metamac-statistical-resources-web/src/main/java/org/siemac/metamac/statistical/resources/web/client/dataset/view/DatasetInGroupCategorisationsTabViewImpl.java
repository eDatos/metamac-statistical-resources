package org.siemac.metamac.statistical.resources.web.client.dataset.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.CategorisationDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetInGroupCategorisationsTabPresenter.DatasetInGroupCategorisationsTabView;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetInGroupCategorisationsTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.model.record.CategorisationRecord;
import org.siemac.metamac.statistical.resources.web.client.widgets.CategorisationsInGroupPanel;
import org.siemac.metamac.web.common.shared.criteria.SrmExternalResourceRestCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;

public class DatasetInGroupCategorisationsTabViewImpl extends ViewWithUiHandlers<DatasetInGroupCategorisationsTabUiHandlers> implements DatasetInGroupCategorisationsTabView {

    private VLayout                            panel;

    private DatasetInGroupCategorisationsPanel categorisationsPanel;

    public DatasetInGroupCategorisationsTabViewImpl() {
        panel = new VLayout();
        panel.setHeight100();

        categorisationsPanel = new DatasetInGroupCategorisationsPanel();

        panel.addMember(categorisationsPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setCategorySchemesForCategorisations(List<ExternalItemDto> categorySchemes, Integer firstResultOut, Integer totalResults) {
        categorisationsPanel.setCategorySchemesForCategorisations(categorySchemes, firstResultOut, totalResults);
    }

    @Override
    public void setCategoriesForCategorisations(List<ExternalItemDto> categories, Integer firstResultOut, Integer totalResults) {
        categorisationsPanel.setCategoriesForCategorisations(categories, firstResultOut, totalResults);
    }

    private class DatasetInGroupCategorisationsPanel extends CategorisationsInGroupPanel {

        @Override
        protected void retrieveCategoriesForCategorisations(int firstResult, int maxResults, SrmItemRestCriteria categoryWebCriteria) {
            getUiHandlers().retrieveCategoriesForCategorisations(firstResult, maxResults, categoryWebCriteria);
        }

        @Override
        protected void retrieveCategorySchemesForCategorisations(int firstResult, int maxResults, SrmExternalResourceRestCriteria categorySchemeWebCriteria) {
            getUiHandlers().retrieveCategorySchemesForCategorisations(firstResult, maxResults, categorySchemeWebCriteria);
        }

        @Override
        protected void addNewCategorisations(List<ExternalItemDto> selectedResources) {
            List<CategorisationDto> dto = new ArrayList<CategorisationDto>();

            for (ExternalItemDto category : selectedResources) {
                if (categorisationsPanel.checkExistCategorisationInListGrid(category.getUrn())) {
                    continue;
                }
                CategorisationDto categorisationDto = new CategorisationDto();
                categorisationDto.setId(category.getId());
                categorisationDto.setCode(category.getCode());
                categorisationDto.setTitle(category.getTitle());
                categorisationDto.setUrn(category.getUrn());
                categorisationDto.setCategory(category);
                dto.add(categorisationDto);
            }
            categorisationsPanel.addCategorisations(dto);

        }

        @Override
        public void deleteCategorisations(ListGridRecord[] selectedResources) {
            CategorisationRecord[] categorisations = new CategorisationRecord[categorisationListGrid.getRecords().length - categorisationListGrid.getSelectedRecords().length];
            int numCategorisation = 0;

            for (ListGridRecord rawRecord : categorisationListGrid.getRecords()) {
                CategorisationRecord categorisationRecord = (CategorisationRecord) rawRecord;
                if (!deleteCategorisation(categorisationRecord.getUrn())) {
                    categorisations[numCategorisation++] = categorisationRecord;
                }
            }

            categorisationListGrid.setAutoFitMaxRecords(categorisations.length);
            categorisationListGrid.setData(categorisations);

        }

        private boolean deleteCategorisation(String urnCategorisation) {
            for (ListGridRecord record : categorisationListGrid.getSelectedRecords()) {
                CategorisationRecord categorisationRecord = (CategorisationRecord) record;
                if (categorisationRecord.getUrn().equals(urnCategorisation)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canAllCategorisationsBeDeleted(ListGridRecord[] records) {
            return true;
        }

        @Override
        protected void deleteCategorisations(List<String> selectedCategorisationUrns) {
            // TODO Auto-generated method stub
        }

        @Override
        public void updateNewButtonVisibility() {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean canCancelAllCategorisationsValidity(ListGridRecord[] records) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        protected void createCategorisations(List<String> selectedResourcesUrns) {
            // TODO Auto-generated method stub

        }

        @Override
        protected void endCategorisationsValidity(List<String> selectedCategorisationUrns, Date endValidityDate) {
            // TODO Auto-generated method stub

        }
    }
}
