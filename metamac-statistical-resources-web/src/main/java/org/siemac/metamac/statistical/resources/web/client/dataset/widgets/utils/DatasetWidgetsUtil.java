package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.utils;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;

public class DatasetWidgetsUtil {

    public static FormItemIfFunction getIsSingleUpdateFunction(final boolean isMultipleUpdate) {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !isMultipleUpdate;
            }
        };
    }
}
