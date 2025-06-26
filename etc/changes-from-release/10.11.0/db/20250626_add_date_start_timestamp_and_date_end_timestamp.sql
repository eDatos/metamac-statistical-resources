alter table tb_datasets_versions 
add date_start_timestamp timestamp null;

alter table tb_datasets_versions 
add DATE_END_TIMESTAMP timestamp null;

alter table tb_datasets_versions 
add date_start_timestamp_tz varchar(50) NULL;

alter table tb_datasets_versions 
add DATE_END_TIMESTAMP_TZ varchar(50) NULL;

update tb_datasets_versions tdv 
set date_start_timestamp = sdmx_to_date(tdv.date_start),
date_start_timestamp_tz = 'Atlantic/Canary'
where date_start is not null;

commit;