package com.eorion.bo.enhancement.externallauncher.adapter.inbound;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class OpenVariableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Test
    @Deployment(resources = "model/open-process-start-test-true.bpmn")
    public void getVariableInstances() throws Exception {

        Map<String, Object> variables = new HashMap<>();
        variables.put("user", Map.of("name", "jack", "age", 30, "gender", "male"));
        variables.put("orderId", UUID.randomUUID().toString());
        variables.put("amount", 70);
        variables.put("system", List.of("macos", "linux", "windows"));
        var processInstance = runtimeService.startProcessInstanceByKey("process_open_start_true", variables);
        var task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/open/variable-instance?taskId={taskId}", task.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").isNotEmpty())
                .andDo(print());
    }
}
