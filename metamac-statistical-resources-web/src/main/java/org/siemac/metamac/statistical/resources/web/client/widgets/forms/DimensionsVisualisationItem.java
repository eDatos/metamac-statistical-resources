package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.ListUtils;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;

public class DimensionsVisualisationItem extends CustomCanvasItem {

    private boolean                       editionMode;

    private VStack                        dimensionsStack;

    private HStack                        headingDimensionsStack;
    private VStack                        stubDimensionsStack;

    private List<RelatedResourceDto>      dsdDimensions;
    private List<RelatedResourceDto>      visualisationDimensions;
    private CustomCheckboxItem            modifyDistributionDimension;
    private DimensionsVisualisationCanvas dimensionsVisualisationCanvas;
    private HLayout                       principalLayout;
    public static final String            MODIFY_DISTRIBUTION_DIMENSION = "dataset-mod-distribution";

    public DimensionsVisualisationItem(String name, String title, boolean editionMode) {
        super(name, title);
        this.editionMode = editionMode;

        setCellStyle("dragAndDropCellStyle");
        if (!editionMode) {
            setTitleStyle("staticFormItemTitle");
        }

        DynamicForm form = new DynamicForm();
        form.setMargin(15);
        modifyDistributionDimension = new CustomCheckboxItem(MODIFY_DISTRIBUTION_DIMENSION, getConstants().datasetModifyDistributionDimensions());
        modifyDistributionDimension.setValue(false);
        form.setFields(modifyDistributionDimension);

        dimensionsVisualisationCanvas = new DimensionsVisualisationCanvas(editionMode);
        dimensionsVisualisationCanvas.setBackgroundColor("#dee6f3");
        dimensionsVisualisationCanvas.markForRedraw();

        principalLayout = new HLayout();
        principalLayout.setMembersMargin(15);
        principalLayout.addMember(form);
        principalLayout.addMember(dimensionsVisualisationCanvas);
        principalLayout.setBorder("1px solid #d1d3da");
        principalLayout.setAutoHeight();
        principalLayout.setAutoWidth();

        setDimensionStack(editionMode, principalLayout);
        principalLayout.markForRedraw();
        setCanvas(principalLayout);
        addHandler(principalLayout);
    }

    private void setDimensionStack(boolean editionMode, HLayout principalLayout) {
        if (editionMode && dimensionsStack == null) {
            // List of dimensions that are not in the heading o stub dimensions
            Canvas vDropLineProp = new Canvas();
            vDropLineProp.setBackgroundColor("#008BD0");
            dimensionsStack = new VStack(10);
            dimensionsStack.setHeight("*");
            dimensionsStack.setLayoutMargin(15);
            dimensionsStack.setCanAcceptDrop(editionMode);
            dimensionsStack.setAnimateMembers(editionMode);
            dimensionsStack.setShowDragPlaceHolder(editionMode);
            dimensionsStack.setDropLineProperties(vDropLineProp);
            dimensionsStack.setBackgroundColor("#d1d3da");
            Canvas dimensionsCanvas = new Canvas();
            dimensionsCanvas.setHeight("*");
            dimensionsCanvas.setBackgroundColor("#d1d3da");
            dimensionsCanvas.addChild(dimensionsStack);

            principalLayout.addMember(dimensionsCanvas);
        }
    }

