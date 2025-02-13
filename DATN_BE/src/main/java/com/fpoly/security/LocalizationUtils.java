package com.fpoly.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;
@Component
public class LocalizationUtils {
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private LocaleResolver localeResolver;

	public String getLocalizedMessage(String messageKey, Object... params) {// spread operator
		HttpServletRequest request = WebUtils.getCurrentRequest();
		Locale locale = localeResolver.resolveLocale(request);

		return messageSource.getMessage(messageKey, params, locale);
	}
}
