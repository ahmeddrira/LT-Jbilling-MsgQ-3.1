
package com.sapienter.jbilling.client

import java.util.List;

import org.codehaus.groovy.grails.web.servlet.GrailsFlashScope 
import org.springframework.context.support.ReloadableResourceBundleMessageSource 
import com.sapienter.jbilling.common.SessionInternalError;

class ViewUtils {
	// thanks Groovy for adding the setters and getters for me
	ReloadableResourceBundleMessageSource messageSource;
	
	/**
	 * Will add to flas.errorMessages a list of string with each error message, if any.
	 * @param flash
	 * @param locale
	 * @param exception
	 * @return
	 * true if there are validation errors, otherwise false
	 */
	boolean resolveExceptionForValidation(GrailsFlashScope flash, Locale locale, SessionInternalError exception) {
		List<String> messages = new ArrayList<String>();
		if (exception.getErrorMessages()?.length > 0) {
			for (String message : exception.getErrorMessages()) {
				List<String> fields = message.split(",");
				String type = messageSource.getMessage("bean." + fields[0], null, locale);
				String property = messageSource.getMessage("bean." + fields[0] + "." + fields[1], null, locale);
				List restOfFields = null;
				if (fields.size() >= 4) {
					restOfFields = fields[3..fields.size()-1];
				}
				String errorMessage = messageSource.getMessage(fields[2], restOfFields as Object[] , locale);
				String finalMessage = messageSource.getMessage("validation.message", 
					[type, property, errorMessage] as Object[], locale);
				messages.add finalMessage;
			}
		}
		if (messages.size() > 0) {
			flash.errorMessages = messages;
            flash.message = null;
			return true;
		} else {
			return false;
		}
	}
	
}
