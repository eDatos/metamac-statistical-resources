package org.siemac.metamac.statistical.resources.core.lifecycle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.siemac.metamac.statistical.resources.core.error.utils.ServiceExceptionParametersUtils.addParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.base.domain.HasLifecycle;
import org.siemac.metamac.statistical.resources.core.base.domain.HasSiemacMetadata;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionRationaleType;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Datasource;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionSingleParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.StatisticalResourcesMockFactory;

public class LifecycleCommonMetadataCheckerTest extends StatisticalResourcesBaseTest {

    private final LifecycleCommonMetadataChecker lifecycleCommonMetadataChecker;

    private static final String                  PARAMETER_VALUE_DATASET_VERSION__RELATED_DSD              = "dataset_version.related_dsd";
    private static final String                  PARAMETER_VALUE_DATASET_VERSION__GEOGRAPHIC_GRANULARITIES = "dataset_version.geographic_granularities";
    private static final String                  PARAMETER_VALUE_DATASET_VERSION__TEMPORAL_GRANULARITIES   = "dataset_version.temporal_granularities";
    private static final String                  PARAMETER_VALUE_DATASET_VERSION__UPDATE_FREQUENCY         = "dataset_version.update_frequency";
    private static final String                  PARAMETER_VALUE_DATASET_VERSION__STATISTIC_OFFICIALITY    = "dataset_version.statistic_officiality";
    private static final String                  PARAMETER_VALUE_DATASET_VERSION__DATE_NEXT_UPDATE         = "dataset_version.date_next_update";

    public LifecycleCommonMetadataCheckerTest() {
        lifecycleCommonMetadataChecker = new LifecycleCommonMetadataChecker();
    }

