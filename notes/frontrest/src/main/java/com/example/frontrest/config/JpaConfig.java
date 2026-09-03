package com.example.frontrest.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan({
    "com.example.note.model"
})
@EnableJpaRepositories({
    "com.example.note.repository"
})
public class JpaConfig {

}