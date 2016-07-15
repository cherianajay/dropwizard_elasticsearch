# dropwizard_elasticsearch

Dropwizard example to connect to Elasticsearch using "dropwizard-elasticsearch" module

Setup:
- Bring up an instance of local elasticsearch and/or cluster
- Use the sample data file attached directly to push data to elasticsearch or use "sense" chrome plugin.
- Run the server  using  **java -Des.path.home=/  -jar dropwizard-es.jar server**

*Please note the additional parameter path.home is a parameter required by "dropwizard-elasticsearch" module but not used

Use following URLs to test the service
- http://localhost:8080/employees/search?wildcard=J*e 
- http://localhost:8080/employees/1100
