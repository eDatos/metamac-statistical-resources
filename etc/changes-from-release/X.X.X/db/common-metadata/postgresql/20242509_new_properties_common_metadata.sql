-- ---------------------------------------------------------------------------------------------------
-- EDATOS-4587 - Se crea entrada en common_metadata

-- Relacionada con : nombre del topic para colecciones que provienen de jaxi
-- ---------------------------------------------------------------------------------------------------


--1) Se configura a 3 horas -> 10800 segundos y se irá viendo según necesidades
 INSERT INTO TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,'metamac.statistical_resources.geo_cache_update.quartz_scheduler','10800',false); 
 UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

--2) Job de recuperación de entradas de caché al arrancar el servidor. Por defecto se pone a 2a minutos = 1200 segundos  para dejar que el servidor arranque y luego trate las entradas que hayan podido quedar pendientes
 INSERT INTO TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,'metamac.statistical_resources.geo_cache_recovery.quartz_scheduler','1200',false); 
 UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';



--2) topic de kafka para colecciones jaxi
--ATENCIÓN, ESTA PROPIEDAD PUEDE NO SER NECESARIA EN TODOS LOS ENTORNOS, EN CUYO CASO DEJAR VACÍO.

 INSERT INTO TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,'metamac.kafka.topic.external_collections_publications','FILL_ME',false);
 UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

/*
 -- PARA ISTAC E IESTADIS
  INSERT INTO TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,'metamac.kafka.topic.external_collections_publications','',false);
 UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';
 
 */


-- SÓLO PARA ENTORNO IBESTAT Y DEMO
/*
 INSERT INTO TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,'metamac.kafka.topic.external_collections_publications','JAXI_COLLECTIONS_PUBLICATIONS',false);
 UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';
 */

 COMMIT;

 