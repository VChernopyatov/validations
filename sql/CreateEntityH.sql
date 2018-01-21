CREATE TABLE entity_h
(
	entity_version_id SMALLSERIAL NOT NULL
		CONSTRAINT entity_h_pkey
			PRIMARY KEY,
	entity_id VARCHAR(50) NOT NULL,
	name VARCHAR(100),
	description VARCHAR(250),
	version SMALLINT NOT NULL,
	date TIMESTAMP WITH TIME ZONE NOT NULL,
	commentary VARCHAR(500),
	username VARCHAR(50),
	ip CIDR
);