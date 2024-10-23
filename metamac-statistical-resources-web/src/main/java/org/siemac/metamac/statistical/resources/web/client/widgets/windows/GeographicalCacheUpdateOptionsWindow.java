package org.siemac.metamac.statistical.resources.web.client.widgets.windows;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
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

public class GeographicalCacheUpdateOptionsWindow extends Window {

    protected static final int             FORM_ITEM_CUSTOM_WIDTH  = 450;
    protected static final int             FORM_ITEM_CUSTOM_HEIGTH = 270;

    protected ToolStripButton              updateCacheButton;
    protected ToolStripButton              cancelUpdateButton;

    private CreateGeoCacheUpdateConfigForm form;

    public GeographicalCacheUpdateOptionsWindow(String title, String message) {
        super();
        setWidth(FORM_ITEM_CUSTOM_WIDTH);
        setHeight(FORM_ITEM_CUSTOM_HEIGTH);
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

        updateCacheButton = new ToolStripButton(MetamacWebCommon.getConstants().accept(), RESOURCE.success().getURL());

        cancelUpdateButton = new ToolStripButton(MetamacWebCommon.getConstants().actionCancel(), RESOURCE.close().getURL());
        cancelUpdateButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });

        toolStrip.addButton(updateCacheButton);
        toolStrip.addButton(cancelUpdateButton);

        layout.addMember(toolStrip);

        form = new CreateGeoCacheUpdateConfigForm();
        layout.addMember(form);

        addItem(layout);

    }

    public boolean validate() {
        return form.validate();
    }

    public ToolStripButton getUpdateCacheButton() {
        return updateCacheButton;
    }

    public List<StatisticalResourceTypeEnum> getStatisticalResourcesSelectedOptions(boolean isExternalResources) {
        return form.getStatisticalResourcesSelectedOptions(isExternalResources);
    }

    private class CreateGeoCacheUpdateConfigForm extends CustomDynamicForm {

        private static final String FIELD_DATASET             = "dataset-geo-cache-resource";
        private static final String FIELD_QUERY               = "query-geo-cache-resource";
        private static final String FIELD_COLLECTION          = "collection-geo-cache-resource";
        private static final String FIELD_EXTERNAL_DATASET    = "external-dataset-geo-cache-resource";
        private static final String FIELD_EXTERNAL_COLLECTION = "external-collection-geo-cache-resource";

        public CreateGeoCacheUpdateConfigForm() {
            super();
            setMargin(5);

            CustomCheckboxItem datasets = new CustomCheckboxItem(FIELD_DATASET, getConstants().geoCacheDatasetReload());

            CustomCheckboxItem queries = new CustomCheckboxItem(FIELD_QUERY, getConstants().geoCacheQueryReload());

            CustomCheckboxItem collections = new CustomCheckboxItem(FIELD_COLLECTION, getConstants().geoCacheCollectionReload());

            setFields(datasets, queries, collections);

            if (CommonUtils.getExternalDatasetTopicName() != null) {
                CustomCheckboxItem externalDatasets = new CustomCheckboxItem(FIELD_EXTERNAL_DATASET, getConstants().geoCacheJaxiDatasetReload());
                addFields(externalDatasets);
            }

            if (CommonUtils.getExternalCollectionTopicName() != null) {
                CustomCheckboxItem externalCollections = new CustomCheckboxItem(FIELD_EXTERNAL_COLLECTION, getConstants().geoCacheJaxiCollectionReload());
                addFields(externalCollections);
            }

        }

        private boolean isOptionSelected(String field) {
            return getValue(field) != null ? (Boolean) getValue(field) : false;
        }

        @Override
        public boolean validate() {
            return isOptionSelected(FIELD_DATASET) || isOptionSelected(FIELD_QUERY) || isOptionSelected(FIELD_COLLECTION) || isOptionSelected(FIELD_EXTERNAL_DATASET)
                    || isOptionSelected(FIELD_EXTERNAL_COLLECTION);
        }

        public List<StatisticalResourceTypeEnum> getStatisticalResourcesSelectedOptions(boolean isExternalResources) {
            List<StatisticalResourceTypeEnum> resources = new ArrayList<StatisticalResourceTypeEnum>();
            if (!isExternalResources) {

                if (isOptionSelected(FIELD_DATASET)) {
                    resources.add(StatisticalResourceTypeEnum.DATASET);
                }

                if (isOptionSelected(FIELD_QUERY)) {
                    resources.add(StatisticalResourceTypeEnum.QUERY);
                }

                if (isOptionSelected(FIELD_COLLECTION)) {
                    resources.add(StatisticalResourceTypeEnum.COLLECTION);
                }
            } else {
                if (isOptionSelected(FIELD_EXTERNAL_DATASET)) {
                    resources.add(StatisticalResourceTypeEnum.DATASET);
                }

                if (isOptionSelected(FIELD_EXTERNAL_COLLECTION)) {
                    resources.add(StatisticalResourceTypeEnum.COLLECTION);
                }
            }
            return resources;
        }

    }
}
