package org.siemac.metamac.statistical.resources.core.query.mapper;

import static org.junit.Assert.assertEquals;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.QueryAsserts.assertEqualsQuery;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.QueryAsserts.assertEqualsQueryVersion;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.QueryAsserts.assertEqualsQueryVersionDoAndDtoCollection;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_01_WITH_SELECTION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_02_BASIC_ORDERED_01_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_03_BASIC_ORDERED_02_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_05_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_15_PUBLISHED_NAME;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/statistical-resources/include/rest-services-mockito.xml", "classpath:spring/statistical-resources/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class QueryDo2DtoMapperTest extends StatisticalResourcesBaseTest {

    @Autowired
    private QueryDo2DtoMapper queryDo2DtoMapper;

    @Test
    @MetamacMock(QUERY_VERSION_01_WITH_SELECTION_NAME)
    public void testQueryDo2Dto() throws MetamacException {
        QueryVersion expected = queryVersionMockFactory.retrieveMock(QUERY_VERSION_01_WITH_SELECTION_NAME);
        QueryVersionDto actual = queryDo2DtoMapper.queryVersionDoToDto(getServiceContextWithoutPrincipal(), expected);
        assertEqualsQueryVersion(expected, actual);
    }

    @Test
    @MetamacMock(QUERY_VERSION_15_PUBLISHED_NAME)
    public void testQueryDo2DtoProcStatusPublishedVisible() throws MetamacException {
        QueryVersion expected = queryVersionMockFactory.retrieveMock(QUERY_VERSION_15_PUBLISHED_NAME);
        QueryVersionDto actual = queryDo2DtoMapper.queryVersionDoToDto(getServiceContextWithoutPrincipal(), expected);
        assertEquals(ProcStatusEnum.PUBLISHED, actual.getProcStatus());
    }

    @Test
    @MetamacMock({QUERY_VERSION_02_BASIC_ORDERED_01_NAME, QUERY_VERSION_03_BASIC_ORDERED_02_NAME, QUERY_VERSION_05_BASIC_NAME, QUERY_VERSION_01_WITH_SELECTION_NAME})
    public void testQueryDoListToDtoList() throws MetamacException {
        List<QueryVersion> expected = new ArrayList<QueryVersion>();
        expected.add(queryVersionMockFactory.retrieveMock(QUERY_VERSION_02_BASIC_ORDERED_01_NAME));
        expected.add(queryVersionMockFactory.retrieveMock(QUERY_VERSION_03_BASIC_ORDERED_02_NAME));
        expected.add(queryVersionMockFactory.retrieveMock(QUERY_VERSION_05_BASIC_NAME));
        expected.add(queryVersionMockFactory.retrieveMock(QUERY_VERSION_01_WITH_SELECTION_NAME));

        List<QueryVersionBaseDto> actual = queryDo2DtoMapper.queryVersionDoListToDtoList(getServiceContextWithoutPrincipal(), expected);

        assertEquals(expected.size(), actual.size());
        assertEqualsQueryVersionDoAndDtoCollection(expected, actual);
    }

    @Test
    @MetamacMock(QUERY_VERSION_01_WITH_SELECTION_NAME)
    public void testQueryVersionDoToQueryRelatedResourceDto() throws MetamacException {
        QueryVersion expected = queryVersionMockFactory.retrieveMock(QUERY_VERSION_01_WITH_SELECTION_NAME);
        RelatedResourceDto actual = queryDo2DtoMapper.queryVersionDoToQueryRelatedResourceDto(expected);
        assertEqualsQuery(expected, actual);
    }
}
