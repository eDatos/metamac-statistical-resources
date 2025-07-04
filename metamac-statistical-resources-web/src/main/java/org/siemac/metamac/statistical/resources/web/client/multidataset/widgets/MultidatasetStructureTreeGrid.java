package org.siemac.metamac.statistical.resources.web.client.multidataset.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetCubeDto;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.base.widgets.NavigableTreeGrid;
import org.siemac.metamac.statistical.resources.web.client.multidataset.model.ds.MultidatasetCubeDS;
import org.siemac.metamac.statistical.resources.web.client.multidataset.model.ds.MultidatasetCubeTreeNode;
import org.siemac.metamac.statistical.resources.web.client.multidataset.utils.MultidatasetClientSecurityUtils;
import org.siemac.metamac.statistical.resources.web.client.multidataset.view.handlers.MultidatasetStructureTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.publication.model.ds.ElementLevelDS;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.StatisticalResourcesRecordUtils;
import org.siemac.metamac.web.common.client.resources.StyleUtils;
import org.siemac.metamac.web.common.client.utils.ListGridUtils;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.google.web.bindery.event.shared.HandlerRegistration;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderContextClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderContextClickHandler;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;
import com.smartgwt.client.widgets.tree.events.LeafContextClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafContextClickHandler;
import com.smartgwt.client.widgets.viewer.DetailViewer;
import com.smartgwt.client.widgets.viewer.DetailViewerField;

public class MultidatasetStructureTreeGrid extends NavigableTreeGrid {

    protected static final String                  SCHEME_NODE_NAME = "scheme-node";

    protected MultidatasetStructureNodeClickAction nodeClickAction;

    protected Menu                                 contextMenu;
    private MenuItem                               createCubeMenuItem;
    private MenuItem                               deleteElementMenuItem;

    protected HandlerRegistration                  folderContextHandlerRegistration;
    protected HandlerRegistration                  leafContextHandlerRegistration;
    protected HandlerRegistration                  folderClickHandlerRegistration;
    protected HandlerRegistration                  leafClickHandlerRegistration;
    protected HandlerRegistration                  folderDropHandlerRegistration;

    protected MultidatasetVersionDto               multidatasetVersion;

    protected Tree                                 tree;
    protected TreeGridField                        identifierField;
    protected TreeGridField                        titleField;
    protected TreeGridField                        urnField;
    protected TreeGridField                        resourceField;
    protected TreeGridField                        resourceTypeField;
    protected TreeGridField                        dsdField;

    protected TreeGridField                        queryDatasetField;

    protected TreeGridField                        orderField;
    protected TreeGridField                        infoField;

    protected HandlerRegistration                  filterEditionHandler;

    protected MultidatasetStructureTabUiHandlers   uiHandlers;

    protected MultidatasetCubeDto                  selectedContextClickElement;

    public MultidatasetStructureTreeGrid(MultidatasetStructureNodeClickAction treeNodeClickAction) {
        nodeClickAction = treeNodeClickAction;

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
        setCanDragSelectText(false); // do not allow text selection! (strange behavior when dragging nodes)

        setCanReorderRecords(true);
        setCanAcceptDroppedRecords(true);
        setCanDragRecordsOut(false);
        setDragDataAction(DragDataAction.MOVE);
        setShowOpenIcons(true);
        setShowDropIcons(true);

        identifierField = new TreeGridField(MultidatasetCubeDS.IDENTIFIER, getConstants().multidatasetStructureCubeIdentifier());
        identifierField.setShowHover(false); // only show hover in info field
        identifierField.setCanFilter(true);
        identifierField.setCanSort(false);

        titleField = new TreeGridField(MultidatasetCubeDS.TITLE, getConstants().multidatasetStructureCubeTitle());
        titleField.setShowHover(false); // only show hover in info field
        titleField.setCanFilter(true);
        titleField.setCanSort(false);

        urnField = new TreeGridField(MultidatasetCubeDS.URN, getConstants().multidatasetStructureCubeURN());
        urnField.setShowHover(false); // only show hover in info field
        urnField.setCanFilter(true);
        urnField.setCanSort(false);

        resourceTypeField = new TreeGridField(MultidatasetCubeDS.RESOURCE_TYPE_TO_LINK, getConstants().multidatasetStructureCubeResourceTypeLinked());
        resourceTypeField.setShowHover(false); // only show hover in info field
        resourceTypeField.setCanFilter(true);
        resourceTypeField.setCanSort(false);

        resourceField = new TreeGridField(MultidatasetCubeDS.RESOURCE_TO_LINK, getConstants().multidatasetStructureCubeResource());
        resourceField.setShowHover(false); // only show hover in info field
        resourceField.setCanFilter(true);
        resourceField.setCanSort(false);
        resourceField.setType(ListGridFieldType.LINK);
        resourceField.setCellFormatter(new CellFormatter() {

            @Override
            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
                return ResourceFieldUtils.formatResourceLink(value, record, ElementLevelDS.RESOURCE_TYPE_TO_LINK);
            }
        });

