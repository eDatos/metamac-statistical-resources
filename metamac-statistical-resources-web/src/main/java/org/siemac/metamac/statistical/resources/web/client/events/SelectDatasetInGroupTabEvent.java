package org.siemac.metamac.statistical.resources.web.client.events;

import org.siemac.metamac.statistical.resources.web.client.enums.DatasetTabTypeEnum;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class SelectDatasetInGroupTabEvent extends GwtEvent<SelectDatasetInGroupTabEvent.SelectDatasetInGroupTabHandler> {

    public interface SelectDatasetInGroupTabHandler extends EventHandler {

        void onSelectDatasetInGroupTab(SelectDatasetInGroupTabEvent event);
    }

    private static Type<SelectDatasetInGroupTabHandler> TYPE = new Type<SelectDatasetInGroupTabHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<SelectDatasetInGroupTabHandler> getAssociatedType() {
        return TYPE;
    }

    public static void fire(HasHandlers source, DatasetTabTypeEnum datasetTabType) {
        if (TYPE != null) {
            source.fireEvent(new SelectDatasetInGroupTabEvent(datasetTabType));
        }
    }

    private final DatasetTabTypeEnum datasetTabType;

    public SelectDatasetInGroupTabEvent(DatasetTabTypeEnum tabType) {
        this.datasetTabType = tabType;
    }

    public DatasetTabTypeEnum getDatasetTabTypeEnum() {
        return datasetTabType;
    }

    @Override
    protected void dispatch(SelectDatasetInGroupTabHandler handler) {
        handler.onSelectDatasetInGroupTab(this);
    }

    public static Type<SelectDatasetInGroupTabHandler> getType() {
        return TYPE;
    }
}
