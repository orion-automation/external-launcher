package com.eorion.bo.enhancement.externallauncher.service;


import com.eorion.bo.enhancement.externallauncher.domain.OpenTaskDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.dto.task.CompleteTaskDto;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.VariableMap;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class OpenUserTaskService {

    private static final Logger log = LoggerFactory.getLogger(OpenUserTaskService.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RepositoryService repositoryService;

    private final TaskService taskService;

    private final ExternalFormService externalFormService;

    private final FormService formService;

    private final ProcessEngine processEngine;

    public OpenUserTaskService(RepositoryService repositoryService, TaskService taskService,
                               ExternalFormService externalFormService, FormService formService,
                               ProcessEngine processEngine) {
        this.repositoryService = repositoryService;
        this.taskService = taskService;
        this.externalFormService = externalFormService;
        this.formService = formService;
        this.processEngine = processEngine;
    }

    public ResponseEntity<?> getTaskInfoById(String id) {
        TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(id).singleResult();
        if (task != null) {
            String processDefinitionId = task.getProcessDefinitionId();
            String activityId = task.getTaskDefinitionKey();
            boolean enabled = publicAccessEnabledByProcessDefinitionIdAndActivityId(processDefinitionId, activityId);
            if (enabled) {
                var openTaskDto = OpenTaskDto.fromEntity(task);
                var formKey = formService.getTaskFormKey(processDefinitionId, activityId);
                if (StringUtils.hasLength(formKey)) {
                    openTaskDto.setFormKey(formKey);
                    var fromDetailById = externalFormService.getFromDetailById(formKey);
                    if (fromDetailById != null) {
                        String fromData = fromDetailById.getFromData();
                        Object ObjFormData = null;
                        try {
                            ObjFormData = objectMapper.readValue(fromData, Object.class);
                        } catch (JsonProcessingException e) {
                            log.error("", e);
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

                        }
                        openTaskDto.setFormData(ObjFormData);
                    }
                }
                return ResponseEntity.ok(openTaskDto);
            }
        }
        return ResponseEntity.notFound().build();
    }


    public ResponseEntity<?> submitFormTask(String id, CompleteTaskDto completeTaskDto) {
        Task task = taskService.createTaskQuery().taskId(id).singleResult();
        if (task != null) {
            String processDefinitionId = task.getProcessDefinitionId();
            String activityId = task.getTaskDefinitionKey();
            boolean enabled = publicAccessEnabledByProcessDefinitionIdAndActivityId(processDefinitionId, activityId);
            if (enabled) {
                if (completeTaskDto.getVariables() != null && !completeTaskDto.getVariables().isEmpty()) {
                    Map<String, VariableValueDto> variables = completeTaskDto.getVariables();
                    VariableMap variableMap;
                    try {
                        variableMap = VariableValueDto.toMap(variables, processEngine, this.objectMapper);
                    } catch (Exception e) {
                        log.error("VariableValue to map error: {}", e.getMessage());
                        return ResponseEntity.badRequest().body("Variable is not Correct ！");
                    }
                    if (variableMap != null && !variables.isEmpty()) {
                        formService.submitTaskForm(task.getId(), variableMap);
                    }
                    return ResponseEntity.noContent().build();
                }
            }
        }
        return ResponseEntity.notFound().build();
    }

    protected boolean publicAccessEnabledByProcessDefinitionIdAndActivityId(String processDefinitionId, String activityId) {
        if (!StringUtils.hasLength(processDefinitionId) || !StringUtils.hasLength(activityId))
            return false;
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        if (Objects.isNull(definition))
            return false;
        InputStream processModelInputStream = repositoryService.getProcessModel(definition.getId());

        if (processModelInputStream != null) {
            Document document;
            try {
                document = DocumentHelper.parseText(new String(processModelInputStream.readAllBytes()));
            } catch (DocumentException | IOException e) {
                throw new RuntimeException(e);
            }

            String xpathStr = "/bpmn:definitions/bpmn:process/bpmn:userTask[@id=\"" +
                    activityId +
                    "\"]/bpmn:extensionElements/camunda:properties/camunda:property[@name=\"externalAccessEnable\" and @value=\"true\"]";
            org.dom4j.XPath xpath = document.createXPath(xpathStr);
            List<Node> nodes = xpath.selectNodes(document,xpath);
            return nodes != null && !nodes.isEmpty();
        }
        return false;
    }
}

