-- ---------------------------------------------------------------------------------------------------
-- EDATOS-5739 - Añadir campo lastUpdate a consultas e indicadores

-- Rellenar campo lastUpdate de las consultas que no lo tenían para ponerles lo mismo que su fecha de auditoría.
-- ---------------------------------------------------------------------------------------------------


UPDATE TB_STAT_RESOURCES
SET LAST_UPDATE = LAST_UPDATED, LAST_UPDATE_TZ = LAST_UPDATED_TZ
WHERE LAST_UPDATE IS NULL
  AND ID IN (SELECT LIFECYCLE_RESOURCE_FK FROM TB_QUERIES_VERSIONS);

COMMIT;
