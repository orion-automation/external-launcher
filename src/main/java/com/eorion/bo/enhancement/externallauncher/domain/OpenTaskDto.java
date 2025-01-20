package com.eorion.bo.enhancement.externallauncher.domain;

import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.form.CamundaFormRef;
import org.camunda.bpm.engine.task.Task;

import java.util.Date;

public class OpenTaskDto {
    private String id;
    private String name;
    private String assignee;
    private Date created;
    private Date due;
    private Date followUp;
    private Date lastUpdated;
    private String delegationState;
    private String description;
    private String executionId;
    private String owner;
    private String parentTaskId;
    private int priority;
    private String processDefinitionId;
    private String processInstanceId;
    private String taskDefinitionKey;
    private String caseExecutionId;
    private String caseInstanceId;
    private String caseDefinitionId;
    private boolean suspended;
    private String formKey;
    private CamundaFormRef camundaFormRef;
    private String tenantId;
    private Object formData;

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public void setFormData(Object formData) {
        this.formData = formData;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAssignee() {
        return assignee;
    }

    public Date getCreated() {
        return created;
    }

    public Date getDue() {
        return due;
    }

    public Date getFollowUp() {
        return followUp;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public String getDelegationState() {
        return delegationState;
    }

    public String getDescription() {
        return description;
    }

    public String getExecutionId() {
        return executionId;
    }

    public String getOwner() {
        return owner;
    }

    public String getParentTaskId() {
        return parentTaskId;
    }

    public int getPriority() {
        return priority;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public String getCaseExecutionId() {
        return caseExecutionId;
    }

    public String getCaseInstanceId() {
        return caseInstanceId;
    }

    public String getCaseDefinitionId() {
        return caseDefinitionId;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public String getFormKey() {
        return formKey;
    }

    public CamundaFormRef getCamundaFormRef() {
        return camundaFormRef;
    }

    public String getTenantId() {
        return tenantId;
    }

    public Object getFormData() {
        return formData;
    }

    public static OpenTaskDto fromEntity(Task task) {
        OpenTaskDto dto = new OpenTaskDto();
        dto.id = task.getId();
        dto.name = task.getName();
        dto.assignee = task.getAssignee();
        dto.created = task.getCreateTime();
        dto.lastUpdated = task.getLastUpdated();
        dto.due = task.getDueDate();
        dto.followUp = task.getFollowUpDate();
        if (task.getDelegationState() != null) {
            dto.delegationState = task.getDelegationState().toString();
        }
        dto.description = task.getDescription();
        dto.executionId = task.getExecutionId();
        dto.owner = task.getOwner();
        dto.parentTaskId = task.getParentTaskId();
        dto.priority = task.getPriority();
        dto.processDefinitionId = task.getProcessDefinitionId();
        dto.processInstanceId = task.getProcessInstanceId();
        dto.taskDefinitionKey = task.getTaskDefinitionKey();
        dto.caseDefinitionId = task.getCaseDefinitionId();
        dto.caseExecutionId = task.getCaseExecutionId();
        dto.caseInstanceId = task.getCaseInstanceId();
        dto.suspended = task.isSuspended();
        dto.tenantId = task.getTenantId();
        try {
            dto.formKey = task.getFormKey();
            dto.camundaFormRef = task.getCamundaFormRef();
        } catch (BadUserRequestException ignored) {

        }

        return dto;
    }
}
