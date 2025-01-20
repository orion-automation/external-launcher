package com.eorion.bo.enhancement.externallauncher.adapter.inbound;

import org.camunda.bpm.engine.RepositoryService;
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
public class OpenProcessStartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RepositoryService repositoryService;

    @Test
    @Deployment(resources = "model/open-process-start-test-true.bpmn")
    public void getDetailByProcessDefinitionId() throws Exception {

        var processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("process_open_start_true").latestVersion().singleResult();
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/open/process-definition/{id}", processDefinition.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.formData").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.key").value("process_open_start_true"))
                .andDo(print());
    }

    @Test
    @Deployment(resources = "model/open-process-start-test-false.bpmn")
    public void getDetailByProcessDefinitionId_404() throws Exception {

        var processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("process_open_start_false").latestVersion().singleResult();
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/open/process-definition/{id}", processDefinition.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Deployment(resources = "model/open-process-start-test-true.bpmn")
    public void getDetailByProcessDefinitionKey() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/open/process-definition/key/{key}", "process_open_start_true")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("userId", "demo")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.formData").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andDo(print());
    }

    @Test
    @Deployment(resources = "model/open-process-start-test-true.bpmn")
    public void submitFormByProcessDefinitionId() throws Exception {
        var processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("process_open_start_true").latestVersion().singleResult();
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/enhancement/open/process-definition/{id}/submit-form", processDefinition.getId())
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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andDo(print());
    }

    @Test
    @Deployment(resources = "model/open-process-start-test-true.bpmn")
    public void submitFormByProcessDefinitionKey() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/enhancement/open/process-definition/key/{key}/submit-form", "process_open_start_true")
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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andDo(print());
    }

    @Test
    @Deployment(resources = "model/open-process-start-test-connector.bpmn")
    public void getDetailByProcessDefinitionIdWithConnector() throws Exception {

        var processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("Process_test_read_bpmn_model_instance").latestVersion().singleResult();
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/open/process-definition/{id}", processDefinition.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.formData").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.key").value("Process_test_read_bpmn_model_instance"))
                .andDo(print());
    }
}
