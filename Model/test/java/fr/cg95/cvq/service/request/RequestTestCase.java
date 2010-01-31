package fr.cg95.cvq.service.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.context.ConfigurableApplicationContext;

import fr.cg95.cvq.business.authority.Agent;
import fr.cg95.cvq.business.request.Category;
import fr.cg95.cvq.business.request.CategoryProfile;
import fr.cg95.cvq.business.request.RequestType;
import fr.cg95.cvq.business.request.ecitizen.VoCardRequest;
import fr.cg95.cvq.business.users.Adult;
import fr.cg95.cvq.business.users.Child;
import fr.cg95.cvq.business.users.CreationBean;
import fr.cg95.cvq.business.users.FamilyStatusType;
import fr.cg95.cvq.business.users.RoleType;
import fr.cg95.cvq.business.users.SexType;
import fr.cg95.cvq.business.users.TitleType;
import fr.cg95.cvq.dao.IGenericDAO;
import fr.cg95.cvq.exception.CvqException;
import fr.cg95.cvq.security.SecurityContext;
import fr.cg95.cvq.service.document.IDocumentService;
import fr.cg95.cvq.service.document.IDocumentTypeService;
import fr.cg95.cvq.testtool.BusinessObjectsFactory;
import fr.cg95.cvq.testtool.ServiceTestCase;
import fr.cg95.cvq.util.Critere;

public class RequestTestCase extends ServiceTestCase {

    protected IDocumentService documentService;
    protected IDocumentTypeService documentTypeService;
    
    protected IRequestSearchService requestSearchService;
    protected IRequestWorkflowService requestWorkflowService;
    protected IRequestDocumentService requestDocumentService;
    protected IMeansOfContactService meansOfContactService;

    protected IRequestTypeService requestTypeService;
    protected ICategoryService categoryService;

    protected Long voCardRequestId;
    protected Map<Long, Long> homeFolderVoCardRequestIds = new HashMap<Long, Long>();

    @Override
    protected void onSetUp() throws Exception {
        
        super.onSetUp();
        
        ConfigurableApplicationContext cac = getContext(getConfigLocations());
        
        IGenericDAO genericDAO = (IGenericDAO) cac.getBean("genericDAO");

        SecurityContext.setCurrentSite(localAuthorityName, SecurityContext.BACK_OFFICE_CONTEXT);
        SecurityContext.setCurrentAgent(agentNameWithSiteRoles);

        List<Category> categories = categoryService.getAll();
        if (categories != null && !categories.isEmpty())
            return;

        Category category = new Category();
        category.setName("General");
        List<RequestType> requestTypesSet = requestTypeService.getAllRequestTypes();
        for (RequestType requestType : requestTypesSet) {
            requestType.setCategory(category);
            genericDAO.update(requestType);
        }
        category.setRequestTypes(new HashSet<RequestType>(requestTypesSet));
        genericDAO.create(category);

        Agent agent = agentService.getByLogin(agentNameWithCategoriesRoles);
        categoryService.addCategoryRole(agent.getId(), category.getId(), 
                CategoryProfile.READ_WRITE);
        agent = agentService.getByLogin(agentNameWithManageRoles);
        categoryService.addCategoryRole(agent.getId(), category.getId(), 
                CategoryProfile.MANAGER);

        continueWithNewTransaction();
    }

    @Override
    protected void onTearDown() throws Exception {

        try {
            continueWithNewTransaction();
            
            SecurityContext.setCurrentSite(localAuthorityName, SecurityContext.BACK_OFFICE_CONTEXT);
            // to force re-association of agent within current session
            SecurityContext.setCurrentAgent(agentNameWithCategoriesRoles);
            // only delete for those who asked us for an home folder with request
            // at the beginning of their tests
            for (Long homeFolderId : homeFolderVoCardRequestIds.keySet()) {
                requestWorkflowService.delete(homeFolderVoCardRequestIds.get(homeFolderId));
            }

            voCardRequestId = null;
            homeFolderVoCardRequestIds.clear();

            continueWithNewTransaction();

            SecurityContext.setCurrentSite(localAuthorityName, SecurityContext.BACK_OFFICE_CONTEXT);
            // to force re-association of agent within current session
            SecurityContext.setCurrentAgent(agentNameWithCategoriesRoles);

            // ensure all requests have been deleted after each test
            assertEquals(0, requestSearchService.get(new HashSet<Critere>(), null, null, -1, 0).size());

            rollbackTransaction();
            SecurityContext.resetCurrentSite();
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error during tear down : " + e.getMessage());
        }

        super.onTearDown();
    }

