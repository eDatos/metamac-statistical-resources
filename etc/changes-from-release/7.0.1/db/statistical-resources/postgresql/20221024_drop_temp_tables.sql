-- --------------------------------------------------------------------------------------------------
-- En edatos-3744 se crearon tablas temporales para actualizar los campos date del tipo timespan a tipo varchar en formato sdmx. Estas tablas temporales se deben borrar pasado un tiempo prudencial.
--  **** Eliminar tabla de resguardo de campos next_version_date y next_version_date_tz 
--  **** Eliminar tabla de resguardo de campos DATE_NEXT_UPDATE y DATE_NEXT_UPDATE_TZ
-- --------------------------------------------------------------------------------------------------

drop table TEMP_TB_STAT_RESOURCES;

drop table TEMP_TB_DATASETS_VERSIONS;