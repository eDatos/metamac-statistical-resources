package org.siemac.metamac.statistical.resources.web.client.model.record;

import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.web.client.model.ds.LifeCycleResourceDS;

import com.smartgwt.client.widgets.form.fields.FormItemIcon;

public abstract class LifeCycleResourceRecord extends VersionableResourceRecord {

    public LifeCycleResourceRecord() {
    }

    public void setProcStatus(String value) {
        setAttribute(LifeCycleResourceDS.PROC_STATUS, value);
    }

    public void setCreationDate(String value) {
        setAttribute(LifeCycleResourceDS.CREATION_DATE, value);
    }

    public void setPublicationDate(String value) {
        setAttribute(LifeCycleResourceDS.PUBLICATION_DATE, value);
    }

    public abstract ProcStatusEnum getProcStatusEnum();

    public void setPublicationStreamStatus(FormItemIcon formItemIcon) {
        FormItemIcon publicationStreamStatusIcon = (formItemIcon != null && formItemIcon.getSrc() == null) ? null : formItemIcon;

        if (publicationStreamStatusIcon != null) {
            publicationStreamStatusIcon.setShowOver(false);
            setAttribute(LifeCycleResourceDS.PUBLICATION_STREAM_STATUS, publicationStreamStatusIcon);
        }
    }

    public void resetPublicationStreamStatus() {
        FormItemIcon publicationStreamStatusIcon = null;
        setAttribute(LifeCycleResourceDS.PUBLICATION_STREAM_STATUS, publicationStreamStatusIcon);
    }
}
