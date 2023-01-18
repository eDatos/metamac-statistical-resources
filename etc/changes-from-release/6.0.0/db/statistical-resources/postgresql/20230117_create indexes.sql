-- ---------------------------------------------------------------------------------------------------
-- EDATOS-3770 - Creación de índice para mejorar rendimiento en borrados de caché
-- ---------------------------------------------------------------------------------------------------
-- Se crea índice para la base de datos PostgreSQL de cara a mejorar el rendimiento en el proceso masivo de borrado de caché
CREATE INDEX tb_localised_strings_international_string_fk ON tb_localised_strings USING btree (international_string_fk);

CREATE INDEX pk_tb_geocov_varelem_cache_variable_element_fk ON tb_geocov_varelem_cache_datasets_versions USING btree (variable_element_fk);
CREATE INDEX pk_tb_geocov_varelem_cache_title_fk ON tb_geocov_varelem_cache_datasets_versions USING btree (title_fk);
CREATE INDEX pk_tb_geocov_varelem_cache_operation_title_fk ON tb_geocov_varelem_cache_datasets_versions USING btree (operation_title_fk);

CREATE INDEX pk_tb_external_items_title_fk ON tb_external_items USING btree (title_fk);

commit;