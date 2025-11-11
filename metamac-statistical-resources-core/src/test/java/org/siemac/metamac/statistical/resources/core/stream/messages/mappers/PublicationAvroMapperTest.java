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
import org.siemac.metamac.statistical.resources.core.publication.domain.Publication;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersionRepository;
import org.siemac.metamac.statistical.resources.core.stream.messages.PublicationAvro;

public class PublicationAvroMapperTest {

    @Mock
    protected PublicationVersionRepository publicationVersionRepository;

    @Mock
    protected ConfigurationService         configurationService;

    @Before
    public void setUp() throws MetamacException {
        MockitoAnnotations.initMocks(this);
        AvroMapperUtils.publicationVersionRepository = publicationVersionRepository;
        AvroMapperUtils.configurationService = configurationService;
        try {
            when(configurationService.retrieveStatisticalResourcesInternalApiUrlBase()).thenReturn(MappersMockUtils.EXPECTED_API_BASE);
            when(configurationService.retrieveSrmExternalApiUrlBase()).thenReturn(MappersMockUtils.EXPECTED_API_BASE);
            when(configurationService.retrieveStatisticalOperationsExternalApiUrlBase()).thenReturn(MappersMockUtils.EXPECTED_API_BASE);
        } catch (MetamacException e) {
        }
    }

    @Test
    public void testDo2Avro() throws Exception {
        PublicationAvro expected = MappersMockUtils.mockPublicationAvro();
        Publication source = MappersMockUtils.mockPublication(true);

        PublicationAvro actual = PublicationDo2AvroMapper.do2Avro(source);

        assertThat(actual, is(equalTo(expected)));
    }

}
