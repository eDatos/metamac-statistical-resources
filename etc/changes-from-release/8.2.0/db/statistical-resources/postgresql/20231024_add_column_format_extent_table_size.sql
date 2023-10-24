-- --------------------------------------------------------------------------------------------------
-- EDATOS-4271 - Mostrar el número de celdas de una tabla
-- 
-- Se añade la columna FORMAT_EXTENT_TABLE_SIZE a la tabla TB_DATASETS_VERSIONS
-- --------------------------------------------------------------------------------------------------

ALTER TABLE TB_DATASETS_VERSIONS
ADD COLUMN FORMAT_EXTENT_TABLE_SIZE INTEGER;

COMMIT;