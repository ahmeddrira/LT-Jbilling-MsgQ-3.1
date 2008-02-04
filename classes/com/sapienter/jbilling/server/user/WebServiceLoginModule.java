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
