-- ---------------------------------------------------------------------------------------------------
-- EDATOS-5739 - Añadir campo lastUpdate a consultas e indicadores

-- Rellenar campo lastUpdate de todos los recursos que no lo tenían para ponerles lo mismo que su fecha de auditoría.
-- ---------------------------------------------------------------------------------------------------


UPDATE TB_STAT_RESOURCES
SET LAST_UPDATE = LAST_UPDATED, LAST_UPDATE_TZ = LAST_UPDATED_TZ
WHERE LAST_UPDATE IS NULL;

COMMIT;