    /**
     * Utility method used to easily get an home folder and individuals while running services
     * related tests
     */
    public CreationBean gimmeAnHomeFolderWithRequest()
        throws CvqException {

        // keep current context to reset it after home folder creation
        String currentContext = SecurityContext.getCurrentContext();
        Agent currentAgent = SecurityContext.getCurrentAgent();
        
        SecurityContext.setCurrentSite(localAuthorityName, SecurityContext.FRONT_OFFICE_CONTEXT);

        VoCardRequest request = new VoCardRequest();

        address = BusinessObjectsFactory.gimmeAdress("12","Rue d'Aligre", "Paris", "75012");

        homeFolderResponsible =
            BusinessObjectsFactory.gimmeAdult(TitleType.MISTER, "LASTNAME", "responsible", address,
                    FamilyStatusType.MARRIED);
        homeFolderService.addHomeFolderRole(homeFolderResponsible, RoleType.HOME_FOLDER_RESPONSIBLE);
        homeFolderResponsible.setPassword("toto");

        homeFolderWoman =
            BusinessObjectsFactory.gimmeAdult(TitleType.MADAM, "LASTNAME", "wife", address,
                    FamilyStatusType.MARRIED);
        homeFolderUncle =
            BusinessObjectsFactory.gimmeAdult(TitleType.MISTER, "LASTNAME", "uncle", address,
                    FamilyStatusType.SINGLE);
        List<Adult> adultSet = new ArrayList<Adult>();
        adultSet.add(homeFolderResponsible);
        adultSet.add(homeFolderWoman);
        adultSet.add(homeFolderUncle);

        child1 = BusinessObjectsFactory.gimmeChild("LASTNAME", "childone");
        child1.setSex(SexType.MALE);
        homeFolderService.addIndividualRole(homeFolderResponsible, child1, RoleType.CLR_FATHER);
        homeFolderService.addIndividualRole(homeFolderWoman, child1, RoleType.CLR_MOTHER);
        homeFolderService.addIndividualRole(homeFolderUncle, child1, RoleType.CLR_TUTOR);
        
        child2 = BusinessObjectsFactory.gimmeChild("LASTNAME", "childtwo");
        child2.setSex(SexType.MALE);
        homeFolderService.addIndividualRole(homeFolderResponsible, child2, RoleType.CLR_FATHER);

        List<Child> childSet = new ArrayList<Child>();
        childSet.add(child1);
        childSet.add(child2);

        requestWorkflowService.createAccountCreationRequest(request, adultSet, childSet, null, 
                address, null);

        CreationBean cb = new CreationBean();
        cb.setRequestId(request.getId());
        voCardRequestId = request.getId();
        cb.setHomeFolderId(request.getHomeFolderId());
        cb.setLogin(homeFolderResponsible.getLogin());
        
        homeFolderVoCardRequestIds.put(request.getHomeFolderId(), voCardRequestId);
        homeFolderIds.add(request.getHomeFolderId());

        if (currentContext != null)
            SecurityContext.setCurrentContext(currentContext);
        if (currentAgent != null)
            SecurityContext.setCurrentAgent(currentAgent);
        
        continueWithNewTransaction();
        
        return cb;
    }

    public void setDocumentService(IDocumentService documentService) {
        this.documentService = documentService;
    }
    
    public void setDocumentTypeService(IDocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }

    public void setRequestDocumentService(IRequestDocumentService requestDocumentService) {
        this.requestDocumentService = requestDocumentService;
    }

    public void setRequestSearchService(IRequestSearchService requestSearchService) {
        this.requestSearchService = requestSearchService;
    }

    public void setRequestWorkflowService(IRequestWorkflowService requestWorkflowService) {
        this.requestWorkflowService = requestWorkflowService;
    }

    public void setRequestTypeService(IRequestTypeService requestTypeService) {
        this.requestTypeService = requestTypeService;
    }

    public void setCategoryService(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void setMeansOfContactService(IMeansOfContactService meansOfContactService) {
        this.meansOfContactService = meansOfContactService;
    }
}
