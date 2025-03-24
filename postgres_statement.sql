-- Table: public.current_coordinate

-- DROP TABLE IF EXISTS public.current_coordinate;

CREATE TABLE IF NOT EXISTS public.current_coordinate
(
    x_coordinate integer NOT NULL,
    y_coordinate integer NOT NULL,
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    CONSTRAINT current_coordinate_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.current_coordinate
    OWNER to postgres;

-- Trigger: before_current_coordinate_update

-- DROP TRIGGER IF EXISTS before_current_coordinate_update ON public.current_coordinate;

CREATE OR REPLACE TRIGGER before_current_coordinate_update
    BEFORE UPDATE 
    ON public.current_coordinate
    FOR EACH ROW
    EXECUTE FUNCTION public.probe_visited_history_update();


-- Table: public.ocean_obstacle_coordinates

-- DROP TABLE IF EXISTS public.ocean_obstacle_coordinates;

CREATE TABLE IF NOT EXISTS public.ocean_obstacle_coordinates
(
    id integer NOT NULL,
    x_coordinate integer NOT NULL,
    y_coordinate integer NOT NULL,
    x integer,
    y integer,
    CONSTRAINT ocean_obstacle_coordinates_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.ocean_obstacle_coordinates
    OWNER to postgres;

INSERT INTO public.ocean_obstacle_coordinates(id, x, y)
	VALUES (1, 4, 4),(2, 4,6),(3,8,6),(4,8,4);


-- Table: public.probe_visited_history

-- DROP TABLE IF EXISTS public.probe_visited_history;

CREATE TABLE IF NOT EXISTS public.probe_visited_history
(
    id integer NOT NULL DEFAULT nextval('probe_visited_history_id_seq'::regclass),
    x_coordinate integer NOT NULL,
    y_coordinate integer NOT NULL,
    changed_at time with time zone NOT NULL,
    CONSTRAINT probe_visited_history_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.probe_visited_history
    OWNER to postgres;


-- FUNCTION: public.probe_visited_history_update()

-- DROP FUNCTION IF EXISTS public.probe_visited_history_update();

CREATE OR REPLACE FUNCTION public.probe_visited_history_update()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
BEGIN
    INSERT INTO probe_visited_history( x_coordinate, y_coordinate, changed_at)
    VALUES ( OLD.x_coordinate, OLD.y_coordinate, NOW());
    RETURN NEW;
END;
$BODY$;

ALTER FUNCTION public.probe_visited_history_update()
    OWNER TO postgres;
