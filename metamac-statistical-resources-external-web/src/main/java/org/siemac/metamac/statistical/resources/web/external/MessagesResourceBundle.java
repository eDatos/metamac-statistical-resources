package org.siemac.metamac.statistical.resources.web.external;

import static java.util.ResourceBundle.Control.getNoFallbackControl;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.LocaleUtils;

// https://stackoverflow.com/questions/7469223/jsp-and-resourcebundles
public class MessagesResourceBundle extends ResourceBundle {

    private static final String MESSAGES_ATTRIBUTE_NAME = "msg";
    private static final String MESSAGES_BASE_NAME      = "i18n.messages-swagger"; // Comes from folders and filename: i18n/messages-swagger.properties

    public MessagesResourceBundle(Locale locale) {
        setLocale(locale);
    }

    public MessagesResourceBundle(String locale) {
        setLocale(locale);
    }

    public static void setFor(HttpServletRequest request) {
        if (request.getSession().getAttribute(MESSAGES_ATTRIBUTE_NAME) == null) {
            request.getSession().setAttribute(MESSAGES_ATTRIBUTE_NAME, new MessagesResourceBundle(request.getLocale()));
        }
    }

    public static MessagesResourceBundle getCurrentInstance(HttpServletRequest request) {
        return (MessagesResourceBundle) request.getSession().getAttribute(MESSAGES_ATTRIBUTE_NAME);
    }

    public void setLocale(Locale locale) {
        if (parent == null || !parent.getLocale().equals(locale)) {
            setParent(getBundle(MESSAGES_BASE_NAME, locale, getNoFallbackControl(ResourceBundle.Control.FORMAT_DEFAULT)));
        }
    }

    public void setLocale(String locale) {
        setLocale(LocaleUtils.toLocale(locale));
    }

    @Override
    public Enumeration<String> getKeys() {
        return parent.getKeys();
    }

    @Override
    protected Object handleGetObject(String key) {
        return parent.getObject(key);
    }

}
