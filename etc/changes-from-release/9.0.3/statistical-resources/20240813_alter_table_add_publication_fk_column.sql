-- ---------------------------------------------------------------------------------------------------
-- EDATOS-4584 Permitir enlazar colecciones a las colecciones
-- ---------------------------------------------------------------------------------------------------
 alter table tb_cubes
 add column publication_fk int8 null;