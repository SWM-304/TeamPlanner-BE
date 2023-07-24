ALTER TABLE profile
    RENAME TO basic_profile,
    ADD created_at DATETIME(6),
    ADD updated_at DATETIME(6),
    ADD evaluation_public bit DEFAULT FALSE;
