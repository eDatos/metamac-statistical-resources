package org.siemac.metamac.statistical.resources.core.geocache.repositoryimpl;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class GeoCacheResourceRepositoryTest extends StatisticalResourcesBaseTest implements GeoCacheResourceRepositoryTestBase {

    @Autowired
    protected GeoCacheResourceRepository geoCacheResourceRepository;

    // TODO EDATOS-4587 VER ESTOS TESTS
    /*
     * @Test
     * @MetamacMock(GEO_COV_VAR_ELEMENT_CACHE_01)
     * public void testRetrieveByDatasetVersionUrn() throws Exception {
     * GeoCovVarElementCacheDatasetVersion actual = geoCacheResourceRepository.retrieveMock(GEO_COV_VAR_ELEMENT_CACHE_01);
     * List<GeoCovVarElementCacheDatasetVersion> expected = geoCovVarElementCacheDatasetVersionRepository.retrieveByDatasetVersionUrn(actual.getUrn());
     * assertEqualsGeoCovVarElementCacheDatasetVersion(expected.get(0), actual);
     * }
     * @Test
     * @MetamacMock(GEO_COV_VAR_ELEMENT_CACHE_02)
     * public void testDisabledByDatasetVersionUrn() throws Exception {
     * GeoCovVarElementCacheDatasetVersion actual = geoCacheResourceRepository.retrieveMock(GEO_COV_VAR_ELEMENT_CACHE_02);
     * List<GeoCovVarElementCacheDatasetVersion> expected = geoCovVarElementCacheDatasetVersionRepository.retrieveByDatasetVersionUrn(actual.getUrn());
     * Assert.assertNotNull(expected);
     * Assert.assertEquals(expected.size(), 1);
     * geoCovVarElementCacheDatasetVersionRepository.disabledByDatasetVersionUrn(actual.getUrn());
     * expected = geoCovVarElementCacheDatasetVersionRepository.retrieveByDatasetVersionUrn(actual.getUrn());
     * Assert.assertEquals(expected.size(), 0);
     * }
     * @Test
     * @MetamacMock(GEO_COV_VAR_ELEMENT_CACHE_02)
     * public void testDeleteAll() throws Exception {
     * GeoCovVarElementCacheDatasetVersion actual = geoCacheResourceRepository.retrieveMock(GEO_COV_VAR_ELEMENT_CACHE_02);
     * List<GeoCovVarElementCacheDatasetVersion> expected = geoCovVarElementCacheDatasetVersionRepository.retrieveByDatasetVersionUrn(actual.getUrn());
     * Assert.assertNotNull(expected);
     * Assert.assertEquals(expected.size(), 1);
     * expected.get(0).setIsActivated(false);
     * geoCovVarElementCacheDatasetVersionRepository.save(expected.get(0));
     * geoCovVarElementCacheDatasetVersionRepository.deleteAll();
     * expected = geoCovVarElementCacheDatasetVersionRepository.retrieveByDatasetVersionUrn(actual.getUrn());
     * Assert.assertEquals(expected.size(), 0);
     * }
     * private static void assertEqualsGeoCovVarElementCacheDatasetVersion(GeoCovVarElementCacheDatasetVersion expected, GeoCovVarElementCacheDatasetVersion actual) throws MetamacException {
     * if ((expected != null && actual == null) || (expected == null && actual != null)) {
     * fail("The expected GeoCovVarElementCacheDatasetVersion cache Item and the actual are not equals");
     * } else if (expected != null && actual != null) {
     * assertEquals(expected, actual);
     * }
     * }
     * private static void assertEquals(GeoCovVarElementCacheDatasetVersion expected, GeoCovVarElementCacheDatasetVersion actual) throws MetamacException {
     * CommonAsserts.assertEqualsExternalItem(expected.getVariableElement(), actual.getVariableElement());
     * CommonAsserts.assertEqualsInternationalString(expected.getTitle(), actual.getTitle());
     * CommonAsserts.assertEqualsInternationalString(expected.getOperationTitle(), actual.getOperationTitle());
     * Assert.assertEquals(expected.getCode(), actual.getCode());
     * Assert.assertEquals(expected.getOperationUrn(), actual.getOperationUrn());
     * Assert.assertEquals(expected.getHtmlLink(), actual.getHtmlLink());
     * }
     */
    @Test
    public void testDeleteAll() throws Exception {
        // TODO Auto-generated method stub
        fail("testDeleteAll not implemented");
    }

    @Test
    public void testDisabledByResourceVersionUrn() throws Exception {
        // TODO Auto-generated method stub
        fail("testDisabledByResourceVersionUrn not implemented");
    }

    @Test
    public void testRetrieveByResourceVersionUrn() throws Exception {
        // TODO Auto-generated method stub
        fail("testRetrieveByResourceVersionUrn not implemented");
    }
}
