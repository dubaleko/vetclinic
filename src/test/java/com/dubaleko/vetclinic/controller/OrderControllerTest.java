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
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getOrders() throws Exception {
        this.mockMvc.perform(get("/api/order?id=1&page=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageList",hasSize(1)));
    }

    @Test
    void saveOrder() throws Exception {
        this.mockMvc.perform(post("/api/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"employee\": {\"id\": 13,\"name\": \"Дубалеко\",\"education\": \"Высшее\",\"position\": \"Врач\"," +
                        " \"clinic\": {\"id\": 1,\"name\": \"Айболит\",\"city\": \"Солигорск\",\"address\": \"Шахтёрная 24\",\n" +
                        "  \"description\": \"Ветеринарная клиника с большим опытом ухода за животными\"},\"specs\": [{" +
                        "  \"id\": 1,\"name\": \"Дерматология\"}],\"onVacation\": false,\"days\": [{\"id\": 1,\"dayName\": \"Понедельник\"" +
                        "  }],\"startWork\": {\"id\": 1,\"time\": \"07:00:00\"},\"endWork\": {\"id\": 7,\"time\": \"09:30:00\"}}," +
                        "  \"receptionDate\": {\"id\": 1,\"date\": \"2021-06-07\"},\"receptionTime\": " +
                        "  {\"id\": 1,\"occupied\": false,\"time\": \"07:00:00\"}, \"user\": {\"id\": 1,\"userName\": \"Admin\"," +
                        "  \"password\": \"$2a$10$J.72dUEaRAq18CzL3zXru.DdMYe4Byc1XeD6sqhYnlw45MkUJLPIS\",\n" +
                        "  \"petName\": null,\"petType\": null,\"petGender\": null,\"petAge\": 0,\"ownerName\": null," +
                        "  \"ownerSecondName\": null,\"role\": \"ADMIN\",\"clinic\": null,\"doctor\": null}}")
        ).andExpect(status().isCreated());
    }

    @Test
    void refuseOrder() throws Exception {
        this.mockMvc.perform(delete("/api/order?id=1"))
                .andExpect(status().isOk());
    }
}