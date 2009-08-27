package fr.cg95.cvq.generator.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rdj@zenexity.fr
 */
public class ElementCommon {
    
    private Step step;
    private List<Condition> conditions = new ArrayList<Condition>();
    private String jsRegexp;
    private Autofill autofill;

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public List<Condition> getConditions() {
        return conditions;
    }
    
    public void addCondition(Condition condition) {
        for (Condition c : conditions)
            if (c.getName().equals(condition.getName())) 
                throw new RuntimeException("addCondition() - Condition {" + condition.getName() +"} " +
                		"have more than 1 participation in element");
        
        if (condition.getType() == null)
            throw new RuntimeException("addCondition() - Condition {" + condition.getName() +"} " +
            		"must define attribute type=\"\" element ");
        try {
            Condition.ConditionType.valueOf(condition.getType().toUpperCase());
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("addCondition() - Condition type {"+ condition.getType() +"} is not one of " +
            		"{"+ Condition.ConditionType.valuesAsString() +"}");
        }
        conditions.add(condition);
    }
    
    public String getJsRegexp() {
        return jsRegexp;
    }

    public void setJsRegexp(String jsRegexp) {
        this.jsRegexp = jsRegexp;
    }

    public Autofill getAutofill() {
        return autofill;
    }

    public void setAutofill(Autofill autofill) {
        this.autofill = autofill;
    }

}
