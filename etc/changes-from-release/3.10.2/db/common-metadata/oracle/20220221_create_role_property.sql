-- ---------------------------------------------------------------------------------------------------
-- EDATOS-3555 - Creación de vistas en el data de statistical-resources
-- ---------------------------------------------------------------------------------------------------
-- NOTA IMPORTANTE: El valor de la propiedad metamac.statistical_resources.bbbd.data_views_role que
-- se da de alta en este script será el mismo que tenga la propiedad indicators.bbbd.data_views_role
-- en los diversos entornos en los que se instale la tarea
-- ---------------------------------------------------------------------------------------------------

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,1,'metamac.statistical_resources.bbbd.data_views_role',(select conf_value from tb_data_configurations where conf_key = 'indicators.bbbd.data_views_role'));
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;