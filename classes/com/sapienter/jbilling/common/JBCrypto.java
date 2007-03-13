/*
The contents of this file are subject to the Jbilling Public License
Version 1.1 (the "License"); you may not use this file except in
compliance with the License. You may obtain a copy of the License at
http://www.jbilling.com/JPL/

Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.

The Original Code is jbilling.

The Initial Developer of the Original Code is Emiliano Conde.
Portions created by Sapienter Billing Software Corp. are Copyright 
(C) Sapienter Billing Software Corp. All Rights Reserved.

Contributor(s): ______________________________________.
*/
package com.sapienter.jbilling.common;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.apache.log4j.Logger;
import org.jboss.security.Base64Utils;

public abstract class JBCrypto {
	protected static final Charset UTF8 = Charset.forName("UTF-8");
	public static final String PROP_SHOULD_CRYPT_CREDIT_CARDS = "credit_card_secure";
	public static final String PROP_CREDIT_CARDS_CRYPTO_PASSWORD = "credit_card_password";
	public static final String PROP_DIGEST_ALL_PASSWORDS = "password_encrypt_all";
	public static final int MIN_UNDIGESTED_ROLE = CommonConstants.TYPE_PARTNER;
	
	private static JBCrypto ourCreditCardCrypto;
	
	public abstract String encrypt(String text);
	public abstract String decrypt(String crypted);
	
	public static JBCrypto getCreditCardCrypto(){
		if (ourCreditCardCrypto == null){
			ourCreditCardCrypto = loadCreditCardCrypto();
		}
		return ourCreditCardCrypto;
	}
	
	public static JBCrypto getPasswordCrypto(Integer role){
		boolean digestAll = Boolean.parseBoolean(
				Util.getSysProp(PROP_DIGEST_ALL_PASSWORDS));
		
		return (digestAll || role == null || role < MIN_UNDIGESTED_ROLE) ? 
				ONE_WAY : DUMMY;
	}
	
	public static String digest(String input){
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(
					"MD5 digest is expected to be available :" + e);
		}

		byte[] hash = md5.digest(input.getBytes(UTF8));
		return Base64Utils.tob64(hash);
	}
	
	private static JBCrypto loadCreditCardCrypto(){
		String property = Util.getSysProp(PROP_SHOULD_CRYPT_CREDIT_CARDS);
		String password = Util.getSysProp(PROP_CREDIT_CARDS_CRYPTO_PASSWORD);
		boolean shouldCrypt = Boolean.parseBoolean(property) && password != null && password.length() != 0;
		
		JBCrypto result = DUMMY;
		if (shouldCrypt){
			try {
				result = new JBCryptoImpl(password);
			} catch (InvalidKeySpecException e) {
				Logger.getLogger(JBCrypto.class).error("Can not use suggested credit card password. Encryption won't be used", e);
			}
		}
		
		return result; 
	}
	
	public static JBCrypto DUMMY = new JBCrypto(){
	
		public String encrypt(String text) {
			return text;
		}
	
		public String decrypt(String crypted) {
			return crypted;
		}
	};
	
	private static JBCrypto ONE_WAY = new JBCrypto(){
		public String encrypt(String text) {
			return digest(text);
		}
		
		public String decrypt(String crypted) {
			throw new UnsupportedOperationException("I am one way digets only");
		}
	};	

}
