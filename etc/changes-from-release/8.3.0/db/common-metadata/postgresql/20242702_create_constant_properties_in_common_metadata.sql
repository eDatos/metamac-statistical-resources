-- ---------------------------------------------------------------------------------------------------
-- EDATOS-4385 Permitir cumplimentar metadatos en bloque en los datasets
    -- "metamac.statistical_resources.web.max_permitted_datasets_to_update"
-- ---------------------------------------------------------------------------------------------------


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.statistical_resources.web.max_permitted_datasets_to_update','50',false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;

