package com.mitrais.brainstorm.persistence.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

/**
 *
 */
@RequiredArgsConstructor
public class MongoConfig extends AbstractReactiveMongoConfiguration {
	private final Environment environment;

	@Bean
	@DependsOn("embeddedMongoServer")
	@Override
	public MongoClient reactiveMongoClient() {
		int port = environment.getRequiredProperty("local.mongo.port", Integer.class);
		return MongoClients.create(String.format("mongodb://localhost:%d", port));
	}

	@Override
	protected String getDatabaseName() {
		return "brainstorm";
	}
}
