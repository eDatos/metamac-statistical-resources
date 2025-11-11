package org.siemac.metamac.statistical.resources.core.stream.messages.mappers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.NameableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.stream.messages.NameableStatisticalResourceAvro;

public class NameableStatisticalResourceAvroMapperTest {
    @Mock
    private static ConfigurationService configurationService;

    @Before
    public void setUp() throws MetamacException {
        MockitoAnnotations.initMocks(this);
        AvroMapperUtils.configurationService = configurationService;
        try {
            when(configurationService.retrieveStatisticalResourcesInternalApiUrlBase()).thenReturn(MappersMockUtils.EXPECTED_API_BASE);
            when(configurationService.retrieveSrmExternalApiUrlBase()).thenReturn(MappersMockUtils.EXPECTED_API_BASE);
            when(configurationService.retrieveStatisticalOperationsExternalApiUrlBase()).thenReturn(MappersMockUtils.EXPECTED_API_BASE);
        } catch (MetamacException e) {
        }
    }

    @Test
    public void testNameableStatisticalResourceDo2Avro() {
        NameableStatisticalResourceAvro expected = MappersMockUtils.mockNameableStatisticalResourceAvro();
        NameableStatisticalResource source = MappersMockUtils.mockNameableStatisticalResource();

        NameableStatisticalResourceAvro actual = NameableStatisticalResourceDo2AvroMapper.do2Avro(source);

        assertThat(actual, is(equalTo(expected)));
    }

}
