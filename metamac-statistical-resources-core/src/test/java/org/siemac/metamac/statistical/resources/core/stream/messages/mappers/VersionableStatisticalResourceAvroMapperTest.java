package org.siemac.metamac.statistical.resources.core.stream.messages.mappers;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.common.serviceapi.TranslationService;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;

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
        } catch (MetamacException e) {
        }
    }
    /* TODO EDATOS-3744
    @Test
    public void testVersionableStatisticalResourceDo2Avro() throws Exception {
        VersionableStatisticalResourceAvro expected = MappersMockUtils.mockVersionableStatisticalResourceAvro();
        VersionableStatisticalResource source = MappersMockUtils.mockVersionableStatisticalResource();

        VersionableStatisticalResourceAvro actual = VersionableStatisticalResourceDo2AvroMapper.do2Avro(source);

        assertThat(expected, is(equalTo(actual)));
    }
    */

}
