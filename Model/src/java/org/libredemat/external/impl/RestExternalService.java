package org.libredemat.external.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.libredemat.business.payment.ExternalAccountItem;
import org.libredemat.business.payment.ExternalDepositAccountItem;
import org.libredemat.business.payment.ExternalInvoiceItem;
import org.libredemat.business.payment.ExternalTicketingContractItem;
import org.libredemat.business.payment.PurchaseItem;
import org.libredemat.business.request.RequestState;
import org.libredemat.business.request.external.RequestExternalAction;
import org.libredemat.business.request.workflow.event.IWorkflowPostAction;
import org.libredemat.business.request.workflow.event.impl.WorkflowCompleteEvent;
import org.libredemat.business.request.workflow.event.impl.WorkflowExtInProgressEvent;
import org.libredemat.business.request.workflow.event.impl.WorkflowPendingEvent;
import org.libredemat.business.request.workflow.event.impl.WorkflowRectifiedEvent;
import org.libredemat.dao.request.external.IRequestExternalActionDAO;
import org.libredemat.exception.CvqConfigurationException;
import org.libredemat.exception.CvqException;
import org.libredemat.exception.CvqRemoteException;
import org.libredemat.external.ExternalServiceBean;
import org.libredemat.external.ExternalServiceUtils;
import org.libredemat.security.SecurityContext;
import org.libredemat.service.request.IRequestWorkflowService;
import org.libredemat.util.web.WS;
import org.libredemat.util.web.WS.HttpResponse;
import org.libredemat.xml.common.RequestType;

import org.libredemat.modules.payment.schema.acc.AccountDetailsResponseDocument;
import org.libredemat.modules.payment.schema.ban.AccountUpdateType;
import org.libredemat.modules.payment.schema.ban.ContractUpdateType;
import org.libredemat.modules.payment.schema.ban.CreditAccountRequestDocument;
import org.libredemat.modules.payment.schema.ban.CreditAccountRequestDocument.CreditAccountRequest;
import org.libredemat.modules.payment.schema.ban.FamilyType;
import org.libredemat.modules.payment.schema.ban.InvoiceUpdateType;
import org.libredemat.modules.payment.schema.ban.PaymentType;
import org.libredemat.modules.payment.schema.cer.CheckExternalReferentialResponseDocument;
import org.libredemat.modules.payment.schema.cns.GetConsumptionsResponseDocument;
import org.libredemat.modules.payment.schema.fam.FamilyAccountsResponseDocument;
import org.libredemat.modules.payment.schema.inv.InvoiceDetailsResponseDocument;
import org.libredemat.modules.payment.schema.rei.ExternalInformationType;
import org.libredemat.modules.payment.schema.rei.GetExternalInformationResponseDocument;


public class RestExternalService extends ExternalProviderServiceAdapter {

    private static Logger logger = Logger.getLogger(RestExternalService.class);
    
    private IRequestExternalActionDAO requestExternalActionDAO;
    
    private String label;
    private Map<String, String> urls;
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void checkConfiguration(ExternalServiceBean externalServiceBean, String localAuthorityName)
            throws CvqConfigurationException {
    }

    @Override
    public List<String> checkExternalReferential(XmlObject requestXml) {
        CheckExternalReferentialResponseDocument responseDoc;
        try {
            String body = ExternalServiceUtils.getRequestFromFragment(requestXml);
            HttpResponse response = WS.url(urls.get("checkExternalReferential")).body(body).post();
            responseDoc = CheckExternalReferentialResponseDocument.Factory.parse(response.getString());
        } catch (Exception e) {
            logger.error("checkExternalReferential() got an exception : " + e.getMessage());
            List<String> errorList = new ArrayList<String>();
            errorList.add(e.getMessage());
            return errorList;
        }
        return Arrays.asList(responseDoc.getCheckExternalReferentialResponse().getMessageArray());
    }

