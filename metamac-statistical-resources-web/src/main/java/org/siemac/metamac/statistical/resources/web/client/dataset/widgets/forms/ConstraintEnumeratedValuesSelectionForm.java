package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getCoreMessages;

import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DimensionConstraintsDS;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.ItemsSelectionTreeItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.types.Alignment;

public class ConstraintEnumeratedValuesSelectionForm extends ConstraintEnumeratedValuesSelectionBaseForm {

    ViewTextItem srmResourceRestriction;

    public ConstraintEnumeratedValuesSelectionForm(String groupTitle) {
        super(groupTitle);

        ViewTextItem inclusionTypeField = new ViewTextItem(DimensionConstraintsDS.INCLUSION_TYPE, getConstants().datasetConstraintInclusionType());
        inclusionTypeField.setAlign(Alignment.LEFT);
        inclusionTypeField.setWidth(100);

        treeItem = new ItemsSelectionTreeItem(DimensionConstraintsDS.VALUES, "tree-values-selection", false);
        treeItem.setShowTitle(false);
        treeItem.setStartRow(true);
        treeItem.setColSpan(4);

        setFields(inclusionTypeField, treeItem);
    }

    @Override
    protected void setInclusionTypeValue(Boolean isIncluded) {
        setValue(DimensionConstraintsDS.INCLUSION_TYPE,
                BooleanUtils.isTrue(isIncluded) ? getCoreMessages().datasetConstraintInclusionTypeEnumINCLUSION() : getCoreMessages().datasetConstraintInclusionTypeEnumEXCLUSION());
    }
}
