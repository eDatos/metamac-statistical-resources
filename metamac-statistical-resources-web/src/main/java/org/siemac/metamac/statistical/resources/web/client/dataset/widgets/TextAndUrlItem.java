package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;

import org.siemac.metamac.statistical.resources.web.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomDateItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;

import com.smartgwt.client.types.FormErrorOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class TextAndUrlItem extends CustomCanvasItem {

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
    
    private DynamicForm form;

    private CustomDateItem nextVersionDate;
    CustomSelectItem dateFormatType;
    private TextItem customSdmxTimePeriodItem;
    
    private void common(boolean required) {
        setTitleStyle(( !required) ? "localeLabel" : "requiredLocaleLabel");
        setCellStyle("localeCellStyle");
        setTitleVAlign(VerticalAlignment.CENTER);
    }
    
    public TextAndUrlItem(String name, String title, boolean required) {
        super(name, title);
        setRedrawOnChange(true);
        setShouldSaveValue(true);
        common(required);

        form = new DynamicForm();
        form.setAutoHeight();
        form.setCellPadding(1);
        form.setStyleName("urlForm");
        form.setRedrawOnResize(true);

        form.setErrorOrientation(FormErrorOrientation.RIGHT);
        form.setValidateOnChange(true);
        dateFormatType = new CustomSelectItem(name + nameSelectorField, "-");
        dateFormatType.setShowTitle(false);
        dateFormatType.setRequired(required);
        dateFormatType.setValueMap(getDateFormatTypeHashMap());
        
        dateFormatType.addChangeHandler(new com.smartgwt.client.widgets.form.fields.events.ChangeHandler() {

            @Override
            public void onChange(com.smartgwt.client.widgets.form.fields.events.ChangeEvent event) {
                if (DateFormatTypeEnum.SPECIFIC_DATE.getName().equals(event.getValue())) {
                    customSdmxTimePeriodItem.hide();
                    customSdmxTimePeriodItem.setVisible(false);
                    nextVersionDate.show();
                    nextVersionDate.setVisible(true);
                    
                } else {
                    customSdmxTimePeriodItem.show();
                    customSdmxTimePeriodItem.setVisible(true);
                    nextVersionDate.hide();
                    nextVersionDate.setVisible(false);
                }
            }
        });
        
        customSdmxTimePeriodItem = new TextItem(name + nameCustomField, "-");
        customSdmxTimePeriodItem.setShowTitle(false);
        customSdmxTimePeriodItem.setRequired(required);
        customSdmxTimePeriodItem.setWidth("150");
        
        nextVersionDate = new CustomDateItem(name + nameDateField, getConstants().versionableStatisticalResourceNextVersionDate());
        nextVersionDate.setShowTitle(false);
       
        form.setFields(dateFormatType, nextVersionDate, customSdmxTimePeriodItem);

        Canvas canvas = new Canvas();
        canvas.addChild(form);
        canvas.setAutoHeight();

        setCanvas(canvas);
        setCellHeight(20);
        
    }
   
    public void setValue(String value, String url) {

        dateFormatType.setValue(value);
    }

    public String getTextValue() {
        if (DateFormatTypeEnum.SPECIFIC_DATE.equals(dateFormatType.getValue())) {
            return nextVersionDate.getValue();
        } else {
            dateFormatTypeHashMap.put(a.name(), "Personalizado");
        }
    }

    @Override
    public void setDefaultValue(String sdmxTimePeriod) {
        if (DateUtils.isValidDateInSdmx(sdmxTimePeriod)) {
            customSdmxTimePeriodItem.setVisible(false);
            nextVersionDate.setVisible(true);
            nextVersionDate.setValue(DateUtils.getDate(sdmxTimePeriod));
        } else {
            customSdmxTimePeriodItem.setVisible(true);
            nextVersionDate.setVisible(false);
            customSdmxTimePeriodItem.setValue(sdmxTimePeriod);
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