    @Override
    public void creditHomeFolderAccounts(Collection<PurchaseItem> purchaseItems,
            String cvqReference, String bankReference, Long homeFolderId,
            String externalHomeFolderId, String externalId, Date validationDate)
            throws CvqException {
        Calendar calendar = Calendar.getInstance();
        CreditAccountRequestDocument doc = CreditAccountRequestDocument.Factory.newInstance();
        CreditAccountRequest creditAccountRequest = doc.addNewCreditAccountRequest();
        FamilyType familyType = creditAccountRequest.addNewFamily();
        familyType.setExternalLibreDematId(externalHomeFolderId);
        familyType.setExternalId(externalId);
        familyType.setId(homeFolderId);
        familyType.setZip(SecurityContext.getCurrentSite().getPostalCode());
        int totalAmount = 0;
        String broker = null;
        for (PurchaseItem purchaseItem : purchaseItems) {
            // purchase items in a payment transaction can not belong to more than one broker
            // so take the first we meet
            if (broker == null)
                broker = purchaseItem.getSupportedBroker();
            totalAmount += purchaseItem.getAmount().intValue();
        }
        PaymentType paymentType = creditAccountRequest.addNewPayment();
        paymentType.setPaymentBroker(broker);
        paymentType.setCvqAck(cvqReference);
        paymentType.setPaymentAck(bankReference);
        paymentType.setPaymentAmount(totalAmount);
        paymentType.setPaymentType("CB");
        calendar.setTime(validationDate);
        paymentType.setPaymentDate(calendar);
        List<AccountUpdateType> accountUpdateTypes = new ArrayList<AccountUpdateType>();
        List<ContractUpdateType> contractUpdateTypes = new ArrayList<ContractUpdateType>();
        List<InvoiceUpdateType> invoiceUpdateTypes = new ArrayList<InvoiceUpdateType>();
        for (PurchaseItem purchaseItem : purchaseItems) {
            if (purchaseItem instanceof ExternalDepositAccountItem) {
                ExternalDepositAccountItem edai = (ExternalDepositAccountItem) purchaseItem;
                AccountUpdateType updateType = AccountUpdateType.Factory.newInstance();
                updateType.setAccountId(edai.getExternalItemId());
                updateType.setExternalApplicationId(Long.valueOf(edai.getExternalApplicationId()));
                updateType.setExternalFamilyAccountId(edai.getExternalHomeFolderId());
                updateType.setAccountNewValue(edai.getOldValue().intValue() + edai.getAmount().intValue());
                updateType.setAccountOldValue(edai.getOldValue().intValue());
                calendar.setTime(edai.getOldValueDate());
                updateType.setAccountOldValueDate(calendar);
                accountUpdateTypes.add(updateType);
            } else if (purchaseItem instanceof ExternalInvoiceItem) {
                ExternalInvoiceItem eii = (ExternalInvoiceItem) purchaseItem;
                InvoiceUpdateType updateType = InvoiceUpdateType.Factory.newInstance();
                updateType.setInvoiceId(eii.getExternalItemId());
                updateType.setExternalApplicationId(Long.valueOf(eii.getExternalApplicationId()));
                updateType.setExternalFamilyAccountId(eii.getExternalHomeFolderId());
                updateType.setAmount(eii.getAmount().intValue());
                invoiceUpdateTypes.add(updateType);
            } else if (purchaseItem instanceof ExternalTicketingContractItem) {
                ExternalTicketingContractItem etci = (ExternalTicketingContractItem) purchaseItem;
                ContractUpdateType updateType = ContractUpdateType.Factory.newInstance();
                updateType.setContractId(etci.getExternalItemId());
                updateType.setExternalApplicationId(Long.valueOf(etci.getExternalApplicationId()));
                updateType.setExternalFamilyAccountId(etci.getExternalHomeFolderId());
                updateType.setExternalIndividualId(etci.getExternalIndividualId());
                updateType.setCapwebctIndividualId(etci.getSubjectId());
                updateType.setPrice(etci.getUnitPrice().intValue());
                updateType.setQuantity(etci.getQuantity());
                updateType.setAmount(etci.getAmount().intValue());
                contractUpdateTypes.add(updateType);
            }
        }
        if (accountUpdateTypes.size() > 0)
            creditAccountRequest.addNewAccounts().setAccountArray(
                    accountUpdateTypes.toArray(new AccountUpdateType[]{}));
        if (contractUpdateTypes.size() > 0)
            creditAccountRequest.addNewContracts().setContractArray(
                    contractUpdateTypes.toArray(new ContractUpdateType[]{}));
        if (invoiceUpdateTypes.size() > 0)
            creditAccountRequest.addNewInvoices().setInvoiceArray(
                    invoiceUpdateTypes.toArray(new InvoiceUpdateType[]{}));
        HttpResponse response = WS.url(urls.get("creditAccount")).body(doc.xmlText()).post();
        if (response.getStatus() != 200) {
            logger.error("creditHomeFolderAccounts() : bad response");
            logger.error(response.getString());
            throw new CvqRemoteException();
        }
    }

