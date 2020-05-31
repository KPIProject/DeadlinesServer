package com.ddanilyuk.DeadlinesServer.controllers;

import antlr.build.Tool;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest  {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void passwordEncoder() {
    }

    @Test
    public void testFindAllUsers() throws Exception {
        this.mockMvc.perform(get("/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findByUserNegativeTest() throws Exception {
        this.mockMvc.perform(get("/findByUsername/denis")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getRegistrationTest() throws Exception {
        Map<String, String> states = new HashMap<String, String>();
        states.put("userFirstName", "Denis");
        states.put("userSecondName", "Danil");
        states.put("username", "dd");
        states.put("password", "12345");
        this.mockMvc.perform( MockMvcRequestBuilders
                .post("/registration")
                .content(String.valueOf(new JSONObject(states)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").exists());
    }

    @Test
    public void getLoginTest() throws Exception {
        Map<String, String> states = new HashMap<String, String>();
        states.put("username", "dd");
        states.put("password", "12345");
        this.mockMvc.perform( MockMvcRequestBuilders
                .post("/login")
                .content(String.valueOf(new JSONObject(states)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").exists());
    }

    @Test
    public void findByUserPositiveTest() throws Exception {
        this.mockMvc.perform(get("/findByUsername/dd")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}