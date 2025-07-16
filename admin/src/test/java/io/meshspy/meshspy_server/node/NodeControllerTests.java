package io.meshspy.meshspy_server.node;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class NodeControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addNodeCreatesRequest() throws Exception {
        Node node = new Node("1", "Test", "localhost", 0.0, 0.0);

        mockMvc.perform(post("/nodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(node)))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/nodes/1"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/node-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));
    }

    @Test
    void requestAndApproveNode() throws Exception {
        mockMvc.perform(post("/nodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Node("2", "Req", "addr", 1.0, 2.0))))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/node-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("2"));

        mockMvc.perform(post("/node-requests/2/approve"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/nodes/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Req"));
    }

    @Test
    void uiAvailable() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));    }

    @Test
    void versionEndpoint() throws Exception {
        mockMvc.perform(get("/version"))
                .andExpect(status().isOk());
    }
}
