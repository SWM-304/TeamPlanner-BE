package com.tbfp.teamplannerbe;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;

@SpringBootApplication
public class TeamPlannerBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeamPlannerBeApplication.class, args);
    }

}
