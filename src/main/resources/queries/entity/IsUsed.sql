select exists(select null from validation_entity WHERE entity_id = :id)