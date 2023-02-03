package org.siemac.metamac.statistical.resources.core.dataset.repositoryimpl;

import static org.junit.Assert.assertNull;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_27_WITH_COVERAGE_FILLED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_47_WITH_COVERAGE_FILLED_WITH_TITLES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks.mockCodeDimension;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks.mockCodeDimensionsWithIdentifiers;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimensionRepository;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/statistical-resources/include/rest-services-mockito.xml", "classpath:spring/statistical-resources/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class CodeDimensionRepositoryTest extends StatisticalResourcesBaseTest implements CodeDimensionRepositoryTestBase {

    @Autowired
    protected CodeDimensionRepository codeDimensionRepository;

    @Override
    @Test
    @MetamacMock(DATASET_VERSION_27_WITH_COVERAGE_FILLED_NAME)
    public void testFindCodesForDatasetVersionByDimensionId() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_27_WITH_COVERAGE_FILLED_NAME);
        {
            List<CodeDimension> codeDimensions = codeDimensionRepository.findCodesForDatasetVersionByDimensionId(datasetVersion.getId(), "dim-none", null);
            Assert.assertEquals(0, codeDimensions.size());
        }
        {
            List<CodeDimension> codeDimensions = codeDimensionRepository.findCodesForDatasetVersionByDimensionId(datasetVersion.getId(), "dim1", null);
            DatasetsAsserts.assertEqualsCodeDimensionsCollection(mockCodeDimensionsWithIdentifiers(datasetVersion, "dim1", "code-d1-1", "code-d1-2"), codeDimensions);
        }
        {
            List<CodeDimension> codeDimensions = codeDimensionRepository.findCodesForDatasetVersionByDimensionId(datasetVersion.getId(), "dim2", null);
            DatasetsAsserts.assertEqualsCodeDimensionsCollection(mockCodeDimensionsWithIdentifiers(datasetVersion, "dim2", "code-d2-1", "code-d2-2"), codeDimensions);
        }
        {
            List<CodeDimension> codeDimensions = codeDimensionRepository.findCodesForDatasetVersionByDimensionId(datasetVersion.getId(), "dim3", null);
            DatasetsAsserts.assertEqualsCodeDimensionsCollection(mockCodeDimensionsWithIdentifiers(datasetVersion, "dim3", "code-d3-1"), codeDimensions);
        }
    }

    @Test
    @Override
    @MetamacMock(DATASET_VERSION_47_WITH_COVERAGE_FILLED_WITH_TITLES_NAME)
    public void testFindTitleForDatasetVersionByDimensionId() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_47_WITH_COVERAGE_FILLED_WITH_TITLES_NAME);
        CodeDimension codeDimensionMock = mockCodeDimension(datasetVersion, "TIME_PERIOD","2010-M02", "Febrero 2010");
        CodeDimension codeDimension = codeDimensionRepository.findTitleForDatasetVersionByDimensionId(datasetVersion.getId(), "TIME_PERIOD", "2010-M01");
        assertNull(codeDimension.getTitle());
        Assert.assertEquals(codeDimensionMock.getTitle(),codeDimension.getTitle());
    }

    @Test
    @MetamacMock(DATASET_VERSION_27_WITH_COVERAGE_FILLED_NAME)
    public void testFindCodesForDatasetVersionByDimensionIdNotExist() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_27_WITH_COVERAGE_FILLED_NAME);

        List<CodeDimension> codeDimensions = codeDimensionRepository.findCodesForDatasetVersionByDimensionId(datasetVersion.getId(), "dim-none", null);
        Assert.assertEquals(0, codeDimensions.size());
    }

    @Test
    @MetamacMock(DATASET_VERSION_47_WITH_COVERAGE_FILLED_WITH_TITLES_NAME)
    public void testFindCodesForDatasetVersionByDimensionIdWithFilters() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_47_WITH_COVERAGE_FILLED_WITH_TITLES_NAME);
        {
            List<CodeDimension> codeDimensions = codeDimensionRepository.findCodesForDatasetVersionByDimensionId(datasetVersion.getId(), "TIME_PERIOD", null);
            DatasetsAsserts.assertEqualsCodeDimensionsCollection(CodeDimensionsMockBuilder.prepare(datasetVersion, "TIME_PERIOD").addCode("2011").addCode("2010").addCode("2010-M02", "Febrero 2010")
                    .addCode("2010-M01", "Enero 2010").build(), codeDimensions);
        }
        {
            List<CodeDimension> codeDimensions = codeDimensionRepository.findCodesForDatasetVersionByDimensionId(datasetVersion.getId(), "TIME_PERIOD", "2010");
            DatasetsAsserts.assertEqualsCodeDimensionsCollection(
                    CodeDimensionsMockBuilder.prepare(datasetVersion, "TIME_PERIOD").addCode("2010").addCode("2010-M02", "Febrero 2010").addCode("2010-M01", "Enero 2010").build(), codeDimensions);
        }
        {
            List<CodeDimension> codeDimensions = codeDimensionRepository.findCodesForDatasetVersionByDimensionId(datasetVersion.getId(), "TIME_PERIOD", "Enero");
            DatasetsAsserts.assertEqualsCodeDimensionsCollection(CodeDimensionsMockBuilder.prepare(datasetVersion, "TIME_PERIOD").addCode("2010-M01", "Enero 2010").build(), codeDimensions);
        }
        {
            List<CodeDimension> codeDimensions = codeDimensionRepository.findCodesForDatasetVersionByDimensionId(datasetVersion.getId(), "GEO_DIM", "");
            Assert.assertEquals(4, codeDimensions.size());

            DatasetsAsserts.assertEqualsCodeDimensionsCollection(
                    CodeDimensionsMockBuilder.prepare(datasetVersion, "GEO_DIM").addCode("ES", "España").addCode("ES61", "Andalucia").addCode("ES70", "Canarias").addCode("ES45", "Cataluña").build(),
                    codeDimensions);
        }
    }

    private static class CodeDimensionsMockBuilder {

        private final DatasetVersion      datasetVersion;
        private final String              dsdComponentId;
        private final List<CodeDimension> codes;

        private CodeDimensionsMockBuilder(DatasetVersion datasetVersion, String dsdComponentId) {
            this.datasetVersion = datasetVersion;
            this.dsdComponentId = dsdComponentId;
            this.codes = new ArrayList<CodeDimension>();
        }

        public static CodeDimensionsMockBuilder prepare(DatasetVersion datasetVersion, String dsdComponentId) {
            return new CodeDimensionsMockBuilder(datasetVersion, dsdComponentId);
        }

        public CodeDimensionsMockBuilder addCode(String identifier, String title) {
            codes.add(mockCodeDimension(datasetVersion, dsdComponentId, identifier, title));
            return this;
        }

        public CodeDimensionsMockBuilder addCode(String identifier) {
            codes.add(mockCodeDimension(datasetVersion, dsdComponentId, identifier, identifier));
            return this;
        }

        public List<CodeDimension> build() {
            return codes;
        }
    }

}
