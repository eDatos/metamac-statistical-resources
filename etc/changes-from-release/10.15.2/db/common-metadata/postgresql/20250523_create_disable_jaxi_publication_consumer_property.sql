-- ---------------------------------------------------------------------------------------------------
-- EDATOS-4447 - EDATOS-4760 -e-Catalogo debe permitir alimentarse desde las tablas publicadas en Alfresco/JAXI
-- Añadir parámetro para deshabilitar el consumidor de eTerritorios a conveniencia. 
-- En principio se usará durante la carga masiva en e-Catalogo de todos los datasets de jaxi. 
-- Esta carga masiva envía aprox. más de 20000 datasets a eCatalogo. Pero también son tratados por la funcionalidad para eTerritorios del statistical-resources
-- Este tratamiento es innecesario ya que la caché debe estar correctamente creada. Por lo que se puede deshabilitar durante este proceso y volverlo a habilitar cuando termine.

-- ---------------------------------------------------------------------------------------------------

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.statistical_resources.kafka.jaxi_publication_consumer_disabled', false,false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

--Nota. Para el istac e iestadis no tiene sentido por lo que crear poniendo el valor True

/* ISTAC, IESTADIS, DEMO 
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.statistical_resources.kafka.jaxi_publication_consumer_disabled', true,false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


*/

commit;