    private void addHandler(final HLayout hLayout) {
        modifyDistributionDimension.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                Canvas[] headingDimensions = headingDimensionsStack.getMembers();
                Canvas[] stubDimensions = stubDimensionsStack.getMembers();
                boolean isChecked = (Boolean) event.getValue();
                hLayout.removeMember(dimensionsVisualisationCanvas);
                dimensionsVisualisationCanvas = new DimensionsVisualisationCanvas(isChecked);
                dimensionsVisualisationCanvas.setBackgroundColor("#dee6f3");
                for (Canvas headingDimension : headingDimensions) {
                    headingDimension.setCanDragReposition(isChecked);
                    headingDimension.setCanDrop(isChecked);
                    headingDimensionsStack.addMember(headingDimension);
                }
                for (Canvas stubDimension : stubDimensions) {
                    stubDimension.setCanDragReposition(isChecked);
                    stubDimension.setCanDrop(isChecked);
                    stubDimensionsStack.addMember(stubDimension);
                }
                hLayout.addMember(dimensionsVisualisationCanvas);
                hLayout.markForRedraw();
            }
        });
    }

    public DimensionsVisualisationCanvas getDimensionsVisualisationCanvas() {
        return dimensionsVisualisationCanvas;
    }

    public void setDimensionsVisualisationCanvas(DimensionsVisualisationCanvas dimensionsVisualisationCanvas) {
        this.dimensionsVisualisationCanvas = dimensionsVisualisationCanvas;
    }

    public void setDimensions(List<RelatedResourceDto> dimensions) {
        this.dsdDimensions = dimensions;
        updateDimensionList();
    }

    @SuppressWarnings("unchecked")
    public void setVisualisationDimensions(List<RelatedResourceDto> headingDimensions, List<RelatedResourceDto> stubDimensions) {
        this.visualisationDimensions = ListUtils.sum(headingDimensions, stubDimensions);

        setHeadingDimensions(headingDimensions);
        setStubDimensions(stubDimensions);
    }

    private void setHeadingDimensions(List<RelatedResourceDto> headingDimensions) {
        setDimensionsInStack(headingDimensionsStack, headingDimensions, true);
    }

    private void setStubDimensions(List<RelatedResourceDto> stubDimensions) {
        setDimensionsInStack(stubDimensionsStack, stubDimensions, true);
    }

    public List<RelatedResourceDto> getHeadingDimensions() {
        return getDimensionsFromStack(headingDimensionsStack);
    }

    public List<RelatedResourceDto> getStubDimensions() {
        return getDimensionsFromStack(stubDimensionsStack);
    }

    /**
     * Update the dimensions in the dimensions list to avoid duplicate dimensions in the heading or stub ones.
     */
    private void updateDimensionList() {
        if (editionMode) { // Dimensions list is only shown in the edition mode
            List<RelatedResourceDto> dimensionsToAdd = new ArrayList<RelatedResourceDto>();
            if (dsdDimensions != null && visualisationDimensions != null) {
                dimensionsToAdd = RelatedResourceUtils.substractLists(dsdDimensions, visualisationDimensions);
            }
            setDimensionsInStack(dimensionsStack, dimensionsToAdd, false);
        }
    }

    private void setDimensionsInStack(Layout stack, List<RelatedResourceDto> dimensions, boolean updateDimensionList) {
        stack.removeMembers(stack.getMembers());
        for (RelatedResourceDto dimension : dimensions) {
            stack.addMember(new DragPiece(dimension, editionMode));
        }
        if (updateDimensionList) {
            updateDimensionList();
        }
    }

    private List<RelatedResourceDto> getDimensionsFromStack(Layout stack) {
        List<RelatedResourceDto> dimensions = new ArrayList<RelatedResourceDto>();
        Canvas[] canvas = stack.getMembers();
        for (Canvas c : canvas) {
            if (c instanceof DragPiece) {
                dimensions.add(((DragPiece) c).getRelatedResourceDto());
            }
        }
        return dimensions;
    }

    /**
     * Canvas with the visual representation of a table with heading and stub dimensions
     */
    private class DimensionsVisualisationCanvas extends HLayout {

        public DimensionsVisualisationCanvas(boolean editionMode) {
            headingDimensionsStack = new HStack(10);
            headingDimensionsStack.setHeight(60);
            headingDimensionsStack.setTitle(getConstants().datasetHeadingDimensions());
            headingDimensionsStack.setLayoutMargin(15);
            headingDimensionsStack.setShowEdges(true);
            headingDimensionsStack.setCanAcceptDrop(editionMode);
            headingDimensionsStack.setAnimateMembers(editionMode);
            headingDimensionsStack.setShowDragPlaceHolder(editionMode);
            Canvas vDropLineProp = new Canvas();
            vDropLineProp.setBackgroundColor("#008BD0");
            headingDimensionsStack.setDropLineProperties(vDropLineProp);
            headingDimensionsStack.setBackgroundColor("#EAF1FB");

            stubDimensionsStack = new VStack(10);
            stubDimensionsStack.setTitle(getConstants().datasetStubDimensions());
            stubDimensionsStack.setLayoutMargin(5);
            stubDimensionsStack.setShowEdges(true);
            stubDimensionsStack.setCanAcceptDrop(editionMode);
            stubDimensionsStack.setAnimateMembers(editionMode);
            Canvas hDropLineProp = new Canvas();
            hDropLineProp.setBackgroundColor("#008BD0");
            stubDimensionsStack.setDropLineProperties(hDropLineProp);
            stubDimensionsStack.setBackgroundColor("#EAF1FB");
            stubDimensionsStack.setAlign(Alignment.CENTER);
            stubDimensionsStack.setWidth(70);
            stubDimensionsStack.setHeight(30);

            VLayout vLayout = new VLayout();
            vLayout.addMember(new TitlePiece(getConstants().datasetStubDimensions()));
            vLayout.setBackgroundColor("#dee6f3");
            vLayout.addMember(stubDimensionsStack);
            vLayout.setAlign(Alignment.CENTER);
            vLayout.markForRedraw();

            HLayout hLayout = new HLayout();
            hLayout.addMember(new TitlePiece(getConstants().datasetHeadingDimensions()));
            hLayout.setBackgroundColor("#dee6f3");
            hLayout.addMember(headingDimensionsStack);
            hLayout.markForRedraw();

            addMember(vLayout);
            addMember(hLayout);

            setAutoHeight();
            setAutoWidth();
        }
    }

    /**
     * Representation of a dimension as a draggable piece
     */
    private class DragPiece extends Label {

        private RelatedResourceDto relatedResourceDto;

        public DragPiece(boolean editionMode) {
            setWidth(20);
            setHeight(20);
            setLayoutAlign(Alignment.CENTER);
            setCanDragReposition(editionMode);
            setCanDrop(editionMode);
            setDragAppearance(DragAppearance.TARGET);
        }

        public DragPiece(RelatedResourceDto relatedResourceDto, boolean editionMode) {
            this(editionMode);
            this.relatedResourceDto = relatedResourceDto;
            setContents(relatedResourceDto.getCode());
        }

        public RelatedResourceDto getRelatedResourceDto() {
            return relatedResourceDto;
        }
    }

    /**
     * Title of the set of columns or rows in the table (heading or stub dimensions)
     */
    private class TitlePiece extends Label {

        public TitlePiece() {
            setWidth(20);
            setHeight(60);
            setLayoutAlign(Alignment.CENTER);
            setValign(VerticalAlignment.CENTER);
            setAlign(Alignment.CENTER);
            setMargin(8);
            setStyleName("formTitle");
        }

        public TitlePiece(String contents) {
            this();
            setContents(contents);
        }
    }

    public CustomCheckboxItem getModifyDistributionDimension() {
        return modifyDistributionDimension;
    }

    public void setModifyDistributionDimension(CustomCheckboxItem modifyDistributionDimension) {
        this.modifyDistributionDimension = modifyDistributionDimension;
    }

}
