package com.boenci.srmb.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/test")
@CrossOrigin(origins = "*")
public class EncryptDecryptController {

	private static final String secretKey = "0123456789abcdef";
	private static final String initializationVector = "fedcba9876543210";

	@PostMapping("/decrypt")
	public static String decryptBody(@RequestBody String data) throws Exception {

		byte[] key = secretKey.getBytes(StandardCharsets.UTF_8);
		byte[] iv = initializationVector.getBytes(StandardCharsets.UTF_8);
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
		byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(data));
		String decrypted = new String(decryptedBytes, StandardCharsets.UTF_8);
		//System.out.println("-------decrypted---------"+decrypted);

		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
		byte[] encryptedBytes = cipher.doFinal(decrypted.getBytes(StandardCharsets.UTF_8));
		String encrypted = Base64.getEncoder().encodeToString(encryptedBytes);
		//System.out.println("-------encrypted---------"+encrypted);
		//String test = "{\"email\":\"accounting@gmail.com\"}";
        return "{\"response\":\""+encrypted+"\"}";
       	//return test;
    }
	@PostMapping("/encrypt")
	public static String encryptBody(@RequestBody String data) throws Exception {

		byte[] key = secretKey.getBytes(StandardCharsets.UTF_8);
		byte[] iv = initializationVector.getBytes(StandardCharsets.UTF_8);
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
		byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
		String encrypted = Base64.getEncoder().encodeToString(encryptedBytes);
		System.out.println(encrypted);
        return encrypted;
    }
}
