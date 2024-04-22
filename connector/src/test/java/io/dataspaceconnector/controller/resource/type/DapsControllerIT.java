/*
 * Copyright 2022 sovity GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.dataspaceconnector.controller.resource.type;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.Calendar;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class DapsControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser("ADMIN")
    void create_validInput_returnNew() throws Exception {
        mockMvc.perform(post("/api/daps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"location\":\"http://example.org/" + Calendar.getInstance().getTimeInMillis() + "\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser("ADMIN")
    void getAll_validInput_returnObj() throws Exception {
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/api/daps")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"location\":\"http://example.org/" + Calendar.getInstance().getTimeInMillis() + "\"}"))
                    .andExpect(status().isCreated());
        }

        mockMvc.perform(get("/api/daps")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser("ADMIN")
    void get_validInput_returnObj() throws Exception {
        final var newObject =
                mockMvc.perform(post("/api/daps")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"location\":\"http://example.org/" + Calendar.getInstance().getTimeInMillis() + "\"}"))
                        .andExpect(status().isCreated()).andReturn();

        final var newObj = newObject.getResponse().getHeader("Location");

        assert newObj != null;
        mockMvc.perform(get(URI.create(newObj).getPath())).andExpect(status().isOk());
    }

    @Test
    @WithMockUser("ADMIN")
    void update_validInput_returnAcknowledge() throws Exception {
        final var newObject =
                mockMvc.perform(post("/api/daps")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"location\":\"http://example.org/" + Calendar.getInstance().getTimeInMillis() + "\"}"))
                        .andExpect(status().isCreated()).andReturn();

        final var newObj = newObject.getResponse().getHeader("Location");

        assert newObj != null;
        mockMvc.perform(put(URI.create(newObj).getPath())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser("ADMIN")
    void delete_validInput_returnAcknowledge() throws Exception {
        final var newObject =
                mockMvc.perform(post("/api/daps")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"location\":\"http://example.org/" + Calendar.getInstance().getTimeInMillis() + "\"}"))
                        .andExpect(status().isCreated()).andReturn();

        final var newObj = newObject.getResponse().getHeader("Location");

        assert newObj != null;
        mockMvc.perform(delete(URI.create(newObj).getPath()))
                .andExpect(status().isNoContent());
    }
}
