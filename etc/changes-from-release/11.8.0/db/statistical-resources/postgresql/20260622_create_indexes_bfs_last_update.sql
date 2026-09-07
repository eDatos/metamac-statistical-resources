-- ---------------------------------------------------------------------------------------------------
-- EDATOS-5739 - Añadir campo lastUpdate a consultas e indicadores

-- Crear índices de búsqueda para mejorar rendimiento en el proceso de actualización de la fecha de actualización de todos los recursos relacionados cuando se modifica la de un dataset.
-- Especialmente los recursos asociados a colecciones y multidatasets.
-- ---------------------------------------------------------------------------------------------------



-- Indexes for BFS lastUpdate propagation queries

-- QueryVersion -> Dataset (Q1: find queries linked to a dataset)
CREATE INDEX IF NOT EXISTS idx_tb_queries_versions_dataset_fk ON tb_queries_versions USING btree (dataset_fk);

-- Cube -> Dataset, Query, Multidataset, Publication (Q2-Q5: find publications containing a resource)
CREATE INDEX IF NOT EXISTS idx_tb_cubes_dataset_fk ON tb_cubes USING btree (dataset_fk);
CREATE INDEX IF NOT EXISTS idx_tb_cubes_query_fk ON tb_cubes USING btree (query_fk);
CREATE INDEX IF NOT EXISTS idx_tb_cubes_multidataset_fk ON tb_cubes USING btree (multidataset_fk);
CREATE INDEX IF NOT EXISTS idx_tb_cubes_publication_fk ON tb_cubes USING btree (publication_fk);

-- ElementLevel -> PublicationVersion (join from Cube to PublicationVersion)
CREATE INDEX IF NOT EXISTS idx_tb_elements_levels_table_fk ON tb_elements_levels USING btree (table_fk);

-- MultidatasetCube -> Dataset, Query (Q6-Q7: find multidatasets containing a resource)
CREATE INDEX IF NOT EXISTS idx_tb_md_cubes_dataset_fk ON tb_md_cubes USING btree (dataset_fk);
CREATE INDEX IF NOT EXISTS idx_tb_md_cubes_query_fk ON tb_md_cubes USING btree (query_fk);

COMMIT;
