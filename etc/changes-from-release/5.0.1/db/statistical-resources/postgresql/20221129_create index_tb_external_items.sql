-- ---------------------------------------------------------------------------------------------------
-- EDATOS-3770 - Creación de índice para mejorar rendimiento en e-Territorios
-- ---------------------------------------------------------------------------------------------------

CREATE INDEX tb_external_items_fk_idx ON tb_external_items USING btree (code);

commit;