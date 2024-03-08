package org.siemac.metamac.statistical.resources.web.client.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.web.client.model.record.CategorisationRecord;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public abstract class CategorisationsInGroupPanel extends CategorisationsPanel {

    protected CategorisationsInGroupPanel() {
        super();
    }

    @Override
    protected void retrieveSelectedCategories(List<ExternalItemDto> selectedCategories) {
        addNewCategorisations(selectedCategories);
        categoriesSelectionWindow.markForDestroy();
    }

    @Override
    protected void deleteSelectedCategorisations() {
        deleteCategorisations(categorisationListGrid.getSelectedRecords());
    }

    @Override
    protected String getTitleActionNewButton() {
        return getConstants().actionAdd();
    }

    @Override
    public boolean checkExistCategorisationInListGrid(String urn) {
        for (ListGridRecord rawRecord : categorisationListGrid.getRecords()) {
            CategorisationRecord categorisationRecord = (CategorisationRecord) rawRecord;
            if (urn.equals(categorisationRecord.getCategory().getUrn())) {
                return true;
            }
        }
        return false;
    }

    protected abstract void addNewCategorisations(List<ExternalItemDto> selectedResources);
    protected abstract void deleteCategorisations(ListGridRecord[] selectedResources);
}
