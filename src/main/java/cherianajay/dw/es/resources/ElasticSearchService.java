package cherianajay.dw.es.resources;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author "cherianajay"
 *
 */
@Path("/employees")
@Produces(MediaType.APPLICATION_JSON)
public class ElasticSearchService {

	final  Logger log  = LoggerFactory.getLogger(this.getClass());
	// Reference to Elastic search client
	public Client client;
	
	
	/**
	 * Default constructor
	 */
	public ElasticSearchService() {
	}
	
	/**
	 * Constructor with elastic search client
	 * @param client
	 */
	public ElasticSearchService(Client client) {
		this.client = client;
	}
	
	
	/**
	 * Fetch employee by Id
	 * @param empId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{empId}")
	public ArrayList fetchEmployeeById(@PathParam("empId") String empId) {
		IdsQueryBuilder qb = QueryBuilders.idsQuery("employee"); 

		qb.addIds(empId); 
		SearchRequestBuilder searchBuilder = client.prepareSearch("employee_index")
				.setQuery(qb);
		

		log.info("Query:\n" + searchBuilder.internalBuilder());

		SearchResponse search = searchBuilder.get();
		ArrayList employeeList = new ArrayList();
		SearchHit[] results = search.getHits().getHits();
		for (SearchHit hit : results) {
			employeeList.add(hit.getSource());
		}
		return employeeList;
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/search")
	public ArrayList fetchEmployeeByWildcardSearch(@QueryParam("wildcard") String wildcardSearch) {
		
		if(wildcardSearch==null){
			return null;	
		}
		// for some reason wildcard does not work with uppercase. But below will fetch results in either case
		QueryBuilder qb = QueryBuilders.wildcardQuery("firstName", wildcardSearch.toLowerCase());

		SearchRequestBuilder searchBuilder = client.prepareSearch("employee_index")
				.setQuery(qb).setTypes("employee");
		
		log.info("Query:\n" + searchBuilder.internalBuilder());

		SearchResponse search = searchBuilder.get();
		ArrayList employeeList = new ArrayList();
		SearchHit[] results = search.getHits().getHits();
		for (SearchHit hit : results) {
			employeeList.add(hit.getSource());
		}
		return employeeList;
	}
	
	
	
}