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
class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getServices() throws Exception {
        this.mockMvc.perform(get("/api/service?page=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(19)));
    }

    @Test
    void getPreviewService() throws Exception {
        this.mockMvc.perform(get("/api/service/preview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(6)));
    }

    @Test
    void addNewService() throws Exception {
        this.mockMvc.perform(post("/api/service")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": \"1\",\"serviceName\": \"Service\",\"serviceCost\": \"12\"" +
                        ",\"serviceType\": {\"id\":1,\"name\":\"Анестезия и интенсивная терапия\"}," +
                        "\"clinic\":{\"id\":1,\"name\":\"Айболит\",\"city\":\"Солигорск\",\"address\":\"Шахтёрная 24\"," +
                        "\"description\":\"Ветеринарная клиника с большим опытом ухода за животными\"}}")
        ).andExpect(status().isCreated());
    }

    @Test
    void editService() throws Exception {
        this.mockMvc.perform(put("/api/service")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": \"1\",\"serviceName\": \"Service\",\"serviceCost\": \"12\"" +
                        ",\"serviceType\": {\"id\":1,\"name\":\"Анестезия и интенсивная терапия\"}," +
                        "\"clinic\":{\"id\":1,\"name\":\"Айболит\",\"city\":\"Солигорск\",\"address\":\"Шахтёрная 24\"," +
                        "\"description\":\"Ветеринарная клиника с большим опытом ухода за животными\"}}")
        ).andExpect(status().isOk());
    }

    @Test
    void deleteService() throws Exception {
        this.mockMvc.perform(delete("/api/service?id=1"))
                .andExpect(status().isOk());
    }
}