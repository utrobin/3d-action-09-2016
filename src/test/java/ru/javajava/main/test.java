package ru.javajava.main;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by ivan on 05.10.16.
 */

public class test {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void testSomething() {
        URI uri = URI.create("http://localhost:8080/api/signup");
       RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("login", "mad");
        body.add("password", "123123");
        body.add("email", "xyz@mail.ru");


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> requestEntity = new HttpEntity<Object>(body, headers);

        ResponseEntity<Response> responseEntity = restTemplate.exchange(uri, HttpMethod.POST,
                requestEntity, Response.class);

        Response response = responseEntity.getBody();
        assert (response.getLogin().equals("madsunrise"));
    }



    static final class Response {
        private String login;
        private String email;

        public Response() { }

        public Response(String login, String email) {
            this.login = login;
            this.email = email;
        }

        @SuppressWarnings("unused")
        public String getLogin() {
            return login;
        }
        @SuppressWarnings("unused")
        public String getEmail() {
            return email;
        }
    }
}

