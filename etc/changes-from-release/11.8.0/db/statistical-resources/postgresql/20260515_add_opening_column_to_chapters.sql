-- ---------------------------------------------------------------------------------------------------
-- EDATOS-5178 - Permitir plegar los capítulos y subcapítulos en las colecciones
-- Se crea campo para controlar el estado abierto/cerrado en los capitulos
-- ---------------------------------------------------------------------------------------------------

ALTER TABLE TB_CHAPTERS ADD COLUMN OPENING BOOLEAN DEFAULT TRUE NOT NULL;

COMMIT;