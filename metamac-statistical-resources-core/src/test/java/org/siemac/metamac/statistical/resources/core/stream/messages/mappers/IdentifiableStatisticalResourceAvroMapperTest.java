package org.siemac.metamac.statistical.resources.core.stream.messages.mappers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.stream.messages.IdentifiableStatisticalResourceAvro;

public class IdentifiableStatisticalResourceAvroMapperTest {

    @Test
    public void testDo2Avro() {
        IdentifiableStatisticalResourceAvro expected = MappersMockUtils.mockIdentifiableStatisticalResourceAvro();
        IdentifiableStatisticalResource source = MappersMockUtils.mockIdentifiableStatisticalResource();

        IdentifiableStatisticalResourceAvro actual = IdentifiableStatisticalResourceDo2AvroMapper.do2Avro(source);

        assertThat(actual.getCode(), is(equalTo(expected.getCode())));
        assertThat(actual.getUrn(), is(equalTo(expected.getUrn())));
    }

}
