-- ---------------------------------------------------------------------------------------------------
-- EDATOS-3460 - Publicador. Reenviar todo el histórico de datasets al ckan
    -- "metamac.statistical_resources.resend_dataset_kafka_message.cron_expression
	--Programar para que se ejecute una sóla vez con la expresión '0 30 20 29 AUG ? 2023' que significa: 29 de Agosto de 2023 a las 20:30
-- ---------------------------------------------------------------------------------------------------

--ATENCIÓN!!! Programar el job para que se ejecute una sola vez. !!!!!!!

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.statistical_resources.resend_dataset_kafka_message.cron_expression','0 30 20 29 AUG ? 2023',false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;

--Ejemplo:
-- 0 30 20 29 AUG ? 2023      -- 29 de Agosto de 2023 a las 20:30