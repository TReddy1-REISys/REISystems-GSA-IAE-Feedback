SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

CREATE SCHEMA IF NOT EXISTS feedbackschema authorization postgres;

--set search path to feedbackchema
SET search_path = feedbackschema;
ALTER ROLE postgres SET search_path TO feedbackschema;

--Create sequences
CREATE SEQUENCE question_id_seq 
	START WITH 1
	INCREMENT BY 1
	NO MINVALUE
	NO MAXVALUE
	CACHE 1;

CREATE SEQUENCE feedback_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MINVALUE
	NO MAXVALUE
	CACHE 1;

ALTER TABLE question_id_seq OWNER TO postgres;
ALTER TABLE feedback_id_seq OWNER TO postgres;

CREATE TABLE question(
	question_id bigint DEFAULT nextval('question_id_seq' :: regclass) NOT NULL,
	question_desc character varying(250) NOT NULL, 
	question_options jsonb NOT NULL,
	created_by character varying(100) NOT NULL,
	created_date timestamp without time zone DEFAULT clock_timestamp() NOT NULL,
	last_modified_by character varying(100) NOT NULL,
	last_modified_date timestamp without time zone DEFAULT clock_timestamp() NOT NULL
);

ALTER TABLE ONLY question ADD CONSTRAINT question_pkey PRIMARY KEY (question_id);
ALTER TABLE question OWNER TO postgres;

CREATE TABLE feedback(
	feedback_id bigint DEFAULT nextval('feedback_id_seq' :: regclass) NOT NULL,
	feedback_path character varying(256) NOT NULL,
	question_id bigint NOT NULL,
	user_id character varying(100),
	feedback_response jsonb NOT NULL,
	created_by character varying(100) NOT NULL,
	created_date timestamp without time zone DEFAULT clock_timestamp() NOT NULL,
	last_modified_by character varying(100) NOT NULL,
	last_modified_date timestamp without time zone DEFAULT clock_timestamp() NOT NULL
);

ALTER TABLE ONLY feedback ADD CONSTRAINT feedback_pkey PRIMARY KEY (feedback_id);
ALTER TABLE ONLY feedback ADD CONSTRAINT feedback_ques_id_fkey FOREIGN KEY (question_id) REFERENCES question(question_id);
ALTER TABLE feedback OWNER TO postgres;

-- Adding Table Descriptions
COMMENT ON TABLE feedback IS 'This table contains information of all the feedback responses provided by the end users';
COMMENT ON TABLE question IS 'This table contains all the questions for which feedback is requested';

-- Adding Table column description
COMMENT ON column feedback.feedback_id IS 'This field is auto incremented id for feedback table.';
COMMENT ON column feedback.question_id IS 'This field is a foriegn key relationship with question_id of question table';
COMMENT ON column question.question_id IS 'This field is auto incremented id for question table.';