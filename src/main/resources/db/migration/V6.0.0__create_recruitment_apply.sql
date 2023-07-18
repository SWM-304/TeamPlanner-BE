drop table if exists recruitment_apply;
CREATE TABLE recruitment_apply (
                                   recruitment_apply_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                   content VARCHAR(255),
                                   state VARCHAR(255),
                                   recruitment_id BIGINT,
                                   member_id BIGINT,
                                   created_at TIMESTAMP(6),
                                   updated_at TIMESTAMP(6),
                                   FOREIGN KEY (recruitment_id) REFERENCES recruitment (recruitment_id),
                                   FOREIGN KEY (member_id) REFERENCES member (member_id)
)engine=InnoDB;
