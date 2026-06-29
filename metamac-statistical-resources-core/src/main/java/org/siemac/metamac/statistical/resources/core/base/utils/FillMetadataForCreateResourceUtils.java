package org.siemac.metamac.statistical.resources.core.base.utils;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionRationaleType;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.utils.CommonVersioningCopyUtils;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesVersionUtils;

public final class FillMetadataForCreateResourceUtils {

    private FillMetadataForCreateResourceUtils() {
    }

    public static void fillMetadataForCretateSiemacResource(SiemacMetadataStatisticalResource resource, ExternalItem statisticalOperation, StatisticalResourceTypeEnum type, ServiceContext ctx) {
        fillMetadataForCreateLifeCycleResource(resource, statisticalOperation, ctx);

        resource.setType(type);
    }

    public static void fillMetadataForCreateLifeCycleResource(LifeCycleStatisticalResource resource, ExternalItem statisticalOperation, ServiceContext ctx) {
        fillMetadataForCreateVersionableResource(resource, statisticalOperation);

        resource.setProcStatus(ProcStatusEnum.DRAFT);
        resource.setCreatedDate(new DateTime());
        resource.setCreatedBy(ctx.getUserId());
        resource.setCreationDate(resource.getCreatedDate());
        resource.setCreationUser(ctx.getUserId());
        resource.setLastVersion(true);
    }

    public static void fillMetadataForCreateVersionableResource(VersionableStatisticalResource resource, ExternalItem statisticalOperation) {
        fillMetadataForCreateNameableResource(resource, statisticalOperation);

        resource.setVersionLogic(StatisticalResourcesVersionUtils.INITIAL_VERSION);
        resource.setPatch(0);
        resource.getVersionRationaleTypes().clear();
        resource.addVersionRationaleType(new VersionRationaleType(VersionRationaleTypeEnum.MAJOR_NEW_RESOURCE));
    }

    public static void fillMetadataForCreateNameableResource(IdentifiableStatisticalResource resource, ExternalItem statisticalOperation) {
        fillMetadataForCreateIdentifiableResource(resource, statisticalOperation);
    }

    public static void fillMetadataForCreateIdentifiableResource(IdentifiableStatisticalResource resource, ExternalItem statisticalOperation) {
        fillMetadataForCreateStatistiscalResource(resource, statisticalOperation);

        // CODE and URN are setting in specific methods for each entity.
        // - Query Versions: fillMetadataForCreateQuery
        // - Datasets Versions and Publications Versions: just before saving, because the computation for code must be synchronized and this way, we minimize the synchronized block
    }

    public static void fillMetadataForCreateStatistiscalResource(IdentifiableStatisticalResource resource, ExternalItem statisticalOperation) {
        resource.setStatisticalOperation(CommonVersioningCopyUtils.copyExternalItem(statisticalOperation));
    }

}
