package org.libredemat.util.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.libredemat.business.request.Request;
import org.libredemat.business.users.MeansOfContact;
import org.libredemat.business.users.MeansOfContactEnum;
import org.libredemat.dao.hibernate.GenericDAO;
import org.libredemat.dao.hibernate.HibernateUtil;
import org.libredemat.dao.jpa.IGenericDAO;
import org.libredemat.dao.request.IRequestDAO;
import org.libredemat.exception.CvqException;
import org.libredemat.security.SecurityContext;
import org.libredemat.service.authority.ILocalAuthorityRegistry;
import org.libredemat.service.authority.impl.LocalAuthorityRegistry;
import org.libredemat.service.users.IMeansOfContactService;
import org.libredemat.service.users.impl.MeansOfContactService;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class MeansOfContactTransformer {
    private static Logger logger = Logger.getLogger(MeansOfContactTransformer.class);
    
    private static ILocalAuthorityRegistry localAuthorityRegistry;
    private static IMeansOfContactService meansOfContactService;
    private static IRequestDAO requestDAO;
    
    private List rmocSubList;
    private StringBuffer updateScriptStringBuffer;
    private StringBuffer subStringBuffer;
    
    
    private boolean isMail(RequestMeansOfContactDTO rmoc){
        if (rmoc.getName() == null)
            return false;
        if (rmoc.getName().equals("Mail")
            || rmoc.getName().equals("SimpleMail")
            || rmoc.getName().equals("ExpressMail"))
            return true;
        else
            return false;
    }
    
    private boolean isEmail(RequestMeansOfContactDTO rmoc){
        if (rmoc.getName() == null)
            return false;
        if (rmoc.getName().equals("Email"))
            return true;
        else
            return false;
    }
    
    private boolean isHomePhone(RequestMeansOfContactDTO rmoc){
        if (rmoc.getName() == null)
            return false;
        if (rmoc.getName().equals("HomePhone")
            || rmoc.getName().equals("Phone")
            || rmoc.getName().equals("Domicile Phone"))
            return true;
        else
            return false;
    }

    private boolean isMobilePhone(RequestMeansOfContactDTO rmoc){
        if (rmoc.getName() == null)
            return false;
        if (rmoc.getName().equals("MobilePhone"))
            return true;
        else
            return false;
    }

    private boolean isOfficePhone(RequestMeansOfContactDTO rmoc){
        if (rmoc.getName() == null)
            return false;
        if (rmoc.getName().equals("OfficePhone"))
            return true;
        else
            return false;
    }
    
    private boolean isLAOffice(RequestMeansOfContactDTO rmoc){
        if (rmoc.getName() == null)
            return false;
        if (rmoc.getName().equals("Visit"))
            return true;
        else
            return false;
    }

    private boolean isSms(RequestMeansOfContactDTO rmoc){
        if (rmoc.getName() == null)
            return false;
        if (rmoc.getName().equals("SMS"))
            return true;
        else
            return false;
    }
    
    /* Test Method ...
     */
    public void test(String localAuthority, String requestId) {
        logger.debug("TEST SINGLE");
        try {
            MeansOfContact mocMail= meansOfContactService.getMeansOfContactByType(MeansOfContactEnum.MAIL);
            Request request = (Request)requestDAO.findById(new Long(requestId));
            request.setMeansOfContact(mocMail);
            requestDAO.update(request);
            System.out.println("request.id=" + request.getId());
        } catch (Exception e) {
            logger.debug(e.getStackTrace());
        }
    }
    
    /* Generate an update instruction if :
     */
    private StringBuffer generateRequestMocUpdate(Request request) {        
        StringBuffer sb = new StringBuffer("UPDATE request SET ")
                .append("means_of_contact_id=").append(request.getMeansOfContact().getId()).append(" ")
                .append("WHERE id=").append(request.getId()).append(";"); 
        return sb;
    }
    
    /* Apply Mean of Contact's transformation to a subList.
     * To avoid hibernate memory error
     */
    public void transformMocSubList() {
        
        MeansOfContact mocMail =
            meansOfContactService.getMeansOfContactByType(MeansOfContactEnum.MAIL);
        MeansOfContact mocEmail =
            meansOfContactService.getMeansOfContactByType(MeansOfContactEnum.EMAIL);
        MeansOfContact mocHomePhone =
            meansOfContactService.getMeansOfContactByType(MeansOfContactEnum.HOME_PHONE);
        MeansOfContact mocMobilePhone =
            meansOfContactService.getMeansOfContactByType(MeansOfContactEnum.MOBILE_PHONE);
        MeansOfContact mocOfficePhone =
            meansOfContactService.getMeansOfContactByType(MeansOfContactEnum.OFFICE_PHONE);
        MeansOfContact mocSms =
            meansOfContactService.getMeansOfContactByType(MeansOfContactEnum.SMS);
        MeansOfContact mocLAOffice =
            meansOfContactService.getMeansOfContactByType(MeansOfContactEnum.LOCAL_AUTHORITY_OFFICE);
        
        // Used to generate updtae instruction if a HibernateError occures
        boolean isErrorCatched = false;
        List<Request> requestList = new ArrayList<Request>();
        
        for (Iterator it = rmocSubList.iterator(); it.hasNext();){
            RequestMeansOfContactDTO rmoc= (RequestMeansOfContactDTO) it.next();
            Request request = (Request)requestDAO.findById(rmoc.getRequestId());
            if (request == null) {
                logger.debug("request with id " + rmoc.getRequestId() + " does not exist");
            }
            if (isMail(rmoc))
                request.setMeansOfContact(mocMail);
            else if (isEmail(rmoc))
                request.setMeansOfContact(mocEmail);
            else if (isHomePhone(rmoc))
                request.setMeansOfContact(mocHomePhone);
            else if (isOfficePhone(rmoc))
                request.setMeansOfContact(mocOfficePhone);
            else if (isMobilePhone(rmoc))
                request.setMeansOfContact(mocMobilePhone);
            else if (isHomePhone(rmoc))
                request.setMeansOfContact(mocHomePhone);
            else if (isSms(rmoc))
                request.setMeansOfContact(mocSms);
            else if (isLAOffice(rmoc))
                request.setMeansOfContact(mocLAOffice);
            else
                request.setMeansOfContact(mocMail);

            requestDAO.update(request);
            requestList.add(request);

            try {
                HibernateUtil.getSession().flush();
            } catch (HibernateException he) {
                logger.debug("error request =" + request.getId());
                if (!isErrorCatched)
                    isErrorCatched = true;
            }
        }
        if (isErrorCatched)
            for (Request request : requestList)
                subStringBuffer.append(generateRequestMocUpdate(request)).append("\n");
    }
    
    /* Split list of RequestMeanOfcontact into sublist with size=100
     * Open one hibernate transaction per sublist and apply MeanOfContact changes
     */
    public void transformMoc() throws CvqException {
        
        updateScriptStringBuffer =
            new StringBuffer("-- REQUEST'S MEANS OF CONTACT UPDATES").append("\n\n");
        subStringBuffer = new StringBuffer();
        
        List rmocList = RequestMeansOfContactDAO.listAll();
        
        // Display current Local Authority
        String localAuthorityName = SecurityContext.getCurrentSite().getName();
        System.out.println("Means Of Contact migration for : " + localAuthorityName);
        
        int subListSize = 50;
        int listSize = rmocList.size();
        int subListNumber = listSize / subListSize;
        int lastSubListSize = listSize % subListSize;
        
        logger.debug("transformMoc() - listSize="+listSize
                +" subListNumber="+subListNumber
                +" lastSubListSize="+lastSubListSize);
        
        for (int i=0;i<subListNumber;i++) {
            rmocSubList = rmocList.subList(subListSize*i, subListSize*(i+1));
            localAuthorityRegistry.callback(
                    localAuthorityName, this, "transformMocSubList", null);
            System.out.print(".");
        }
        if (lastSubListSize > 0) {
            rmocSubList = rmocList.subList(subListSize*subListNumber, listSize);
            localAuthorityRegistry.callback(
                localAuthorityName, this, "transformMocSubList", null);
                System.out.print(".");
        }
        System.out.println();
        
        try {
            // Generate an sql update script for request which causes error
            updateScriptStringBuffer.append(subStringBuffer);
            File xmlFile = 
                File.createTempFile(localAuthorityName + "_request_meansofcontact_", ".sql");
            FileOutputStream xmlFos = new FileOutputStream(xmlFile);
            xmlFos.write(updateScriptStringBuffer.toString().getBytes());
        } catch(IOException ioe){
            throw new CvqException(ioe.getMessage());
        }
    }
    
    
    public static void main(final String[] args) throws Exception {
        String config = args[0];
//        String localAuthoriyName = args[1];
//        String requestId = args[2];
        
        // Switch Off logs for localAuthorityRegistry
        Logger loggerLAR = Logger.getLogger(LocalAuthorityRegistry.class);
        loggerLAR.setLevel(Level.OFF);
        // Switch Off logs for this
        logger.setLevel(Level.ERROR);
        
        ClassPathXmlApplicationContext cpxa = SpringApplicationContextLoader.loadContext(config);
        
        localAuthorityRegistry = (LocalAuthorityRegistry)cpxa.getBean("localAuthorityRegistry");
        meansOfContactService = (MeansOfContactService)cpxa.getBean("meansOfContactService");
        requestDAO = (IRequestDAO)cpxa.getBean("genericDAO");
        
        MeansOfContactTransformer meansOfContactTransformer= new MeansOfContactTransformer();
        localAuthorityRegistry.browseAndCallback(meansOfContactTransformer, "transformMoc", null);
        //localAuthorityRegistry.browseAndCallback(meansOfContactTransformer, "test", new Object[]{requestId});
    }
    
    private static class RequestMeansOfContactDAO {
        public static List listAll(){
            return HibernateUtil.getSession().createSQLQuery(
                 "SELECT " +
                 "  request_id AS requestId, means_of_contact_id AS meansOfContactId, name" + 
                 "  FROM request_means_of_contact AS rmoc" +
                 "      LEFT JOIN local_referential_data AS lrd " +
                 "      ON rmoc.means_of_contact_id = lrd.id")
                .addScalar("requestId")
                .addScalar("meansOfContactId")
                .addScalar("name")
                .setResultTransformer(Transformers.aliasToBean(RequestMeansOfContactDTO.class))
                .list();
        }
    }
    
}
