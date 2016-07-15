package cherianajay.dw.es;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import io.dropwizard.elasticsearch.config.EsConfiguration;

public class ApplicationConfig extends Configuration {
	
	private EsConfiguration esConfiguration = new EsConfiguration();

	public EsConfiguration getEsConfiguration() {
		return esConfiguration;
	}

	public void setEsConfiguration(EsConfiguration esConfiguration) {
		this.esConfiguration = esConfiguration;
	}
	
	    
}
