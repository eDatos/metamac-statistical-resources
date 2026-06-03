CREATE INDEX tb_code_dimensions_dataset_version_fk_idx ON tb_code_dimensions USING btree (dataset_version_fk);
CREATE INDEX tb_code_dimensions_dsd_component_id_idx ON tb_code_dimensions USING btree (dsd_component_id);
CREATE INDEX tb_external_items_fk_idx ON tb_external_items USING btree (code);
CREATE INDEX pk_tb_attribute_values_value_fk ON tb_attribute_values (value_fk);
CREATE INDEX tb_external_items_urn_idx ON tb_external_items USING btree (urn);
