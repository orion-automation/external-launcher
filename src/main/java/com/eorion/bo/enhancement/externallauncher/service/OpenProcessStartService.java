package com.eorion.bo.enhancement.externallauncher.service;

import com.eorion.bo.enhancement.externallauncher.domain.FormDetailDto;
import com.eorion.bo.enhancement.externallauncher.domain.OpenProcessDefinitionDto;
import com.eorion.bo.enhancement.externallauncher.domain.OpenProcessInstanceDto;
import com.eorion.bo.enhancement.externallauncher.domain.OpenStartProcessInstanceDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.impl.instance.ProcessImpl;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
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
public class OpenProcessStartService {

    private static final Logger log = LoggerFactory.getLogger(OpenProcessStartService.class);

    private static final String PUBLIC_ACCESS_ENABLED = "publicAccessEnabled";

    private static final String PUBLIC_ACCESS_ENABLED_FLAG = "true";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RepositoryService repositoryService;

    private final FormService formService;

    private final ExternalFormService externalFormService;

    private final ProcessEngine processEngine;

    public OpenProcessStartService(RepositoryService repositoryService, FormService formService, ExternalFormService externalFormService, ProcessEngine processEngine) {
        this.repositoryService = repositoryService;
        this.formService = formService;
        this.externalFormService = externalFormService;
        this.processEngine = processEngine;
    }

    protected boolean publicAccessEnabledByProcessDefinitionKey(String key){
        if (!StringUtils.hasLength(key))
            return false;
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).latestVersion().singleResult();
        if (Objects.isNull(definition))
            return false;

