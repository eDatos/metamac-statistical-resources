----------------------------------------------------------------------------------------------------------------------------
-- EDATOS-5431 - Modificar la API del statistical-resources para incluir cabeceras adecuadas de Cache / LastModified / E-tag
----------------------------------------------------------------------------------------------------------------------------

CREATE INDEX tb_external_items_urn_idx ON tb_external_items USING btree (urn);

COMMIT;