    @Test
    public void testCheckLifecycleCommonMetadata() throws Exception {
        HasLifecycle mockedResource = mock(HasLifecycle.class);
        when(mockedResource.getLifeCycleStatisticalResource()).thenReturn(new LifeCycleStatisticalResource());

        String baseMetadata = ServiceExceptionSingleParameters.DATASET_VERSION;
        expectedMetamacException(
                new MetamacException(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.STATISTICAL_OPERATION)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.CODE)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.URN)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.TITLE)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.DESCRIPTION)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.VERSION_LOGIC)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.VERSION_RATIONALE_TYPES)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.NEXT_VERSION)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.PROC_STATUS)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.MAINTAINER)))));

        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        lifecycleCommonMetadataChecker.checkLifecycleCommonMetadata(mockedResource, baseMetadata, exceptionItems);
        throw new MetamacException(exceptionItems);
    }

    @Test
    public void testCheckLifecycleCommonMetadataReplacesVersionNotRequired() throws Exception {
        HasLifecycle mockedResource = mock(HasLifecycle.class);
        when(mockedResource.getLifeCycleStatisticalResource()).thenReturn(new LifeCycleStatisticalResource());
        mockedResource.getLifeCycleStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.INIT_VERSION);

        String baseMetadata = ServiceExceptionSingleParameters.DATASET_VERSION;
        expectedMetamacException(
                new MetamacException(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.STATISTICAL_OPERATION)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.CODE)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.URN)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.TITLE)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.DESCRIPTION)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.VERSION_RATIONALE_TYPES)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.NEXT_VERSION)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.PROC_STATUS)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.MAINTAINER)))));

        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        lifecycleCommonMetadataChecker.checkLifecycleCommonMetadata(mockedResource, baseMetadata, exceptionItems);
        throw new MetamacException(exceptionItems);
    }

    @Test
    public void testCheckLifecycleCommonMetadataReplacesVersionRequired() throws Exception {
        HasLifecycle mockedResource = mock(HasLifecycle.class);
        when(mockedResource.getLifeCycleStatisticalResource()).thenReturn(new LifeCycleStatisticalResource());
        mockedResource.getLifeCycleStatisticalResource().setVersionLogic(StatisticalResourcesMockFactory.SECOND_VERSION);

        String baseMetadata = ServiceExceptionSingleParameters.DATASET_VERSION;
        expectedMetamacException(
                new MetamacException(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.STATISTICAL_OPERATION)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.CODE)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.URN)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.TITLE)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.DESCRIPTION)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.VERSION_RATIONALE_TYPES)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.NEXT_VERSION)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.PROC_STATUS)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.REPLACES_VERSION)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.MAINTAINER)))));

        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        lifecycleCommonMetadataChecker.checkLifecycleCommonMetadata(mockedResource, baseMetadata, exceptionItems);
        throw new MetamacException(exceptionItems);
    }

    @Test
    public void testCheckLifecycleCommonMetadataVersionRationaleRequiredIfMinorErrata() throws Exception {
        String baseMetadata = ServiceExceptionSingleParameters.DATASET_VERSION;

        for (VersionRationaleTypeEnum versionRationaleType2Test : VersionRationaleTypeEnum.values()) {
            HasLifecycle mockedResource = mock(HasLifecycle.class);
            when(mockedResource.getLifeCycleStatisticalResource()).thenReturn(new LifeCycleStatisticalResource());

            mockedResource.getLifeCycleStatisticalResource().addVersionRationaleType(new VersionRationaleType(versionRationaleType2Test));

            MetamacException expected = null;
            if (versionRationaleType2Test.equals(VersionRationaleTypeEnum.MINOR_ERRATA)) {
                expected = new MetamacException(
                        Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.STATISTICAL_OPERATION)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.CODE)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.URN)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.TITLE)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.DESCRIPTION)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.VERSION_LOGIC)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.VERSION_RATIONALE)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.NEXT_VERSION)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.PROC_STATUS)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.MAINTAINER))));
            } else {
                expected = new MetamacException(
                        Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.STATISTICAL_OPERATION)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.CODE)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.URN)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.TITLE)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.DESCRIPTION)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.VERSION_LOGIC)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.NEXT_VERSION)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.PROC_STATUS)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.MAINTAINER))));
            }

            List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
            lifecycleCommonMetadataChecker.checkLifecycleCommonMetadata(mockedResource, baseMetadata, exceptionItems);
            MetamacException actual = new MetamacException(exceptionItems);
            MetamacAsserts.assertEqualsMetamacException(expected, actual);
        }
    }

    @Test
    public void testCheckLifecycleCommonMetadataNextVersionDateInvalidIfNotScheduledNextVersion() throws Exception {
        String baseMetadata = ServiceExceptionSingleParameters.DATASET_VERSION;

        for (NextVersionTypeEnum nextVersion2Test : NextVersionTypeEnum.values()) {
            HasLifecycle mockedResource = mock(HasLifecycle.class);
            when(mockedResource.getLifeCycleStatisticalResource()).thenReturn(new LifeCycleStatisticalResource());

            mockedResource.getLifeCycleStatisticalResource().setNextVersion(nextVersion2Test);
            mockedResource.getLifeCycleStatisticalResource().setNextVersionDate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime().plusDays(1).toDate()));

            MetamacException expected = null;
            if (!nextVersion2Test.equals(NextVersionTypeEnum.SCHEDULED_UPDATE)) {
                expected = new MetamacException(
                        Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.STATISTICAL_OPERATION)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.CODE)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.URN)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.TITLE)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.DESCRIPTION)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.VERSION_LOGIC)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.VERSION_RATIONALE_TYPES)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED, addParameter(baseMetadata, ServiceExceptionSingleParameters.NEXT_VERSION_DATE)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.PROC_STATUS)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.MAINTAINER))));
            } else {
                expected = new MetamacException(
                        Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.STATISTICAL_OPERATION)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.CODE)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.URN)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.TITLE)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.DESCRIPTION)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.VERSION_LOGIC)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.VERSION_RATIONALE_TYPES)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.PROC_STATUS)),
                                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.MAINTAINER))));
            }

            List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
            lifecycleCommonMetadataChecker.checkLifecycleCommonMetadata(mockedResource, baseMetadata, exceptionItems);
            MetamacException actual = new MetamacException(exceptionItems);
            MetamacAsserts.assertEqualsMetamacException(expected, actual);
        }
    }

    @Test
    public void testCheckSiemacResourceCommonMetadata() throws Exception {
        HasSiemacMetadata mockedResource = mock(HasSiemacMetadata.class);
        when(mockedResource.getSiemacMetadataStatisticalResource()).thenReturn(new SiemacMetadataStatisticalResource());
        when(mockedResource.getLifeCycleStatisticalResource()).thenReturn(new LifeCycleStatisticalResource());

        String baseMetadata = ServiceExceptionSingleParameters.DATASET_VERSION;
        expectedMetamacException(
                new MetamacException(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.LANGUAGE)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.LANGUAGES)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.TYPE)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.CREATOR)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.DATA_PROVIDER)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.LAST_UPDATE)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.PUBLISHER)),
                        new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, addParameter(baseMetadata, ServiceExceptionSingleParameters.COMMON_METADATA)))));
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        lifecycleCommonMetadataChecker.checkSiemacCommonMetadata(mockedResource, baseMetadata, exceptionItems);
        throw new MetamacException(exceptionItems);
    }

    @Test
    public void testDatasetVersionCommonMetadata() throws Exception {
        DatasetVersion resource = new DatasetVersion();
        resource.setSiemacMetadataStatisticalResource(new SiemacMetadataStatisticalResource());
        resource.getSiemacMetadataStatisticalResource().setUrn("dataset-urn");

        String baseMetadata = ServiceExceptionSingleParameters.DATASET_VERSION;

        //@formatter:off
        expectedMetamacException(new MetamacException(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__RELATED_DSD),
                                                                    new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__GEOGRAPHIC_GRANULARITIES),
                                                                    new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__TEMPORAL_GRANULARITIES),
                                                                    new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__STATISTIC_OFFICIALITY),
                                                                    new MetamacExceptionItem(ServiceExceptionType.DATASET_EMPTY_DATASOURCES, resource.getSiemacMetadataStatisticalResource().getUrn()))));
        //@formatter:on

        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        lifecycleCommonMetadataChecker.checkDatasetVersionCommonMetadata(getServiceContextAdministrador(), resource, baseMetadata, exceptionItems);
        throw new MetamacException(exceptionItems);
    }

    @Test
    public void testDatasetVersionCommonMetadataWithDatasourceWithNoDateNextUpdateAndScheduledUpdate() throws Exception {
        DatasetVersion resource = new DatasetVersion();
        resource.setSiemacMetadataStatisticalResource(new SiemacMetadataStatisticalResource());
        resource.getSiemacMetadataStatisticalResource().setNextVersion(NextVersionTypeEnum.SCHEDULED_UPDATE);
        resource.addDatasource(new Datasource());

        String baseMetadata = ServiceExceptionSingleParameters.DATASET_VERSION;

        //@formatter:off
        expectedMetamacException(new MetamacException(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__RELATED_DSD),
                                                                    new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__GEOGRAPHIC_GRANULARITIES),
                                                                    new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__TEMPORAL_GRANULARITIES),
                                                                    new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__UPDATE_FREQUENCY),
                                                                    new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__STATISTIC_OFFICIALITY),
                                                                    new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__DATE_NEXT_UPDATE))));
        //@formatter:on

        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        lifecycleCommonMetadataChecker.checkDatasetVersionCommonMetadata(getServiceContextAdministrador(), resource, baseMetadata, exceptionItems);
        throw new MetamacException(exceptionItems);
    }

    @Test
    public void testDatasetVersionCommonMetadataWithDatasourceWithNoDateNextUpdateAndNonScheduledUpdate() throws Exception {
        DatasetVersion resource = new DatasetVersion();
        resource.setSiemacMetadataStatisticalResource(new SiemacMetadataStatisticalResource());
        resource.getSiemacMetadataStatisticalResource().setNextVersion(NextVersionTypeEnum.NON_SCHEDULED_UPDATE);
        resource.addDatasource(new Datasource());

        String baseMetadata = ServiceExceptionSingleParameters.DATASET_VERSION;

        //@formatter:off
        expectedMetamacException(new MetamacException(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__RELATED_DSD),
                                                                    new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__GEOGRAPHIC_GRANULARITIES),
                                                                    new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__TEMPORAL_GRANULARITIES),
                                                                    new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__UPDATE_FREQUENCY),
                                                                    new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__STATISTIC_OFFICIALITY))));
        //@formatter:on

        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        lifecycleCommonMetadataChecker.checkDatasetVersionCommonMetadata(getServiceContextAdministrador(), resource, baseMetadata, exceptionItems);
        throw new MetamacException(exceptionItems);
    }

    @Test
    public void testDatasetVersionCommonMetadataWithDatasourceWithNoDateNextUpdateAndNoUpdates() throws Exception {
        DatasetVersion resource = new DatasetVersion();
        resource.setSiemacMetadataStatisticalResource(new SiemacMetadataStatisticalResource());
        resource.getSiemacMetadataStatisticalResource().setNextVersion(NextVersionTypeEnum.NO_UPDATES);
        resource.addDatasource(new Datasource());

        String baseMetadata = ServiceExceptionSingleParameters.DATASET_VERSION;

        //@formatter:off
        expectedMetamacException(new MetamacException(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__RELATED_DSD),
                                                                    new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__GEOGRAPHIC_GRANULARITIES),
                                                                    new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__TEMPORAL_GRANULARITIES),
                                                                    new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__STATISTIC_OFFICIALITY))));
        //@formatter:on

        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        lifecycleCommonMetadataChecker.checkDatasetVersionCommonMetadata(getServiceContextAdministrador(), resource, baseMetadata, exceptionItems);
        throw new MetamacException(exceptionItems);
    }

    @Test
    public void testDatasetVersionCommonMetadataWithDatasourceWithUserDateNextUpdate() throws Exception {
        DatasetVersion resource = new DatasetVersion();
        resource.setSiemacMetadataStatisticalResource(new SiemacMetadataStatisticalResource());
        resource.addDatasource(new Datasource());
        resource.setUserModifiedDateNextUpdate(true);
        resource.setDateNextUpdate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime().toDate()));

        String baseMetadata = ServiceExceptionSingleParameters.DATASET_VERSION;

        //@formatter:off
        expectedMetamacException(new MetamacException(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__RELATED_DSD),
                                                                    new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__GEOGRAPHIC_GRANULARITIES),
                                                                    new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__TEMPORAL_GRANULARITIES),
                                                                    new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, PARAMETER_VALUE_DATASET_VERSION__STATISTIC_OFFICIALITY))));
        //@formatter:on

        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        lifecycleCommonMetadataChecker.checkDatasetVersionCommonMetadata(getServiceContextAdministrador(), resource, baseMetadata, exceptionItems);
        throw new MetamacException(exceptionItems);
    }

    @Test
    public void testPublicationVersionCommonMetadata() throws Exception {
        PublicationVersion resource = new PublicationVersion();
        resource.setFormatExtentResources(1);

        String baseMetadata = ServiceExceptionSingleParameters.PUBLICATION_VERSION;

        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        lifecycleCommonMetadataChecker.checkPublicationVersionCommonMetadata(resource, baseMetadata, exceptionItems);
        assertEquals(0, exceptionItems.size());
    }

    @Test
    public void testQueryVersionCommonMetadata() throws Exception {
        QueryVersion resource = new QueryVersion();

        String params = buildCommaSeparatedString(ServiceExceptionParameters.QUERY_VERSION__FIXED_DATASET_VERSION, ServiceExceptionParameters.QUERY_VERSION__DATASET);
        String baseMetadata = ServiceExceptionParameters.QUERY_VERSION;
        expectedMetamacException(new MetamacException(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.METADATA_SOME_REQUIRED, params),
                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.QUERY_VERSION__STATUS),
                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.QUERY_VERSION__TYPE),
                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.QUERY_VERSION__SELECTION))));

        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        lifecycleCommonMetadataChecker.checkQueryVersionCommonMetadata(resource, baseMetadata, exceptionItems);
        throw new MetamacException(exceptionItems);
    }

    @Test
    public void testQueryVersionCommonMetadataTypeLatestData() throws Exception {
        QueryVersion resource = new QueryVersion();
        resource.setType(QueryTypeEnum.LATEST_DATA);

        String params = buildCommaSeparatedString(ServiceExceptionParameters.QUERY_VERSION__FIXED_DATASET_VERSION, ServiceExceptionParameters.QUERY_VERSION__DATASET);
        String baseMetadata = ServiceExceptionParameters.QUERY_VERSION;
        expectedMetamacException(new MetamacException(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.METADATA_SOME_REQUIRED, params),
                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.QUERY_VERSION__STATUS),
                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.QUERY_VERSION__SELECTION),
                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.QUERY_VERSION__LATEST_DATA_NUMBER))));

        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        lifecycleCommonMetadataChecker.checkQueryVersionCommonMetadata(resource, baseMetadata, exceptionItems);
        throw new MetamacException(exceptionItems);
    }

    @Test
    public void testQueryVersionCommonMetadataTypeAutoincremental() throws Exception {
        QueryVersion resource = new QueryVersion();
        resource.setType(QueryTypeEnum.AUTOINCREMENTAL);

        String params = buildCommaSeparatedString(ServiceExceptionParameters.QUERY_VERSION__FIXED_DATASET_VERSION, ServiceExceptionParameters.QUERY_VERSION__DATASET);
        String baseMetadata = ServiceExceptionParameters.QUERY_VERSION;
        expectedMetamacException(new MetamacException(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.METADATA_SOME_REQUIRED, params),
                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.QUERY_VERSION__STATUS),
                new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.QUERY_VERSION__SELECTION))));

        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        lifecycleCommonMetadataChecker.checkQueryVersionCommonMetadata(resource, baseMetadata, exceptionItems);
        throw new MetamacException(exceptionItems);
    }
}
