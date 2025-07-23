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
    /* 0) YYYY (anual) */
    IF txt ~ '^\d{4}$' THEN
        RETURN make_date(txt::int, 1, 1);
    END IF;

    /* 1) YYYY-MM-DD (fecha) */
    IF txt ~ '^\d{4}-\d{2}-\d{2}$' THEN
        RETURN txt::date;
    END IF;

    /* 1-bis) YYYY-MM-DDThh:mm:ss (ISO 8601 completa) */
    IF txt ~ '^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}$' THEN
        RETURN (substring(txt,1,10))::date;    -- sólo la parte de la fecha
    END IF;

    /* 2) YYYY-M## (mes) */
    IF txt ~ '^\d{4}-M\d{2}$' THEN
        y := substring(txt,1,4)::int;
        m := substring(txt,7,2)::int;
        RETURN make_date(y, m, 1);
    END IF;

    /* 3) YYYY-Q# (trimestre) */
    IF txt ~ '^\d{4}-Q[1-4]$' THEN
        y := substring(txt,1,4)::int;
        m := (substring(txt,7,1)::int - 1)*3 + 1;
        RETURN make_date(y, m, 1);
    END IF;

    /* 4) YYYY-W## (semana ISO) */
    IF txt ~ '^\d{4}-W\d{2}$' THEN
        RETURN to_date(txt||'-1','IYYY-"W"IW-ID');  -- lunes de la semana
    END IF;

    /* 5) YYYY-A# (periodo anual “A1” = todo el año) */
    IF txt ~ '^\d{4}-A[1-4]$' THEN
        y := substring(txt,1,4)::int;
        RETURN make_date(y, 1, 1);                -- se interpreta como 1-ene-YYYY
    END IF;

    /* 6) Opcional: YYYY-MM-DD/P#####D  (intervalos ISO 8601) */
    IF txt ~ '^\d{4}-\d{2}-\d{2}/P\d+D$' THEN
        RETURN substring(txt,1,10)::date;         -- fecha inicial del intervalo
    END IF;

    /* 7) Sin patrón reconocido */
    RAISE EXCEPTION 'Formato SDMX no soportado: %', txt;
END;
$$;