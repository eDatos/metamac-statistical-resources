package org.siemac.metamac.statistical.resources.web.client.widgets;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.constants.CommonWebConstants;
import org.siemac.metamac.web.common.client.widgets.SearchSectionStack;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;

public abstract class OperationSearchSectionStack extends SearchSectionStack {

    public OperationSearchSectionStack() {
        getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent arg0) {
                retrieveResources();
                
            }

        });

        addSearchItemKeyPressHandler(new KeyPressHandler() {

            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (StringUtils.equalsIgnoreCase(event.getKeyName(), CommonWebConstants.ENTER_KEY)) {
                    retrieveResources();
                } 
            }
        });
    }
    
    abstract void retrieveResources();

    public MetamacWebCriteria getDataConfigurationWebCriteria() {
        MetamacWebCriteria dataConfigurationWebCriteria = new MetamacWebCriteria();
        dataConfigurationWebCriteria.setCriteria(getSearchCriteria());
        return dataConfigurationWebCriteria;
    }

}
