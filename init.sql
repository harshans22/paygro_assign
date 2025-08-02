--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5 (Debian 17.5-1.pgdg120+1)
-- Dumped by pg_dump version 17.5 (Debian 17.5-1.pgdg120+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: bookings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bookings (
    id uuid NOT NULL,
    comment character varying(255),
    proposed_rate double precision NOT NULL,
    requested_at timestamp(6) with time zone NOT NULL,
    status character varying(255) NOT NULL,
    transporter_id character varying(255) NOT NULL,
    load_id uuid NOT NULL,
    CONSTRAINT bookings_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'ACCEPTED'::character varying, 'REJECTED'::character varying])::text[])))
);


ALTER TABLE public.bookings OWNER TO postgres;

--
-- Name: loads; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.loads (
    id uuid NOT NULL,
    comment character varying(255),
    date_posted timestamp(6) with time zone NOT NULL,
    loading_date timestamp(6) with time zone NOT NULL,
    loading_point character varying(255) NOT NULL,
    unloading_date timestamp(6) with time zone NOT NULL,
    unloading_point character varying(255) NOT NULL,
    no_of_trucks integer NOT NULL,
    product_type character varying(255) NOT NULL,
    shipper_id character varying(255) NOT NULL,
    status character varying(255) NOT NULL,
    truck_type character varying(255) NOT NULL,
    weight double precision NOT NULL,
    CONSTRAINT loads_status_check CHECK (((status)::text = ANY ((ARRAY['POSTED'::character varying, 'BOOKED'::character varying, 'CANCELLED'::character varying])::text[])))
);


ALTER TABLE public.loads OWNER TO postgres;

--
-- Data for Name: bookings; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bookings (id, comment, proposed_rate, requested_at, status, transporter_id, load_id) FROM stdin;
40bf0830-46e0-43f6-bddb-35fc353d024e	Can deliver by 11th Aug	1500	2025-07-30 19:29:35.069537+00	PENDING	transporter1	1c487e2b-f579-45b6-97ec-8b9d89a7c7e9
804f9996-9960-4b6d-9f49-798c5a488c79		1450	2025-07-30 19:29:58.865491+00	PENDING	transporter2	1c487e2b-f579-45b6-97ec-8b9d89a7c7e9
e6dd5c0e-f076-47fb-b261-89c9eacc9e55	Refrigerated trucks available	900	2025-07-30 19:30:25.187533+00	PENDING	transporter1	a190572c-c948-434a-a1d1-22c88a274248
\.


