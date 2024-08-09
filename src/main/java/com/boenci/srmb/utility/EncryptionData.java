
package com.boenci.srmb.utility;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class EncryptionData {
	
	public EncryptionData() {
	}
	
	public String getEncryptedData(String message) {
		String strEncrypted = "";

		try {
			System.out.println("clear message: " + message);
			strEncrypted = doEncrypt(message);
			System.out.println("encrypted message: " + strEncrypted);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strEncrypted;
	}

	private String doEncrypt(String message) throws IllegalBlockSizeException,
				BadPaddingException, NoSuchAlgorithmException,
				NoSuchPaddingException, InvalidKeyException,
				UnsupportedEncodingException {
		
		  Cipher cipher = Cipher.getInstance(AppConstants.CIPHER_TEXT);
	      SecretKeySpec secretKey = new SecretKeySpec(AppConstants.PRIVATE_KEY, AppConstants.KEY_GENERATOR);
	      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	      String encryptedString = "";// =new String( Base64.encode(cipher.doFinal(message.getBytes())));
	      return encryptedString;
	}
}
