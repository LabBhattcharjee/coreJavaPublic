package com.example.demo.config;

import java.net.InetSocketAddress;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@PropertySource(value = { "classpath:cassandra.properties" })
@EnableCassandraRepositories(basePackages = "com.example.demo.repository")
public class CassandraConfig extends AbstractCassandraConfiguration {
	// private static final Log LOGGER = LogFactory.getLog(CassandraConfig.class);

	@Autowired
	private Environment environment;

	@Override
	protected String getKeyspaceName() {
		return environment.getProperty("cassandra.keyspace");
	}
	
	@Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.RECREATE;
        		//RECREATE_DROP_UNUSED;
    }

//    @Bean
//    public CassandraOperations cassandraOperations() throws Exception {
//        return new CassandraTemplate(session().getObject());
//    }

//    @Override
//    @Bean
//    public CassandraClusterFactoryBean cluster() {
//        final CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
//        cluster.setContactPoints(environment.getProperty("cassandra.contactpoints"));
//        cluster.setPort(Integer.parseInt(environment.getProperty("cassandra.port")));
//        LOGGER.info("Cluster created with contact points [" + environment.getProperty("cassandra.contactpoints") + "] " + "& port [" + Integer.parseInt(environment.getProperty("cassandra.port")) + "].");
//        return cluster;
//    }
	
	//https://stackoverflow.com/questions/64910934/cassandra-authentication-issue-after-upgrading-to-spring-boot-2-3-5-release

	@Bean
    @Override
    public CqlSessionFactoryBean cassandraSession() {
        final CqlSessionFactoryBean cassandraSession = super.cassandraSession();//super session should be called only once
//        cassandraSession.setUsername(username);
//        cassandraSession.setPassword(password);
        final String contactPoint = environment.getProperty("cassandra.contactpoints");
		cassandraSession.setContactPoints(Collections.singleton(new InetSocketAddress(contactPoint, Integer.parseInt(environment.getProperty("cassandra.port")))));
        return cassandraSession;
    }

//	@Override
//	@Bean
//	public CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
//		return new CassandraMappingContext();
//	}
}