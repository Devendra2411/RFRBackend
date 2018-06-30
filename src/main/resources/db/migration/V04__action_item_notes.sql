CREATE TABLE rfr_action_item_notes (
  id serial NOT NULL,
  notes varchar(255) NOT NULL,
  action_items_id int4 REFERENCES rfr_action_items(id),
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