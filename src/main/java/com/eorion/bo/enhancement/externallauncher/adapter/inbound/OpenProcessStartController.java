package com.eorion.bo.enhancement.externallauncher.adapter.inbound;

import com.eorion.bo.enhancement.externallauncher.domain.OpenProcessDefinitionDto;
import com.eorion.bo.enhancement.externallauncher.domain.OpenProcessInstanceDto;
import com.eorion.bo.enhancement.externallauncher.domain.OpenStartProcessInstanceDto;
import com.eorion.bo.enhancement.externallauncher.service.OpenProcessStartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enhancement/open")
public class OpenProcessStartController {

    private final OpenProcessStartService service;

    public OpenProcessStartController(OpenProcessStartService service) {
        this.service = service;
    }

    @GetMapping("/process-definition/{id}")
    public ResponseEntity<OpenProcessDefinitionDto> getDetailByProcessDefinitionId(@PathVariable("id") String id) {
        return service.getDetailByProcessDefinitionId(id);
    }

    @GetMapping("/process-definition/key/{key}")
    public ResponseEntity<OpenProcessDefinitionDto> getDetailByProcessDefinitionKey(@PathVariable("key") String key){
        return service.getDetailByProcessDefinitionKey(key);
    }

    @PostMapping("/process-definition/{id}/submit-form")
    public ResponseEntity<?> submitFormByProcessDefinitionId(@PathVariable("id") String id, @RequestBody OpenStartProcessInstanceDto startProcessInstanceDto){
        return service.submitFormByProcessDefinitionId(id, startProcessInstanceDto);
    }

    @PostMapping("/process-definition/key/{key}/submit-form")
    public ResponseEntity<OpenProcessInstanceDto> submitFormByProcessDefinitionKey(@PathVariable("key") String key, @RequestBody OpenStartProcessInstanceDto startProcessInstanceDto){
        return service.submitFormByProcessDefinitionKey(key, startProcessInstanceDto);
    }
}
