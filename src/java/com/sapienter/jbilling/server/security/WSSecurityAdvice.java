/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2010 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package com.sapienter.jbilling.server.security;

import com.sapienter.jbilling.client.authentication.CompanyUserDetails;
import com.sapienter.jbilling.server.user.db.UserDAS;
import com.sapienter.jbilling.server.user.db.UserDTO;
import com.sapienter.jbilling.server.util.Context;
import grails.plugins.springsecurity.SpringSecurityService;
import org.apache.log4j.Logger;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Security advice for web-service method calls to ensure that only data belonging to the
 * web-service caller is accessed.
 *
 * @author Brian Cowdery
 * @since 01-11-2010
 */
public class WSSecurityAdvice implements MethodBeforeAdvice {
    private static final Logger LOG = Logger.getLogger(WSSecurityAdvice.class);

    private SpringSecurityService springSecurityService;
    private TransactionTemplate transactionTemplate;

    public SpringSecurityService getSpringSecurityService() {
        if (springSecurityService == null)
            springSecurityService = Context.getBean(Context.Name.SPRING_SECURITY_SERVICE);
        return springSecurityService;
    }

    public void setSpringSecurityService(SpringSecurityService springSecurityService) {
        this.springSecurityService = springSecurityService;
    }

    public TransactionTemplate getTransactionTemplate() {
        if (transactionTemplate == null) {
            PlatformTransactionManager transactionManager = Context.getBean(Context.Name.TRANSACTION_MANAGER);
            transactionTemplate = new TransactionTemplate(transactionManager);
        }
        return transactionTemplate;
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public Integer getCallerCompanyId() {
        CompanyUserDetails details = (CompanyUserDetails) getSpringSecurityService().getPrincipal();
        return details.getCompanyId();
    }

    public String getCallerUserName() {
        CompanyUserDetails details = (CompanyUserDetails) getSpringSecurityService().getPrincipal();
        return details.getUsername();
    }

    /**
     * Validates that method call arguments are accessible to the web-service caller company.
     *
     * @param method method to call
     * @param args method arguments to validate
     * @param target method call target, may be null
     * @throws Throwable throws a SecurityException if the calling user does not have access to the given data
     */
    public void before(Method method, Object[] args, Object target) throws Throwable {
        if (!getSpringSecurityService().isLoggedIn())
            throw new SecurityException("Web-service call has not been authenticated.");

        LOG.debug("Validating web-service method '" + method.getName() + "()'");

        // try validating the method call itself
        WSSecured securedMethod = getMappedSecuredWS(method, args);
        if (securedMethod != null)
            validate(securedMethod);

        // validate each method call argument
        for (Object o : args) {
            if (o != null) {
                if (o instanceof Collection) {
                    for (Object element : (Collection) o)
                        validate(element);

                } else if (o.getClass().isArray()) {
                    for (Object element : (Object[]) o)
                        validate(element);

                } else {
                    validate(o);
                }
            }
        }
    }

    /**
     * Attempt to map the method call as an instance of WSSecured so that it can be validated.
     *
     * @see com.sapienter.jbilling.server.security.WSSecurityMethodMapper
     *
     * @param method method to map
     * @param args method arguments
     * @return mapped method call, or null if method call is unknown
     */
    protected WSSecured getMappedSecuredWS(final Method method, final Object[] args) {
        return getTransactionTemplate().execute(new TransactionCallback<WSSecured>() {
            public WSSecured doInTransaction(TransactionStatus status) {
                return WSSecurityMethodMapper.getMappedSecuredWS(method, args);
            }
        });
    }

    /**
     * Attempt to map the given object as an instance of WSSecured so that it can be validated.
     *
     * @see com.sapienter.jbilling.server.security.WSSecurityEntityMapper
     *
     * @param o object to map
     * @return mapped object, or null if object is of an unknown type
     */
    protected WSSecured getMappedSecuredWS(final Object o) {
        LOG.debug("Non WSSecured object " + o.getClass().getSimpleName()
                  + ", attempting to map a secure class for validation.");

        return getTransactionTemplate().execute(new TransactionCallback<WSSecured>() {
            public WSSecured doInTransaction(TransactionStatus status) {
                return WSSecurityEntityMapper.getMappedSecuredWS(o);
            }
        });
    }

    /**
     * Attempt to validate the given object.
     *
     * @param o object to validate
     * @throws SecurityException thrown if user is accessing data that does not belonging to them
     */
    protected void validate(Object o) throws SecurityException {
        if (o != null) {
            final WSSecured secured = (o instanceof WSSecured)
                                      ? (WSSecured) o
                                      : getMappedSecuredWS(o);

            if (secured != null) {
                LOG.debug("Validating secure object " + secured.getClass().getSimpleName());

                getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        if (secured.getOwningEntityId() != null)
                            validateEntity(secured.getOwningEntityId());

                        if (secured.getOwningUserId() != null)
                            validateUser(secured.getOwningUserId());
                    }
                });
            }
        }
    }

    /**
     * Validates that the given owningUserId resides under the same entity as authenticated
     * user account making the web-service call.
     *
     * @param owningUserId user id owning the data being accessed
     * @throws SecurityException thrown if user is accessing data that does not belonging to them
     */
    protected void validateUser(Integer owningUserId) throws SecurityException {
        // validate only when the owning user ID has been persisted (not a transient user)
        UserDAS userDas = new UserDAS();
        if (userDas.isIdPersisted(owningUserId)) {

            UserDTO user = userDas.find(owningUserId);
            if (user != null && user.getCompany() != null) {
                // extract company and validate entity id against the caller
                validateEntity(user.getCompany().getId());

            } else {
                // impossible, a persisted user must belong to a company
                throw new SecurityException("User " + owningUserId + " does not belong to an entity.");
            }

        } else {
            LOG.warn("Data accessed via web-service call belongs to a transient user.");
        }
    }

    /**
     * Validates that the given owningEntityId matches the entity of the user account making
     * the web-service call.
     *
     * @param owningEntityId entity id owning the data being accessed
     * @throws SecurityException thrown if user is accessing data that does not belong to them
     */
    protected void validateEntity(Integer owningEntityId) throws SecurityException {
        if (!getCallerCompanyId().equals(owningEntityId))
            throw new SecurityException("Unauthorized access to entity " + owningEntityId
                    + " by caller '" + getCallerUserName() + "' (id " + getCallerCompanyId() + ")");
    }
}
