-- ---------------------------------------------------------------------------------------------------------
-- EDATOS-4397 Algunos parámetros de la API parecen filtrar por valores correspondientes a otras propiedades
-- ---------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION sdmx_to_date(txt text)
RETURNS date
LANGUAGE plpgsql
IMMUTABLE
RETURNS NULL ON NULL INPUT
AS $$
DECLARE
    y int;
    m int := 1;
    d int := 1;
BEGIN
    /* 0) YYYY */
    IF txt ~ '^\d{4}$' THEN
        y := txt::int;
        RETURN make_date(y,m,d);
    END IF;

    /* 1) YYYY-MM-DD */
    IF txt ~ '^\d{4}-\d{2}-\d{2}$' THEN
        RETURN to_date(txt,'YYYY-MM-DD');
    END IF;

    /* 2) YYYY-M## */
    IF txt ~ '^\d{4}-M\d{2}$' THEN
        y := substring(txt,1,4)::int;
        m := substring(txt,7,2)::int;
        RETURN make_date(y,m,d);
    END IF;

    /* 3) YYYY-Q# */
    IF txt ~ '^\d{4}-Q[1-4]$' THEN
        y := substring(txt,1,4)::int;
        m := (substring(txt,7,1)::int - 1)*3 + 1;
        RETURN make_date(y,m,d);
    END IF;

    /* 4) YYYY-W## (ISO week) */
    IF txt ~ '^\d{4}-W\d{2}$' THEN
        RETURN to_date(txt||'-1','IYYY-"W"IW-ID');
    END IF;

    /* 4bis) YYYY-MM-DD/PnD  -> se devuelve la fecha de inicio */
    IF txt ~ '^\d{4}-\d{2}-\d{2}/P\d+D$' THEN
        RETURN substring(txt,1,10)::date;
    END IF;

    /* 5) patrón desconocido */
    RAISE EXCEPTION 'Formato SDMX no soportado: %', txt;
END;
$$;