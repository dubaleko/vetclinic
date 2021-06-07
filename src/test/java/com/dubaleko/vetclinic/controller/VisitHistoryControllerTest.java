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
class VisitHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getVisitHistory() throws Exception {
        this.mockMvc.perform(get("/api/visitHistory?page=1&id=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageList",hasSize(9)));
    }

    @Test
    void getVisitHistoryForUser() throws Exception {
        this.mockMvc.perform(get("/api/visitHistory/profile?page=1&id=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageList",hasSize(1)));
    }

    @Test
    void saveNewEvent() throws Exception {
        this.mockMvc.perform(post("/api/visitHistory?orderId=13")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": \"1\",\"date\": \"2021-06-04\",\"article\": \"Инфа\"" +
                        ",\"user\": {\"id\": 4,\"userName\": \"Никита\",\"role\": \"USER\"}" +
                        ",\"employee\": {\"id\": 14,\"name\": \"Валик\",\"education\": \"Образование\"}}")
        ).andExpect(status().isCreated());
    }

    @Test
    void updateEvent() throws Exception {
        this.mockMvc.perform(put("/api/visitHistory")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": \"1\",\"date\": \"2021-06-04\",\"article\": \"Инфа\"" +
                        ",\"user\": {\"id\": 4,\"userName\": \"Никита\",\"role\": \"USER\"}" +
                        ",\"employee\": {\"id\": 14,\"name\": \"Валик\",\"education\": \"Образование\"}}")
        ).andExpect(status().isOk());
    }

    @Test
    void deleteEvent() throws Exception {
        this.mockMvc.perform(delete("/api/visitHistory?id=1"))
                .andExpect(status().isOk());
    }
}