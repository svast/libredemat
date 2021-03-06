package org.libredemat.service.payment.job;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.libredemat.business.payment.ExternalAccountItem;
import org.libredemat.business.payment.ExternalNotificationStatus;
import org.libredemat.business.payment.Payment;
import org.libredemat.business.payment.PurchaseItem;
import org.libredemat.business.users.external.HomeFolderMapping;
import org.libredemat.dao.payment.IPaymentDAO;
import org.libredemat.exception.CvqException;
import org.libredemat.external.IExternalProviderService;
import org.libredemat.external.IExternalService;
import org.libredemat.security.SecurityContext;
import org.libredemat.service.authority.ILocalAuthorityRegistry;
import org.libredemat.service.payment.external.IPaymentExternalService;
import org.libredemat.service.users.external.IExternalHomeFolderService;
import org.libredemat.util.mail.IMailService;
import org.libredemat.util.translation.ITranslationService;


public class PaymentNotifyToExternalServicesJob {

    private static Logger logger = Logger.getLogger(PaymentNotifyToExternalServicesJob.class);

    private ILocalAuthorityRegistry localAuthorityRegistry;
    private IPaymentDAO paymentDAO;
    private IExternalService externalService;
    private IPaymentExternalService paymentExternalService;
    private IExternalHomeFolderService externalHomeFolderService;
    private ITranslationService translationService;
    private IMailService mailService;

    public void launchPaymentNotification() {
        localAuthorityRegistry.browseAndCallback(this, "notifyExternalServices", null);
    }

    public void launchErrorReport() {
        localAuthorityRegistry.browseAndCallback(this, "errorReport", null);
    }

    public void notifyExternalServices() {
        List<Payment> paymentsToNotify = paymentDAO.paymentNotSent();
        if (paymentsToNotify == null || paymentsToNotify.isEmpty()) return;

        for (Payment payment: paymentsToNotify) {
            try {
                sendPurchaseItems(payment,
                        paymentExternalService.externalServicesToNotify(payment));
            } catch (Exception e) {
                logger.error("Unexpected when sendPurchaseItems for payment with id : "
                        + payment.getId(), e);
            }
        }
    }

    private void sendPurchaseItems(Payment payment,
            Map<String, List<PurchaseItem>> externalServicesToNotify) {
        if (!externalServicesToNotify.isEmpty()) {
            for (String externalServiceLabel : externalServicesToNotify.keySet()) {
                IExternalProviderService service = externalService
                        .getExternalServiceByLabel(externalServiceLabel);
                if (service == null) {
                    logger.error("notifyPayments() No external service with label " +
                            externalServiceLabel + " has been found");
                    continue;
                }
                HomeFolderMapping mapping =
                    externalHomeFolderService.getHomeFolderMapping(externalServiceLabel, payment.getHomeFolderId());
                List<PurchaseItem> purchaseItems = externalServicesToNotify.get(externalServiceLabel);
                try {
                    service.creditHomeFolderAccounts(purchaseItems,
                            payment.getCvqReference(), payment.getBankReference(),
                            payment.getHomeFolderId(),
                            mapping == null ? null : mapping.getExternalLibreDematId(),
                            mapping == null ? null : mapping.getExternalId(), payment.getCommitDate());
                    changeItemsStatus(purchaseItems, ExternalNotificationStatus.ITEM_SENT);
                } catch (CvqException e) {
                    logger.error("Error sending payment notification to external service "
                            + externalServiceLabel);
                    changeItemsStatus(purchaseItems, ExternalNotificationStatus.ITEM_SENT_ERROR);
                } catch (Exception e) {
                    logger.error("Unexpected error sending payment notification to external service "
                            + externalServiceLabel);
                    changeItemsStatus(purchaseItems, ExternalNotificationStatus.ITEM_SENT_ERROR);
                } finally {
                    paymentDAO.update(payment);
                }
            }
        }
    }

    private void changeItemsStatus(List<PurchaseItem> purchaseItems,
            ExternalNotificationStatus status) {
        for (PurchaseItem item: purchaseItems) {
            if (item instanceof ExternalAccountItem) {
                ((ExternalAccountItem) item).setExternalNotificationStatus(status);
            }
        }
    }

    public void errorReport() {
        List<Payment> payments = paymentDAO.paymentSentError();
        if (payments != null && !payments.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append(translationService.translate("payment.error.report.nb",
                    new Object[]{payments.size()}));
            sb.append("\n\n" + translationService.translate("payment.error.report.details") + "\n");
            for (Payment payment: payments) {
                sb.append("\n" + translationService.translate("payment.error.report.payment",
                        new Object[] {payment.getId(), payment.getCvqReference(),
                        payment.getBroker()}) + "\n");
                if (payment.getPurchaseItems() != null) {
                    for (PurchaseItem item : payment.getPurchaseItems()) {
                        if (!(item instanceof ExternalAccountItem)) continue;
                        sb.append(translationService.translate("payment.error.report.item",
                                new Object[] {item.getId(),
                                ((ExternalAccountItem) item).getExternalServiceLabel(),
                                item.getInformativeFriendlyLabel()
                                }) + "\n");
                    }
                }
            }
            try {
                mailService.send(
                        SecurityContext.getCurrentSite().getAdminEmail(),
                        SecurityContext.getCurrentSite().getAdminEmail(),
                        null,
                        translationService.translate("payment.error.report.email.subject",
                                new Object[]{SecurityContext.getCurrentSite().getDisplayTitle()}),
                        translationService.translate("payment.error.report.email.body",
                                new Object[]{SecurityContext.getCurrentSite().getDisplayTitle()}),
                        sb.toString().getBytes("UTF-8"),
                        translationService.translate("payment.error.report.email.attachment"));
            } catch (Exception e) {
                logger.error("Error sending payments error report : " + sb.toString(), e);
            }
        }
    }

    public void setLocalAuthorityRegistry(ILocalAuthorityRegistry localAuthorityRegistry) {
        this.localAuthorityRegistry = localAuthorityRegistry;
    }

    public void setPaymentDAO(IPaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    public void setExternalService(IExternalService externalService) {
        this.externalService = externalService;
    }

    public void setPaymentExternalService(IPaymentExternalService paymentExternalService) {
        this.paymentExternalService = paymentExternalService;
    }

    public void setExternalHomeFolderService(IExternalHomeFolderService externalHomeFolderService) {
        this.externalHomeFolderService = externalHomeFolderService;
    }

    public void setTranslationService(ITranslationService translationService) {
        this.translationService = translationService;
    }

    public void setMailService(IMailService mailService) {
        this.mailService = mailService;
    }

}
