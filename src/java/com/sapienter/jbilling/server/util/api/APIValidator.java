/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.sapienter.jbilling.server.util.api;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.springframework.aop.MethodBeforeAdvice;

import com.sapienter.jbilling.server.user.UserWS;

/**
 *
 * @author emilc
 */
public class APIValidator implements MethodBeforeAdvice {

    private static final Logger LOG = Logger.getLogger(APIValidator.class);
    
    Validator validator;

    public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	public static Logger getLog() {
		return LOG;
	}

	public void before(Method method, Object[] args, Object target) throws Throwable {
        for (Object arg: args) {
        	String objectname = arg.getClass().getName();
        	if (objectname.endsWith("WS")) {
        		LOG.debug("Call to " + method.getName() + " Validating " + objectname);
        		Set<ConstraintViolation<UserWS>> constraintViolations =	validator.validate(newUser);
        		if (constraintViolations.size() > 0) {
        			log.warn "The user has errors" + constraintViolations.iterator().next().getMessage()
        			render(view:"user");
        		} else {
        			log.warn "The user does not have errors"
        		}
        	}
        }
    }

    /*
    public void afterReturning(Object ret, Method method, Object[] args, Object target) throws Throwable {
        StringBuffer retStr = new StringBuffer();
        if (ret != null) {
            if (ret.getClass().isArray()) {
                for (int f = 0; f < Array.getLength(ret); f++) {
                    Object val = Array.get(ret, f);
                    retStr.append("[");
                    retStr.append(val == null ? "null" : Array.get(ret, f).toString());
                    retStr.append("]");
                }
            } else {
                retStr.append(ret.toString());
            }
        } else {
            retStr.append("null");
        }
        LOG.debug("Done call to " + method.getName() + " returning: " + retStr);
    }
    */
}
