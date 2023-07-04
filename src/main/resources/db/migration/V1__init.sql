drop table if exists profile;
drop table if exists comment;
drop table if exists board;
drop table if exists member;
create table board (
                       board_id bigint not null auto_increment,
                       created_at datetime(6),
                       updated_at datetime(6),
                       activity_detail MEDIUMTEXT,
                       activity_name varchar(255),
                       activity_area varchar(255),
                       activity_benefits varchar(255),
                       activity_field varchar(255),
                       activity_img varchar(255),
                       activity_key varchar(255),
                       activity_period varchar(255),
                       activity_url TEXT,
                       category varchar(255),
                       company_type varchar(255),
                       competition_category varchar(255),
                       homepage varchar(255),
                       interest_area varchar(255),
                       meeting_time varchar(255),
                       preferred_skills varchar(255),
                       prize_scale varchar(255),
                       recruitment_count varchar(255),
                       recruitment_period varchar(255),
                       target varchar(255),
                       member_id bigint,
                       primary key (board_id)
) engine=InnoDB;
create table comment (
                         comment_id bigint not null auto_increment,
                         created_at datetime(6),
                         updated_at datetime(6),
                         content varchar(255) not null,
                         depth integer not null,
                         is_confidential bit not null,
                         state bit not null,
                         board_id bigint,
                         member_id bigint,
                         parent_comment_id bigint,
                         primary key (comment_id)
) engine=InnoDB;
create table member (
                        member_id bigint not null auto_increment,
                        created_at datetime(6),
                        updated_at datetime(6),
                        email varchar(255),
                        login_id varchar(300),
                        member_role varchar(255),
                        password varchar(300),
                        phone varchar(255),
                        provider_id varchar(255),
                        provider_type varchar(255),
                        state bit,
                        primary key (member_id)
) engine=InnoDB;
create table profile (
                         profile_id bigint not null auto_increment,
                         address varchar(255),
                         birth date,
                         contact_email varchar(255),
                         education varchar(255),
                         education_grade integer not null,
                         gender varchar(255),
                         is_public bigint,
                         job varchar(255),
                         kakao_id varchar(255),
                         profile_image varchar(255),
                         profile_intro varchar(1000),
                         member_id bigint,
                         primary key (profile_id)
) engine=InnoDB;
alter table board
    add constraint FKsds8ox89wwf6aihinar49rmfy
        foreign key (member_id)
            references member (member_id);
alter table comment
    add constraint FKlij9oor1nav89jeat35s6kbp1
        foreign key (board_id)
            references board (board_id);
alter table comment
    add constraint FKmrrrpi513ssu63i2783jyiv9m
        foreign key (member_id)
            references member (member_id);
alter table comment
    add constraint FKhvh0e2ybgg16bpu229a5teje7
        foreign key (parent_comment_id)
            references comment (comment_id);
alter table profile
    add constraint FKrce05ncmyjbnso2x9wmop113v
        foreign key (member_id)
            references member (member_id);
