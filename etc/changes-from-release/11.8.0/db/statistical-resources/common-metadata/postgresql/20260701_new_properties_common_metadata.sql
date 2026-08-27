-- ---------------------------------------------------------------------------------------------------
-- EDATOS-5739 - Añadir campo lastUpdate a consultas e indicadores

-- Añadir constante para el retardo en ejecución del job de recovery de datos en tb_tasks tras reinicio
-- ---------------------------------------------------------------------------------------------------

-- Por defecto se pone a 30 minutos = 1800 segundos
 INSERT INTO TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,'metamac.statistical_resources.update_dates.quartz_scheduler','1800',false);
 UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

 COMMIT;
