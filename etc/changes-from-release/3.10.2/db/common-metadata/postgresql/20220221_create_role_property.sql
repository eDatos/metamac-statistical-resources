-- ---------------------------------------------------------------------------------------------------
-- EDATOS-3555 - Creación de vistas en el data de statistical-resources
-- ---------------------------------------------------------------------------------------------------

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.statistical_resources.bbbd.data_views_role',(select conf_value from tb_data_configurations where conf_key = 'indicators.bbbd.data_views_role'),false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;