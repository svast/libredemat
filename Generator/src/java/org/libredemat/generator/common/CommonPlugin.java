package org.libredemat.generator.common;

import org.apache.log4j.Logger;
import org.libredemat.generator.ApplicationDocumentation;
import org.libredemat.generator.ElementProperties;
import org.libredemat.generator.IPluginGenerator;
import org.libredemat.generator.UserDocumentation;
import org.libredemat.generator.common.Condition.RoleType;
import org.w3c.dom.Node;



/**
 * Common (pseudo) Plugin manage application's informations shared by all Plugin
 * 
 * @author rdj@zenexity.fr
 */
public class CommonPlugin implements IPluginGenerator {
    private static Logger logger =
        Logger.getLogger(CommonPlugin.class);
    
    private RequestCommon requestCommon;
    private int depth;
    
    public static CommonPlugin getInstance() { return INSTANCE; }
    
    private static final CommonPlugin INSTANCE = new CommonPlugin();
    
    private CommonPlugin() {}
    
    
    public void onApplicationInformation(ApplicationDocumentation appDoc) {
        logger.debug( "onApplicationInformation() " + 
                appDoc.getNodeName() + " / " + appDoc.getXmlString());
        
        if (depth < 1 ) {
            if (appDoc.hasChildNode("namespace"))
                requestCommon.setNamespace(ApplicationDocumentation.getNodeAttributeValue(
                        appDoc.getChildrenNodes("namespace")[0], "name"));

            Step step;
            if (appDoc.hasChildNode("steps")) {
                for (Node node : 
                    ApplicationDocumentation.getChildrenNodes(appDoc.getChildrenNodes("steps")[0], "step")) {
                    if (ApplicationDocumentation.getNodeAttributeValue(node, "ref") != null) {
                        step = new CommonStep(
                            ApplicationDocumentation.getNodeAttributeValue(node, "index"),
                            ApplicationDocumentation.getNodeAttributeValue(node, "ref")
                        );
                    } else {
                        step = new CustomStep(
                            ApplicationDocumentation.getNodeAttributeValue(node, "index"),
                            ApplicationDocumentation.getNodeAttributeValue(node, "name"),
                            ApplicationDocumentation.getNodeAttributeValue(node, "displayNotInValidation"),
                            ApplicationDocumentation.getNodeAttributeValue(node, "displayNotInPDF"),
                            ApplicationDocumentation.getNodeAttributeValue(node, "required")
                        );
                    }
                    
                    requestCommon.addStep(step);
                    
                    Node[] conditionsNodes = ApplicationDocumentation.getChildrenNodes(node, "conditions");
                    if(conditionsNodes != null)
                        for (Node conditionNode : 
                            ApplicationDocumentation.getChildrenNodes(conditionsNodes[0], "condition"))
                            requestCommon.addConditionToStep(step, new Condition(
                                ApplicationDocumentation
                                    .getNodeAttributeValue(conditionNode, "name")));
                    
                    
                    Node[] widgetNodes = ApplicationDocumentation.getChildrenNodes(node, "widgets");
                    if (widgetNodes != null)
                        for (Node widgetNode :
                            ApplicationDocumentation.getChildrenNodes(widgetNodes[0])) {
                                Widget widget = new Widget(widgetNode.getLocalName()
                                        ,ApplicationDocumentation.getNodeAttributeValue(widgetNode, "into"));
                                step.addWidget(widget);
                                Node[] autofillNode = ApplicationDocumentation.getChildrenNodes(widgetNode, "autofill");
                                if (autofillNode != null) {
                                    if (autofillNode.length > 1) {
                                        throw new RuntimeException("Element {" + appDoc.getNodeName() + "} has more than one autofill tag");
                                    }
                                    requestCommon.setWidgetAutofill(widget,
                                            ApplicationDocumentation.getNodeAttributeValue(autofillNode[0], "name"),
                                            ApplicationDocumentation.getNodeAttributeValue(autofillNode[0], "type"),
                                            ApplicationDocumentation.getNodeAttributeValue(autofillNode[0], "field"));
                                }
                        }
                }
            }
        }
        else {
            if (appDoc.hasChildNode("step")) {
                Node step = appDoc.getChildrenNodes("step")[0];
                String stepName = ApplicationDocumentation.getNodeAttributeValue(step, "ref");
                if (stepName == null)
                    stepName = ApplicationDocumentation.getNodeAttributeValue(step, "name");
                requestCommon.setCurrentElementStep(stepName);
            }

            if (appDoc.hasChildNode("conditions"))
                for (Node node : 
                    ApplicationDocumentation.getChildrenNodes(appDoc.getChildrenNodes("conditions")[0], "condition")) {
                    String name = ApplicationDocumentation.getNodeAttributeValue(node, "name");
                    RoleType role = RoleType.valueOf(ApplicationDocumentation.getNodeAttributeValue(node, "type"));
                    if (role == RoleType.trigger)
                        requestCommon.addConditionTrigger(name);
                    else
                        requestCommon.addConditionListener(name, role, Boolean.valueOf(
                            ApplicationDocumentation.getNodeAttributeValue(node, "required")));
                }
            if (appDoc.hasChildNode("validation"))
                requestCommon.getCurrentElementCommon().setJsRegexp(
                        ApplicationDocumentation.getNodeAttributeValue(appDoc.getChildrenNodes("validation")[0], "jsregexp"));
            if (appDoc.hasChildNode("autofill")) {
                Node[] autofill = appDoc.getChildrenNodes("autofill");
                if (autofill.length > 1) {
                    throw new RuntimeException("Element {" + appDoc.getNodeName() + "} has more than one autofill tag");
                }
                requestCommon.setCurrentElementAutofill(
                        ApplicationDocumentation.getNodeAttributeValue(autofill[0], "name"),
                        ApplicationDocumentation.getNodeAttributeValue(autofill[0], "type"),
                        ApplicationDocumentation.getNodeAttributeValue(autofill[0], "field"));
            }
        }
        appDoc.setRequestCommon(requestCommon);
    }
    
    public void onOtherApplicationInformation(ApplicationDocumentation appDoc) {
        appDoc.setRequestCommon(requestCommon);
    }

    public void startRequest(String requestName, String targetNamespace) {
        logger.debug("startRequest() - ");
        requestCommon = new RequestCommon();
        depth = 0;
    }
    
    public void startElement(String elementName, String type) {
        requestCommon.setCurrentElementCommon(new ElementCommon(elementName));
        depth ++;
    }
    
    public void onUserInformation(UserDocumentation userDocumentation) {}
    
    public void startElementProperties(ElementProperties elementProperties) {}
    
    public void endElementProperties() {}
    
    public void endElement(String elementName) {
        depth --;
    }

    public void endRequest(String requestName) {}

    public void shutdown() {}

    public void initialize(Node configurationNode) {}
}
