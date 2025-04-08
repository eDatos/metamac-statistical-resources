package org.siemac.metamac.statistical.resources.core.invocation.service;

import java.io.Serializable;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.notices.v1_0.domain.enume.MetamacRolesEnum;
import org.siemac.metamac.statistical.resources.core.base.domain.HasSiemacMetadata;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;

import es.ibestat.jaxi.stream.messages.DatasetAvro;

public interface NoticesRestInternalService {

    public static final String BEAN_ID = "noticesRestInternalService";

    // Background Notifications
    public void createErrorBackgroundNotification(String user, String actionCode, MetamacException exception);
    public void createErrorBackgroundNotification(String actionCode, MetamacException exception);
    public void createDatabaseImportErrorBackgroundNotification(DatasetVersion datasetVersion, String actionCode, MetamacException exception);
    public void createDatabaseImportErrorBackgroundNotification(DatasetVersion datasetVersion, String actionCode, MetamacException exception, MetamacRolesEnum... aValue);
    public void createSuccessBackgroundNotification(String user, String actionCode, String successMessageCode, Serializable... successMessageParameters);
    public void createDatabaseBackgroundNotification(DatasetVersion datasetVersion, String actionCode, String successMessageCode, Serializable... successMessageParameters);
    public void createDatabaseImportSuccessBackgroundNotification(DatasetVersion datasetVersion, String actionCode, String successMessageCode, String tableName, MetamacRolesEnum... aValue);
    public void createUpdateGeocoverageCacheNotification(DatasetVersion datasetVersion, String actionCode, String messageCode, Serializable... successMessageParameters);
    public void createErrorUpdateGeocoverageCacheBackgroundNotification(DatasetAvro jaxiDatasetVersionAvro, String actionCode, String messageCode, Serializable... messageParameters);
    public void createExternalPublicationUpdateErrorBackgroundNotification(String keyMessage);

    // Stream Messaging Notifications
    public void createErrorOnStreamMessagingService(String user, String actionCode, HasSiemacMetadata affectedResource, String errorMessageCode, Serializable... extraParameters);
    public void createErrorOnStreamMessagingService(String user, String actionCode, QueryVersion affectedResource, String errorMessageCode, Serializable... extraParameters);
    public void createConsumerFromKafkaErrorBackgroundNotification(String keyMessage);

    void createAssignRolePermissionsDatasetErrorBackgroundNotification(String dataViewsRole, String viewCode);
    void createCreateReplaceDatasetErrorBackgroundNotification(DatasetVersion datasetVersion, String viewCode, String datasetRepositoryId);
    
    void createLifeCycleNotification(ServiceContext serviceContext, ProcStatusEnum procStatus, DatasetVersion datasetVersion) throws MetamacException;
}
