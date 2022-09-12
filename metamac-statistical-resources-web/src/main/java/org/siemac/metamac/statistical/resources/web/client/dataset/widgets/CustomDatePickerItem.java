package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getCoreMessages;

import java.io.Serializable;
import java.util.LinkedHashMap;

import org.siemac.metamac.core.common.util.shared.StringUtils;
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

    protected CustomDateItem date;
    CustomSelectItem dateFormatType;
    protected TextItem customSdmxTimePeriodItem;
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
            customDatePickerItemForm();
        } else {
            customDatePickerItemEditionForm(name, required); 
        }
        

        Canvas canvas = new Canvas();
        canvas.addChild(form);
        canvas.setAutoHeight();

        setCanvas(canvas);
        setCellHeight(20);
        
    }

    public void defaultDateType() {
        enabledDate();
    }
    
    private void enabledDate() {
        customSdmxTimePeriodItem.hide();
        customSdmxTimePeriodItem.setVisible(false);
        customSdmxTimePeriodItem.clearValue();
        date.show();
        date.setVisible(true);
    }

    private void enabledCustomSdmxTimePeriodItem() {
        customSdmxTimePeriodItem.show();
        customSdmxTimePeriodItem.setVisible(true);
        date.hide();
        date.setVisible(false);
        if (date.getValue() != null) {
            date.clearValue();
        }
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
                    enabledDate();
                } else {
                    enabledCustomSdmxTimePeriodItem();
                }
            }
        });
        
        customSdmxTimePeriodItem = createCustomSdmxTimePeriodItem(name, required);
        
        date = createDateItem(name);
               
        form.setFields(dateFormatType, date, customSdmxTimePeriodItem);
    }
    
    private CustomDateItem createDateItem(String name) {
        CustomDateItem item = new CustomDateItem(name + nameDateField, "-");
        item.setShowTitle(false);
        return item;
    }
        
    private TextItem createCustomSdmxTimePeriodItem(String name, boolean required) {

        TextItem customSdmxDate = new TextItem(name + nameCustomField, "-");
        customSdmxDate.setShowTitle(false);
        customSdmxDate.setRequired(required);
        customSdmxDate.setWidth("150");
        return customSdmxDate;
    }
    
    public void setIconCreateDateItem(String message) {
        FormItemIcon infoIcon = createIcon(message);
        date.setIcons(infoIcon);
    }

    public void setIconCustomSdmxTimePeriodItem(String message) {
        FormItemIcon infoIcon = new FormItemIcon();
        infoIcon.setSrc(GlobalResources.RESOURCE.info().getURL());
        infoIcon.setPrompt(message);
        customSdmxTimePeriodItem.setIcons(infoIcon);
    }
    
    private FormItemIcon createIcon(String message) {
        FormItemIcon infoIcon = new FormItemIcon();
        infoIcon.setSrc(GlobalResources.RESOURCE.info().getURL());
        infoIcon.setPrompt(message);
        return infoIcon;
    }
    
    private void customDatePickerItemForm() {
        staticTimePeriodItem = new ViewTextItem();
        staticTimePeriodItem.setShowTitle(false); 
        staticTimePeriodItem.setRedrawOnChange(true);
        form.setFields(staticTimePeriodItem);
    }

    @Override
    public String getValue() {
        // get value as observational time period
        if (DateFormatTypeEnum.SPECIFIC_DATE.getName().equals(dateFormatType.getValueAsString())) {
            return DateUtils.getDateInSdmxFormat(date.getValueAsDate());
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
        if (StringUtils.isBlank(sdmxTimePeriod) || DateUtils.isValidDateInSdmx(sdmxTimePeriod)) {
            dateFormatType.setValue(DateFormatTypeEnum.SPECIFIC_DATE.getName());
            enabledDate();
            date.setValue(DateUtils.getDate(sdmxTimePeriod));
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
            String value = getCoreMessages().getString(getCoreMessages().dateFormatTypeEnum() + a.getName());
            dateFormatTypeHashMap.put(a.name(), value);
        }

        return dateFormatTypeHashMap;
    }

}
