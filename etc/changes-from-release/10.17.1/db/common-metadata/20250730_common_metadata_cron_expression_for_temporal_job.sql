-- --------------------------------------------------------------------------------------------------
-- EDATOS-5154 - Añadir descripciones a las vistas de datos
-- 
-- Añadir fórmula cron para ejecutar el job temporal que carga las tablas con la nueva estructura en statistical resources data

-- --------------------------------------------------------------------------------------------------

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.statistical_resources.data_view_adjustment.cron_expression','0 30 20 29 AUG ? 2024',false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;
 
