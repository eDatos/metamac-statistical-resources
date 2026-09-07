package org.siemac.metamac.statistical.resources.web.client.query.utils;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils;

import com.smartgwt.client.widgets.form.fields.FormItem;

public class QueryRelatedDatasetUtils {

    private QueryRelatedDatasetUtils() {
        // NOTHING TO DO HERE!
    }

    public static void setRelatedDataset(QueryVersionDto queryVersionDto, FormItem formItem) {
        if (queryVersionDto != null && queryVersionDto.getRelatedDatasetVersion() != null) {
            if (QueryTypeEnum.FIXED.equals(queryVersionDto.getQueryType())) {
                // FIXED queries always display the specific version, even when it is currently the last one
                formItem.setTitle(getConstants().queryDatasetVersion());
                StatisticalResourcesFormUtils.setVersionableRelatedResourceValue(formItem, queryVersionDto.getRelatedDatasetVersion());
            } else {
                setRelatedDataset(queryVersionDto.getRelatedDatasetVersion(), formItem);
            }
        } else {
            StatisticalResourcesFormUtils.setRelatedResourceValue(formItem, null);
        }
    }

    public static void setRelatedDataset(RelatedResourceDto relatedResourceDto, FormItem formItem) {
        if (relatedResourceDto != null && relatedResourceDto.getLastVersion() != null) {
            if (relatedResourceDto.getLastVersion()) {
                formItem.setTitle(getConstants().queryDataset());
                StatisticalResourcesFormUtils.setRelatedResourceValue(formItem, relatedResourceDto);
            } else {
                formItem.setTitle(getConstants().queryDatasetVersion());
                StatisticalResourcesFormUtils.setVersionableRelatedResourceValue(formItem, relatedResourceDto);
            }
        } else {
            formItem.setTitle(getConstants().queryDataset());
            StatisticalResourcesFormUtils.setRelatedResourceValue(formItem, null);
        }
    }
}
