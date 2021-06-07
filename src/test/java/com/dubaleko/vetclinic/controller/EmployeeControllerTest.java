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
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void list() throws Exception {
        this.mockMvc.perform(get("/api/employee?page=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageList",hasSize(5)));
    }

    @Test
    void allEmployees() throws Exception {
        this.mockMvc.perform(get("/api/employee/all"))
                .andExpect(status().isOk());
    }

    @Test
    void getWorkTime() throws Exception {
        this.mockMvc.perform(get("/api/employee/workTime"))
                .andExpect(status().isOk());
    }

    @Test
    void addNewEmployee() throws Exception {
        this.mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": \"1\",\"name\": \"ФИО\",\"position\": \"Врач\"" +
                        ",\"specs\": [{\"id\":1,\"name\":\"Дерматология\"},{\"id\":11,\"name\":\"Эндокринология\"}]" +
                        ",\"days\": [],\"startWork\": {\"id\":1,\"time\":\"07:00:00\"}" +
                        ",\"onVacation\": true,\"endWork\": {\"id\":19,\"time\":\"14:30:00\"}," +
                        "\"clinic\":{\"id\":1,\"name\":\"Айболит\",\"city\":\"Солигорск\",\"address\":\"Шахтёрная 24\"," +
                        "\"description\":\"Ветеринарная клиника с большим опытом ухода за животными\"}}")
        ).andExpect(status().isCreated());
    }

    @Test
    void updateEmployee() throws Exception {
        this.mockMvc.perform(put("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": \"1\",\"name\": \"ФИО\",\"position\": \"Врач\"" +
                        ",\"specs\": [{\"id\":1,\"name\":\"Дерматология\"},{\"id\":11,\"name\":\"Эндокринология\"}]" +
                        ",\"days\": [],\"startWork\": {\"id\":1,\"time\":\"07:00:00\"}" +
                        ",\"onVacation\": true,\"endWork\": {\"id\":19,\"time\":\"14:30:00\"}," +
                        "\"clinic\":{\"id\":1,\"name\":\"Айболит\",\"city\":\"Солигорск\",\"address\":\"Шахтёрная 24\"," +
                        "\"description\":\"Ветеринарная клиника с большим опытом ухода за животными\"}}")
        ).andExpect(status().isOk());
    }

    @Test
    void deleteEmployee() throws Exception {
        this.mockMvc.perform(delete("/api/employee?id=1"))
                .andExpect(status().isOk());
    }
}