        BpmnModelInstance bpmnModelInstance = repositoryService.getBpmnModelInstance(definition.getId());
        if (Objects.isNull(bpmnModelInstance))
            return false;
        ProcessImpl parallelGatewayTest = bpmnModelInstance.getModelElementById(definition.getKey());
        var camundaProperties = parallelGatewayTest.getExtensionElements().getElementsQuery().filterByType(CamundaProperties.class)
                .singleResult();
        for (CamundaProperty camundaProperty : camundaProperties.getCamundaProperties()) {
            var name = camundaProperty.getCamundaName();
            var value = camundaProperty.getCamundaValue();
            if (PUBLIC_ACCESS_ENABLED.equals(name) && PUBLIC_ACCESS_ENABLED_FLAG.equals(value)){
                return true;
            }
        }
        return false;
    }

    protected boolean publicAccessEnabledByProcessDefinitionId(String id){
        if (!StringUtils.hasLength(id))
            return false;
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
        if (Objects.isNull(definition))
            return false;
        InputStream processModelInputStream = repositoryService.getProcessModel(definition.getId());

        if (processModelInputStream != null){
            Document document;
            try {
                document = DocumentHelper.parseText(new String(processModelInputStream.readAllBytes()));
            } catch (DocumentException | IOException e) {
                throw new RuntimeException(e);
            }
            org.dom4j.XPath xpath = document.createXPath("/bpmn:definitions/bpmn:process/bpmn:extensionElements/camunda:properties/camunda:property");
            List<Node> nodeList = xpath.selectNodes(document);
            if (nodeList != null && !nodeList.isEmpty()) {
                for (Node node : nodeList) {
                    Element element1 = (Element) node;
                    String value = element1.attributeValue("value");
                    String name = element1.attributeValue("name");
                    if (PUBLIC_ACCESS_ENABLED.equals(name) && PUBLIC_ACCESS_ENABLED_FLAG.equals(value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ResponseEntity<OpenProcessDefinitionDto> getDetailByProcessDefinitionId(String id) {

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();

        if (Objects.isNull(processDefinition))
            return ResponseEntity.notFound().build();
        boolean publicAccessEnabled = publicAccessEnabledByProcessDefinitionId(processDefinition.getId());
        if (!publicAccessEnabled)
            return ResponseEntity.notFound().build();

        String startFormKey = formService.getStartFormKey(processDefinition.getId());
        OpenProcessDefinitionDto openProcessDefinitionDto = OpenProcessDefinitionDto.fromProcessDefinition(processDefinition);
        if (!StringUtils.hasLength(startFormKey))
            return ResponseEntity.ok(openProcessDefinitionDto);
        FormDetailDto fromDetail = externalFormService.getFromDetailById(startFormKey);
        if (Objects.isNull(fromDetail))
            return ResponseEntity.ok(openProcessDefinitionDto);
        openProcessDefinitionDto.setFormId(fromDetail.getId());
        if (Objects.nonNull(fromDetail.getFromData())){
            Object formData;
            try {
                formData = objectMapper.readValue(fromDetail.getFromData(), Object.class);
            } catch (JsonProcessingException e) {
                log.error("", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

            }
            openProcessDefinitionDto.setFormData(formData);
        }
        return ResponseEntity.ok(openProcessDefinitionDto);
    }

    public ResponseEntity<OpenProcessDefinitionDto> getDetailByProcessDefinitionKey(String key) {

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).latestVersion().singleResult();

        if (Objects.isNull(processDefinition))
            return ResponseEntity.notFound().build();
        boolean publicAccessEnabled = publicAccessEnabledByProcessDefinitionId(processDefinition.getId());
        if (!publicAccessEnabled)
            return ResponseEntity.notFound().build();

        String startFormKey = formService.getStartFormKey(processDefinition.getId());
        OpenProcessDefinitionDto openProcessDefinitionDto = OpenProcessDefinitionDto.fromProcessDefinition(processDefinition);
        if (!StringUtils.hasLength(startFormKey))
            return ResponseEntity.ok(openProcessDefinitionDto);
        FormDetailDto fromDetail = externalFormService.getFromDetailById(startFormKey);
        if (Objects.isNull(fromDetail))
            return ResponseEntity.ok(openProcessDefinitionDto);
        openProcessDefinitionDto.setFormId(fromDetail.getId());
        if (Objects.nonNull(fromDetail.getFromData())){
            Object formData;
            try {
                formData = objectMapper.readValue(fromDetail.getFromData(), Object.class);
            } catch (JsonProcessingException e) {
                log.error("", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

            }
            openProcessDefinitionDto.setFormData(formData);
        }
        return ResponseEntity.ok(openProcessDefinitionDto);
    }

    public ResponseEntity<?> submitFormByProcessDefinitionId(String id, OpenStartProcessInstanceDto startProcessInstanceDto) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
        if (Objects.isNull(processDefinition))
            return ResponseEntity.notFound().build();
        var enabled = publicAccessEnabledByProcessDefinitionId(processDefinition.getId());
        if (!enabled)
            return ResponseEntity.notFound().build();
        if (Objects.nonNull(startProcessInstanceDto)){
            String businessKey = startProcessInstanceDto.getBusinessKey();
            Map<String, VariableValueDto> variables = startProcessInstanceDto.getVariables();
            VariableMap variableMap;
            try {
                variableMap = VariableValueDto.toMap(variables, processEngine, this.objectMapper);
            }catch (Exception e){
                log.error("VariableValue to map error: {}", e.getMessage());
                return ResponseEntity.badRequest().body("Variable is not Correct ！");
            }
            ProcessInstance processInstance;
            if (StringUtils.hasLength(businessKey)){
                processInstance = formService.submitStartForm(processDefinition.getId(), businessKey, variableMap);
            }else {
                processInstance = formService.submitStartForm(processDefinition.getId(), variableMap);
            }
            if (processInstance != null)
                return ResponseEntity.ok(OpenProcessInstanceDto.fromProcessInstance(processInstance));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public ResponseEntity<OpenProcessInstanceDto> submitFormByProcessDefinitionKey(String key, OpenStartProcessInstanceDto startProcessInstanceDto) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).latestVersion().singleResult();
        if (Objects.isNull(processDefinition))
            return ResponseEntity.notFound().build();
        var enabled = publicAccessEnabledByProcessDefinitionId(processDefinition.getId());
        if (!enabled)
            return ResponseEntity.notFound().build();
        if (Objects.nonNull(startProcessInstanceDto)){
            String businessKey = startProcessInstanceDto.getBusinessKey();
            Map<String, VariableValueDto> variables = startProcessInstanceDto.getVariables();
            VariableMap variableMap = VariableValueDto.toMap(variables, processEngine, this.objectMapper);
            ProcessInstance processInstance;
            if (StringUtils.hasLength(businessKey)){
                processInstance = formService.submitStartForm(processDefinition.getId(), businessKey, variableMap);
            }else {
                processInstance = formService.submitStartForm(processDefinition.getId(), variableMap);
            }
            if (processInstance != null)
                return ResponseEntity.ok(OpenProcessInstanceDto.fromProcessInstance(processInstance));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
