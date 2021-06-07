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
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void saveUser() throws Exception {
        this.mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": \"1\",\"userName\": \"Admin\",\"password\": \"123456\"}")
                ).andExpect(status().isCreated());
    }

    @Test
    void getUsers() throws Exception {
        this.mockMvc.perform(get("/api/users?page=1&name=Admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageList",hasSize(1)));
    }

    @Test
    void updateUser() throws Exception {
        this.mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": \"1\",\"userName\": \"Admin\",\"password\": \"123456\"}")
        ).andExpect(status().isOk());
    }

    @Test
    void deleteUserById() throws Exception {
        this.mockMvc.perform(delete("/api/users?id=1"))
                .andExpect(status().isOk());
    }
}