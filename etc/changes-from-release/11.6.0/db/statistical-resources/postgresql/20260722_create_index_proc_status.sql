----------------------------------------------------------------------------------------------------------------------------
-- EDATOS-5851 - Estudiar posibles mejoras en rendimiento en listados de colecciones

-- Añadir índice para mejorar consulta realizada por las apis de datasets, colecciones, multidatasets y consultas en los endpoints de listados.
----------------------------------------------------------------------------------------------------------------------------

CREATE INDEX idx_tb_stat_resources_proc_status ON tb_stat_resources (proc_status);

COMMIT;