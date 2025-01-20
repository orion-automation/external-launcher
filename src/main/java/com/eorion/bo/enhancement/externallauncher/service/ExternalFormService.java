package com.eorion.bo.enhancement.externallauncher.service;

import com.eorion.bo.enhancement.externallauncher.domain.FormDetailDto;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.interceptor.Command;

import java.util.HashMap;
import java.util.Map;

public class ExternalFormService extends EorionFormServiceImpl {

    public ExternalFormService() {
        super();
    }

    public ExternalFormService(ProcessEngine processEngine) {
        super(processEngine);
    }

    public FormDetailDto getFromDetailById(String id){
        Command<FormDetailDto> command = commandContext -> {
            Map<String, String> params = new HashMap<>();
            params.put("id", id);
            var from = commandContext.getDbEntityManager().selectOne("ExternalFormMapper.selectExternalFormById", params);
            return (FormDetailDto) from;
        };
        return commandExecutor.execute(command);
    }
}
