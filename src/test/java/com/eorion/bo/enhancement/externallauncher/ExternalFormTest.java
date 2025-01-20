package com.eorion.bo.enhancement.externallauncher;

import com.eorion.bo.enhancement.externallauncher.domain.FormDetailDto;
import com.eorion.bo.enhancement.externallauncher.service.ExternalFormService;
import org.camunda.bpm.container.RuntimeContainerDelegate;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.test.junit5.ProcessEngineExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ProcessEngineExtension.class)
public class ExternalFormTest {
    public ProcessEngine processEngine;

    @BeforeEach
    void setUp() {
        RuntimeContainerDelegate.INSTANCE.get().registerProcessEngine(processEngine);
    }

    @AfterEach
    void tearDown() {
        RuntimeContainerDelegate.INSTANCE.get().unregisterProcessEngine(processEngine);
    }

    @Test
    void selectFormByIdShouldReturnData(){
        ExternalFormService formService = new ExternalFormService();
        FormDetailDto fromDetail = formService.getFromDetailById("324234234234");
        Assertions.assertNotNull(fromDetail);
        Assertions.assertEquals("324234234234", fromDetail.getId());
        Assertions.assertNotNull(fromDetail.getFromData());

    }
}
