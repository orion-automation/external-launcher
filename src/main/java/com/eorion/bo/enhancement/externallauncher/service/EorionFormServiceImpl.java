package com.eorion.bo.enhancement.externallauncher.service;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.ServiceImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;

public class EorionFormServiceImpl extends ServiceImpl {

    public EorionFormServiceImpl() {
        super.commandExecutor = ((ProcessEngineConfigurationImpl) BpmPlatform.getDefaultProcessEngine()
                .getProcessEngineConfiguration()).getCommandExecutorTxRequired();
    }

    public EorionFormServiceImpl(ProcessEngine processEngine) {
        super.commandExecutor = ((ProcessEngineConfigurationImpl) processEngine
                .getProcessEngineConfiguration()).getCommandExecutorTxRequired();
    }
}