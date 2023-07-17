drop table if exists recruitment;

CREATE TABLE recruitment (
                             recruitment_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                             title VARCHAR(255),
                             content VARCHAR(255),
                             max_member_size INT DEFAULT 0,
                             current_member_size INT DEFAULT 0,
                             board_id BIGINT,
                             member_id BIGINT,
                             view_count INT DEFAULT 0,
                             like_count INT DEFAULT 0,
                             created_at DATETIME(6),
                             updated_at DATETIME(6),
                             FOREIGN KEY (board_id) REFERENCES board (board_id),
                             FOREIGN KEY (member_id) REFERENCES member (member_id)
) engine=InnoDB;

