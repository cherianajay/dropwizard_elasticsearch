package cherianajay.dw.es;

import java.util.EnumSet;
import java.util.HashMap;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.validation.Validation;
import javax.validation.Validator;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cherianajay.dw.es.resources.ElasticSearchService;
import io.dropwizard.Application;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.elasticsearch.config.EsConfiguration;
import io.dropwizard.elasticsearch.health.EsClusterHealthCheck;
import io.dropwizard.elasticsearch.managed.ManagedEsClient;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ElasticSearchApplication extends Application<ApplicationConfig> {
	
	public static void main(String[] args) throws Exception {
		new ElasticSearchApplication().run(args);
	}

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	private final ConfigurationFactory<EsConfiguration> configFactory = new ConfigurationFactory<>(
			EsConfiguration.class, validator, Jackson.newObjectMapper(), "");

	@Override
	public String getName() {
		return "dropwizard-es";
	}

	@Override
	public void initialize(Bootstrap<ApplicationConfig> bootstrap) {
	}

	@Override
	public void run(ApplicationConfig configuration, Environment environment) throws Exception {

		ManagedEsClient managedEsClient = new ManagedEsClient(configuration.getEsConfiguration());
		managedEsClient.start();
		// Get a managed client
		Client client = managedEsClient.getClient();
		Logger log  = LoggerFactory.getLogger(this.getClass());
		//dropwizard elastic-search connectors has an in built EsClusterHealthCheck
		//you can register more checks as required
		environment.healthChecks().register("ES cluster health", new EsClusterHealthCheck(client));

		environment.jersey().register(new ElasticSearchService(client));
		
		
		
		// @todo
		// below section allows for configuring CORS settings in the service
		// currently the config is hardcoded but needs to be externalized
		HashMap<String,String> corsMap = new HashMap<String,String>();
		corsMap.put("allowedOrigins", "*");
		corsMap.put("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
		corsMap.put("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
		
		final FilterRegistration.Dynamic cors =environment.servlets().addFilter("CORS",CrossOriginFilter.class);
		cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
		cors.setInitParameters(corsMap);

		
	}
}
