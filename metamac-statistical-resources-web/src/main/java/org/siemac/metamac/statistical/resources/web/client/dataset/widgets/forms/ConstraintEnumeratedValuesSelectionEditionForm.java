package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.constraint.KeyPartDto;
import org.siemac.metamac.statistical.resources.core.dto.constraint.KeyValueDto;
import org.siemac.metamac.statistical.resources.core.dto.constraint.RegionValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.ItemDto;
import org.siemac.metamac.statistical.resources.core.enume.constraint.domain.KeyPartTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DimensionConstraintsDS;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetConstraintsTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.ItemsSelectionTreeItem;
import org.siemac.metamac.statistical.resources.web.client.enums.DatasetConstraintInclusionTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectAndActionItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

public class ConstraintEnumeratedValuesSelectionEditionForm extends ConstraintEnumeratedValuesSelectionBaseForm {

    private CustomSelectItem                inclusionTypeField;
    protected List<ExternalItemDto>         restrictions;
    private DatasetConstraintsTabUiHandlers uiHandlers;
    private List<ItemDto>                   srmRestrictionCodes;
    private CustomSelectAndActionItem       customSelectAndActionItem;
    public static final String              WIDTH_CUSTOM_SRM_RESTRICTION = "350";

    public ConstraintEnumeratedValuesSelectionEditionForm(String groupTitle) {
        super(groupTitle);

        createCustomSelectAndActionItem();

        inclusionTypeField = new CustomSelectItem(DimensionConstraintsDS.INCLUSION_TYPE, getConstants().datasetConstraintInclusionType());
        inclusionTypeField.setRequired(true);
        inclusionTypeField.setValueMap(CommonUtils.getConstraintInclusionTypeHashMap());
        inclusionTypeField.setAlign(Alignment.LEFT);
        inclusionTypeField.setWidth(100);

        treeItem = new ItemsSelectionTreeItem(DimensionConstraintsDS.VALUES, "tree-values-selection", true);
        treeItem.setShowTitle(false);
        treeItem.setStartRow(true);
        treeItem.setColSpan(4);

        setFields(customSelectAndActionItem, inclusionTypeField, treeItem);
    }

    /**
     * Updates the region with the specified values. Each {@link RegionValueDto} has a list of {@link KeyValueDto}. All the {@link KeyPartDto} of a {@link KeyValueDto} belongs to the same dimension.
     * 
     * @param regionValueDto
     */
    public RegionValueDto updateRegionDto(RegionValueDto regionValueDto) {
        Boolean included = DatasetConstraintInclusionTypeEnum.INCLUSION.equals(CommonUtils.getDatasetConstraintInclusionTypeEnum(inclusionTypeField.getValueAsString()));
        Map<String, Boolean> selectedItems = treeItem.getSelectedItems();

        KeyValueDto keyValueDto = CommonUtils.getKeyValueOfDimension(dsdDimensionDto, regionValueDto);
        if (keyValueDto == null) {
            keyValueDto = new KeyValueDto();
            keyValueDto.setRegion(regionValueDto);
            regionValueDto.addKey(keyValueDto);
        }
        keyValueDto.setIncluded(included);
        keyValueDto.removeAllParts();
        for (String itemCode : selectedItems.keySet()) {
            KeyPartDto keyPartDto = new KeyPartDto();
            keyPartDto.setType(KeyPartTypeEnum.NORMAL);
            keyPartDto.setIdentifier(dsdDimensionDto.getDimensionId());
            keyPartDto.setValue(itemCode);
            keyPartDto.setCascadeValues(selectedItems.get(itemCode));
            keyPartDto.setPosition(dsdDimensionDto.getPosition());
            keyValueDto.addPart(keyPartDto);
        }
        if (keyValueDto.getParts().isEmpty()) {
            regionValueDto.removeKey(keyValueDto);
        }
        return regionValueDto;
    }

    public void setTreeFromSrmRestriction() {

        List<KeyPartDto> srmRestrictionCodesKeyParts = new ArrayList<KeyPartDto>();
        for (ItemDto codeRestriction : srmRestrictionCodes) {
            KeyPartDto keyPartDto = new KeyPartDto();
            keyPartDto.setType(KeyPartTypeEnum.NORMAL);
            keyPartDto.setIdentifier(dsdDimensionDto.getDimensionId());
            keyPartDto.setValue(codeRestriction.getCode());
            keyPartDto.setCascadeValues(false);
            keyPartDto.setPosition(dsdDimensionDto.getPosition());
            srmRestrictionCodesKeyParts.add(keyPartDto);
        }

        setSavedRegionValues(srmRestrictionCodesKeyParts);

    }

    @Override
    protected void setInclusionTypeValue(Boolean isIncluded) {
        setValue(DimensionConstraintsDS.INCLUSION_TYPE, BooleanUtils.isTrue(isIncluded) ? DatasetConstraintInclusionTypeEnum.INCLUSION.name() : DatasetConstraintInclusionTypeEnum.EXCLUSION.name());
    }

    public List<ExternalItemDto> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(List<ExternalItemDto> restrictions) {
        this.restrictions = restrictions;
        customSelectAndActionItem.enabledAction(restrictions != null && !restrictions.isEmpty());
        customSelectAndActionItem.populateSelectionItem(CommonUtils.getRestrictionsHashMap(restrictions));
    }

    public void showRestrictions(Boolean isIncluded) {
        if (isIncluded) {
            customSelectAndActionItem.show();
        } else {
            customSelectAndActionItem.hide();
        }
        customSelectAndActionItem.showRestrictions(isIncluded);
    }

    public void setUiHandlers(DatasetConstraintsTabUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public DatasetConstraintsTabUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public void setSrmRestrictionCodes(List<ItemDto> itemDtos) {
        this.srmRestrictionCodes = itemDtos;
        if (srmRestrictionCodes != null && !srmRestrictionCodes.isEmpty()) {
            customSelectAndActionItem.enabledAction(true);
        }
    }

    private void createCustomSelectAndActionItem() {
        customSelectAndActionItem = new CustomSelectAndActionItem(DimensionConstraintsDS.SRM_RESOURCE_RESTRICTION, getConstants().srmResourceRestriction(), WIDTH_CUSTOM_SRM_RESTRICTION,
                DimensionConstraintsDS.ACTION_APPLY_SRM_RESTRICTION, getConstants().actionApplySrmRestriction(), false);
        customSelectAndActionItem.setAlign(Alignment.LEFT);
        customSelectAndActionItem.setColSpan(2);
        customSelectAndActionItem.getSelectionItem().addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                String codeSrmRestriction = customSelectAndActionItem.getValueSelectionItem();
                if (!StringUtils.isEmpty(codeSrmRestriction)) {
                    customSelectAndActionItem.enabledAction(false);
                    getUiHandlers().retrieveCodes(getSelectedDimension(), codeSrmRestriction);
                }
            }
        });

        customSelectAndActionItem.getAction().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                if (srmRestrictionCodes != null && !srmRestrictionCodes.isEmpty()) {
                    setTreeFromSrmRestriction();
                }
            }
        });
    }
}
