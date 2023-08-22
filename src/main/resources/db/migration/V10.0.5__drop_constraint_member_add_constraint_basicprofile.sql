alter table member
    drop constraint FKr9dj1m8dp10fb18x9wmoppd01;

alter table member
    drop column basic_profile_id;

alter table basic_profile
    add constraint FKrce05ncmyjbnso2x9wmop113v
    foreign key (member_id)
    references member (member_id);
