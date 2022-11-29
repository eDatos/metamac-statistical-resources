-- Drop table

-- DROP TABLE metamac_statistical_resources_bd.tb_geocov_varelem_cache_datasets_versions

CREATE TABLE tb_geocov_varelem_cache_datasets_versions (
	id int8 NOT NULL,
	urn varchar(4000) NOT NULL,
	variable_element_fk int8 NOT NULL,
	code varchar(255) NOT NULL,
	title_fk BIGINT,
	operation_code varchar(255),
	operation_urn varchar(4000),
	operation_title_fk BIGINT,
	htmlLink varchar(4000),
	is_external_source boolean NOT NULL,
	is_last_version boolean NOT NULL,
	CONSTRAINT pk_tb_geocov_varelem_cache_datasets_versions PRIMARY KEY (id),
	CONSTRAINT pk_tb_geocov_varelem_cache_datasets_versions_urn_variable_element UNIQUE (urn, variable_element_fk),
	CONSTRAINT fk_tb_geocov_varelem_cache_datasets_versions_var_element_fk FOREIGN KEY (variable_element_fk) REFERENCES tb_external_items(id) DEFERRABLE initially IMMEDIATE,
	CONSTRAINT FK_tb_geocov_varelem_cache_datasets_versions_title_fk FOREIGN KEY (title_fk) REFERENCES TB_INTERNATIONAL_STRINGS (ID) DEFERRABLE initially IMMEDIATE,
	CONSTRAINT FK_tb_geocov_varelem_cache_datasets_versions_operation_title_fk FOREIGN KEY (operation_title_fk) REFERENCES TB_INTERNATIONAL_STRINGS (ID) DEFERRABLE initially IMMEDIATE
);

commit;