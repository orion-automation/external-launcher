package com.eorion.bo.enhancement.externallauncher.adapter.inbound;

import org.camunda.bpm.engine.RepositoryService;
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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class OpenUserTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Test
    @Deployment(resources = "model/open-process-start-test-true.bpmn")
    public void getTaskInfoById() throws Exception {

        var processInstance = runtimeService.startProcessInstanceByKey("process_open_start_true");
        var task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/open/task/{id}", task.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.formData").isNotEmpty())
                .andDo(print());
    }

    @Test
    @Deployment(resources = "model/open-process-start-test-true.bpmn")
    public void submitFromTask() throws Exception {

        var processInstance = runtimeService.startProcessInstanceByKey("process_open_start_true");
        var task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/enhancement/open/task/{id}/submit-form", task.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                              "variables": {
                                                "aVariable" : {
                                                  "value" : "aStringValue",
                                                  "type": "String",
                                                  "valueInfo" : {
                                                    "transient" : true
                                                  }
                                                },
                                                "anotherVariable" : {
                                                  "value" : true,
                                                  "type": "Boolean"
                                                }
                                              },
                                              "businessKey" : "myBusinessKey"
                                            }
                                        """)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());
    }
}
