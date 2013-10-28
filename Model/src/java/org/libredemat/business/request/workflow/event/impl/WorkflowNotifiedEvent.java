package org.libredemat.business.request.workflow.event.impl;

import org.libredemat.business.request.Request;
import org.libredemat.business.request.workflow.event.IWorkflowEventVisitor;
import org.libredemat.business.request.workflow.event.impl.WorkflowGenericEvent;
import org.libredemat.exception.CvqException;

public class WorkflowNotifiedEvent extends WorkflowGenericEvent {

    public WorkflowNotifiedEvent(Request request) {
        super(request);
    }

    @Override
    public void accept(IWorkflowEventVisitor workflowEventVisitor) throws CvqException {
        workflowEventVisitor.visit(this);
    }
}
