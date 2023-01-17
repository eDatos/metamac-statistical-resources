-- ---------------------------------------------------------------------------------------------------
-- EDATOS-3770 - Creación de índice para mejorar rendimiento en borrados de caché
-- ---------------------------------------------------------------------------------------------------
-- Se crea índice para la base de datos PostgreSQL de cara a mejorar el rendimiento en el proceso masivo de borrado de caché
CREATE INDEX tb_localised_strings_international_string_fk ON tb_localised_strings USING btree (international_string_fk);

commit;