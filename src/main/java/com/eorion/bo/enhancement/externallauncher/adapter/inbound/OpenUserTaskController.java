package com.eorion.bo.enhancement.externallauncher.adapter.inbound;

import com.eorion.bo.enhancement.externallauncher.service.OpenUserTaskService;
import org.camunda.bpm.engine.rest.dto.task.CompleteTaskDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enhancement/open")
public class OpenUserTaskController {
    private final OpenUserTaskService openUserTaskService;

    public OpenUserTaskController(OpenUserTaskService openUserTaskService) {
        this.openUserTaskService = openUserTaskService;
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<?> getTaskInfoById(@PathVariable("id") String id){
        return openUserTaskService.getTaskInfoById(id);
    }

    @PostMapping("/task/{id}/submit-form")
    public ResponseEntity<?> submitFormTask(@PathVariable("id") String id, @RequestBody CompleteTaskDto completeTaskDto){
        return openUserTaskService.submitFormTask(id, completeTaskDto);
    }

}
