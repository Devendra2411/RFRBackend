CREATE TABLE rfr_action_item_files 
  ( 
     id                    SERIAL NOT NULL, 
     file_name             VARCHAR(255) NOT NULL, 
     file_path             VARCHAR(255) NOT NULL, 
     file_size             INT8 NOT NULL, 
     created_by_firstname  VARCHAR(255), 
     created_by_lastname   VARCHAR(255), 
     created_by_sso        VARCHAR(255), 
     created_date          TIMESTAMP, 
     modified_by_firstname VARCHAR(255), 
     modified_by_lastname  VARCHAR(255), 
     modified_by_sso       VARCHAR(255), 
     modified_date         TIMESTAMP, 
     action_item_id        INT4 NOT NULL REFERENCES rfr_action_items(id),
     PRIMARY KEY (id) 
  ) 