package fr.cg95.cvq.security;

import fr.cg95.cvq.permission.PrivilegeDescriptor;
import fr.cg95.cvq.security.annotation.ContextPrivilege;

public class PermissionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Object o;
    private PrivilegeDescriptor p;
    private ContextPrivilege privilege;
    private String message;

    public PermissionException(Class<?> c, Object o, PrivilegeDescriptor p) {
        super("");
        this.o = o;
        this.p = p;
        // Compute message early, so that the exception is still useable
        // after the transaction is rollbacked (e.g. in unit tests)
        this.message = "permission check at level " + p.toString() 
            + " failed for object <" + o.toString() + ">" + " in " + c;
    }

    public PermissionException(String service, PrivilegeDescriptor p) {
        super("");
        this.p = p;
        this.message = "permission check at level " + p.toString() 
            + " failed for service <" + service + ">";
    }
    
    public PermissionException(Class<?> c, Object o, ContextPrivilege privilege) {
        super("");
        this.o = o;
        this.privilege = privilege;
        this.message = "permission check at level " + privilege.toString() 
            + " failed for object <" + o.toString() + ">" + " in " + c;
    }
    
    public PermissionException(String message) {
        super("");
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }

    public Object getObject() {
        return o;
    }
    
    public ContextPrivilege getPrivilege() {
        return privilege;
    }
    
    public PrivilegeDescriptor getPrivilegeDescriptor() {
        return p;
    }

}
