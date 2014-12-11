package org.libredemat.service.payment.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.libredemat.business.payment.Payment;
import org.libredemat.business.payment.PaymentMode;
import org.libredemat.exception.CvqConfigurationException;
import org.libredemat.exception.CvqException;
import org.libredemat.service.payment.IPaymentProviderService;
import org.libredemat.service.payment.PaymentResultBean;
import org.libredemat.service.payment.PaymentResultStatus;
import org.libredemat.service.payment.PaymentServiceBean;


public final class FakePaymentProviderService implements IPaymentProviderService {

    private String paymentUrl;
    private String callbackUrl;

    public void checkConfiguration(PaymentServiceBean paymentServiceBean)
            throws CvqConfigurationException {
    }

    public URL doInitPayment(Payment payment, PaymentServiceBean paymentServiceBean)
        throws CvqException {

        try {
            String cvqReference = "FPS_" + payment.getHomeFolderId()
                + "_" + System.currentTimeMillis();
            payment.setCvqReference(cvqReference);

            String domainName = payment.getPaymentSpecificData().get("domainName");
            String scheme = payment.getPaymentSpecificData().get("scheme");
            String port = payment.getPaymentSpecificData().get("port");

            if (payment.getPaymentSpecificData().get("callbackUrl") != null)
                callbackUrl = payment.getPaymentSpecificData().get("callbackUrl");

            if(port == null) port = "80";
            if(scheme == null) scheme = "https";

            String baseSiteUrl = scheme + "://" + domainName + ":" + port;

            StringBuffer urlBuffer = new StringBuffer().append(baseSiteUrl).append(paymentUrl)
                .append("?cvqReference=").append(cvqReference)
                .append("&amount=").append(payment.getEuroAmount())
                .append("&callbackUrl=").append(baseSiteUrl).append(callbackUrl)
                .append("&libreDematFake=true");

            String email = payment.getPaymentSpecificDataByKey(Payment.SPECIFIC_DATA_EMAIL);
            if (email != null && !email.equals(""))
                urlBuffer.append("&email=").append(email);

            return new URL(urlBuffer.toString());

        } catch (MalformedURLException mue) {
            throw new CvqException(mue.getMessage());
        }
    }

    public PaymentResultBean doCommitPayment(Map<String, String> parameters,
            PaymentServiceBean paymentServiceBean) throws CvqException {

        PaymentResultStatus returnStatus = getStateFromParameters(parameters, paymentServiceBean);

        String bankReference = parameters.get("bankReference");
        return new PaymentResultBean(returnStatus, parameters.get("cvqReference"),
                bankReference == null ? parameters.get("cvqReference") : bankReference);
    }

    public PaymentMode getPaymentMode() {
        return PaymentMode.INTERNET;
    }

    public boolean handleParameters(Map<String, String> parameters) {
        if (parameters.get("libreDematFake") != null)
            return true;
        else
            return false;
    }

    public PaymentResultStatus getStateFromParameters(Map<String, String> parameters,
            PaymentServiceBean paymentServiceBean) throws CvqException {

        if (!handleParameters(parameters))
            return PaymentResultStatus.UNKNOWN;

        String bankTransactionStatus = parameters.get("status");
        if (bankTransactionStatus.equals("OK"))
            return PaymentResultStatus.OK;
        else if (bankTransactionStatus.equals("CANCELLED"))
            return PaymentResultStatus.CANCELLED;
        else if (bankTransactionStatus.equals("REFUSED"))
            return PaymentResultStatus.REFUSED;

        return PaymentResultStatus.UNKNOWN;
    }

    public final void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public final void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    @Override
    public boolean allowMultiplePurchaseItems() {
        return true;
    }
}
