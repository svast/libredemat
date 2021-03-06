package org.libredemat.security.annotation;

import org.libredemat.business.authority.SiteProfile;

public enum ContextType {

    /**
     * For unauthenticated ecitizens : 
     * <ul>
     *   <li>while creating an home folder</li>
     *   <li>while issuing an unregistered request</li>
     * </ul>
     */
    UNAUTH_ECITIZEN,

    /**
     * For authenticated ecitizens.
     */
    ECITIZEN,

    /**
     * For authenticated agents with {@link SiteProfile#AGENT agent profile} on current site.
     */
    AGENT,

    /**
     * For authenticated agents with {@link SiteProfile#ADMIN admin profile} on current site.
     */
    ADMIN,

    /** 
     * For super administrators (ie administrators of the whole platform, typically cron jobs :-) ).
     */
    SUPER_ADMIN,
    
    /**
     * For external services invoking our WS 
     */
    EXTERNAL_SERVICE;
}
