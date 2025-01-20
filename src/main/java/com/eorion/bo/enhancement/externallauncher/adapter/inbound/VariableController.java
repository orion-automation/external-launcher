package com.eorion.bo.enhancement.externallauncher.adapter.inbound;

import com.eorion.bo.enhancement.externallauncher.service.VariableService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/enhancement/variable")
public class VariableController {

    private final VariableService service;

    public VariableController(VariableService service) {
        this.service = service;
    }

    @GetMapping("/{taskId}/rewind")
    public Map<String, Object> getRewindVariable(@PathVariable("taskId") String taskId){
      return service.getRewindVariable(taskId);
    }
}
