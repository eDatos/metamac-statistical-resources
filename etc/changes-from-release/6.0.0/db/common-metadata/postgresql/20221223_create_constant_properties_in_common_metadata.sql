-- ---------------------------------------------------------------------------------------------------
-- EDATOS-3770 - Se crea entrada en common_metadata
    -- metamac.statistical_resources.geografic_coverage_cache_clear.cron_expression
-- ---------------------------------------------------------------------------------------------------

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.statistical_resources.geografic_coverage_cache_clear.cron_expression','0 0 17 * * ?',false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;


-- 0 0 17 * * ? At 17:00:00pm every day