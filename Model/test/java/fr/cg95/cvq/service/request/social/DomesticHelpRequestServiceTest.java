package fr.cg95.cvq.service.request.social;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import fr.cg95.cvq.business.document.DepositOrigin;
import fr.cg95.cvq.business.document.DepositType;
import fr.cg95.cvq.business.document.Document;
import fr.cg95.cvq.business.request.MeansOfContact;
import fr.cg95.cvq.business.request.MeansOfContactEnum;
import fr.cg95.cvq.business.request.Request;
import fr.cg95.cvq.business.request.RequestDocument;
import fr.cg95.cvq.business.request.RequestState;
import fr.cg95.cvq.business.request.social.DhrDwellingKindType;
import fr.cg95.cvq.business.request.social.DhrDwellingStatusType;
import fr.cg95.cvq.business.request.social.DhrGuardianMeasureType;
import fr.cg95.cvq.business.request.social.DhrPrincipalPensionPlanType;
import fr.cg95.cvq.business.request.social.DhrRequestKindType;
import fr.cg95.cvq.business.request.social.DomesticHelpRequest;
import fr.cg95.cvq.business.users.Address;
import fr.cg95.cvq.business.users.Adult;
import fr.cg95.cvq.business.users.CreationBean;
import fr.cg95.cvq.business.users.FamilyStatusType;
import fr.cg95.cvq.business.users.HomeFolder;
import fr.cg95.cvq.business.users.NationalityType;
import fr.cg95.cvq.business.users.RoleType;
import fr.cg95.cvq.business.users.TitleType;
import fr.cg95.cvq.exception.CvqException;
import fr.cg95.cvq.exception.CvqObjectNotFoundException;
import fr.cg95.cvq.security.SecurityContext;
import fr.cg95.cvq.service.document.IDocumentTypeService;
import fr.cg95.cvq.testtool.BusinessObjectsFactory;
import fr.cg95.cvq.testtool.ServiceTestCase;
import fr.cg95.cvq.util.Critere;

/**
 * Generated by Velocity if not present, can be edited safely !
 */
public class DomesticHelpRequestServiceTest extends ServiceTestCase {

    protected IDomesticHelpRequestService iDomesticHelpRequestService;

    @Override
    protected void onSetUp() throws Exception {
    	super.onSetUp();
        iDomesticHelpRequestService = 
            (IDomesticHelpRequestService) getBean(StringUtils.uncapitalize("DomesticHelpRequest") + "Service");
    }