    @Override
    public Map<String, List<ExternalAccountItem>> getAccountsByHomeFolder(Long homeFolderId,
        String externalHomeFolderId, String externalId) throws CvqException {
        Map<String, Object> params = new HashMap<String, Object>(4);
        params.put("externalLibreDematId", externalHomeFolderId);
        params.put("externalId", externalId);
        params.put("homeFolderId", homeFolderId);
        params.put("localAuthority", SecurityContext.getCurrentSite().getName());
        HttpResponse response = WS.url(urls.get("getFamilyAccounts")).params(params).get();
        try {
            return ExternalServiceUtils.parseFamilyDocument(
                FamilyAccountsResponseDocument.Factory.parse(response.getString()),
                getLabel());
        } catch (XmlException e) {
            logger.error("getAccountsByHomeFolder() : bad response");
            logger.error(response.getString());
            throw new CvqRemoteException();
        }
    }

    @Override
    public Map<Date, String> getConsumptions(Long key, Date dateFrom, Date dateTo)
        throws CvqException {
        Map<String, Object> params = new HashMap<String, Object>(4);
        params.put("dateFrom", dateFormat.format(dateFrom));
        params.put("dateTo", dateFormat.format(dateTo));
        params.put("localAuthority", SecurityContext.getCurrentSite().getName());
        params.put("requestId", key);
        HttpResponse response = WS.url(urls.get("getConsumptions")).params(params).get();
        try {
            return ExternalServiceUtils.parseConsumptions(
                GetConsumptionsResponseDocument.Factory.parse(response.getString()));
        } catch (XmlException e) {
            logger.error("getConsumptions() : bad response");
            logger.error(response.getString());
            throw new CvqRemoteException();
        }
    }

    @Override
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    @Override
    public boolean handlesTraces() {
        return true;
    }

    @Override
    public String helloWorld() throws CvqException {
        return null;
    }

