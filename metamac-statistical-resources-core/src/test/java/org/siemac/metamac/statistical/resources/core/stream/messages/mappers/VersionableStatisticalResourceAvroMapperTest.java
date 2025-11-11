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
import org.siemac.metamac.statistical.resources.core.base.domain.VersionableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.serviceapi.TranslationService;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.stream.messages.VersionableStatisticalResourceAvro;

public class VersionableStatisticalResourceAvroMapperTest {

    @Mock
    private static DatasetVersionRepository datasetVersionRepository;

    @Mock
    private static ConfigurationService     configurationService;
    
    @Mock
    private static TranslationService     translationService;

    @Before
    public void setUp() throws MetamacException {
        MockitoAnnotations.initMocks(this);
        AvroMapperUtils.configurationService = configurationService;
        AvroMapperUtils.datasetVersionRepository = datasetVersionRepository;
        AvroMapperUtils.translationService = translationService;
        try {
            when(configurationService.retrieveStatisticalResourcesInternalApiUrlBase()).thenReturn(MappersMockUtils.EXPECTED_API_BASE);
            when(translationService.retrieveTimeTranslation(Mockito.any(), Mockito.anyString())).thenReturn(MappersMockUtils.mockMapTranslateDateSdmx());
            when(configurationService.retrieveSrmExternalApiUrlBase()).thenReturn(MappersMockUtils.EXPECTED_API_BASE);
            when(configurationService.retrieveStatisticalOperationsExternalApiUrlBase()).thenReturn(MappersMockUtils.EXPECTED_API_BASE);
        } catch (MetamacException e) {
        }
    }
    
    @Test
    public void testVersionableStatisticalResourceDo2Avro() throws Exception {
        VersionableStatisticalResourceAvro expected = MappersMockUtils.mockVersionableStatisticalResourceAvro();
        VersionableStatisticalResource source = MappersMockUtils.mockVersionableStatisticalResource();

        VersionableStatisticalResourceAvro actual = VersionableStatisticalResourceDo2AvroMapper.do2Avro(source);

        assertThat(expected, is(equalTo(actual)));
    }

}
