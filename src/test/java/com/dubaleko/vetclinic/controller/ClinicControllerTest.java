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
class ClinicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getClinics() throws Exception {
        this.mockMvc.perform(get("/api/clinic?page=1&city=Слуцк"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(2)));
    }

    @Test
    void getCityClinics() throws Exception {
        this.mockMvc.perform(get("/api/clinic/cities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)));
    }

    @Test
    void getPreviewClinics() throws Exception {
        this.mockMvc.perform(get("/api/clinic/preview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)));
    }

    @Test
    void getAllClinics() throws Exception {
        this.mockMvc.perform(get("/api/clinic/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(8)));
    }

    @Test
    void addNewClinic() throws Exception {
        this.mockMvc.perform(post("/api/clinic")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": \"1\",\"address\": \"Адрес\",\"name\": \"Клиника\"" +
                        ",\"city\": \"Город\",\"description\": \"Описание\"}")
        ).andExpect(status().isCreated());
    }

    @Test
    void editClinic() throws Exception {
        this.mockMvc.perform(put("/api/clinic")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": \"1\",\"address\": \"Адрес\",\"name\": \"Клиника\"" +
                        ",\"city\": \"Город\",\"description\": \"Описание\"}")
        ).andExpect(status().isOk());
    }

    @Test
    void deleteClinic() throws Exception {
        this.mockMvc.perform(delete("/api/clinic?id=1"))
                .andExpect(status().isOk());
    }
}