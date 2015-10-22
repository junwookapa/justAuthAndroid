package team.justtag.security;

import com.auth0.jwt.internal.org.apache.commons.codec.DecoderException;
import com.auth0.jwt.internal.org.apache.commons.codec.binary.Hex;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AESSecurity {

	public String encoding(String str, String tokenString) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES");
			Key key = new SecretKeySpec(tokenString.getBytes(), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			
			byte[] encryptedData;
			encryptedData = cipher.doFinal(str.getBytes());
			return Hex.encodeHexString(encryptedData);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		}
	}

	public String decoding(String str, String tokenString) {
		try {
			Cipher cipherx = Cipher.getInstance("AES");
			Key key = new SecretKeySpec(tokenString.getBytes(), "AES");
			cipherx.init(Cipher.DECRYPT_MODE, key);
			byte[] plainText = cipherx
					.doFinal(Hex.decodeHex(str.toCharArray()));
			return new String(plainText);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		} catch (DecoderException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		}
	}

}
