ALTER TABLE basic_profile
    CHANGE COLUMN profile_id basic_profile_id BIGINT AUTO_INCREMENT PRIMARY KEY;

ALTER TABLE member
    ADD COLUMN basic_profile_id BIGINT;

alter table member
    add constraint FKr9dj1m8dp10fb18x9wmoppd01
        foreign key (basic_profile_id)
            references basic_profile (basic_profile_id);
