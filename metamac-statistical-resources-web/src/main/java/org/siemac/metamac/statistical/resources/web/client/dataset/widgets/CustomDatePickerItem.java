package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.io.Serializable;
import java.util.LinkedHashMap;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomDateItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.types.FormErrorOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class CustomDatePickerItem extends CustomCanvasItem {

    public enum DateFormatTypeEnum implements Serializable {
        SPECIFIC_DATE, CUSTOM;

        private DateFormatTypeEnum() {
        }

        public String getName() {
            return name();
        }
    }
     
    private static String nameCustomField = "-custom";
    private static String nameDateField = "-date";
    private static String nameSelectorField = "-selector";
    private boolean readOnly = true;
    private DynamicForm form;

    private CustomDateItem nextVersionDate;
    CustomSelectItem dateFormatType;
    private TextItem customSdmxTimePeriodItem;
    private ViewTextItem staticTimePeriodItem;
    
    private void common(boolean readOnly, boolean required) {
        setTitleStyle((!readOnly && !required)  ? "localeLabel" : "requiredLocaleLabel");
        setCellStyle("localeCellStyle");
        setTitleVAlign(VerticalAlignment.CENTER);
    }
    
    public CustomDatePickerItem(String name, String title, boolean readOnly, boolean required) {
        super(name, title);
        this.readOnly = readOnly;
        setRedrawOnChange(true);
        setShouldSaveValue(true);
        common(readOnly, required);

        form = new DynamicForm();
        form.setAutoHeight();
        form.setCellPadding(1);
        form.setStyleName("urlForm");
        form.setRedrawOnResize(true);

        form.setErrorOrientation(FormErrorOrientation.RIGHT);
        form.setValidateOnChange(true);
        
        if (this.readOnly) {
            customDatePickerItemForm(name, required);
        } else {
            customDatePickerItemEditionForm(name, required); 
        }
        

        Canvas canvas = new Canvas();
        canvas.addChild(form);
        canvas.setAutoHeight();

        setCanvas(canvas);
        setCellHeight(20);
        
    }
    
    private void enabledNextVersionDate() {
        customSdmxTimePeriodItem.hide();
        customSdmxTimePeriodItem.setVisible(false);
        customSdmxTimePeriodItem.clearValue();
        nextVersionDate.show();
        nextVersionDate.setVisible(true);
    }
    
    private void enabledCustomSdmxTimePeriodItem() {
        customSdmxTimePeriodItem.show();
        customSdmxTimePeriodItem.setVisible(true);
        nextVersionDate.hide();
        nextVersionDate.setVisible(false);
        nextVersionDate.clearValue();
    }

    private void customDatePickerItemEditionForm(String name, boolean required) {
        dateFormatType = new CustomSelectItem(name + nameSelectorField, "-");
        dateFormatType.setShowTitle(false);
        dateFormatType.setRequired(required);
        dateFormatType.setValueMap(getDateFormatTypeHashMap());

        dateFormatType.addChangeHandler(new com.smartgwt.client.widgets.form.fields.events.ChangeHandler() {

            @Override
            public void onChange(com.smartgwt.client.widgets.form.fields.events.ChangeEvent event) {
                if (DateFormatTypeEnum.SPECIFIC_DATE.getName().equals(event.getValue())) {
                    enabledNextVersionDate();
                } else {
                    enabledCustomSdmxTimePeriodItem();
                }
            }
        });
        
        customSdmxTimePeriodItem = createCustomSdmxTimePeriodItem(name, required);
        
        nextVersionDate = createDateNextUpdateItem(name);
       
        form.setFields(dateFormatType, nextVersionDate, customSdmxTimePeriodItem);
    }
    
    private CustomDateItem createDateNextUpdateItem(String name) {

        FormItemIcon infoIcon = new FormItemIcon();
        infoIcon.setSrc(GlobalResources.RESOURCE.info().getURL());
        infoIcon.setPrompt(StatisticalResourcesWeb.getMessages().dateNextUpdateInfo());
        CustomDateItem item = new CustomDateItem(name + nameDateField, getConstants().datasetDateNextUpdate());
        item.setShowTitle(false);
        item.setIcons(infoIcon);
        return item;
    }

    private TextItem createCustomSdmxTimePeriodItem(String name, boolean required) {

        FormItemIcon infoIcon = new FormItemIcon();
        infoIcon.setSrc(GlobalResources.RESOURCE.info().getURL());
        infoIcon.setPrompt(StatisticalResourcesWeb.getMessages().dateNextUpdateInfo());
        TextItem customSdmxTimePeriodItem = new TextItem(name + nameCustomField, "-");
        customSdmxTimePeriodItem.setShowTitle(false);
        customSdmxTimePeriodItem.setRequired(required);
        customSdmxTimePeriodItem.setWidth("150");
        customSdmxTimePeriodItem.setIcons(infoIcon);
        return customSdmxTimePeriodItem;
    }
    
    private void customDatePickerItemForm(String name, boolean required) {
        staticTimePeriodItem = new ViewTextItem();
        staticTimePeriodItem.setShowTitle(false); 
        staticTimePeriodItem.setRedrawOnChange(true);
        form.setFields(staticTimePeriodItem);
    }

    public String getValue() {
        // get value as observational time period
        if (DateFormatTypeEnum.SPECIFIC_DATE.getName().equals(dateFormatType.getValueAsString())) {
            return DateUtils.getDateInSdmxFormat(nextVersionDate.getValueAsDate());
        } else {
            return customSdmxTimePeriodItem.getValueAsString();
        }
    }

    @Override
    public void setValue(String sdmxTimePeriod) {
        if (this.readOnly) {
            setValueInForm(sdmxTimePeriod);
        } else {
            setValueInEditionForm(sdmxTimePeriod);
        }
    }
      
    private void setValueInEditionForm(String sdmxTimePeriod) {
        if (DateUtils.isValidDateInSdmx(sdmxTimePeriod)) {
            dateFormatType.setValue(DateFormatTypeEnum.SPECIFIC_DATE.getName());
            enabledNextVersionDate();
            nextVersionDate.setValue(DateUtils.getDate(sdmxTimePeriod));
        } else {
            dateFormatType.setValue(DateFormatTypeEnum.CUSTOM.getName());
            enabledCustomSdmxTimePeriodItem();
            customSdmxTimePeriodItem.setValue(sdmxTimePeriod);
        }
    }

    private void setValueInForm(String sdmxTimePeriod) {
        if (!StringUtils.isBlank(sdmxTimePeriod)) {
            if (DateUtils.isValidDateInSdmx(sdmxTimePeriod)) {
                staticTimePeriodItem.setValue(DateUtils.getVisualizationDateFormat(sdmxTimePeriod));
            } else {
                staticTimePeriodItem.setValue(sdmxTimePeriod);
            }
        }
    }

    public static LinkedHashMap<String, String> getDateFormatTypeHashMap() {
        LinkedHashMap<String, String> dateFormatTypeHashMap = new LinkedHashMap<String, String>();
        for (DateFormatTypeEnum a : DateFormatTypeEnum.values()) {
            //String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().normalizationTypeEnum() + a.getName());
            // TODO EDATOS-3744
            if (DateFormatTypeEnum.SPECIFIC_DATE.equals(a)) {
                dateFormatTypeHashMap.put(a.name(), "Fecha");
            } else {
                dateFormatTypeHashMap.put(a.name(), "Personalizado");
            }
            
        }
        return dateFormatTypeHashMap;
    }

}
