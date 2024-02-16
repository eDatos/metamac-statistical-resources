package org.siemac.metamac.statistical.resources.web.client.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;

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

    protected abstract void addNewCategorisations(List<ExternalItemDto> selectedResources);
    protected abstract void deleteCategorisations(ListGridRecord[] selectedResources);
}
