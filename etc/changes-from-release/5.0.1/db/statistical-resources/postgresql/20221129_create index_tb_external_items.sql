-- ---------------------------------------------------------------------------------------------------
-- EDATOS-3770 - Creación de índice para mejorar rendimiento en e-Territorios
-- ---------------------------------------------------------------------------------------------------
-- Se crean índices para la base de datos PostgreSQL de cara a mejorar el rendimiento del servicio web que obtiene datasets por elemento de variable con cobertura geográfica /resources

CREATE INDEX tb_external_items_fk_idx ON tb_external_items USING btree (code);

commit;