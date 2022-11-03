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
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.serviceapi.TranslationService;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetRepository;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.stream.messages.SiemacMetadataStatisticalResourceAvro;

public class SiemacMetadataStatisticalResourceAvroMapperTest {

    @Mock
    protected DatasetRepository        datasetRepository;

    @Mock
    protected DatasetVersionRepository datasetVersionRepository;

    @Mock
    protected ConfigurationService     configurationService;
    
    @Mock
    private static TranslationService     translationService;

    @Before
    public void setUp() throws MetamacException {
        MockitoAnnotations.initMocks(this);
        AvroMapperUtils.datasetRepository = datasetRepository;
        AvroMapperUtils.datasetVersionRepository = datasetVersionRepository;
        AvroMapperUtils.configurationService = configurationService;
        AvroMapperUtils.translationService = translationService;
        try {
            when(configurationService.retrieveStatisticalResourcesInternalApiUrlBase()).thenReturn(MappersMockUtils.EXPECTED_API_BASE);
            when(translationService.retrieveTimeTranslation(Mockito.any(), Mockito.anyString())).thenReturn(MappersMockUtils.mockMapTranslateDateSdmx());
        } catch (MetamacException e) {
        }
    }

    @Test
    public void testDo2Avro() throws MetamacException {
        when(datasetVersionRepository.retrieveByUrn(MappersMockUtils.EXPECTED_URN)).thenReturn(MappersMockUtils.mockDatasetVersion());

        SiemacMetadataStatisticalResourceAvro expected = MappersMockUtils.mockSiemacMetadataStatisticalResourceAvro(TypeRelatedResourceEnum.DATASET_VERSION);
        SiemacMetadataStatisticalResource source = MappersMockUtils.mockSiemacMetadataStatisticalResource(TypeRelatedResourceEnum.DATASET_VERSION);

        SiemacMetadataStatisticalResourceAvro actual = SiemacMetadataStatisticalResourceDo2AvroMapper.do2Avro(source);

        assertThat(actual, is(equalTo(expected)));

    }

}
