drop table if exists recruitment_comment;
CREATE TABLE recruitment_comment (
                                  recruitment_comment_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                  content VARCHAR(255),
                                  is_deleted BIT NOT NULL,
                                  is_confidential BIT NOT NULL,
                                  member_id BIGINT,
                                  recruitment_id BIGINT,
                                  parent_comment_id BIGINT,
                                  created_at datetime(6),
                                  updated_at datetime(6),
                                  FOREIGN KEY (member_id) REFERENCES member (member_id),
                                  FOREIGN KEY (recruitment_id) REFERENCES recruitment (recruitment_id),
                                  FOREIGN KEY (parent_comment_id) REFERENCES recruitment_comment (recruitment_comment_id)
);