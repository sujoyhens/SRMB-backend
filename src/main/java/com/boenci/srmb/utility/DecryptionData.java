
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


public class DecryptionData {

	public DecryptionData() {
	}
	
	private String doDecrypt(String encrypted) throws InvalidKeyException,
		NoSuchAlgorithmException, NoSuchPaddingException,
		IllegalBlockSizeException, BadPaddingException, IOException {
		
		  Cipher cipher = Cipher.getInstance(AppConstants.CIPHER_TEXT);
	      SecretKeySpec secretKey = new SecretKeySpec(AppConstants.PRIVATE_KEY, AppConstants.KEY_GENERATOR);
	      cipher.init(Cipher.DECRYPT_MODE, secretKey);
	      String decryptedString = "";//= new String(cipher.doFinal(Base64.decode(encrypted.getBytes())));
	      return decryptedString;
	}
		
	
	public String getDecryptedData(String message) {
		String strDecrypted = "";
		try {
			System.out.println("clear message: " + message);

			strDecrypted = doDecrypt(message);
			System.out.println("decrypted message: " + strDecrypted);

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
		return strDecrypted;
	}
}
