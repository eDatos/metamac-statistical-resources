package org.siemac.metamac.statistical.resources.web.client.publication.view;

import org.siemac.metamac.statistical.resources.core.dto.NameableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.ChapterDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.CubeDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.ElementLevelDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationStructureDto;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.publication.presenter.PublicationStructureTabPresenter.PublicationStructureTabView;
import org.siemac.metamac.statistical.resources.web.client.publication.utils.PublicationClientSecurityUtils;
import org.siemac.metamac.statistical.resources.web.client.publication.view.handlers.PublicationStructureTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.publication.widgets.ImportPublicationStructureWindow;
import org.siemac.metamac.statistical.resources.web.client.publication.widgets.PublicationStructureElementPanel;
import org.siemac.metamac.statistical.resources.web.client.publication.widgets.PublicationStructureTreeGrid;
import org.siemac.metamac.statistical.resources.web.client.publication.widgets.TreeNodeClickAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetsResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.GetMultidatasetsResult;
import org.siemac.metamac.statistical.resources.web.shared.publication.GetPublicationsResult;
import org.siemac.metamac.statistical.resources.web.shared.query.GetQueriesResult;
import org.siemac.metamac.web.common.client.listener.UploadListener;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class PublicationStructureTabViewImpl extends ViewWithUiHandlers<PublicationStructureTabUiHandlers> implements PublicationStructureTabView {

    public static Long                             FIST_TREE_ELEMENT_POSITION = 1L;

    private VLayout                                panel;
    private PublicationStructureTreeGrid           publicationStructureTreeGrid;
    private PublicationStructureElementPanel       publicationStructureElementPanel;

    private final CustomToolStripButton            importPublicationVersionStructureButton;
    private final ImportPublicationStructureWindow importPublicationStructureWindow;

    private PublicationStructureDto                publicationStructureDto;

    @Inject
    public PublicationStructureTabViewImpl() {
        panel = new VLayout();
        panel.setMargin(15);

        createPublicationStructureTree();
        createPublicationStructureElementPanel();

        HLayout subPanel = new HLayout(10);

        // Tree

        VLayout treePanel = new VLayout();
        ToolStrip toolStrip = new ToolStrip();
        importPublicationVersionStructureButton = createImportPublicationVersionStructureButton();
        toolStrip.addButton(importPublicationVersionStructureButton);
        treePanel.addMember(toolStrip);
        treePanel.addMember(publicationStructureTreeGrid);

        subPanel.addMember(treePanel);

        // Element

        subPanel.addMember(publicationStructureElementPanel);

        panel.addMember(subPanel);

        // Publication structure importation window

        importPublicationStructureWindow = new ImportPublicationStructureWindow();
        importPublicationStructureWindow.setUploadListener(new UploadListener() {

            @Override
            public void uploadFailed(String errorMessage) {
                getUiHandlers().resourceImportationFailed(errorMessage);
            }

            @Override
            public void uploadComplete(String fileName) {
                getUiHandlers().resourceImportationSucceed(fileName, publicationStructureDto.getPublicationVersion().getUrn());
            }
        });
    }

    private void createPublicationStructureElementPanel() {
        publicationStructureElementPanel = new PublicationStructureElementPanel();
        publicationStructureElementPanel.setVisible(false);
    }

    private void createPublicationStructureTree() {
        publicationStructureTreeGrid = new PublicationStructureTreeGrid(new TreeNodeClickAction() {

            @Override
            public void onNodeClick(ElementLevelDto elementLevelDto) {
                if (elementLevelDto != null) {
                    setElementInPanel(elementLevelDto.getChapter() != null ? elementLevelDto.getChapter() : elementLevelDto.getCube());
                }
            }
        });

        publicationStructureTreeGrid.addCreateChapterMenuItemClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                ElementLevelDto selectedContextClickElement = publicationStructureTreeGrid.getSelectedContextClickElement();

                ChapterDto chapterDto = new ChapterDto();
                chapterDto.setOrderInLevel(FIST_TREE_ELEMENT_POSITION);
                if (selectedContextClickElement != null && selectedContextClickElement.getChapter() != null) {
                    chapterDto.setParentChapterUrn(selectedContextClickElement.getChapter().getUrn());
                }

                setNewElementInPanel(chapterDto);
            }
        });

        publicationStructureTreeGrid.addCreateCubeMenuItemClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                ElementLevelDto selectedContextClickElement = publicationStructureTreeGrid.getSelectedContextClickElement();

                CubeDto cubeDto = new CubeDto();
                cubeDto.setOrderInLevel(FIST_TREE_ELEMENT_POSITION);
                if (selectedContextClickElement != null && selectedContextClickElement.getChapter() != null) {
                    cubeDto.setParentChapterUrn(selectedContextClickElement.getChapter().getUrn());
                }

                setNewElementInPanel(cubeDto);
            }
        });
    }

    private CustomToolStripButton createImportPublicationVersionStructureButton() {
        CustomToolStripButton importButton = new CustomToolStripButton(StatisticalResourcesWeb.getConstants().actionImport(),
                org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.importResource().getURL());
        importButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                importPublicationStructureWindow.show();

            }
        });
        return importButton;
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(PublicationStructureTabUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        publicationStructureTreeGrid.setUiHandlers(uiHandlers);
        publicationStructureElementPanel.setUiHandlers(uiHandlers);
        importPublicationStructureWindow.setUiHandlers(uiHandlers);
    }

    @Override
    public void setPublicationStructure(PublicationStructureDto publicationStructureDto) {
        this.publicationStructureDto = publicationStructureDto;
        importPublicationVersionStructureButton.setVisible(PublicationClientSecurityUtils.canImportPublicationVersionStructure(publicationStructureDto.getPublicationVersion().getProcStatus()));
        importPublicationStructureWindow.setPublicationVersionUrn(publicationStructureDto.getPublicationVersion().getUrn());
        publicationStructureTreeGrid.setPublicationStructure(publicationStructureDto);
        publicationStructureElementPanel.setPublicationVersion(publicationStructureDto.getPublicationVersion());
        publicationStructureElementPanel.hide();
    }

    @Override
    public void setPublicationStructure(PublicationStructureDto publicationStructureDto, NameableStatisticalResourceDto selectedElement) {
        setPublicationStructure(publicationStructureDto);
        selectElement(selectedElement);
    };

    private void selectElement(NameableStatisticalResourceDto element) {
        publicationStructureTreeGrid.selectElement(element);
        setElementInPanel(element);
    }

    private void setElementInPanel(NameableStatisticalResourceDto element) {
        publicationStructureElementPanel.setElement(element);
        publicationStructureElementPanel.show();
    }

    private void setNewElementInPanel(NameableStatisticalResourceDto element) {
        setElementInPanel(element);
        publicationStructureElementPanel.setMainFormLayoutEditionMode();
    }

    //
    // RELATED RESOURCES
    //

    @Override
    public void setStatisticalOperationsForDatasetSelection(GetStatisticalOperationsPaginatedListResult result) {
        publicationStructureElementPanel.setStatisticalOperationsForDatasetSelection(result);
    }

    @Override
    public void setDatasetsForCubes(GetDatasetsResult result) {
        publicationStructureElementPanel.setDatasetsForCubes(result);
    }

    @Override
    public void setStatisticalOperationsForQuerySelection(GetStatisticalOperationsPaginatedListResult result) {
        publicationStructureElementPanel.setStatisticalOperationsForQuerySelection(result);
    }

    @Override
    public void setQueriesForCubes(GetQueriesResult result) {
        publicationStructureElementPanel.setQueriesForCubes(result);
    }

    @Override
    public void setPublicationsForCubes(GetPublicationsResult result) {
        publicationStructureElementPanel.setCollectionsForCubes(result);
    }

    @Override
    public void setStatisticalOperationsForMultidatasetSelection(GetStatisticalOperationsPaginatedListResult result) {
        publicationStructureElementPanel.setStatisticalOperationsForMultidatasetSelection(result);
    }

    @Override
    public void setMultidatasetsForCubes(GetMultidatasetsResult result) {
        publicationStructureElementPanel.setMultidatasetsForCubes(result);
    }
}
