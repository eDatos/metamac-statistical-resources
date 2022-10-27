-- Drop table

-- DROP TABLE metamac_statistical_resources_bd.tb_geocov_varelem_cache_datasets_versions

CREATE TABLE metamac_statistical_resources_bd.tb_geocov_varelem_cache_datasets_versions (
	id int8 NOT NULL,
	dataset_version_urn varchar(4000) NOT NULL,
	external_item_fk int8 NOT NULL,
	is_external_source boolean NOT NULL,
	CONSTRAINT pk_tb_geocov_varelem_cache_datasets_versions PRIMARY KEY (dataset_version_urn,external_item_fk),
	CONSTRAINT fk_tb_geocov_varelem_cache_datasets_versions_external_item_fk FOREIGN KEY (external_item_fk) REFERENCES metamac_statistical_resources_bd.tb_external_items(id)
);

cibercentro:
928 117 912