CREATE TABLE entity
(
	entity_id VARCHAR(50) NOT NULL
		CONSTRAINT entity_pkey
			PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	description VARCHAR(250),
	version SMALLINT DEFAULT 1 NOT NULL,
	commentary VARCHAR(500) NOT NULL
);