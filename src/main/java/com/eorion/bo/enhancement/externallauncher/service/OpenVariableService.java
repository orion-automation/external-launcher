package com.eorion.bo.enhancement.externallauncher.service;

import com.eorion.bo.enhancement.externallauncher.utils.ProcessModelUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.rest.dto.runtime.VariableInstanceDto;
import org.camunda.bpm.engine.runtime.VariableInstance;
import org.camunda.bpm.engine.runtime.VariableInstanceQuery;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OpenVariableService {

    private static final Logger log = LoggerFactory.getLogger(OpenVariableService.class);

    private final RepositoryService repositoryService;

    private final TaskService taskService;

    private final RuntimeService runtimeService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    public OpenVariableService(RepositoryService repositoryService, TaskService taskService, RuntimeService runtimeService) {
        this.repositoryService = repositoryService;
        this.taskService = taskService;
        this.runtimeService = runtimeService;
    }

    public ResponseEntity<?> getVariableInstances(String taskId, Integer firstResult, Integer maxResults, boolean deserializeValues){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        String processDefinitionId = task.getProcessDefinitionId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();

        if (Objects.isNull(processDefinition))
            return ResponseEntity.notFound().build();

        boolean publicAccessEnabled;
        try(InputStream processModel = repositoryService.getProcessModel(processDefinitionId)) {
            publicAccessEnabled = ProcessModelUtils.processModelIsEnable(processModel);
        } catch (IOException e) {
            log.error("read process model error: " + e);
            throw new RuntimeException("read process model error !");
        }

        if (!publicAccessEnabled)
            return ResponseEntity.notFound().build();

        VariableInstanceQuery query = runtimeService.createVariableInstanceQuery();
        query.processInstanceIdIn(task.getProcessInstanceId());
        query.disableBinaryFetching();

        if (!deserializeValues) {
            query.disableCustomObjectDeserialization();
        }

        List<VariableInstance> matchingInstances;
        if (firstResult == null && maxResults == null) {
            matchingInstances = query.list();
        } else {
            matchingInstances = this.executePaginatedQuery(query, firstResult, maxResults);
        }

        List<VariableInstanceDto> instanceResults = matchingInstances.stream().map(VariableInstanceDto::fromVariableInstance).collect(Collectors.toList());

        return ResponseEntity.ok(instanceResults);

    }

    private List<VariableInstance> executePaginatedQuery(VariableInstanceQuery query, Integer firstResult, Integer maxResults) {
        if (firstResult == null) {
            firstResult = 0;
        }

        if (maxResults == null) {
            maxResults = Integer.MAX_VALUE;
        }

        return query.listPage(firstResult, maxResults);
    }
}
