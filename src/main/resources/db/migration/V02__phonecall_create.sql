CREATE TABLE phone_call_details (
  id serial NOT NULL,
  meeting_line varchar(500) NOT NULL,
  meeting_date timestamp NOT NULL,
  phone_call_minutes text,
  workflow_id int4 REFERENCES rfr_workflow,
  created_by_firstname varchar(255) NOT NULL,
  created_by_lastname varchar(255) NOT NULL,
  created_by_sso varchar(255) NOT NULL,
  created_date timestamp NOT NULL,
  modified_by_firstname varchar(255) NOT NULL,
  modified_by_lastname varchar(255) NOT NULL,
  modified_by_sso varchar(255) NOT NULL,
  modified_date timestamp NOT NULL,
  PRIMARY KEY (id)
);

CREATE UNIQUE INDEX phone_call_details_unq_name ON phone_call_details (LOWER(meeting_line));
