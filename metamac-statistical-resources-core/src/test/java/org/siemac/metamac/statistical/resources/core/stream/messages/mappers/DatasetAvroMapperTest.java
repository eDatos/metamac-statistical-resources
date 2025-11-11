package org.siemac.metamac.statistical.resources.core.stream.messages.mappers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetRepository;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DimensionRepresentationMapping;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetAvro;
import org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts;

public class DatasetAvroMapperTest {

    @Mock
    private static DatasetVersionRepository datasetVersionRepository;

    @Mock
    private static DatasetRepository        datasetRepository;

    @Mock
    private static ConfigurationService     configurationService;

    @Before
    public void setUp() throws MetamacException {
        MockitoAnnotations.initMocks(this);
        AvroMapperUtils.datasetVersionRepository = datasetVersionRepository;
        AvroMapperUtils.datasetRepository = datasetRepository;
        AvroMapperUtils.configurationService = configurationService;
        try {
            when(configurationService.retrieveStatisticalResourcesInternalApiUrlBase()).thenReturn(MappersMockUtils.EXPECTED_API_BASE);
            when(configurationService.retrieveSrmExternalApiUrlBase()).thenReturn(MappersMockUtils.EXPECTED_API_BASE);
            when(configurationService.retrieveStatisticalOperationsExternalApiUrlBase()).thenReturn(MappersMockUtils.EXPECTED_API_BASE);
        } catch (MetamacException e) {
        }
    }

    @Test
    public void testDo2Avro() {
        DatasetAvro expected = MappersMockUtils.mockDatasetAvro(3);
        Dataset source = MappersMockUtils.mockDataset();

        DatasetAvro actual = DatasetDo2AvroMapper.do2Avro(source);

        assertThat(actual, is(equalTo(expected)));
    }

    protected void assertEqualVersionList(Dataset expected, Dataset actual) throws MetamacException {
        DatasetsAsserts.assertEqualsDatasetVersionCollection(expected.getVersions(), actual.getVersions());
    }

    protected void assertEqualDimensionRepresentationMapping(Dataset expected, Dataset actual) throws MetamacException {
        int i = 0;
        for (DimensionRepresentationMapping expectedDimension : expected.getDimensionRepresentationMappings()) {
            DatasetsAsserts.assertEqualsDimensionRepresentationMapping(expectedDimension, actual.getDimensionRepresentationMappings().get(i));
            i++;
        }
    }

}
