package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.constraint.KeyPartDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.ItemDto;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;

public class ItemsSelectionTreeItem extends CustomCanvasItem {

    private ItemsTreeGrid itemsTreeGrid;

    public ItemsSelectionTreeItem(String name, String title, boolean editionMode) {
        super(name, title);
        setCellStyle("dragAndDropCellStyle");
        if (!editionMode) {
            setTitleStyle("staticFormItemTitle");
        }

        itemsTreeGrid = new ItemsTreeGrid(editionMode);
        itemsTreeGrid.setMargin(10);
        setCanvas(itemsTreeGrid);
    }

    public void setItems(ExternalItemDto itemScheme, List<ItemDto> items) {
        itemsTreeGrid.setItems(itemScheme, items);
    }

    public void selectItems(Map<String, KeyPartDto> keyParts, boolean resetSelectedNodes) {
        itemsTreeGrid.selectItems(keyParts, resetSelectedNodes);
    }

    public Map<String, Boolean> getSelectedItems() {
        return itemsTreeGrid.getSelectedItems();
    }

    public void clearSelectedNodes() {
        itemsTreeGrid.resetFilterAndSelection();
    }
}
