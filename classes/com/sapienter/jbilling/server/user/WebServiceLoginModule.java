/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.sapienter.jbilling.server.user;

import org.apache.log4j.Logger;
import org.jboss.security.auth.spi.DatabaseServerLoginModule;

import com.sapienter.jbilling.common.JBCrypto;

public class WebServiceLoginModule extends DatabaseServerLoginModule {
	private static Logger LOG = Logger.getLogger(WebServiceLoginModule.class);

	@Override
	protected boolean validatePassword(String inputPassword,
			String expectedPassword) {

		inputPassword = JBCrypto.getPasswordCrypto(null).encrypt(inputPassword);		

        LOG.debug("ws: validating " + inputPassword + "=" + expectedPassword);
		return super.validatePassword(inputPassword, expectedPassword);
	}

}
