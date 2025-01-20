package com.eorion.bo.enhancement.externallauncher.domain;

import org.camunda.bpm.engine.repository.ProcessDefinition;

public class OpenProcessDefinitionDto {
    protected String id;
    protected String key;
    protected String category;
    protected String description;
    protected String name;
    protected int version;
    protected String resource;
    protected String deploymentId;
    protected String diagram;
    protected boolean suspended;
    protected String tenantId;
    protected String versionTag;
    protected Integer historyTimeToLive;
    protected boolean isStartableInTasklist;

    protected String formId;

    protected Object formData;

    public OpenProcessDefinitionDto() {
    }

    public String getId() {
        return this.id;
    }

    public String getKey() {
        return this.key;
    }

    public String getCategory() {
        return this.category;
    }

    public String getDescription() {
        return this.description;
    }

    public String getName() {
        return this.name;
    }

    public int getVersion() {
        return this.version;
    }

    public String getResource() {
        return this.resource;
    }

    public String getDeploymentId() {
        return this.deploymentId;
    }

    public String getDiagram() {
        return this.diagram;
    }

    public boolean isSuspended() {
        return this.suspended;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public String getVersionTag() {
        return this.versionTag;
    }

    public Integer getHistoryTimeToLive() {
        return this.historyTimeToLive;
    }

    public boolean isStartableInTasklist() {
        return this.isStartableInTasklist;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Object getFormData() {
        return formData;
    }

    public void setFormData(Object formData) {
        this.formData = formData;
    }

    public static OpenProcessDefinitionDto fromProcessDefinition(ProcessDefinition definition) {
        OpenProcessDefinitionDto dto = new OpenProcessDefinitionDto();
        dto.id = definition.getId();
        dto.key = definition.getKey();
        dto.category = definition.getCategory();
        dto.description = definition.getDescription();
        dto.name = definition.getName();
        dto.version = definition.getVersion();
        dto.resource = definition.getResourceName();
        dto.deploymentId = definition.getDeploymentId();
        dto.diagram = definition.getDiagramResourceName();
        dto.suspended = definition.isSuspended();
        dto.tenantId = definition.getTenantId();
        dto.versionTag = definition.getVersionTag();
        dto.historyTimeToLive = definition.getHistoryTimeToLive();
        dto.isStartableInTasklist = definition.isStartableInTasklist();
        return dto;
    }
}