    @Override
    public void loadDepositAccountDetails(ExternalDepositAccountItem edai)
        throws CvqException {
        if (edai.getExternalItemId() == null
            || edai.getExternalApplicationId() == null
            || edai.getExternalHomeFolderId() == null) {
            logger.debug("loadDepositAccountDetails() Received un-handled deposit account, returning");
            return;
        }
        Map<String, Object> params = new HashMap<String, Object>(6);
        params.put("localAuthority", SecurityContext.getCurrentSite().getName());
        params.put("accountId", edai.getExternalItemId());
        params.put("externalApplicationId", edai.getExternalApplicationId());
        params.put("externalFamilyAccountId",edai.getExternalHomeFolderId());
        // FIXME : hard-coded 3 months range
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -3);
        params.put("startSearch", dateFormat.format(calendar.getTime()));
        params.put("endSearch", dateFormat.format(new Date()));
        HttpResponse response = WS.url(urls.get("loadAccountDetails")).params(params).get();
        try {
            ExternalServiceUtils.fillDepositAccountItem(edai,
                AccountDetailsResponseDocument.Factory.parse(response.getString()));
        } catch (XmlException e) {
            logger.error("loadDepositAccountDetails() : bad response");
            logger.error(response.getString());
            throw new CvqRemoteException();
        }
    }

    @Override
    public Map<String, Object> loadExternalInformations(XmlObject requestXml)
        throws CvqException {
        RequestType requestType = (RequestType) requestXml;
        HttpResponse response = WS.url(urls.get("loadExternalInformation"))
            .setParameter("requestId", String.valueOf(requestType.getId()))
            .setParameter("localAuthority", SecurityContext.getCurrentSite().getName()).get();
        int status = response.getStatus();
        logger.debug("loadExternalInformations() got response : " + response.getString());
        if (status == 200) {
            String doc = response.getString();
            try {
                Map<String, Object> result = new HashMap<String, Object>();
                for (ExternalInformationType info :
                    GetExternalInformationResponseDocument.Factory.parse(doc)
                        .getGetExternalInformationResponse().getExternalInformationArray()){
                    result.put(info.getKey(), info.getValue());
                }
                return result;
            } catch (XmlException e) {
                logger.error("loadExternalInformations() got an exception : " + e.getMessage());
                return Collections.emptyMap();
            }
        }
        logger.warn("loadExternalInformations() returned with status code : " + status);
        return Collections.emptyMap();
    }

    @Override
    public void loadInvoiceDetails(ExternalInvoiceItem eii) throws CvqException {
        if (eii.getExternalItemId() == null
                || eii.getExternalApplicationId() == null
                || eii.getExternalHomeFolderId() == null) {
            return;
        }
        Map<String, Object> params = new HashMap<String, Object>(4);
        params.put("localAuthority", SecurityContext.getCurrentSite().getName());
        params.put("invoiceId", eii.getExternalItemId());
        params.put("externalApplicationId", eii.getExternalHomeFolderId());
        params.put("externalFamilyAccountId", eii.getExternalHomeFolderId());
        HttpResponse response = WS.url(urls.get("loadInvoiceDetails")).params(params).get();
        try {
            ExternalServiceUtils.fillInvoiceItem(eii,
                InvoiceDetailsResponseDocument.Factory.parse(response.getString()));
        } catch (XmlException e) {
            logger.error("loadInvoiceDetails() : bad response");
            logger.error(response.getString());
            throw new CvqRemoteException();
        }
    }

    @Override
    public String sendRequest(XmlObject requestXml) throws CvqException {
        RequestType requestType = (RequestType) requestXml;
        RequestExternalAction est = new RequestExternalAction(new Date(),
            requestType.getId(), "libredemat", null, getLabel(), null);
        String body = ExternalServiceUtils.getRequestFromFragment(requestXml);
        logger.debug("sendRequest() sending : " + body);
        String id = null;
        try {
            HttpResponse response = WS.url(urls.get("sendRequest")).body(body).post();
            int status = response.getStatus();
            logger.debug("sendRequest() got response : " + response.getString());
            String message = null;
            if (status == 200 || status == 201) {
                est.setStatus(RequestExternalAction.Status.SENT);
                id = response.getString();
            } else {
                message = response.getString();
                if (status == 500) {
                    est.setStatus(RequestExternalAction.Status.ERROR);
                } else if (status == 404 || status == 403 || status == 401) {
                    est.setStatus(RequestExternalAction.Status.NOT_SENT);
                    est.setMessage("Le service distant a répondu avec le code : " + status);
                } else {
                    est.setStatus(RequestExternalAction.Status.ERROR);
                }
            }
            if (message != null) {
                message = StringEscapeUtils.escapeHtml(message);
                est.setMessage(message.substring(0, Math.min(message.length(), 254)));
            }
        } catch (Exception e) {
            est.setStatus(RequestExternalAction.Status.ERROR);
            est.setMessage(e.getMessage().substring(0, Math.min(e.getMessage().length(), 254)));
        }
        requestExternalActionDAO.create(est);

        return id;
    }

    @Override
    public boolean supportsConsumptions() {
        return false;
    }

    public void setUrls(Map<String, String> urls) {
        this.urls = urls;
    }

    public void setRequestExternalActionDAO(IRequestExternalActionDAO requestExternalActionDAO) {
        this.requestExternalActionDAO = requestExternalActionDAO;
    }

    @Override
    public void visit(final WorkflowPendingEvent wfEvent) throws CvqException {
        wfEvent.setWorkflowPostAction(new IWorkflowPostAction() {
            @Override
            public String getExecutor() {
                return getLabel();
            }

            @Override
            public void execute(IRequestWorkflowService requestWorkflowService) {
                try {
                    requestWorkflowService.updateRequestState(wfEvent.getRequest().getId(),
                            RequestState.EXTINPROGRESS, null);
                } catch (CvqException e) {
                    logger.error(e.getMessage());
                }
            }
        });
    }

    // Send on WorkflowExtInProgressEvent.
    @Override
    public void visit(final WorkflowExtInProgressEvent wfEvent) throws CvqException {
        checkExtReferentialAndSendRequest(wfEvent.getRequest());
    }

    // Default is to send on WorkflowCompleteEvent, hence this overriding.
    @Override
    public void visit(final WorkflowCompleteEvent wfEvent) throws CvqException {}

    @Override
    public void visit(final WorkflowRectifiedEvent wfEvent) throws CvqException {
        wfEvent.setWorkflowPostAction(new IWorkflowPostAction() {
            @Override
            public String getExecutor() {
                return getLabel();
            }

            @Override
            public void execute(IRequestWorkflowService requestWorkflowService) {
                try {
                    requestWorkflowService.updateRequestState(wfEvent.getRequest().getId(),
                            RequestState.EXTINPROGRESS, null);
                } catch (CvqException e) {
                    logger.error(e.getMessage());
                }
            }
        });
    }
}
