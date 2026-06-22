CREATE INDEX tb_code_dimensions_dataset_version_fk_idx ON tb_code_dimensions USING btree (dataset_version_fk);
CREATE INDEX tb_code_dimensions_dsd_component_id_idx ON tb_code_dimensions USING btree (dsd_component_id);
CREATE INDEX tb_external_items_fk_idx ON tb_external_items USING btree (code);
CREATE INDEX pk_tb_attribute_values_value_fk ON tb_attribute_values (value_fk);
CREATE INDEX tb_external_items_urn_idx ON tb_external_items USING btree (urn);


-- From 6.0.0 (20230117_create indexes.sql)
CREATE INDEX pk_tb_categorisations_category_fk ON tb_categorisations USING btree (category_fk);
CREATE INDEX pk_tb_categorisations_maintainer_fk ON tb_categorisations USING btree (maintainer_fk);
CREATE INDEX pk_tb_datasets_versions_related_dsd_fk ON tb_datasets_versions USING btree (related_dsd_fk);
CREATE INDEX pk_tb_datasets_versions_update_frequency_fk ON tb_datasets_versions USING btree (update_frequency_fk);
CREATE INDEX pk_tb_dv_geo_coverage_geo_code_fk ON tb_dv_geo_coverage USING btree (geo_code_fk);
CREATE INDEX pk_tb_dv_geo_granularity_fk ON tb_dv_geo_granularity USING btree (geographic_granularity_fk);
CREATE INDEX pk_fk_tb_dv_measure_coverage_measure_codes_fk ON tb_dv_measure_coverage USING btree (measure_codes_fk);
CREATE INDEX pk_tb_dv_temp_granularity_temporal_granularity_fk ON tb_dv_temp_granularity USING btree (temporal_granularity_fk);
CREATE INDEX pk_tb_ei_contributors_contributor_fk ON tb_ei_contributors USING btree (contributor_fk);
CREATE INDEX pk_tb_ei_data_providers_data_provider_fk ON tb_ei_data_providers USING btree (data_provider_fk);
CREATE INDEX pk_tb_ei_languages_language_fk ON tb_ei_languages USING btree (language_fk);
CREATE INDEX pk_tb_ei_mediators_mediator_fk ON tb_ei_mediators USING btree (mediator_fk);
CREATE INDEX pk_fk_tb_ei_pub_contributors_publisher_cont_fk ON tb_ei_pub_contributors USING btree (publisher_cont_fk);
CREATE INDEX pk_tb_ei_publishers_publisher_fk ON tb_ei_publishers USING btree (publisher_fk);
CREATE INDEX pk_tb_ei_stat_oper_instances_stat_operation_instance_fk ON tb_ei_stat_oper_instances USING btree (stat_operation_instance_fk);
CREATE INDEX pk_tb_ei_statistical_unit_statistical_unit_fk ON tb_ei_statistical_unit USING btree (statistical_unit_fk);
CREATE INDEX pk_tb_stat_resources_common_metadata_fk ON tb_stat_resources USING btree (common_metadata_fk);
CREATE INDEX pk_tb_stat_resources_creator_fk ON tb_stat_resources USING btree (creator_fk);
CREATE INDEX pk_tb_stat_resources_language_fk ON tb_stat_resources USING btree (language_fk);
CREATE INDEX pk_tb_stat_resources_maintainer_fk ON tb_stat_resources USING btree (maintainer_fk);
CREATE INDEX pk_tb_stat_resources_stat_operation_fk ON tb_stat_resources USING btree (stat_operation_fk);
CREATE INDEX tb_localised_strings_international_string_fk ON tb_localised_strings USING btree (international_string_fk);
CREATE INDEX pk_tb_external_items_title_fk ON tb_external_items USING btree (title_fk);
CREATE INDEX pk_tb_datasets_versions_bibliographic_citation_fk ON tb_datasets_versions USING btree (bibliographic_citation_fk);
CREATE INDEX pk_tb_lis_stat_officiality_description_fk ON tb_lis_stat_officiality USING btree (description_fk);
CREATE INDEX pk__tb_multidatasets_versions_filtering_dimension_fk ON tb_multidatasets_versions USING btree (filtering_dimension_fk);
CREATE INDEX pk_tb_stat_resources_abstract_fk ON tb_stat_resources USING btree (abstract_fk);
CREATE INDEX pk_tb_stat_resources_access_rights_fk ON tb_stat_resources USING btree (access_rights_fk);
CREATE INDEX pk_tb_stat_resources_conforms_to_fk ON tb_stat_resources USING btree (conforms_to_fk);
CREATE INDEX pk_tb_stat_resources_conforms_to_internal_fk ON tb_stat_resources USING btree (conforms_to_internal_fk);
CREATE INDEX pk_tb_stat_resources_data_provider_annotations_fk ON tb_stat_resources USING btree (data_provider_annotations_fk);
CREATE INDEX pk_tb_stat_resources_description_fk ON tb_stat_resources USING btree (description_fk);
CREATE INDEX pk_tb_stat_resources_keywords_fk ON tb_stat_resources USING btree (keywords_fk);
CREATE INDEX pk_tb_stat_resources_subtitle_fk ON tb_stat_resources USING btree (subtitle_fk);
CREATE INDEX pk_tb_stat_resources_title_alternative_fk ON tb_stat_resources USING btree (title_alternative_fk);
CREATE INDEX pk_tb_stat_resources_title_fk ON tb_stat_resources USING btree (title_fk);
CREATE INDEX pk_tb_stat_resources_version_rationale_fk ON tb_stat_resources USING btree (version_rationale_fk);
CREATE INDEX pk_tb_translations_title_fk ON tb_translations USING btree (title_fk);

