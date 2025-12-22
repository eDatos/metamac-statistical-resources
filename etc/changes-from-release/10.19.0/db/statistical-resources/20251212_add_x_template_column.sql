-- --------------------------------------------------------------------------------------------------
-- EDATOS-5384 - Mejoras para la gestión de las consultas con finalidad redes sociales
-- --------------------------------------------------------------------------------------------------

-- añadir nuevo campo x_template

alter table tb_queries_versions
add column x_template varchar(255) null;

-- Sólo ejecutar en IBESTAT, ya que es el único que tiene el envío de twitter activo
update tb_queries_versions tqv1
set x_template = (select tls."label" 
from tb_queries_versions tqv 
join tb_international_strings tis on tis.id = tqv.x_templates_fk 
join tb_localised_strings tls on tls.international_string_fk = tis.id
where tls.locale = 'ca'
and tqv.id = tqv1.id)
where tqv1.x_templates_fk is not null;

commit;
 
