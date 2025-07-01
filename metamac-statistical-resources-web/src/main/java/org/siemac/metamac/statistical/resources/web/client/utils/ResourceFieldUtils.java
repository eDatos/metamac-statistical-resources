package org.siemac.metamac.statistical.resources.web.client.utils;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.model.ds.IdentifiableResourceDS;
import org.siemac.metamac.statistical.resources.web.client.model.ds.LifeCycleResourceDS;
import org.siemac.metamac.statistical.resources.web.client.model.ds.NameableResourceDS;
import org.siemac.metamac.statistical.resources.web.client.model.ds.VersionableResourceDS;
import org.siemac.metamac.statistical.resources.web.client.query.model.ds.QueryDS;
import org.siemac.metamac.web.common.client.utils.ListGridUtils;
import org.siemac.metamac.web.common.client.widgets.CustomLinkListGridField;
import org.siemac.metamac.web.common.client.widgets.CustomListGridField;
import org.siemac.metamac.web.common.client.widgets.utils.VersionFieldSortNormalizer;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ResourceFieldUtils {

    //
    // IDENTIFIABLE
    //

    public static CustomListGridField[] getIdentifiableListGridFields() {

        CustomListGridField code = new CustomListGridField(IdentifiableResourceDS.CODE, getConstants().identifiableStatisticalResourceCode());

        CustomListGridField urn = new CustomListGridField(IdentifiableResourceDS.URN, getConstants().identifiableStatisticalResourceURN());
        urn.setHidden(true);

        return new CustomListGridField[]{code, urn};
    }

    //
    // NAMEABLE
    //

    public static CustomListGridField[] getNameableListGridFields() {

        CustomListGridField title = new CustomListGridField(NameableResourceDS.TITLE, getConstants().nameableStatisticalResourceTitle());

        return ListGridUtils.addFields(getIdentifiableListGridFields(), title);
    }

    //
    // VERSIONABLE
    //

    public static CustomListGridField[] getVersionableListGridFields() {

        CustomListGridField version = new CustomListGridField(VersionableResourceDS.VERSION, getConstants().versionableVersion());
        version.setSortNormalizer(new VersionFieldSortNormalizer());

        return ListGridUtils.addFields(getNameableListGridFields(), version);
    }

    //
    // LIFECYCLE
    //

    public static CustomListGridField[] getLifeCycleListGridFields() {

        CustomListGridField procStatus = new CustomListGridField(LifeCycleResourceDS.PROC_STATUS, getConstants().lifeCycleStatisticalResourceProcStatus());

        CustomListGridField creationDate = new CustomListGridField(LifeCycleResourceDS.CREATION_DATE, getConstants().lifeCycleStatisticalResourceCreationDate());
        creationDate.setHidden(true);

        CustomListGridField publicationDate = new CustomListGridField(LifeCycleResourceDS.PUBLICATION_DATE, getConstants().lifeCycleStatisticalResourcePublicationDate());
        publicationDate.setHidden(true);

        CustomListGridField publicationStreamStatus = new CustomListGridField(LifeCycleResourceDS.PUBLICATION_STREAM_STATUS, getConstants().publicationStreamStatus());
        publicationStreamStatus.setType(ListGridFieldType.IMAGE);
        publicationStreamStatus.setAlign(Alignment.CENTER);

        return ListGridUtils.addFields(getVersionableListGridFields(), procStatus, creationDate, publicationDate, publicationStreamStatus);
    }

    //
    // SIEMAC METADATA
    //

    public static CustomListGridField[] getSiemacMetadataListGridFields() {
        return ListGridUtils.addFields(getLifeCycleListGridFields());
    }

    //
    // DATASET
    //

    public static CustomListGridField[] getDatasetListGridFields() {

        CustomLinkListGridField relatedDSD = new CustomLinkListGridField(DatasetDS.RELATED_DSD, getConstants().datasetRelatedDSD());
        relatedDSD.setHidden(true);

        CustomListGridField statisticOfficiality = new CustomListGridField(DatasetDS.STATISTIC_OFFICIALITY, getConstants().datasetStatisticOfficiality());
        statisticOfficiality.setHidden(true);

        return ListGridUtils.addFields(getSiemacMetadataListGridFields(), relatedDSD, statisticOfficiality);
    }

    //
    // PUBLICATION
    //

    public static CustomListGridField[] getPublicationListGridFields() {
        return getSiemacMetadataListGridFields();
    }

    //
    // QUERY
    //

    public static CustomListGridField[] getQueryListGridFields() {

        CustomLinkListGridField relatedDataset = new CustomLinkListGridField(QueryDS.RELATED_DATASET_VERSION, getConstants().queryDataset());

        CustomListGridField status = new CustomListGridField(QueryDS.STATUS, getConstants().queryStatus());

        CustomListGridField type = new CustomListGridField(QueryDS.TYPE, getConstants().queryType());

        return ListGridUtils.addFields(getLifeCycleListGridFields(), relatedDataset, status, type);
    }

    //
    // Multidataset
    //

    public static CustomListGridField[] getMultidatasetListGridFields() {
        return getSiemacMetadataListGridFields();
    }

    public static String formatResourceLink(Object value, ListGridRecord record, String resourceTypeFieldName) {
        if (value == null) {
            return null;
        }
        String urn = value.toString();
        String resourceType = record.getAttribute(resourceTypeFieldName);
        if (!StatisticalResourceTypeEnum.URL.name().equalsIgnoreCase(resourceType) && urn != null && !urn.isEmpty()) {
            return UrnUtils.removePrefix(urn);
        }
        return urn;
    }
}
