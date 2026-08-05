package org.siemac.metamac.statistical.resources.web.client.widgets.windows;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;

import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.VisibilityChangedEvent;
import com.smartgwt.client.widgets.events.VisibilityChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class KafkaTopicReloadOptionsWindow extends Window {

    protected static final int             FORM_ITEM_CUSTOM_WIDTH = 450;

    protected ToolStripButton              reloadButton;
    protected ToolStripButton              cancelButton;

    private KafkaTopicReloadConfigForm     form;

    public KafkaTopicReloadOptionsWindow(String title, String message) {
        super();
        setWidth(FORM_ITEM_CUSTOM_WIDTH);
        setAutoSize(true);
        setTitle(title);
        setShowMinimizeButton(false);
        setIsModal(true);
        setShowModalMask(true);
        setAutoCenter(true);
        addCloseClickHandler(new CloseClickHandler() {

            @Override
            public void onCloseClick(CloseClickEvent event) {
                hide();
            }
        });

        addVisibilityChangedHandler(new VisibilityChangedHandler() {

            @Override
            public void onVisibilityChanged(VisibilityChangedEvent event) {
                if (event.getIsVisible()) {
                    form.clearValues();
                }
            }
        });

        Label label = new Label(message);
        label.setAutoHeight();
        label.setIcon(GlobalResources.RESOURCE.info().getURL());
        label.setIconSize(32);
        label.setIconSpacing(10);

        VLayout layout = new VLayout();
        layout.setWidth100();
        layout.setHeight100();
        layout.setMembersMargin(15);
        layout.addMember(label);
        layout.setMargin(10);

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        reloadButton = new ToolStripButton(MetamacWebCommon.getConstants().accept(), RESOURCE.success().getURL());

        cancelButton = new ToolStripButton(MetamacWebCommon.getConstants().actionCancel(), RESOURCE.cancelListGrid().getURL());
        cancelButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });

        toolStrip.addButton(reloadButton);
        toolStrip.addButton(cancelButton);

        layout.addMember(toolStrip);

        form = new KafkaTopicReloadConfigForm();
        layout.addMember(form);

        addItem(layout);
    }

    public boolean validate() {
        return form.validate();
    }

    public ToolStripButton getReloadButton() {
        return reloadButton;
    }

    public List<StatisticalResourceTypeEnum> getSelectedResourceTypes() {
        return form.getSelectedResourceTypes();
    }

    private class KafkaTopicReloadConfigForm extends CustomDynamicForm {

        private static final String FIELD_DATASET    = "dataset-kafka-reload";
        private static final String FIELD_COLLECTION = "collection-kafka-reload";
        private static final String FIELD_QUERY      = "query-kafka-reload";

        public KafkaTopicReloadConfigForm() {
            super();
            setMargin(5);

            CustomCheckboxItem datasets = new CustomCheckboxItem(FIELD_DATASET, getConstants().reloadKafkaTopicDatasetReload());
            CustomCheckboxItem collections = new CustomCheckboxItem(FIELD_COLLECTION, getConstants().reloadKafkaTopicCollectionReload());
            CustomCheckboxItem queries = new CustomCheckboxItem(FIELD_QUERY, getConstants().reloadKafkaTopicQueryReload());

            setFields(datasets, collections, queries);
        }

        private boolean isOptionSelected(String field) {
            return getValue(field) != null ? (Boolean) getValue(field) : false;
        }

        @Override
        public boolean validate() {
            return isOptionSelected(FIELD_DATASET) || isOptionSelected(FIELD_COLLECTION) || isOptionSelected(FIELD_QUERY);
        }

        public List<StatisticalResourceTypeEnum> getSelectedResourceTypes() {
            List<StatisticalResourceTypeEnum> resources = new ArrayList<StatisticalResourceTypeEnum>();

            if (isOptionSelected(FIELD_DATASET)) {
                resources.add(StatisticalResourceTypeEnum.DATASET);
            }

            if (isOptionSelected(FIELD_COLLECTION)) {
                resources.add(StatisticalResourceTypeEnum.COLLECTION);
            }

            if (isOptionSelected(FIELD_QUERY)) {
                resources.add(StatisticalResourceTypeEnum.QUERY);
            }

            return resources;
        }
    }
}
