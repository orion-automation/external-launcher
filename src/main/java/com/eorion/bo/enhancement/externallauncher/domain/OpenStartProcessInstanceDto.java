

package com.eorion.bo.enhancement.externallauncher.domain;

import org.camunda.bpm.engine.rest.dto.VariableValueDto;

import java.util.Map;

public class OpenStartProcessInstanceDto {
    protected Map<String, VariableValueDto> variables;
    protected String businessKey;

    public OpenStartProcessInstanceDto() {
    }

    public Map<String, VariableValueDto> getVariables() {
        return this.variables;
    }

    public void setVariables(Map<String, VariableValueDto> variables) {
        this.variables = variables;
    }

    public String getBusinessKey() {
        return this.businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
}
