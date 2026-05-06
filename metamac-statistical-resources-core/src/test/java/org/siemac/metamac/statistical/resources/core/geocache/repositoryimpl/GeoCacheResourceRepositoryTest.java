package org.siemac.metamac.statistical.resources.core.geocache.repositoryimpl;

import static org.junit.Assert.fail;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.GeoCacheResourceMockFactory.GEO_COV_VAR_ELEMENT_CACHE_01;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.GeoCacheResourceMockFactory.GEO_COV_VAR_ELEMENT_CACHE_02;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.GeoCacheResourceMockFactory.GEO_COV_VAR_ELEMENT_CACHE_NOT_ACTIVATED_03;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.GeoCacheResourceMockFactory.GEO_COV_VAR_ELEMENT_CACHE_QUERY_RESOURCE_01;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResourceRepository;
import org.siemac.metamac.statistical.resources.core.utils.asserts.CommonAsserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/statistical-resources/include/rest-services-mockito.xml", "classpath:spring/statistical-resources/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class GeoCacheResourceRepositoryTest extends StatisticalResourcesBaseTest implements GeoCacheResourceRepositoryTestBase {

    @Autowired
    protected GeoCacheResourceRepository geoCacheResourceRepository;

    @Test
    @MetamacMock(GEO_COV_VAR_ELEMENT_CACHE_01)
    public void testRetrieveByResourceVersionUrn() throws Exception {
        GeoCacheResource actual = geoCacheResourceFactory.retrieveMock(GEO_COV_VAR_ELEMENT_CACHE_01);

        GeoCacheResource expected = geoCacheResourceRepository.retrieveByResourceVersionUrn(actual.getUrn());
        assertEqualsGeoCovVarElementCacheDatasetVersion(expected, actual);
        Assert.assertEquals(StatisticalResourceTypeEnum.DATASET.getName(), expected.getType());

    }

    @Test
    @MetamacMock(GEO_COV_VAR_ELEMENT_CACHE_QUERY_RESOURCE_01)
    public void testRetrieveByQueryResourceVersionUrn() throws Exception {
        GeoCacheResource actual = geoCacheResourceFactory.retrieveMock(GEO_COV_VAR_ELEMENT_CACHE_QUERY_RESOURCE_01);

        GeoCacheResource expected = geoCacheResourceRepository.retrieveByResourceVersionUrn(actual.getUrn());
        assertEqualsGeoCovVarElementCacheDatasetVersion(expected, actual);
        Assert.assertEquals(StatisticalResourceTypeEnum.QUERY.getName(), expected.getType());
    }

    @Test
    @MetamacMock(GEO_COV_VAR_ELEMENT_CACHE_02)
    public void testDisabledByResourceVersionUrn() throws Exception {
        GeoCacheResource actual = geoCacheResourceFactory.retrieveMock(GEO_COV_VAR_ELEMENT_CACHE_02);
        GeoCacheResource expected = geoCacheResourceRepository.retrieveByResourceVersionUrn(actual.getUrn());
        Assert.assertNotNull(expected);
        geoCacheResourceRepository.disabledByResourceVersionUrn(actual.getUrn());
        expectedMetamacException(new MetamacException(ServiceExceptionType.GEO_CACHE_RESOURCE_NOT_FOUND, actual.getUrn()));
        expected = geoCacheResourceRepository.retrieveByResourceVersionUrn(actual.getUrn());
    }

    @Test
    @MetamacMock(GEO_COV_VAR_ELEMENT_CACHE_NOT_ACTIVATED_03)
    public void testDeleteAll() throws Exception {
        GeoCacheResource actual = geoCacheResourceFactory.retrieveMock(GEO_COV_VAR_ELEMENT_CACHE_NOT_ACTIVATED_03);
        GeoCacheResource expected = geoCacheResourceRepository.retrieveByResourceVersionUrn(actual.getUrn(), false);
        Assert.assertNotNull(expected);

        geoCacheResourceRepository.deleteAll();

        // get cache resource activated or not to probe it was physically deleted
        expectedMetamacException(new MetamacException(ServiceExceptionType.GEO_CACHE_RESOURCE_NOT_FOUND, actual.getUrn()));
        expected = geoCacheResourceRepository.retrieveByResourceVersionUrn(actual.getUrn(), false);

    }

    private static void assertEqualsGeoCovVarElementCacheDatasetVersion(GeoCacheResource expected, GeoCacheResource actual) throws MetamacException {
        if ((expected != null && actual == null) || (expected == null && actual != null)) {
            fail("The expected GeoCacheResource cache Item and the actual are not equals");
        } else if (expected != null && actual != null) {
            assertEquals(expected, actual);
        }
    }

    private static void assertEquals(GeoCacheResource expected, GeoCacheResource actual) throws MetamacException {
        CommonAsserts.assertEqualsExternalItem(expected.getTerritories().get(0), actual.getTerritories().get(0));
        CommonAsserts.assertEqualsInternationalString(expected.getTitle(), actual.getTitle());
        Assert.assertEquals(expected.getCode(), actual.getCode());
        Assert.assertEquals(expected.getOperationUrn(), actual.getOperationUrn());
        Assert.assertEquals(expected.getHtmlLink(), actual.getHtmlLink());
        Assert.assertEquals(expected.getType(), actual.getType());

    }
}