    protected DomesticHelpRequest fillMeARequest() {

        DomesticHelpRequest request = new DomesticHelpRequest();
            request.setDhrSpousePrincipalPensionPlan(DhrPrincipalPensionPlanType.C_N_A_V);
                            request.setDhrSpouseProfession("DhrSpouseProfession");
                request.setDhrNetIncome(BigInteger.valueOf(1));
            request.setProfessionalTaxes(BigInteger.valueOf(1));
            request.setDhrIsSpouseRetired(Boolean.valueOf(true));
              request.setDhrSpouseTitle(TitleType.MISTER);
                request.setDhrRequesterBirthDate(new Date());
            request.setDhrRealEstateInvestmentIncome(BigInteger.valueOf(1));
            request.setDhrRequesterIsFrenchResident(Boolean.valueOf(true));
                            Address DhrCurrentDwellingAddress = BusinessObjectsFactory.gimmeAdress("1", "Unit test address", "Paris", "75012");
            request.setDhrCurrentDwellingAddress(DhrCurrentDwellingAddress);
    	                request.setDhrSpouseFranceArrivalDate(new Date());
                      request.setDhrRequesterNationality(NationalityType.FRENCH);
                request.setDhrCurrentDwellingArrivalDate(new Date());
              if ("DhrReferentFirstName".length() > 38)
        request.setDhrReferentFirstName("DhrReferentFirstName".substring(0, 38));
      else
        request.setDhrReferentFirstName("DhrReferentFirstName");
                request.setDhrIncomesAnnualTotal(BigInteger.valueOf(1));
            request.setDhrRequesterHaveGuardian(Boolean.valueOf(true));
            request.setDhrIncomeTax(BigInteger.valueOf(1));
                request.setDhrSpouseBirthPlace("DhrSpouseBirthPlace");
                request.setDhrSpouseBirthDate(new Date());
              request.setDhrRequesterFranceArrivalDate(new Date());
              request.setDhrCurrentDwellingStatus(DhrDwellingStatusType.OWNER);
                  if ("DhrSpouseFirstName".length() > 38)
        request.setDhrSpouseFirstName("DhrSpouseFirstName".substring(0, 38));
      else
        request.setDhrSpouseFirstName("DhrSpouseFirstName");
                  request.setDhrSpouseFamilyStatus(FamilyStatusType.MARRIED);
                request.setDhrFurnitureInvestmentIncome(BigInteger.valueOf(1));
                            Address DhrGuardianAddress = BusinessObjectsFactory.gimmeAdress("1", "Unit test address", "Paris", "75012");
            request.setDhrGuardianAddress(DhrGuardianAddress);
    	                  if ("DhrReferentName".length() > 38)
        request.setDhrReferentName("DhrReferentName".substring(0, 38));
      else
        request.setDhrReferentName("DhrReferentName");
                request.setLocalRate(BigInteger.valueOf(1));
                request.setDhrSpouseEmployer("DhrSpouseEmployer");
                  request.setDhrRequestKind(DhrRequestKindType.INDIVIDUAL);
                  request.setDhrPrincipalPensionPlan(DhrPrincipalPensionPlanType.C_N_A_V);
                    request.setDhrComplementaryPensionPlan("DhrComplementaryPensionPlan");
                                Address DhrReferentAddress = BusinessObjectsFactory.gimmeAdress("1", "Unit test address", "Paris", "75012");
            request.setDhrReferentAddress(DhrReferentAddress);
    	                request.setPropertyTaxes(BigInteger.valueOf(1));
              if ("DhrGuardianName".length() > 38)
        request.setDhrGuardianName("DhrGuardianName".substring(0, 38));
      else
        request.setDhrGuardianName("DhrGuardianName");
                request.setPensions(BigInteger.valueOf(1));
              request.setDhrCurrentDwellingKind(DhrDwellingKindType.PLACE_OF_RESIDENCE);
                    request.setDhrGuardianMeasure(DhrGuardianMeasureType.SAFEGUARDING_JUSTICE);
                  if ("DhrCurrentDwellingPhone".length() > 10)
        request.setDhrCurrentDwellingPhone("DhrCurrentDwellingPhone".substring(0, 10));
      else
        request.setDhrCurrentDwellingPhone("DhrCurrentDwellingPhone");
                request.setDhrSpouseIsFrenchResident(Boolean.valueOf(true));
            request.setDhrAllowances(BigInteger.valueOf(1));
              request.setDhrSpouseNationality(NationalityType.FRENCH);
                  if ("DhrSpouseMaidenName".length() > 38)
        request.setDhrSpouseMaidenName("DhrSpouseMaidenName".substring(0, 38));
      else
        request.setDhrSpouseMaidenName("DhrSpouseMaidenName");
                          if ("DhrSpouseName".length() > 38)
        request.setDhrSpouseName("DhrSpouseName".substring(0, 38));
      else
        request.setDhrSpouseName("DhrSpouseName");
                    request.setDhrSpousePensionPlanDetail("DhrSpousePensionPlanDetail");
                    request.setDhrRequesterBirthPlace("DhrRequesterBirthPlace");
                                Address DhrSpouseAddress = BusinessObjectsFactory.gimmeAdress("1", "Unit test address", "Paris", "75012");
            request.setDhrSpouseAddress(DhrSpouseAddress);
    	                request.setDhrHaveFamilyReferent(Boolean.valueOf(true));
                request.setDhrPensionPlanDetail("DhrPensionPlanDetail");
                    request.setDhrSpouseComplementaryPensionPlan("DhrSpouseComplementaryPensionPlan");
      
        // Means Of Contact
        MeansOfContact meansOfContact = iMeansOfContactService.getMeansOfContactByType(
                    MeansOfContactEnum.EMAIL);
        request.setMeansOfContact(meansOfContact);
        
        DomesticHelpRequestFeeder.feed(request);
        
        return request;
    }
        	
