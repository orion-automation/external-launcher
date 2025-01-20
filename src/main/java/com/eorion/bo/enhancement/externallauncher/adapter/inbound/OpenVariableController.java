package com.eorion.bo.enhancement.externallauncher.adapter.inbound;

import com.eorion.bo.enhancement.externallauncher.service.OpenVariableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enhancement/open/variable-instance")
public class OpenVariableController {

    private final OpenVariableService openVariableService;

    public OpenVariableController(OpenVariableService openVariableService) {
        this.openVariableService = openVariableService;
    }

    @GetMapping("")
    public ResponseEntity<?> getVariableInstances(
            @RequestParam(value = "taskId") String taskId,
            @RequestParam(value = "firstResult", required = false) Integer firstResult,
            @RequestParam(value = "maxResults", required = false) Integer maxResults,
            @RequestParam(value = "deserializeValues", required = false, defaultValue = "false") boolean deserializeValues){
        return openVariableService.getVariableInstances(taskId, firstResult, maxResults, deserializeValues);
    }
}
