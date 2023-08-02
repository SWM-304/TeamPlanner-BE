ALTER TABLE member ADD COLUMN basic_profile_id BIGINT;

ALTER TABLE member
    ADD FOREIGN KEY (basic_profile_id) REFERENCES basic_profile (basic_profile_id);

ALTER TABLE member
    ADD CONSTRAINT FKr5ehn466nc0myp5hns4owmopr
        foreign key (basic_profile_id)
            references basic_profile (basic_profile_id);
