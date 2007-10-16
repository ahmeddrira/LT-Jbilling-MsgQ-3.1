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
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.jboss.security.Base64Utils;

import com.sapienter.jbilling.server.util.Util;

public final class JBCryptoImpl extends JBCrypto {
	private static final Charset UTF8 = Charset.forName("UTF-8");
	private static final String ALGORITHM = "PBEWithMD5AndDES";
	private static SecretKeyFactory ourKeyFactory;
	private static Cipher ourCipher;
	private static final PBEParameterSpec ourPBEParameters;
    
    //private static final Logger LOG = Logger.getLogger(JBCryptoImpl.class);

	private final SecretKey mySecretKey;

	public JBCryptoImpl(String password) throws InvalidKeySpecException {
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray());
		SecretKeyFactory keyFactory = getSecretKeyFactory();
		mySecretKey = keyFactory.generateSecret(spec);
	}

	public String decrypt(String cryptedText) {
		Cipher cipher = getCipher();
		byte[] crypted = useHexForBinary ? Util.stringToBinary(cryptedText) :
            Base64Utils.fromb64(cryptedText);
		byte[] result;
		try {
			cipher.init(Cipher.DECRYPT_MODE, mySecretKey, ourPBEParameters);
			result = cipher.doFinal(crypted);
		} catch (GeneralSecurityException e) {
			throw new IllegalArgumentException("Can not decrypt:" + cryptedText, e);
		}
		return new String(result, UTF8);
	}

	public String encrypt(String text) {
		Cipher cipher = getCipher();
		byte[] crypted;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, mySecretKey, ourPBEParameters);
			byte[] bytes = text.getBytes(UTF8);
			crypted = cipher.doFinal(bytes);
		} catch (GeneralSecurityException e) {
			throw new IllegalArgumentException("Can not encrypt :" + text, e);
		}
        String cryptedText = useHexForBinary ? Util.binaryToString(crypted) :
            Base64Utils.tob64(crypted);
		return cryptedText;
	}
	
	private static SecretKeyFactory getSecretKeyFactory() {
		if (ourKeyFactory == null) {
			try {
				ourKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);
			} catch (NoSuchAlgorithmException e) {
				throw new IllegalStateException("Algorithm is not supported: "
						+ ALGORITHM, e);
			}
		}
		return ourKeyFactory;
	}

	private static Cipher getCipher() {
		if (ourCipher == null) {
			try {
				ourCipher = Cipher.getInstance(ALGORITHM);

			} catch (NoSuchAlgorithmException e) {
				throw new IllegalStateException(
						"Strange. Algorithm was supported a few seconds ago : "
								+ ALGORITHM, e);
			} catch (NoSuchPaddingException e) {
				throw new IllegalStateException(e);
			}
		}
		return ourCipher;
	}
	
	static {
		// DON'T CHANGE THIS
		// IT WOULD BREAK BACKWARD COMPATIBILITY
		ourPBEParameters = new PBEParameterSpec(new byte[] { //
				(byte) 0x3c, (byte) 0x15, // 
						(byte) 0x27, (byte) 0x7f, //
						(byte) 0x2d, (byte) 0xda, //
						(byte) 0xe6, (byte) 0x64, //
				}, 15);
	}
	
	static {
		
	}
}
