CREATE TABLE pgs_action_items 
  ( 
     id                  SERIAL NOT NULL, 
     category            VARCHAR(255) NOT NULL, 
     due_date            TIMESTAMP NOT NULL, 
     equip_serial_number VARCHAR(255) NOT NULL, 
     item_title          VARCHAR(255) NOT NULL, 
     level_value         INT4 NOT NULL, 
     outage_id           INT4 NOT NULL, 
     owner               VARCHAR(255) NOT NULL, 
     status              VARCHAR(255) NOT NULL, 
     type_of_item        VARCHAR(255) NOT NULL, 
     task_type           VARCHAR(255) NOT NULL,
     PRIMARY KEY (id) 
  );
  
  CREATE TABLE rfr_action_items 
  ( 
     id              SERIAL NOT NULL, 
     category        VARCHAR(255) NOT NULL, 
     due_date        TIMESTAMP NOT NULL, 
     item_title      VARCHAR(255) NOT NULL, 
     level_value     INT4 NOT NULL, 
     owner           VARCHAR(255) NOT NULL, 
     status          VARCHAR(255) NOT NULL, 
     type_of_item    VARCHAR(255) NOT NULL, 
     task_type       VARCHAR(255) NOT NULL,
     workflow_id int4 NOT NULL REFERENCES rfr_workflow(id),
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
  