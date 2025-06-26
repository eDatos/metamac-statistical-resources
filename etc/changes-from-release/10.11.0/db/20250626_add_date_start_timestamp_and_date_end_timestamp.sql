alter table tb_datasets_versions 
add date_start_timestamp timestamp null;

alter table tb_datasets_versions 
add date_end_timestamp timestamp null;

update tb_datasets_versions tdv 
set date_end_timestamp = sdmx_to_date(tdv.date_end),
date_start_timestamp = sdmx_to_date(tdv.date_start);

commit;