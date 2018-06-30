CREATE TABLE rfr_pgs_fspevent 
  ( 
     id                      serial NOT NULL,
     outage_id               INT4 NOT NULL, 
     equip_serial_number     VARCHAR(255) NOT NULL,
     outage_probability      VARCHAR(255) NOT NULL, 
     outage_status           VARCHAR(255) NOT NULL, 
     outage_type             VARCHAR(255) NOT NULL, 
     site_name               VARCHAR(255) NOT NULL, 
     technical_code          VARCHAR(255) NOT NULL, 
     actual_end_date         TIMESTAMP NOT NULL, 
     actual_start_date       TIMESTAMP NOT NULL, 
     actual_start_up_date    TIMESTAMP NOT NULL, 
     estimated_end_date      TIMESTAMP NOT NULL, 
     estimated_start_date    TIMESTAMP NOT NULL, 
     estimated_start_up_date TIMESTAMP NOT NULL,
     created_by_firstname  VARCHAR(255) NOT NULL,
     created_by_lastname   VARCHAR(255) NOT NULL,
     created_by_sso        VARCHAR(255) NOT NULL,
     created_date          TIMESTAMP NOT NULL,
     modified_by_firstname VARCHAR(255) NOT NULL,
     modified_by_lastname  VARCHAR(255) NOT NULL,
     modified_by_sso       VARCHAR(255) NOT NULL,
     modified_date         TIMESTAMP NOT NULL,
     PRIMARY KEY (id) 
  ); 
  
CREATE TABLE rfr_workflow (
  id serial NOT NULL,
  equip_serial_number varchar(255) NOT NULL,
  outage_id int4 NOT NULL,
  site_name VARCHAR(255) NOT NULL,
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

-- Disallow duplicate workflow names
CREATE UNIQUE INDEX rfr_workflow_unq_name ON rfr_workflow (LOWER(equip_serial_number),outage_id);

CREATE TABLE rfr_workflow_assigned_engineers (
  workflow_id int4 NOT NULL REFERENCES rfr_workflow(id),
  sso varchar(255),
  first_name varchar(255),
  last_name varchar(255)
);