        dsdField = new TreeGridField(MultidatasetCubeDS.DSD, getConstants().multidatasetStructureCubeDSD());
        dsdField.setShowHover(false); // only show hover in info field
        dsdField.setCanFilter(true);
        dsdField.setCanSort(false);
        dsdField.setType(ListGridFieldType.LINK);
        dsdField.setCellFormatter(new CellFormatter() {

            @Override
            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
                return CommonUtils.formatExternalItemValue(value, true, false);
            }
        });

        queryDatasetField = new TreeGridField(MultidatasetCubeDS.QUERY_DATASET, getConstants().multidatasetStructureCubeQueryDataset());
        queryDatasetField.setShowHover(false); // only show hover in info field
        queryDatasetField.setCanFilter(true);
        queryDatasetField.setCanSort(false);
        queryDatasetField.setType(ListGridFieldType.LINK);
        queryDatasetField.setCellFormatter(new CellFormatter() {

            @Override
            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
                return ResourceFieldUtils.formatResourceLink(value, record, ElementLevelDS.RESOURCE_TYPE_TO_LINK);
            }
        });

        orderField = new TreeGridField(MultidatasetCubeDS.ORDER_IN_LEVEL, getConstants().multidatasetStructureCubeOrderInLevel());
        orderField.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());
        orderField.setCanSort(true);
        orderField.setShowHover(false);

        infoField = new TreeGridField(MultidatasetCubeDS.INFO, " ");
        infoField.setType(ListGridFieldType.IMAGE);
        infoField.setWidth(50);
        infoField.setAlign(Alignment.CENTER);
        infoField.setCanFilter(false);
        infoField.setShowHover(true);

        setFields(identifierField, titleField, resourceTypeField, resourceField, queryDatasetField, dsdField, urnField, orderField, infoField);

        // Order by ORDER field
        setCanSort(true);
        setSortField(MultidatasetCubeDS.ORDER_IN_LEVEL);
        setShowFilterEditor(true);

        createContextMenu();

        bindEvents();
    }

    private String formatResourceLink(Object value, ListGridRecord record, String resourceTypeFieldName) {
        if (value == null) {
            return null;
        }
        String urn = value.toString();
        String resourceType = record.getAttribute(resourceTypeFieldName);
        if (!StatisticalResourceTypeEnum.URL.name().equalsIgnoreCase(resourceType) && urn != null && !urn.isEmpty()) {
            return UrnUtils.removePrefix(urn);
        }
        return urn;
    }

    private void createContextMenu() {
        contextMenu = new Menu();

        createCubeMenuItem = new MenuItem(getConstants().multidatasetStructureCreateCube());
        contextMenu.addItem(createCubeMenuItem);

        deleteElementMenuItem = new MenuItem(getConstants().multidatasetStructureDeleteCube());
        deleteElementMenuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                DeleteConfirmationWindow deleteConfirmationWindow = new DeleteConfirmationWindow(getMessages().multidatasetStructureCubeDeleteConfirmationTitle(),
                        getMessages().multidatasetStructureCubeDeleteConfirmation());
                deleteConfirmationWindow.show();
                deleteConfirmationWindow.getYesButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        getUiHandlers().deleteCube(multidatasetVersion.getUrn(), getMultidatasetCubeUrn(selectedContextClickElement));
                    }
                });
            }
        });
        contextMenu.addItem(deleteElementMenuItem);
    }

    private void bindEvents() {

        filterEditionHandler = addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {

            @Override
            public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
                event.cancel();
                TreeNode[] treeNodes = tree.getAllNodes();

                String identifierCriteria = event.getCriteria().getAttribute(MultidatasetCubeDS.IDENTIFIER);
                String titleCriteria = event.getCriteria().getAttribute(MultidatasetCubeDS.TITLE);
                String urnCriteria = event.getCriteria().getAttribute(MultidatasetCubeDS.URN);

                if (StringUtils.isBlank(identifierCriteria) && StringUtils.isBlank(titleCriteria) && StringUtils.isBlank(urnCriteria)) {
                    setData(tree);
                    return;
                } else {
                    List<TreeNode> matchingNodes = new ArrayList<TreeNode>();
                    for (TreeNode treeNode : treeNodes) {
                        if (!SCHEME_NODE_NAME.equals(treeNode.getName())) {
                            String identifier = treeNode.getAttributeAsString(MultidatasetCubeDS.IDENTIFIER);
                            String title = treeNode.getAttributeAsString(MultidatasetCubeDS.TITLE);
                            String urn = treeNode.getAttributeAsString(MultidatasetCubeDS.URN);

                            boolean matches = true;

                            if (identifierCriteria != null && !StringUtils.containsIgnoreCase(identifier, identifierCriteria)) {
                                matches = false;
                            }
                            if (titleCriteria != null && !StringUtils.containsIgnoreCase(title, titleCriteria)) {
                                matches = false;
                            }
                            if (urnCriteria != null && !StringUtils.containsIgnoreCase(urn, urnCriteria)) {
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
            }
        });

        cellClickHandlerRegistration = addCellClickHandler(new CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                ListGridField clickedField = getField(event.getColNum());
                ListGridRecord record = event.getRecord();
                String fieldName = clickedField.getName();
                String urn = record.getAttribute(fieldName);
                boolean urnPresent  = urn != null && !urn.isEmpty();

                if (fieldName.equals(resourceField.getName()) && urnPresent ) {
                    getUiHandlers().goToLastVersion(urn);
                    event.cancel();
                    return;
                } else if (fieldName.equals(queryDatasetField.getName()) && urnPresent ) {
                    getUiHandlers().goToRelatedDatasetQuery(urn);
                    event.cancel();
                    return;
                } else {
                    if (record instanceof MultidatasetCubeTreeNode) {
                        onNodeClick(((MultidatasetCubeTreeNode) record).getMultidatasetCubeDto());
                    }
                }

            }
        });

        folderContextHandlerRegistration = addFolderContextClickHandler(new FolderContextClickHandler() {

            @Override
            public void onFolderContextClick(final FolderContextClickEvent event) {
                onNodeContextClick(event.getFolder().getName(), (MultidatasetCubeDto) event.getFolder().getAttributeAsObject(MultidatasetCubeDS.DTO));
            }
        });
        leafContextHandlerRegistration = addLeafContextClickHandler(new LeafContextClickHandler() {

            @Override
            public void onLeafContextClick(LeafContextClickEvent event) {
                onNodeContextClick(event.getLeaf().getName(), (MultidatasetCubeDto) event.getLeaf().getAttributeAsObject(MultidatasetCubeDS.DTO));
            }
        });

        folderDropHandlerRegistration = addFolderDropHandler(new FolderDropHandler() {

            @Override
            public void onFolderDrop(FolderDropEvent event) {
                TreeNode dropFolder = event.getFolder();
                TreeNode droppedNode = event.getNodes().length > 0 ? event.getNodes()[0] : null;
                int position = event.getIndex(); // Absolute position
                if (isDroppable(dropFolder) && canMoveNode(droppedNode)) {
                    updateLocation(dropFolder, droppedNode, position);
                }
                event.cancel();
            }

            private boolean canMoveNode(TreeNode droppedNode) {
                MultidatasetCubeTreeNode treeNode = ((MultidatasetCubeTreeNode) droppedNode);
                MultidatasetCubeDto multidatasetCubeDto = treeNode.getMultidatasetCubeDto();
                if (multidatasetCubeDto != null) {
                    return MultidatasetClientSecurityUtils.canUpdateCubeLocation(multidatasetVersion.getProcStatus());
                }
                return false;
            }

            protected void updateLocation(TreeNode dropFolder, TreeNode droppedNode, int position) {
                TreeNode[] siblings = getData().getChildren(dropFolder);

                // We find out the position of the node under the dropFolder
                int relativePosition = position; // Used to update position
                int pos = -1;
                for (int i = 0; i < siblings.length; i++) {
                    if (siblings[i] == droppedNode) {
                        pos = i;
                    }
                }
                if (pos >= 0 && pos < position) { // If moved node is before final position, the position must be updated
                    relativePosition--;
                }
                relativePosition++;

                if (droppedNode instanceof MultidatasetCubeTreeNode) {
                    getUiHandlers().updateCubeLocation(multidatasetVersion.getUrn(), ((MultidatasetCubeTreeNode) droppedNode).getUrn(), Long.valueOf(relativePosition));
                }
            }
        });

    }

    public void removeHandlerRegistrations() {
        folderContextHandlerRegistration.removeHandler();
        leafContextHandlerRegistration.removeHandler();
        folderClickHandlerRegistration.removeHandler();
        leafClickHandlerRegistration.removeHandler();
    }

    public void setMultidatasetVersion(MultidatasetVersionDto multidatasetVersionDto) {
        multidatasetVersion = multidatasetVersionDto;

        // Clear filter editor
        setFilterEditorCriteria(null);

        MultidatasetCubeTreeNode multidatasetVersionRootNode = createMultidatasetVersionRootNode(multidatasetVersion);
        multidatasetVersionRootNode.setChildren(createMultidatasetCubesTreeNodes(multidatasetVersionDto.getCubes()));

        addTreeNodesToTreeGrid(new MultidatasetCubeTreeNode[]{multidatasetVersionRootNode});

        setAutoFitMaxRecords(multidatasetVersionDto.getCubes().size());

        setCanDrag(MultidatasetClientSecurityUtils.canUpdateCubeLocation(multidatasetVersion.getProcStatus()));
    }

    public MultidatasetCubeTreeNode[] createMultidatasetCubesTreeNodes(List<MultidatasetCubeDto> multidatasetCubeDtos) {
        MultidatasetCubeTreeNode[] treeNodes = new MultidatasetCubeTreeNode[multidatasetCubeDtos.size()];
        for (int i = 0; i < multidatasetCubeDtos.size(); i++) {
            treeNodes[i] = StatisticalResourcesRecordUtils.getMultidatasetCubeNode(multidatasetCubeDtos.get(i));
        }
        return treeNodes;
    }

    public void addTreeNodesToTreeGrid(MultidatasetCubeTreeNode[] treeNodes) {
        tree = new Tree();
        tree.setModelType(TreeModelType.CHILDREN);
        tree.linkNodes(treeNodes);
        setData(tree);
        getData().openAll();
    }

    public void selectCube(MultidatasetCubeDto cube) {
        String urn = cube.getUrn();
        RecordList nodes = getDataAsRecordList();
        Record record = nodes.find(MultidatasetCubeDS.URN, urn);
        if (record != null) {
            selectRecord(record);
        }
    }

    protected void showContextMenu() {
        contextMenu.markForRedraw();
        contextMenu.showContextMenu();
    }

    protected MultidatasetCubeTreeNode createMultidatasetVersionRootNode(MultidatasetVersionDto multidatasetVersion) {
        return StatisticalResourcesRecordUtils.getMultidatasetVersionRootNode(SCHEME_NODE_NAME, multidatasetVersion);
    }

    protected void addItemsToContextMenu(MenuItem... menuItems) {
        for (MenuItem item : menuItems) {
            contextMenu.addItem(item);
        }
    }

    protected void removeFilterEditionHandler() {
        filterEditionHandler.removeHandler();
    }

    protected void disableItemSchemeNode() {
        RecordList recordList = getRecordList();
        if (recordList != null && multidatasetVersion != null) {
            Record record = recordList.find(MultidatasetCubeDS.URN, multidatasetVersion.getUrn());
            if (record != null) {
                int index = getRecordIndex(record);
                if (index != -1) {
                    getRecord(index).setEnabled(false);
                }
            }
        }
    }

    @Override
    protected Canvas getCellHoverComponent(Record record, Integer rowNum, Integer colNum) {
        if (rowNum != 0) { // do not show details if it is the root node (item scheme node)
            DetailViewer detailViewer = new DetailViewer();
            detailViewer.setFields(getDetailViewerFields());
            detailViewer.setData(new Record[]{record});
            return detailViewer;
        }
        return super.getCellHoverComponent(record, rowNum, colNum);
    }

    protected boolean isDroppable(TreeNode dropFolder) {
        return !("/".equals(getDropFolder().getName()));
    }

    protected void onNodeClick(MultidatasetCubeDto MultidatasetCubeDto) {
        nodeClickAction.onNodeClick(MultidatasetCubeDto);
    }

    protected void onNodeContextClick(String nodeName, MultidatasetCubeDto multidatasetCubeDto) {
        selectedContextClickElement = multidatasetCubeDto;
        createCubeMenuItem.setEnabled(canCreateCube());
        deleteElementMenuItem.setEnabled(canDeleteCube(nodeName));
        showContextMenu();

    }
    protected DetailViewerField[] getDetailViewerFields() {
        DetailViewerField identifier = new DetailViewerField(MultidatasetCubeDS.IDENTIFIER, getConstants().multidatasetStructureCubeIdentifier());
        DetailViewerField titleField = new DetailViewerField(MultidatasetCubeDS.TITLE, getConstants().multidatasetStructureCubeTitle());
        DetailViewerField descriptionField = new DetailViewerField(MultidatasetCubeDS.DESCRIPTION, getConstants().multidatasetStructureCubeDescription());
        DetailViewerField urnField = new DetailViewerField(MultidatasetCubeDS.URN, getConstants().multidatasetStructureCubeURN());
        DetailViewerField resourceTypeField = new DetailViewerField(MultidatasetCubeDS.RESOURCE_TYPE_TO_LINK, getConstants().multidatasetStructureCubeResourceTypeLinked());
        DetailViewerField resourceField = new DetailViewerField(MultidatasetCubeDS.RESOURCE_TO_LINK, getConstants().multidatasetStructureCubeResource());
        DetailViewerField queryDatasetField = new DetailViewerField(MultidatasetCubeDS.QUERY_DATASET, getConstants().multidatasetStructureCubeQueryDataset());
        DetailViewerField dsdField = new DetailViewerField(MultidatasetCubeDS.DSD, getConstants().multidatasetStructureCubeDSD());
        return new DetailViewerField[]{identifier, titleField, descriptionField, resourceTypeField, resourceField, queryDatasetField, dsdField, urnField};
    }

    public void addCreateCubeMenuItemClickHandler(ClickHandler clickHandler) {
        createCubeMenuItem.addClickHandler(clickHandler);
    }

    public MultidatasetStructureTabUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public void setUiHandlers(MultidatasetStructureTabUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    private String getMultidatasetCubeUrn(MultidatasetCubeDto multidatasetCubeDto) {
        return multidatasetCubeDto.getUrn();
    }

    public MultidatasetCubeDto getSelectedContextClickElement() {
        return selectedContextClickElement;
    }

    private boolean canCreateCube() {
        // Cubes cannot be created under another cube
        if (selectedContextClickElement != null) {
            return false;
        }

        return MultidatasetClientSecurityUtils.canCreateCube(multidatasetVersion.getProcStatus());
    }

    private boolean canDeleteCube(String nodeName) {
        if (SCHEME_NODE_NAME.equals(nodeName)) {
            return false;
        }
        if (selectedContextClickElement != null) {
            return MultidatasetClientSecurityUtils.canDeleteCube(multidatasetVersion.getProcStatus());
        }
        return false;
    }

}
