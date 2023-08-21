ALTER TABLE basic_profile drop column education_grade;
ALTER TABLE basic_profile
    ADD admission_date DATE,
    ADD graduation_date DATE;