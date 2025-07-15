package io.meshspy.meshspy_server.request;

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
class NodeRequestControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void approveRequestUsesLongNameWhenNameMissing() throws Exception {
        NodeRegistrationRequest req = new NodeRegistrationRequest();
        req.setId("long1");
        req.setAddress("addr");
        req.setLatitude(1.0);
        req.setLongitude(2.0);
        req.setLongName("LongNode");

        mockMvc.perform(post("/node-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/node-requests/long1/approve"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/nodes/long1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("LongNode"));
    }
}