--
-- Data for Name: loads; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.loads (id, comment, date_posted, loading_date, loading_point, unloading_date, unloading_point, no_of_trucks, product_type, shipper_id, status, truck_type, weight) FROM stdin;
03e0ba8d-13f0-471d-b341-25c0218ffb7a	Handle with care	2025-07-30 19:14:27.172894+00	2025-08-10 08:00:00+00	New York	2025-08-12 18:00:00+00	Chicago	3	Electronics	shipperA	POSTED	Flatbed	12000
1c487e2b-f579-45b6-97ec-8b9d89a7c7e9	Perishable goods	2025-07-30 19:14:34.910805+00	2025-08-20 09:00:00+00	Houston	2025-08-20 16:00:00+00	Dallas	2	Food	shipperA	BOOKED	Refrigerated	5000
a190572c-c948-434a-a1d1-22c88a274248	Handle with care	2025-07-30 19:14:08.078813+00	2025-08-10 08:00:00+00	New York	2025-08-12 18:00:00+00	Chicago	3	Electronics	shipperA	BOOKED	Flatbed	12000
be8810f7-227c-475b-83ad-11cee242367e	Perishable goods	2025-07-30 20:05:13.738251+00	2025-08-20 09:00:00+00	Houston	2025-08-20 16:00:00+00	Dallas	2	Food	shipperA	POSTED	Refrigerated	5000
7c57a1ed-c679-4620-919c-787691f54b90	Perishable goods	2025-07-30 20:05:27.128948+00	2025-08-20 09:00:00+00	Houston	2025-08-20 16:00:00+00	Dallas	2	Food	shipperA	POSTED	Refrigerated	5000
e77f915f-975a-4928-8590-94be5fae9e82	Bulky items	2025-07-30 20:05:59.846906+00	2025-08-22 10:00:00+00	Chicago	2025-08-22 18:00:00+00	Detroit	2	Furniture	shipperB	POSTED	Flatbed	8000
0683aebb-3394-4fed-bc4b-a8ea20c1d9ba	Bulky items	2025-07-30 20:06:07.613254+00	2025-08-22 10:00:00+00	Chicago	2025-08-22 18:00:00+00	Detroit	2	Furniture	shipperB	POSTED	Flatbed	8000
dac68742-6ce5-4cea-9448-a917411d63f8	Seasonal shipment	2025-07-30 20:06:17.246624+00	2025-08-23 07:00:00+00	Los Angeles	2025-08-23 15:00:00+00	San Francisco	1	Clothing	shipperC	POSTED	Dry Van	2000
fa3f57dc-37fa-4aac-bd19-9e478773799e	Temperature controlled	2025-07-30 20:06:26.234351+00	2025-08-24 06:30:00+00	Miami	2025-08-24 12:30:00+00	Orlando	1	Pharmaceuticals	shipperD	POSTED	Refrigerated	1500
5eb10df1-f67a-4874-a477-4e7f77789e06	Back to school	2025-07-30 20:06:35.471549+00	2025-08-25 09:30:00+00	New York	2025-08-25 17:00:00+00	Boston	3	Books	shipperE	POSTED	Dry Van	7000
cc0737e0-87b3-49d2-848a-d06bfabfbc7e	Heavy load	2025-07-30 20:06:45.240042+00	2025-08-26 08:00:00+00	Seattle	2025-08-26 12:00:00+00	Portland	2	Timber	shipperF	POSTED	Flatbed	10000
7a197d88-74e1-4446-aeb2-07d3c2278308	Urgent delivery	2025-07-30 20:06:53.949135+00	2025-08-27 10:00:00+00	Denver	2025-08-27 18:00:00+00	Salt Lake City	4	Construction Material	shipperG	POSTED	Flatbed	15000
75376f47-db39-4182-8e40-83fea89a0b08	Keep chilled	2025-07-30 20:07:02.358405+00	2025-08-28 07:45:00+00	Atlanta	2025-08-28 15:30:00+00	Charlotte	3	Beverages	shipperH	POSTED	Refrigerated	6000
f69b24af-d94e-487a-8f82-e68a5c0b22d9	Fragile	2025-07-30 20:07:09.41995+00	2025-08-29 09:00:00+00	Las Vegas	2025-08-29 17:00:00+00	Phoenix	1	Glassware	shipperI	POSTED	Dry Van	2500
64f6a39a-3d38-468c-a88d-ee1c79b41059	Industrial order	2025-07-30 20:07:21.291958+00	2025-08-30 08:30:00+00	Philadelphia	2025-08-30 16:30:00+00	Pittsburgh	2	Tires	shipperJ	POSTED	Flatbed	9000
e6b7146d-db60-42bd-ad30-43a3ec355840	Secure with chains	2025-07-30 20:07:33.928615+00	2025-08-31 06:30:00+00	San Jose	2025-08-31 12:30:00+00	Sacramento	3	Machinery	shipperK	POSTED	Flatbed	12000
7bd74638-5ded-4a2c-8918-98d000c1c620	Hazardous	2025-07-30 20:07:41.820501+00	2025-09-01 09:30:00+00	Indianapolis	2025-09-01 15:30:00+00	Columbus	1	Chemicals	shipperL	POSTED	Tanker	5000
928c42a7-d460-4cf6-8258-a33d56764a70	Warehouse stock	2025-07-30 20:07:55.137083+00	2025-09-02 07:00:00+00	Kansas City	2025-09-02 13:00:00+00	Omaha	2	Paper	shipperM	POSTED	Dry Van	4000
14c276b4-780b-4a48-904a-1fb2a579cb7a	Retail delivery	2025-07-30 20:08:02.791213+00	2025-09-03 10:00:00+00	Cleveland	2025-09-03 18:00:00+00	Cincinnati	2	Appliances	shipperN	POSTED	Dry Van	7500
e8ac34c3-1075-4007-9711-a528e737a550	Factory supply	2025-07-30 20:08:10.701888+00	2025-09-04 08:00:00+00	Milwaukee	2025-09-04 14:00:00+00	Minneapolis	1	Textiles	shipperO	POSTED	Dry Van	3500
b3305f56-d287-4903-9f7b-63db68ae4cfe	Holiday inventory	2025-07-30 20:08:18.855344+00	2025-09-05 09:00:00+00	Nashville	2025-09-05 16:00:00+00	Memphis	2	Toys	shipperP	POSTED	Dry Van	6000
5770bdff-3137-41e4-8bad-f40087f93eaa	Coils, strapped	2025-07-30 20:08:26.802647+00	2025-09-06 07:30:00+00	Baltimore	2025-09-06 14:30:00+00	Richmond	3	Steel	shipperQ	POSTED	Flatbed	13000
0b6793c7-c108-45de-bcb5-9484ddef8567	Farm produce	2025-07-30 20:08:36.420595+00	2025-09-07 06:00:00+00	Tucson	2025-09-07 11:00:00+00	El Paso	1	Vegetables	shipperR	POSTED	Refrigerated	2000
3f255dbc-7b90-40f2-a241-41321f8266de	Fresh catch	2025-07-30 20:08:44.237942+00	2025-09-08 10:00:00+00	New Orleans	2025-09-08 17:00:00+00	Jackson	1	Seafood	shipperS	POSTED	Refrigerated	1800
21b053d0-67ef-4fe9-82be-fdfed78f0a8c	High-value items	2025-07-30 20:08:51.669552+00	2025-09-09 08:15:00+00	St. Louis	2025-09-09 15:15:00+00	Louisville	2	Medical Equipment	shipperT	POSTED	Dry Van	5500
\.


--
-- Name: bookings bookings_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT bookings_pkey PRIMARY KEY (id);


--
-- Name: loads loads_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loads
    ADD CONSTRAINT loads_pkey PRIMARY KEY (id);


--
-- Name: bookings fk3h9wubhva9yo6todesnel1k9k; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT fk3h9wubhva9yo6todesnel1k9k FOREIGN KEY (load_id) REFERENCES public.loads(id);


--
-- PostgreSQL database dump complete
--