-- From 8.4.0 (20240425_create_table_tb_temporal_granularity_selection.sql)
CREATE INDEX pk_tb_temporal_granularity_selection_fk ON tb_temporal_granularity_selection USING btree (temporal_granularity_fk);

-- From 10.12.0 (20250212_create_purpose_tables_and_x_template_relations.sql)
CREATE INDEX pk_tb_purposes_description_fk ON tb_purposes USING btree (description_fk);

-- From 10.23.0 (1_20242208_create_tables_collection_cache.sql)
CREATE INDEX pk_tb_geo_cache_resource_urn ON tb_geo_cache_resource (urn);
CREATE INDEX pk_tb_geo_cache_resource_title_fk ON tb_geo_cache_resource (title_fk);
CREATE INDEX pk_tb_geo_cache_related_resource_urn ON tb_geo_cache_related_resource (urn);
CREATE INDEX pk_tb_geo_cache_related_resource_title_fk ON tb_geo_cache_related_resource (title_fk);
CREATE INDEX ix_tb_geo_cache_resource_by_related_resource ON tb_geo_cache_resource_by_related_resource (geo_cache_resource_fk);
CREATE INDEX pk_tb_territories_by_geo_cache_resource_geo_cache_resource_fk ON tb_territories_by_geo_cache_resource (geo_cache_resource_fk);

-- From 12.0.0 (20260622_create_indexes_bfs_last_update.sql)
CREATE INDEX idx_tb_queries_versions_dataset_fk ON tb_queries_versions USING btree (dataset_fk);
CREATE INDEX idx_tb_cubes_dataset_fk ON tb_cubes USING btree (dataset_fk);
CREATE INDEX idx_tb_cubes_query_fk ON tb_cubes USING btree (query_fk);
CREATE INDEX idx_tb_cubes_multidataset_fk ON tb_cubes USING btree (multidataset_fk);
CREATE INDEX idx_tb_cubes_publication_fk ON tb_cubes USING btree (publication_fk);
CREATE INDEX idx_tb_elements_levels_table_fk ON tb_elements_levels USING btree (table_fk);
CREATE INDEX idx_tb_md_cubes_dataset_fk ON tb_md_cubes USING btree (dataset_fk);
CREATE INDEX idx_tb_md_cubes_query_fk ON tb_md_cubes USING btree (query_fk);