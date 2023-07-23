drop table if exists tech_stack_item;
drop table if exists tech_stack;
drop table if exists activity;
drop table if exists certification;
drop table if exists evaluation;
drop table if exists tech_stack_item_activity_mapping;
CREATE TABLE tech_stack_item(
                                tech_stack_item_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                name VARCHAR(255) NOT NULL,
                                tech_category VARCHAR(255) NOT NULL,
                                user_generated bit DEFAULT FALSE,
                                created_at DATETIME(6),
                                updated_at DATETIME(6)
) engine=InnoDB;
CREATE TABLE tech_stack (
                             tech_stack_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                             experienced_year INT DEFAULT 0,
                             experienced_month INT DEFAULT 0,
                             skill_level VARCHAR(255),
                             tech_stack_item_id BIGINT,
                             member_id BIGINT,
                             created_at DATETIME(6),
                             updated_at DATETIME(6),
                             FOREIGN KEY (tech_stack_item_id) REFERENCES tech_stack_item (tech_stack_item_id),
                             FOREIGN KEY (member_id) REFERENCES member (member_id)
) engine=InnoDB;
CREATE TABLE activity (
                            activity_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            subject VARCHAR(255) NOT NULL,
                            detail VARCHAR(255),
                            start_date DATE,
                            end_date DATE,
                            member_id BIGINT,
                            created_at DATETIME(6),
                            updated_at DATETIME(6),
                            FOREIGN KEY (member_id) REFERENCES member (member_id)
) engine=InnoDB;
CREATE TABLE certification (
                            certification_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            score DOUBLE,
                            gain_date DATE,
                            member_id BIGINT,
                            created_at DATETIME(6),
                            updated_at DATETIME(6),
                            FOREIGN KEY (member_id) REFERENCES member (member_id)
) engine=InnoDB;
CREATE TABLE evaluation (
                            evaluation_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            comment VARCHAR(255),
                            stat1 INTEGER DEFAULT 0,
                            stat2 INTEGER DEFAULT 0,
                            stat3 INTEGER DEFAULT 0,
                            stat4 INTEGER DEFAULT 0,
                            stat5 INTEGER DEFAULT 0,
                            author_member_id BIGINT,
                            subject_member_id BIGINT,
                            created_at DATETIME(6),
                            updated_at DATETIME(6),
                            FOREIGN KEY (author_member_id) REFERENCES member (member_id),
                            FOREIGN KEY (subject_member_id) REFERENCES member (member_id)
) engine=InnoDB;
CREATE TABLE tech_stack_item_activity_mapping (
                            tech_stack_item_activity_mapping_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            tech_stack_item_id BIGINT,
                            activity_id BIGINT,
                            created_at DATETIME(6),
                            updated_at DATETIME(6),
                            FOREIGN KEY (tech_stack_item_id) REFERENCES tech_stack_item (tech_stack_item_id),
                            FOREIGN KEY (activity_id) REFERENCES activity (activity_id)
) engine=InnoDB;