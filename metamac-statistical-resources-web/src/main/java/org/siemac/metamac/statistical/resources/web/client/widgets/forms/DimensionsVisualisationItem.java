package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.ListUtils;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;

public class DimensionsVisualisationItem extends CustomCanvasItem {

    private boolean                  editionMode;

    private VStack                   dimensionsStack;

    private HStack                   headingDimensionsStack;
    private VStack                   stubDimensionsStack;

    private List<RelatedResourceDto> dsdDimensions;
    private List<RelatedResourceDto> visualisationDimensions;

    public DimensionsVisualisationItem(String name, String title, boolean editionMode) {
        super(name, title);
        this.editionMode = editionMode;

        setCellStyle("dragAndDropCellStyle");
        if (!editionMode) {
            setTitleStyle("staticFormItemTitle");
        }

        DimensionsVisualisationCanvas dimensionsVisualisationCanvas = new DimensionsVisualisationCanvas(editionMode);
        dimensionsVisualisationCanvas.setBackgroundColor("#dee6f3");

        HLayout hLayout = new HLayout();
        hLayout.setMembersMargin(15);
        hLayout.addMember(dimensionsVisualisationCanvas);
        hLayout.setBorder("1px solid #d1d3da");
        hLayout.setAutoHeight();
        hLayout.setAutoWidth();

        if (editionMode) {

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

            hLayout.addMember(dimensionsCanvas);
        }

        setCanvas(hLayout);
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
//                dimensionsToAdd = RelatedResourceUtils.substractLists(dsdDimensions, visualisationDimensions);
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

            HLayout hLayout = new HLayout();
            hLayout.addMember(new TitlePiece(getConstants().datasetHeadingDimensions()));
            hLayout.setBackgroundColor("#dee6f3");
            hLayout.addMember(headingDimensionsStack);

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
}
