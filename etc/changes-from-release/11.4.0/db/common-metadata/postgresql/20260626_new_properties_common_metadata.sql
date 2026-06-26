-- ---------------------------------------------------------------------------------------------------
-- EDATOS-5233 Migración de estructura de datos con estructura previa a la i18n en CKAN

-- Se actualizan entradas de actualización.
-- ---------------------------------------------------------------------------------------------------

-- !! ATENCIÓN SÓLO EN ENTORNOS ISTAC PRE-ISTAC Y PRO-ISTAC.
-- EN DEMO TAMBIÉN SE PODRÍA LANZAR SI NO SE HACEN PRUEBAS DE JAXI EN ESE ENTORNO.
UPDATE
TB_DATA_CONFIGURATIONS
set conf_value = null
where conf_key = 'metamac.kafka.topic.external_datasets_publications';

UPDATE
TB_DATA_CONFIGURATIONS
set conf_value = null
where conf_key = 'metamac.kafka.topic.external_collections_publications';

COMMIT;