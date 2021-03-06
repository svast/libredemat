package org.libredemat.service.users.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.libredemat.business.authority.SiteProfile;
import org.libredemat.business.authority.SiteRoles;
import org.libredemat.business.users.HomeFolder;
import org.libredemat.business.users.Individual;
import org.libredemat.business.users.UserSecurityProfile;
import org.libredemat.business.users.UserSecurityRule;
import org.libredemat.dao.jpa.IGenericDAO;
import org.libredemat.dao.users.IHomeFolderDAO;
import org.libredemat.dao.users.IIndividualDAO;
import org.libredemat.security.GenericAccessManager;
import org.libredemat.security.PermissionException;
import org.libredemat.security.SecurityContext;
import org.libredemat.security.annotation.Context;
import org.libredemat.security.annotation.ContextPrivilege;
import org.libredemat.security.annotation.ContextType;
import org.libredemat.security.annotation.IsUser;
import org.springframework.core.Ordered;


@Aspect
public class UsersContextAspect implements Ordered {

    private IHomeFolderDAO homeFolderDAO;

    private IIndividualDAO individualDAO;

    private IGenericDAO genericDAO;

    @Before("org.libredemat.SystemArchitecture.businessService() && @annotation(context) && within(org.libredemat.service.users..*)")
    public void contextAnnotatedMethod(JoinPoint joinPoint, Context context) {

        if (!ArrayUtils.contains(context.types(), ContextType.ECITIZEN)
            && !ArrayUtils.contains(context.types(), ContextType.AGENT)
            && !ArrayUtils.contains(context.types(), ContextType.EXTERNAL_SERVICE))
            return;

        if (SecurityContext.isFrontOfficeContext() && SecurityContext.getCurrentEcitizen() == null
            && ArrayUtils.contains(context.types(), ContextType.UNAUTH_ECITIZEN))
            return;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Method method = signature.getMethod();
        Annotation[][] parametersAnnotations = method.getParameterAnnotations();
        Object[] arguments = joinPoint.getArgs();
        Long homeFolderId = null;
        HomeFolder homeFolder = null;
        Long individualId = null;
        Individual individual = null;
        int i = 0;
        for (Object argument : arguments) {
            if (parametersAnnotations[i] != null && parametersAnnotations[i].length > 0) {
                Annotation parameterAnnotation = parametersAnnotations[i][0];
                if (parameterAnnotation.annotationType().equals(IsUser.class)) {
                    if (argument instanceof Long) {
                        Long id = (Long)argument;
                        individual = individualDAO.findById(id);
                        if (individual == null) {
                            homeFolder = homeFolderDAO.findById(id);
                            if (homeFolder != null)
                                homeFolderId = id;
                        } else {
                            individualId = id;
                        }
                    } else if (argument instanceof Individual) {
                        individual = (Individual)argument;
                        individualId = individual.getId();
                    } else if (argument instanceof HomeFolder) {
                        homeFolder = (HomeFolder)argument;
                        homeFolderId = homeFolder.getId();
                    }
                } 
            }
            i++;
        }
        if (homeFolder == null && individual != null && individual.getHomeFolder() != null) {
            homeFolder = individual.getHomeFolder();
            homeFolderId = homeFolder.getId();
        }
        if (!GenericAccessManager.performPermissionCheck(homeFolderId, individualId, context))
            throw new PermissionException(joinPoint.getSignature().getDeclaringType(), 
                    joinPoint.getSignature().getName(), context.types(), context.privilege(),
                    "access denied on home folder " + homeFolderId 
                        + " / individual " + individualId);

        if (SecurityContext.isBackOfficeContext()) {
            List<SiteRoles> siteRoles = Arrays
                    .asList(SecurityContext.getCurrentCredentialBean().getSiteRoles());
            Boolean isAdmin = false;
            int j = 0;
            while (!isAdmin && j < siteRoles.size()) {
                if (siteRoles.get(j).getProfile().equals(SiteProfile.ADMIN)) {
                    isAdmin = true;
                }
                j++;
            }

            if(!isAdmin) {
                UserSecurityRule rule = genericDAO.simpleSelect(UserSecurityRule.class).and("agentId", SecurityContext.getCurrentUserId()).unique();
                if (!can(rule, context.privilege()))
                    throw new PermissionException(joinPoint.getSignature().getDeclaringType(), joinPoint.getSignature().getName(), context.types(), context.privilege(),
                        "current agent does not have "+ context.privilege() +" prvilege on user referential");
            }
        }
    }

    private boolean can(UserSecurityRule rule, ContextPrivilege privilege) {
        switch(privilege) {
        case MANAGE:
            if (rule != null && UserSecurityProfile.MANAGE.equals(rule.getProfile()))
                return true;
            break;
        case WRITE:
            if (rule != null && UserSecurityProfile.writer.contains(rule.getProfile()))
                return true;
            break;
        default:
            return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 1;
    }

    public void setHomeFolderDAO(IHomeFolderDAO homeFolderDAO) {
        this.homeFolderDAO = homeFolderDAO;
    }

    public void setIndividualDAO(IIndividualDAO individualDAO) {
        this.individualDAO = individualDAO;
    }

    public void setGenericDAO(IGenericDAO genericDAO) {
        this.genericDAO = genericDAO;
    }

}
