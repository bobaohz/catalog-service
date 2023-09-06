package com.polarbookshop.catalogservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@ConfigurationProperties(prefix = "polar")
public class PolarProperties {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * A message to welcome users.
     */
    private String greeting;

    private String name;


    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }


}