package com.kunal.SunbaseDataAssignment.dao.customer;

import com.kunal.SunbaseDataAssignment.models.Customer;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class CustomerDAO {
    private String apiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";
    private String loginUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";

    public Customer[] fetchAllRecords(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Customer[]> response = restTemplate.exchange(
                apiUrl+"?cmd=get_customer_list", // Replace with your API endpoint
                HttpMethod.GET,
                requestEntity,
                Customer[].class
        );
        return response.getBody();
    }

    public String getLoginToken(String login_id, String password) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String loginJSON = "{\"login_id\":\"" + login_id + "\",\"password\":\"" + password + "\"}";

        HttpEntity<String> requestEntity = new HttpEntity<>(loginJSON, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
            loginUrl,
            HttpMethod.POST,
            requestEntity,
            String.class
        );


        String loginToken = response.getBody();
        JSONObject jsonObject = new JSONObject(loginToken);
        return jsonObject.get("access_token").toString();
    }

    public boolean delete(String uuid, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
            apiUrl+"?cmd=delete&uuid="+uuid,
            HttpMethod.POST,
            requestEntity,
            String.class
        );
        return response.getStatusCode().is2xxSuccessful();
    }


    public boolean create(Customer customer, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        JSONObject jsonObject = new JSONObject(customer);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonObject.toString(), headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
        apiUrl+"?cmd=create",
            HttpMethod.POST,
            requestEntity,
            String.class
        );
        return response.getStatusCode().is2xxSuccessful();
    }

    public boolean update(Customer customer, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        JSONObject jsonObject = new JSONObject(customer);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonObject.toString(), headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl+"?cmd=update&uuid="+ customer.getUuid(),
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        return response.getStatusCode().is2xxSuccessful();
    }

    public Customer getCustomerByUUID(String uuid, String token) {
        Customer[] customers = this.fetchAllRecords(token);
        for(Customer customer: customers) {
            if(customer.getUuid().equals(uuid)) {
                return customer;
            }
        }
        return null;
    }
}
