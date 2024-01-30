package my.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import my.model.Employee;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceLowLevelImpl implements EmployeeService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public EmployeeServiceLowLevelImpl(RestClient restClient) {
        this.restClient = restClient;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<Employee> getAllEmployees() {
        try {
            Request request = new Request("GET", "/employees/_search");
            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonNode responseNode = new ObjectMapper().readTree(responseBody);

            List<Employee> employees = new ArrayList<>();
            // iterating through array of employee docs
            for (JsonNode employeeNode : responseNode.get("hits").get("hits")) {
                JsonNode sourceNode = employeeNode.get("_source");
                Employee employee = objectMapper.treeToValue(sourceNode, Employee.class);
                employees.add(employee);
            }
            return employees;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Employee getEmployeeById(String id) {
        try {
            Request request = new Request("GET", "/employees/_doc/" + id);
            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println(responseBody);
            JsonNode responseNode = new ObjectMapper().readTree(responseBody);
            JsonNode sourceNode = responseNode.get("_source");
            Employee employee = objectMapper.treeToValue(sourceNode, Employee.class);
            return employee;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // returns id of created employee
    @Override
    public String createEmployee(Employee employee) {
        try {
            String requestBody = objectMapper.writeValueAsString(employee);
            Request request = new Request("POST", "/employees/_doc");
            request.setJsonEntity(requestBody);

            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonNode responseNode = new ObjectMapper().readTree(responseBody);
            return responseNode.get("_id").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEmployeeById(String id) {
        try {
            Request request = new Request("DELETE", "/employees/_doc/" + id);
            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println(responseBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Employee> search(String field, String value) {
        try {
            String requestBody = String.format(""" 
                    {
                        "query": {
                            "match": {
                                "%s": "%s"
                            }
                        }
                    }
                    """, field, value);
            Request request = new Request("GET", "/employees/_search");
            request.setJsonEntity(requestBody);

            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonNode responseNode = new ObjectMapper().readTree(responseBody);

            List<Employee> employees = new ArrayList<>();
            // iterating through array of employee docs
            for (JsonNode employeeNode : responseNode.get("hits").get("hits")) {
                JsonNode sourceNode = employeeNode.get("_source");
                Employee employee = objectMapper.treeToValue(sourceNode, Employee.class);
                employees.add(employee);
            }

            return employees;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String aggregate(String aggregationField, String metricType, String metricField) {
        try {
            String requestBody = String.format("""
                    {
                        "size": 0,
                        "aggs": {
                            "aggregation": {
                                "terms": {
                                    "field": "%s"
                                },
                                "aggs": {
                                    "operation": {
                                        "%s": {
                                            "field": "%s"
                                        }
                                    }
                                }
                            }
                        }
                    }
                    """, aggregationField, metricType, metricField);

            Request request = new Request("GET", "/employees/_search");
            request.setJsonEntity(requestBody);

            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonNode responseNode = new ObjectMapper().readTree(responseBody);
            String buckets = responseNode.get("aggregations").get("aggregation").get("buckets").toPrettyString();

            return buckets;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
