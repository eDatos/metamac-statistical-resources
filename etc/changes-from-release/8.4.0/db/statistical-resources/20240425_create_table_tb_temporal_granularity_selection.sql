-- ---------------------------------------------------------------------------------------------------
-- EDATOS-3824 Permitir filtrar por granularidad temporal en las consultas
-- ---------------------------------------------------------------------------------------------------

CREATE TABLE metamac_statistical_resources_bd.tb_temporal_granularity_selection (
	query_fk int8 NOT NULL,
	temporal_granularity_fk int8 NOT NULL,
	CONSTRAINT pk_tb_temporal_granularity_selection PRIMARY KEY (temporal_granularity_fk, query_fk),
	CONSTRAINT fk_tb_temporal_granularity_selection_query_fk FOREIGN KEY (query_fk) REFERENCES metamac_statistical_resources_bd.tb_queries_versions(id) DEFERRABLE,
	CONSTRAINT fk_tb_temporal_granularity_selection_temporal_granularity_fk FOREIGN KEY (temporal_granularity_fk) REFERENCES metamac_statistical_resources_bd.tb_external_items(id) DEFERRABLE
);
CREATE INDEX pk_tb_temporal_granularity_selection_fk ON metamac_statistical_resources_bd.tb_temporal_granularity_selection USING btree (temporal_granularity_fk);

commit;

