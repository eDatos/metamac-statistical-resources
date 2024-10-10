CREATE TABLE tb_m_dimension_orders (
	id int8 NOT NULL,
	dim_order int4 NOT NULL,
	"uuid" varchar(36) NOT NULL,
	"version" int8 NOT NULL,
	urn_dim_component_fk varchar(255) null,
	dataset_version_heading_fk int8 NULL,
	dataset_version_stub_fk int8 NULL,
	query_version_heading_fk int8 null,
	query_version_stub_fk int8 null,
	CONSTRAINT pk_tb_m_dimension_orders PRIMARY KEY (id),
	CONSTRAINT uq_tb_m_dimension_orders UNIQUE (uuid),
	CONSTRAINT fk_tb_m_dimension_orders_dataset_version_heading_fk FOREIGN KEY (dataset_version_heading_fk) REFERENCES tb_datasets_versions(id) ON DELETE CASCADE,
	CONSTRAINT fk_tb_m_dimension_orders_dataset_version_stub_fk FOREIGN KEY (dataset_version_stub_fk) REFERENCES tb_datasets_versions(id) ON DELETE cascade
	CONSTRAINT fk_tb_m_dimension_orders_query_version_heading_fk FOREIGN KEY (query_version_heading_fk) REFERENCES tb_queries_versions(id) ON DELETE CASCADE,
	CONSTRAINT fk_tb_m_dimension_orders_query_version_stub_fk FOREIGN KEY (query_version_stub_fk) REFERENCES tb_queries_versions(id) ON DELETE CASCADE
);

CREATE sequence seq_m_dimension_orders;