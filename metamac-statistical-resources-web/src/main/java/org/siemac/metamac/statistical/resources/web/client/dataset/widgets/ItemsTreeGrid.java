package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.constraint.KeyPartDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.ItemDto;
import org.siemac.metamac.statistical.resources.web.client.base.widgets.NavigableExternalItemTreeGrid;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.web.common.client.resources.StyleUtils;

import com.google.web.bindery.event.shared.HandlerRegistration;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

public class ItemsTreeGrid extends NavigableExternalItemTreeGrid {

    protected static final String SCHEME_NODE_NAME  = "scheme-node";

    protected Tree                tree;
    protected TreeGridField       codeField;
    protected TreeGridField       nameField;
    protected TreeGridField       cascadeField;

    protected boolean             editionMode;

    /**
     * Stores <code>true</code> when a filtering in the {@link TreeGrid} is being executed.
     */
    protected boolean             isFilteringActive = false;
    protected ListGridRecord[]    selectedTreeNodes = null;

    protected HandlerRegistration filterEditionHandler;

    public ItemsTreeGrid(boolean editionMode) {
        this.editionMode = editionMode;
        setCanFocus(false); // To avoid scrolling when a record is clicked
        setCanDragSelectText(true); // To allow text selection
        setHeight(175);
        setAutoFitData(Autofit.VERTICAL);
        setShowOpenIcons(false);
        setShowDropIcons(false);
        setShowSelectedStyle(true);
        setShowPartialSelection(true);
        setCascadeSelection(false);
        setCanSort(false);
        setShowConnectors(true);
        setShowHeader(true);
        setLoadDataOnDemand(false);
        setSelectionType(SelectionStyle.SINGLE);
        setShowCellContextMenus(true);
        setLeaveScrollbarGap(Boolean.FALSE);
        setRollUnderCanvasProperties(StyleUtils.getRollUnderCanvasProperties());
        setCanHover(true);
        setShowHover(true);
        setShowHoverComponents(true);
        setShowHeaderContextMenu(false); // do not show context menu in trees (avoid to show columns that should not be shown)
        setShowFilterEditor(true);
        setSelectionType(SelectionStyle.SIMPLE);
        setSelectionAppearance(editionMode ? SelectionAppearance.CHECKBOX : SelectionAppearance.ROW_STYLE);
        setEditEvent(ListGridEditEvent.CLICK);
        createTreeFields();
        createFilterEditionHandlers();
        addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (!isFilteringActive) {
                    if (ItemsTreeGrid.this.getFilterEditorCriteria().getValues().isEmpty()) {
                        selectedTreeNodes = ItemsTreeGrid.this.getSelectedRecords();
                    } else {
                        ListGridRecord selectedRecord = ((ListGridRecord) event.getRecord());
                        selectedTreeNodes = modifyRecordList(selectedTreeNodes, selectedRecord, event.getState());
                    }
                }
            }
        });
    }

    public void setItems(ExternalItemDto itemScheme, List<ItemDto> items) {
        resetFilterAndSelection();

        TreeNode[] treeNodes = new TreeNode[items.size() + 1];
        treeNodes[0] = createItemSchemeTreeNode(itemScheme);

        for (int i = 0; i < items.size(); i++) {
            treeNodes[i + 1] = createItemTreeNode(items.get(i));
        }

        tree = new Tree();
        tree.setModelType(TreeModelType.PARENT);
        tree.linkNodes(treeNodes);
        setData(tree);
        getData().openAll();
    }

    public void selectItems(Map<String, KeyPartDto> keyParts, boolean deselectOtherKeys) {
        if (deselectOtherKeys) {
            resetFilterAndSelection();
        }
        ListGridRecord[] records = getRecords();
        for (ListGridRecord record : records) {
            String code = record.getAttribute(ItemDS.CODE);
            if (keyParts.containsKey(code)) {
                record.setAttribute(ItemDS.CASCADE, BooleanUtils.isTrue(keyParts.get(code).getCascadeValues()) ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
                updateData(record);
                selectRecord(record);
            } else if (deselectOtherKeys) {
                deselectRecord(record);
            }
        }
        selectedTreeNodes = ItemsTreeGrid.this.getSelectedRecords();

        if (deselectOtherKeys) {
            this.refreshFields();
        }
    }

    /**
     * Returns a {@link Map} with the selected codes and a {@link Boolean} value indicating if the cascade option has been selected.
     * 
     * @return
     */
    public Map<String, Boolean> getSelectedItems() {
        Map<String, Boolean> selectedItems = new HashMap<String, Boolean>();
        if (selectedTreeNodes != null) {
            for (ListGridRecord record : selectedTreeNodes) {
                selectedItems.put(record.getAttribute(ItemDS.CODE), Boolean.parseBoolean(record.getAttribute(ItemDS.CASCADE)));
            }
        }
        return selectedItems;
    }

    private void clearFilterEditor() {
        setFilterEditorCriteria(null);
    }

    private void clearSelectedTreeNodes() {
        selectedTreeNodes = null;
    }

    public void resetFilterAndSelection() {
        clearFilterEditor();
        clearSelectedTreeNodes();
        isFilteringActive = false;
    }

    private void createTreeFields() {
        codeField = new TreeGridField(ItemDS.MANAGEMENT_APP_URL, getConstants().identifiableStatisticalResourceCode());
        codeField.setType(ListGridFieldType.LINK);
        codeField.setWidth("30%");
        codeField.setCanFilter(true);
        codeField.setCanEdit(false);
        codeField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                return record.getAttribute(ItemDS.CODE);
            }
        });

        nameField = new TreeGridField(ItemDS.NAME, getConstants().nameableStatisticalResourceTitle());
        nameField.setCanFilter(true);
        nameField.setCanEdit(false);

        cascadeField = new TreeGridField(ItemDS.CASCADE, getConstants().datasetConstraintInCascade());
        cascadeField.setWidth("20%");
        cascadeField.setCanEdit(editionMode);
        cascadeField.setCanFilter(false);
        cascadeField.setEditorType(new SelectItem("inclusion-type", "inclusion type"));
        cascadeField.setValueMap(CommonUtils.getBooleanHashMap());
        cascadeField.setAlign(Alignment.CENTER);
        cascadeField.setShowHover(false);

        setFields(codeField, nameField, cascadeField);
    }

    private void createFilterEditionHandlers() {
        filterEditionHandler = addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {

            @Override
            public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {

                isFilteringActive = true;

                event.cancel();
                TreeNode[] treeNodes = tree.getAllNodes();

                String codeCriteria = event.getCriteria().getAttribute(ItemDS.MANAGEMENT_APP_URL);
                String nameCriteria = event.getCriteria().getAttribute(ItemDS.NAME);

                if (StringUtils.isBlank(codeCriteria) && StringUtils.isBlank(nameCriteria)) {
                    setData(tree);
                } else {
                    List<TreeNode> matchingNodes = new ArrayList<TreeNode>();
                    for (TreeNode treeNode : treeNodes) {
                        if (!SCHEME_NODE_NAME.equals(treeNode.getName())) {
                            String code = treeNode.getAttributeAsString(ItemDS.CODE);
                            String name = treeNode.getAttributeAsString(ItemDS.NAME);

                            boolean matches = true;
                            if (codeCriteria != null && !StringUtils.containsIgnoreCase(code, codeCriteria)) {
                                matches = false;
                            }
                            if (nameCriteria != null && !StringUtils.containsIgnoreCase(name, nameCriteria)) {
                                matches = false;
                            }
                            if (matches) {
                                matchingNodes.add(treeNode);
                            }
                        }
                    }
                    Tree resultTree = new Tree();
                    resultTree.setData(matchingNodes.toArray(new TreeNode[0]));
                    setData(resultTree);
                }
                selectRecords(selectedTreeNodes);

                isFilteringActive = false;
            }
        });
    }

    private TreeNode createItemSchemeTreeNode(ExternalItemDto itemScheme) {
        TreeNode node = new TreeNode(SCHEME_NODE_NAME);
        node.setID(SCHEME_NODE_NAME);
        node.setAttribute(ItemDS.URN, itemScheme.getUrn());
        node.setAttribute(ItemDS.CODE, itemScheme.getCode());
        node.setAttribute(ItemDS.MANAGEMENT_APP_URL, itemScheme.getManagementAppUrl());
        node.setAttribute(ItemDS.NAME, getLocalisedString(itemScheme.getTitle()));
        node.setLinkText(itemScheme.getCode());
        node.setEnabled(false);
        return node;
    }

    private TreeNode createItemTreeNode(ItemDto itemDto) {
        String parentUrn = !StringUtils.isBlank(itemDto.getItemParentUrn()) ? itemDto.getItemParentUrn() : SCHEME_NODE_NAME;
        TreeNode node = new TreeNode();
        node.setID(itemDto.getUrn());
        node.setParentID(parentUrn);
        node.setAttribute(ItemDS.CODE, itemDto.getCode());
        node.setAttribute(ItemDS.MANAGEMENT_APP_URL, itemDto.getManagementAppUrl());
        node.setAttribute(ItemDS.NAME, getLocalisedString(itemDto.getTitle()));
        node.setAttribute(ItemDS.URN, itemDto.getUrn());
        node.setLinkText(itemDto.getCode());

        if (editionMode) {
            node.setAttribute(ItemDS.CASCADE, Boolean.FALSE.toString());
        }
        return node;
    }

    private class ItemDS extends DataSource {

        public static final String CODE               = "item-code";
        public static final String MANAGEMENT_APP_URL = "item-url";
        public static final String NAME               = "item-name";
        public static final String URN                = "item-urn";
        public static final String CASCADE            = "item-cascade";
    }

    /**
     * Modify the record list adding or deleting the specified record. If the state is <code>true</code>, the record is added. If the state is <code>false</code>, is removed.
     * 
     * @param records
     * @param record
     * @param state
     * @return
     */
    private ListGridRecord[] modifyRecordList(ListGridRecord[] records, ListGridRecord record, boolean state) {
        int length = state ? records.length + 1 : records.length - 1;
        ListGridRecord[] result = new ListGridRecord[length];
        int j = 0;
        for (int i = 0; i < records.length; i++) {
            if (!state) {
                if (!StringUtils.equals(records[i].getAttributeAsString(ItemDS.URN), record.getAttributeAsString(ItemDS.URN))) {
                    result[j] = records[i];
                    j++;
                }
            } else {
                result[j] = records[i];
                j++;
            }
        }
        if (state) {
            result[j] = record;
        }
        return result;
    }
}
