-- ---------------------------------------------------------------------------------------------------
-- EDATOS-4562 Statistical-Resources - Llamadas internas a apis internas con autenticación a través de API key
    -- "metamac.statistical_resources.rest.api_key"
-- ---------------------------------------------------------------------------------------------------

--ATENCIÓN!! Poner api-key correspondiente a cada entorno para esta app.
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,FILL_ME,false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;