    protected void completeValidateAndDelete(DomesticHelpRequest request) 
    	throws CvqException, java.io.IOException {
    	
        // add a document to the request
        ///////////////////////////////

        Document doc = new Document();
        doc.setEcitizenNote("Ma carte d'identitÃ© !");
        doc.setDepositOrigin(DepositOrigin.ECITIZEN);
        doc.setDepositType(DepositType.PC);
        doc.setHomeFolderId(request.getHomeFolderId());
        doc.setIndividualId(request.getRequesterId());
        doc.setDocumentType(iDocumentTypeService.getDocumentTypeByType(IDocumentTypeService.IDENTITY_RECEIPT_TYPE));
        Long documentId = iDocumentService.create(doc);
        iDomesticHelpRequestService.addDocument(request.getId(), documentId);
        Set<RequestDocument> documentsSet =
            iDomesticHelpRequestService.getAssociatedDocuments(request.getId());
        assertEquals(documentsSet.size(), 1);

        // FIXME : test list of pending / in-progress registrations
        Critere testCrit = new Critere();
        testCrit.setAttribut(Request.SEARCH_BY_HOME_FOLDER_ID);
        testCrit.setComparatif(Critere.EQUALS);
        testCrit.setValue(request.getHomeFolderId());
        Set<Critere> testCritSet = new HashSet<Critere>();
        testCritSet.add(testCrit);
        List<Request> allRequests = iRequestService.get(testCritSet, null, null, -1, 0);
        assertNotNull(allRequests);

        // close current session and re-open a new one
        continueWithNewTransaction();
        
        SecurityContext.setCurrentSite(localAuthorityName,
                                        SecurityContext.BACK_OFFICE_CONTEXT);
        SecurityContext.setCurrentAgent(agentNameWithCategoriesRoles);
        iRequestWorkflowService.updateRequestState(request.getId(), RequestState.COMPLETE, null);
        iRequestWorkflowService.updateRequestState(request.getId(), RequestState.VALIDATED, null);

        // close current session and re-open a new one
        continueWithNewTransaction();
        
        byte[] generatedCertificate = iRequestService.getCertificate(request.getId(),
                                                                     RequestState.PENDING);

        if (generatedCertificate == null)
            fail("No certificate found");
            
        //     Write tele-service xml data file
        File xmlFile = File.createTempFile("tmp" + request.getId(), ".xml");
        FileOutputStream xmlFos = new FileOutputStream(xmlFile);
        xmlFos.write(iRequestService.getById(request.getId()).modelToXmlString().getBytes());

        File file = File.createTempFile("tmp" + request.getId(), ".pdf");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(generatedCertificate);

        // close current session and re-open a new one
        continueWithNewTransaction();
        
        // delete request
        iDomesticHelpRequestService.delete(request.getId());
    }

