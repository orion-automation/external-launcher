package com.eorion.bo.enhancement.externallauncher.service;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricDetail;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricVariableUpdate;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VariableService {
    private static final Logger log = LoggerFactory.getLogger(VariableService.class);

    private final HistoryService historyService;


    public VariableService(HistoryService historyService) {
        this.historyService = historyService;
    }

    public Map<String, Object> getRewindVariable(String taskId) {
        HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery()
                .taskId(taskId)
                .singleResult();
        VariableMap variableMap = new VariableMapImpl();
        if (task != null) {
            Date startTime = task.getStartTime();
            String processInstanceId = task.getProcessInstanceId();

            var list = historyService.createHistoricDetailQuery()
                    .variableUpdates()
                    .occurredBefore(startTime)
                    .executionId(processInstanceId)
                    .list();

            Map<String, List<HistoricDetail>> collect = list.stream().collect(Collectors.groupingBy(historicDetail -> {
                HistoricVariableUpdate variableUpdate = (HistoricVariableUpdate) historicDetail;
                return variableUpdate.getVariableName();
            }));
            collect.forEach((key, value) -> {
                        value.sort(Comparator.comparing(HistoricDetail::getTime).reversed());
                        variableMap.put(((HistoricVariableUpdate) value.get(0)).getVariableName(), ((HistoricVariableUpdate) value.get(0)).getValue());
                    });
        } else {
            throw new RuntimeException();
        }
        return variableMap;
    }
}
