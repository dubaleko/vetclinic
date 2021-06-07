package com.dubaleko.vetclinic.controller;

import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
class PetEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getPetEvents() throws Exception {
        this.mockMvc.perform(get("/api/petEvent?page=1&id=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(5)));
    }

    @Test
    void saveNewEvent() throws Exception {
        this.mockMvc.perform(post("/api/petEvent")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": \"1\", \"name\": \"Стерилизация\",\"date\": \"2021-06-02\",\n" +
                        " \"user\": {\"id\": 2,\"userName\": \"Admin\",\"role\": \"ADMIN\"}}")
        ).andExpect(status().isCreated());
    }

    @Test
    void updateEvent() throws Exception {
        this.mockMvc.perform(put("/api/petEvent")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": \"1\", \"name\": \"Стерилизация\",\"date\": \"2021-06-02\",\n" +
                        " \"user\": {\"id\": 2,\"userName\": \"Admin\",\"role\": \"ADMIN\"}}")
        ).andExpect(status().isOk());
    }

    @Test
    void deleteEvent() throws Exception {
        this.mockMvc.perform(delete("/api/petEvent?id=1"))
                .andExpect(status().isOk());
    }
}