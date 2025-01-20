package com.eorion.bo.enhancement.externallauncher.domain;

import org.camunda.bpm.engine.rest.dto.LinkableDto;
import org.camunda.bpm.engine.runtime.ProcessInstance;

public class OpenProcessInstanceDto extends LinkableDto {
    private String id;
    private String definitionId;
    private String businessKey;
    private String caseInstanceId;
    private boolean ended;
    private boolean suspended;
    private String tenantId;

    public OpenProcessInstanceDto() {
    }

    public OpenProcessInstanceDto(ProcessInstance instance) {
        this.id = instance.getId();
        this.definitionId = instance.getProcessDefinitionId();
        this.businessKey = instance.getBusinessKey();
        this.caseInstanceId = instance.getCaseInstanceId();
        this.ended = instance.isEnded();
        this.suspended = instance.isSuspended();
        this.tenantId = instance.getTenantId();
    }

    public String getId() {
        return this.id;
    }

    public String getDefinitionId() {
        return this.definitionId;
    }

    public String getBusinessKey() {
        return this.businessKey;
    }

    public String getCaseInstanceId() {
        return this.caseInstanceId;
    }

    public boolean isEnded() {
        return this.ended;
    }

    public boolean isSuspended() {
        return this.suspended;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public static OpenProcessInstanceDto fromProcessInstance(ProcessInstance instance) {
        return new OpenProcessInstanceDto(instance);
    }
}

