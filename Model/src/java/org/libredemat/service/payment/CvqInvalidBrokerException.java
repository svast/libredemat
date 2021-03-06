package org.libredemat.service.payment;

import org.libredemat.exception.CvqException;

public class CvqInvalidBrokerException extends CvqException {

    private static final long serialVersionUID = 1L;

    public CvqInvalidBrokerException(final String key) {
        super(key);
    }

    public CvqInvalidBrokerException(final String key, final String[] i18n)
    {
        super(key, i18n);
    }
}
