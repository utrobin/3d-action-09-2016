package ru.javajava.main;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by ivan on 10.10.16.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@Transactional
public class RegistrationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final String defaultEmail = "example@mail.ru";
    private final String defaultLogin = "Nick";
    private final String defaultPassword = "12345";


    @Test
    public void signup() throws Exception {
        final String user = "{\"login\": \"" + defaultLogin + "\"," +
                " \"password\": \" " + defaultPassword + "\", " +
                "\"email\": \"" + defaultEmail + "\"}";
        mockMvc.perform(post("/api/signup/")
                .content(user)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("email").value(defaultEmail))
        .andExpect(jsonPath("login").value(defaultLogin));
    }


    @Test
    public void login() throws Exception {
        signup();
        final String user = "{\"login\": \"" + defaultLogin + "\"," +
                " \"password\": \" " + defaultPassword + "\"}";
        mockMvc.perform(post("/api/login/")
                .content(user)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(defaultEmail))
                .andExpect(jsonPath("login").value(defaultLogin));

    }

    @Test
    public void testVisits() throws Exception {
        signup();
        final String user = "{\"login\": \"" + defaultLogin + "\"," +
                " \"password\": \" " + defaultPassword + "\"}";
        mockMvc.perform(post("/api/login/")
                .content(user)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("amount").value(2));
        mockMvc.perform(post("/api/login/")
                .content(user)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("amount").value(3));
    }

}