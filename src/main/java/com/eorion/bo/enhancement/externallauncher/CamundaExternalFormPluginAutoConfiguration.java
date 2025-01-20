package com.eorion.bo.enhancement.externallauncher;

import com.eorion.bo.enhancement.externallauncher.plugin.EorionExtensionFromEnginePlugin;
import com.eorion.bo.enhancement.externallauncher.service.ExternalFormService;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

@AutoConfiguration
@ConditionalOnClass(EorionExtensionFromEnginePlugin.class)
public class CamundaExternalFormPluginAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EorionExtensionFromEnginePlugin eorionExtensionFromEnginePlugin() {
        return new EorionExtensionFromEnginePlugin();
    }

    @Bean
    @ConditionalOnMissingBean
    @DependsOn("eorionExtensionFromEnginePlugin")
    public ExternalFormService externalFormService(ProcessEngine processEngine) {
        return new ExternalFormService(processEngine);
    }

}
