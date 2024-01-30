package my.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import my.model.Employee;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceJavaClientImpl implements EmployeeService {

    private final ElasticsearchClient esClient;

    public EmployeeServiceJavaClientImpl(ElasticsearchClient esClient) {
        this.esClient = esClient;
    }

    @Override
    public List<Employee> getAllEmployees() {
        try {
            SearchResponse<Employee> response = esClient.search(s -> s.index("employees"), Employee.class);
            List<Employee> employees = new ArrayList<>();
            response.hits().hits().forEach(hit -> employees.add(hit.source()));
            return employees;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Employee getEmployeeById(String id) {
        try {
            GetResponse<Employee> response = esClient.get(g -> g.index("employees").id(id), Employee.class);
            return response.source();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String createEmployee(Employee employee) {
        try {
            IndexResponse response = esClient.index(i -> i.index("employees").document(employee));
            return response.id();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEmployeeById(String id) {
        try {
            DeleteResponse response = esClient.delete(d -> d.id(id));
            System.out.println(response.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Employee> search(String field, String value) {
        try {
            SearchResponse<Employee> response = esClient.search(s -> s
                    .index("employees")
                    .query(q -> q.match(m -> m
                            .field(field)
                            .query(value))
                    ), Employee.class);
            List<Employee> employees = new ArrayList<>();
            response.hits().hits().forEach(hit -> employees.add(hit.source()));
            return employees;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String aggregate(String aggregationField, String metricType, String metricField) {
        try {
            TermsAggregation termsAggregation = (new TermsAggregation.Builder()).field(aggregationField).build();

            Aggregation subAggregation = switch (metricType) {
                case "avg" -> new Aggregation.Builder()
                        .avg(new AverageAggregation.Builder().field(metricField).build())
                        .build();
                case "max" -> new Aggregation.Builder()
                        .max(new MaxAggregation.Builder().field(metricField).build())
                        .build();
                case "min" -> new Aggregation.Builder()
                        .min(new MinAggregation.Builder().field(metricField).build())
                        .build();
                default -> throw new UnsupportedOperationException();
            };

            SearchResponse<Employee> response = esClient.search(s -> s
                    .index("employees")
                    .size(0)
                    .aggregations("aggregation", a -> a
                            .terms(termsAggregation)
                            .aggregations("operation", subAggregation)), Employee.class);
            String buckets = response.aggregations().get("aggregation").sterms().buckets().toString();

            return buckets.substring(buckets.indexOf("["));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
