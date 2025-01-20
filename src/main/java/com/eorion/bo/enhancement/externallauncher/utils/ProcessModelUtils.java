package com.eorion.bo.enhancement.externallauncher.utils;

import org.dom4j.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class ProcessModelUtils {

    private static final String PUBLIC_ACCESS_ENABLED = "publicAccessEnabled";

    private static final String PUBLIC_ACCESS_ENABLED_FLAG = "true";


    public static boolean processModelIsEnable(InputStream processModel) {
        if (processModel != null) {
            Document document;
            try {
                document = DocumentHelper.parseText(new String(processModel.readAllBytes()));
            } catch (DocumentException | IOException e) {
                throw new RuntimeException(e);
            }
            org.dom4j.XPath xpath = document.createXPath("/bpmn:definitions/bpmn:process/bpmn:extensionElements/camunda:properties/camunda:property");
            List<Node> nodeList = xpath.selectNodes(document);
            if (nodeList != null && !nodeList.isEmpty()) {
                for (Node node : nodeList) {
                    Element element = (Element) node;
                    String value = element.attributeValue("value");
                    String name = element.attributeValue("name");
                    if (PUBLIC_ACCESS_ENABLED.equals(name) && PUBLIC_ACCESS_ENABLED_FLAG.equals(value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
