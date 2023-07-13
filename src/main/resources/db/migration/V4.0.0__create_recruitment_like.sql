drop table if exists recruitment_like;
CREATE TABLE recruitment_like (
                                  recruitment_like_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                  member_id BIGINT,
                                  recruitment_id BIGINT,
                                  FOREIGN KEY (member_id) REFERENCES member (member_id),
                                  FOREIGN KEY (recruitment_id) REFERENCES recruitment (recruitment_id)
);