    public void testWithHomeFolderPojo()
    		throws CvqException, CvqObjectNotFoundException,
                java.io.FileNotFoundException, java.io.IOException {

         SecurityContext.setCurrentSite(localAuthorityName, SecurityContext.FRONT_OFFICE_CONTEXT);

         // create a vo card request (to create home folder and associates)
         CreationBean cb = gimmeAnHomeFolder();

         SecurityContext.setCurrentEcitizen(cb.getLogin());

         // get the home folder id
         HomeFolder homeFolder = iHomeFolderService.getById(cb.getHomeFolderId());
         assertNotNull(homeFolder);
         Long homeFolderId = homeFolder.getId();
         assertNotNull(homeFolderId);

         // fill and create the request
         //////////////////////////////

         DomesticHelpRequest request = fillMeARequest();
         request.setRequesterId(SecurityContext.getCurrentUserId());
         request.setHomeFolderId(homeFolderId);
         DomesticHelpRequestFeeder.setSubject(request, 
             iDomesticHelpRequestService.getSubjectPolicy(), null, homeFolder);
         
         Long requestId =
              iDomesticHelpRequestService.create(request);

         DomesticHelpRequest requestFromDb =
        	 	(DomesticHelpRequest) iDomesticHelpRequestService.getById(requestId);
         assertEquals(requestId, requestFromDb.getId());
         assertNotNull(requestFromDb.getRequesterId());
         assertNotNull(requestFromDb.getRequesterLastName());
         if (requestFromDb.getSubjectId() != null)
             assertNotNull(requestFromDb.getSubjectLastName());
         
         completeValidateAndDelete(requestFromDb);

         HomeFolder homeFolderAfterDelete = iHomeFolderService.getById(homeFolderId);
         assertNotNull(homeFolderAfterDelete);
         assertNotNull(iHomeFolderService.getHomeFolderResponsible(homeFolderAfterDelete.getId()));
         
         SecurityContext.resetCurrentSite();
    }


    public void testWithoutHomeFolder()
        throws CvqException, CvqObjectNotFoundException,
               java.io.FileNotFoundException, java.io.IOException {

	      if (!iDomesticHelpRequestService.supportUnregisteredCreation())
	         return;

	      startTransaction();
	
        SecurityContext.setCurrentSite(localAuthorityName, SecurityContext.FRONT_OFFICE_CONTEXT);
        
        DomesticHelpRequest request = fillMeARequest();

        Address address = BusinessObjectsFactory.gimmeAdress("12", "Rue d'Aligre", "Paris", "75012");
        Adult requester =
            BusinessObjectsFactory.gimmeAdult(TitleType.MISTER, "LASTNAME", "requester", address,
                                              FamilyStatusType.MARRIED);
        requester.setPassword("requester");
        requester.setAdress(address);
        iHomeFolderService.addHomeFolderRole(requester, RoleType.HOME_FOLDER_RESPONSIBLE);
        DomesticHelpRequestFeeder.setSubject(request, 
            iDomesticHelpRequestService.getSubjectPolicy(), requester, null);

        Long requestId =
             iDomesticHelpRequestService.create(request, requester, requester);
        
        // close current session and re-open a new one
        continueWithNewTransaction();
        
        // start testing request creation
        /////////////////////////////////

        DomesticHelpRequest requestFromDb =
            (DomesticHelpRequest) iDomesticHelpRequestService.getById(requestId);
        assertEquals(requestId, requestFromDb.getId());
        assertNotNull(requestFromDb.getRequesterId());
        assertNotNull(requestFromDb.getRequesterLastName());
        if (requestFromDb.getSubjectId() != null)
            assertNotNull(requestFromDb.getSubjectLastName());
        
        Long homeFolderId = requestFromDb.getHomeFolderId();
        Long requesterId = requestFromDb.getRequesterId();

        // close current session and re-open a new one
        continueWithNewTransaction();
        
        completeValidateAndDelete(requestFromDb);
        
        // close current session and re-open a new one
        continueWithNewTransaction();
        
        try {
            iHomeFolderService.getById(homeFolderId);
            fail("should not have found home folder");
        } catch (CvqObjectNotFoundException confe) {
            // great, that was expected
        }
        try {
            iIndividualService.getById(requesterId);
            fail("should not have found requester");
        } catch (CvqObjectNotFoundException confe) {
            // great, that was expected
        }

        SecurityContext.resetCurrentSite();
        
        commitTransaction();
    }
}
