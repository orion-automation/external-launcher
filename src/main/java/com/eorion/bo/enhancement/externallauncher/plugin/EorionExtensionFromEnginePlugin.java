package com.eorion.bo.enhancement.externallauncher.plugin;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EorionExtensionFromEnginePlugin extends AbstractProcessEnginePlugin {

    private static final Logger logger = LoggerFactory.getLogger(EorionExtensionFromEnginePlugin.class);

    @Override
    public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        logger.debug("EorionExtensionFromEnginePlugin initiating");

        var configuration = processEngineConfiguration.getSqlSessionFactory().getConfiguration();
        var xmlParser = new XMLMapperBuilder(
                EorionExtensionFromEnginePlugin.class.getResourceAsStream("/mapper/form/ExternalFormMapper.xml"),
                configuration, getClass().getCanonicalName(), configuration.getSqlFragments());
        xmlParser.parse();
    }
}
