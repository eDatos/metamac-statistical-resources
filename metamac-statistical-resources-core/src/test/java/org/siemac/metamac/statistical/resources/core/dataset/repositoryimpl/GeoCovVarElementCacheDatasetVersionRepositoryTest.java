package org.siemac.metamac.statistical.resources.core.dataset.repositoryimpl;

import static org.junit.Assert.fail;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.GeoCovVarElementCacheDatasetVersionFactory.GEO_COV_VAR_ELEMENT_CACHE_01;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.GeoCovVarElementCacheDatasetVersionFactory.GEO_COV_VAR_ELEMENT_CACHE_02;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.dataset.domain.GeoCovVarElementCacheDatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.GeoCovVarElementCacheDatasetVersionRepository;
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
public class GeoCovVarElementCacheDatasetVersionRepositoryTest extends StatisticalResourcesBaseTest implements GeoCovVarElementCacheDatasetVersionRepositoryTestBase {
    @Autowired
    private GeoCovVarElementCacheDatasetVersionRepository         geoCovVarElementCacheDatasetVersionRepository;
    
    @Test
    @MetamacMock(GEO_COV_VAR_ELEMENT_CACHE_01)
    public void testRetrieveByDatasetVersionUrn() throws Exception {
        GeoCovVarElementCacheDatasetVersion actual = geoCovVarElementCacheDatasetVersionFactory.retrieveMock(GEO_COV_VAR_ELEMENT_CACHE_01);
 
        List<GeoCovVarElementCacheDatasetVersion> expected = geoCovVarElementCacheDatasetVersionRepository.retrieveByDatasetVersionUrn(actual.getUrn());

        assertEqualsGeoCovVarElementCacheDatasetVersion(expected.get(0), actual);    
        
    }
        
    @Test
    @MetamacMock(GEO_COV_VAR_ELEMENT_CACHE_02)
    public void testDeleteAllByDatasetVersionUrn() throws Exception {
        GeoCovVarElementCacheDatasetVersion actual = geoCovVarElementCacheDatasetVersionFactory.retrieveMock(GEO_COV_VAR_ELEMENT_CACHE_02);
        
        List<GeoCovVarElementCacheDatasetVersion> expected = geoCovVarElementCacheDatasetVersionRepository.retrieveByDatasetVersionUrn(actual.getUrn());
        
        Assert.assertNotNull(expected);
        Assert.assertEquals(expected.size(), 1);
        
        geoCovVarElementCacheDatasetVersionRepository.deleteAllByDatasetVersionUrn(actual.getUrn());
        
        expected = geoCovVarElementCacheDatasetVersionRepository.retrieveByDatasetVersionUrn(actual.getUrn());
        
        Assert.assertEquals(expected.size(), 0);

    }
    
    private static void assertEqualsGeoCovVarElementCacheDatasetVersion(GeoCovVarElementCacheDatasetVersion expected, GeoCovVarElementCacheDatasetVersion actual) throws MetamacException {
        if ((expected != null && actual == null) || (expected == null && actual != null)) {
            fail("The expected GeoCovVarElementCacheDatasetVersion cache Item and the actual are not equals");
        } else if (expected != null && actual != null) {
            assertEquals(expected, actual);
        }
    }
    
    private static void assertEquals(GeoCovVarElementCacheDatasetVersion expected, GeoCovVarElementCacheDatasetVersion actual) throws MetamacException {
        CommonAsserts.assertEqualsExternalItem(expected.getVariableElement(), actual.getVariableElement());
        CommonAsserts.assertEqualsInternationalString(expected.getTitle(), actual.getTitle());
        CommonAsserts.assertEqualsInternationalString(expected.getOperationTitle(), actual.getOperationTitle());
        Assert.assertEquals(expected.getCode(), actual.getCode());
        Assert.assertEquals(expected.getOperationUrn(), actual.getOperationUrn());
        Assert.assertEquals(expected.getHtmlLink(), actual.getHtmlLink());
    }
}
