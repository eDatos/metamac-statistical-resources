-- ---------------------------------------------------------------------------------------------------

-- ---------------------------------------------------------------------------------------------------

-- ---------------------------------------------------------------------------------------------------
-- EDATOS-3770 - Se crea entrada en common_metadata

-- Relacionada con : EDATOS-3869 - que también lee de este parámetro en external-users
-- ---------------------------------------------------------------------------------------------------

--ATENCIÓN, ESTA PROPIEDAD PUEDE NO EXISTIR EN TODOS LOS ENTORNOS, EN CUYO CASO DEJAR VACÍO.

 INSERT INTO TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,'metamac.kafka.topic.external_datasets_publications','JAXI_PUBLICATIONS',false);
 UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';
 
 COMMIT;

 