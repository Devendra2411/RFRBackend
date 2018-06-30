DROP table rfr_pgs_fspevent;

CREATE TABLE rfr_pgs_fspevent 
  ( 
     id                    SERIAL NOT NULL, 
	 plant_id              INT4 NOT NULL, 
     block_id              INT4 NOT NULL, 
	 train_id              INT4 NOT NULL, 
	 equip_serial_number   VARCHAR(255) NOT NULL, 
	 outage_id             INT4 NOT NULL, 
	 outage_number         INT4 NOT NULL, 
	 site_name             VARCHAR(255) NOT NULL, 
	 region                VARCHAR(255) NOT NULL, 
	 technical_code        VARCHAR(255) NOT NULL, 
	 equipment_type        VARCHAR(255) NOT NULL, 
     contract_type         VARCHAR(255) NOT NULL, 
	 outage_type           VARCHAR(255) NOT NULL, 
	 outage_probability    VARCHAR(255) NOT NULL, 
	 project_status        VARCHAR(255) NOT NULL, 
	 event_status          VARCHAR(255) NOT NULL, 
     e_end_date            TIMESTAMP NOT NULL, 
     e_start_date          TIMESTAMP NOT NULL, 
     e_unit_startup_date   TIMESTAMP NOT NULL,   
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
  
 ALTER  TABLE rfr_workflow RENAME COLUMN "site_name" TO "workflow_name";
 
DROP INDEX rfr_workflow_unq_name;
   
 CREATE UNIQUE INDEX rfr_workflow_unq_name ON rfr_workflow (LOWER(equip_serial_number),outage_id,LOWER(workflow_name));
  