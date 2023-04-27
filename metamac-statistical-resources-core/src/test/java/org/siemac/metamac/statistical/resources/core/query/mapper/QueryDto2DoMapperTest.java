package org.siemac.metamac.statistical.resources.core.query.mapper;

import static org.junit.Assert.assertTrue;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.QueryAsserts.assertEqualsQueryVersion;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_03_BASIC_WITH_2_C1_DATASET_VERSIONS_NAME;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDtoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/statistical-resources/include/rest-services-mockito.xml", "classpath:spring/statistical-resources/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class QueryDto2DoMapperTest extends StatisticalResourcesBaseTest {

    @Autowired
    private QueryDto2DoMapper queryDto2DoMapper;

    @Test
    @MetamacMock(DATASET_03_BASIC_WITH_2_C1_DATASET_VERSIONS_NAME)
    public void testQueryDtoToDoQueryLinkedToFixedDatasetVersion() throws MetamacException {
        QueryVersionDto expected = StatisticalResourcesDtoMocks.mockQueryVersionDto(datasetVersionMockFactory.retrieveMock(DatasetVersionMockFactory.DATASET_VERSION_03_C1_FOR_DATASET_03_NAME));
        QueryVersion actual = queryDto2DoMapper.queryVersionDtoToDo(expected);
        assertEqualsQueryVersion(expected, actual);
        assertTrue(expected.getRelatedDatasetVersion().getUrn().equals(actual.getFixedDatasetVersion().getSiemacMetadataStatisticalResource().getUrn()));
    }

    @Test
    @MetamacMock(DATASET_03_BASIC_WITH_2_C1_DATASET_VERSIONS_NAME)
    public void testQueryDtoToDoLinkedToDataset() throws MetamacException {
        DatasetVersion lastVersion = datasetVersionMockFactory.retrieveMock(DatasetVersionMockFactory.DATASET_VERSION_04_FOR_DATASET_03_C1_AND_LAST_VERSION_NAME);

        QueryVersionDto expected = StatisticalResourcesDtoMocks.mockQueryVersionDto(lastVersion);
        QueryVersion actual = queryDto2DoMapper.queryVersionDtoToDo(expected);
        assertEqualsQueryVersion(expected, actual);
        assertTrue(lastVersion.getDataset().getIdentifiableStatisticalResource().getUrn().equals(actual.getDataset().getIdentifiableStatisticalResource().getUrn()));
    }
}
