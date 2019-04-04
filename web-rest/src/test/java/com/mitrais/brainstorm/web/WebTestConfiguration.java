package com.mitrais.brainstorm.web;

import com.mitrais.brainstorm.persistence.PersistenceConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 *
 */
@SpringBootConfiguration
@EnableAutoConfiguration
@Import(PersistenceConfiguration.class)
class WebTestConfiguration extends WebConfiguration {

}