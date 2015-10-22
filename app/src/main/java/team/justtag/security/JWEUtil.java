package team.justtag.security;

import android.util.Log;

import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.keys.BigEndianBigInteger;
import org.jose4j.lang.JoseException;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.NoSuchPaddingException;

public class JWEUtil {
		
	public RsaJsonWebKey generateJsonWebKey() throws NoSuchAlgorithmException, JoseException{
		KeyPairGenerator clsKeyPairGenerator = KeyPairGenerator.getInstance("RSA");
	//	clsKeyPairGenerator.initialize(Config.RSA_LENGTH);
		KeyPair clsKeyPair = clsKeyPairGenerator.genKeyPair();
		RsaJsonWebKey jsonWebKey = new RsaJsonWebKey((RSAPublicKey) clsKeyPair.getPublic());
		jsonWebKey.setPrivateKey(clsKeyPair.getPrivate());
		return jsonWebKey;
	}
	
	@SuppressWarnings("unchecked")
	public String publicKeyConvertJsonString(PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException{
		KeyFactory fact = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec clsPublicKeySpec = fact.getKeySpec(publicKey, RSAPublicKeySpec.class);
		JSONObject json = new JSONObject();
		String e = BigEndianBigInteger.toBase64Url(clsPublicKeySpec.getPublicExponent());
		String n = BigEndianBigInteger.toBase64Url(clsPublicKeySpec.getModulus());
		json.put("n", n);
		json.put("e", e);
		return json.toJSONString();
	}
	
	public String encoder(PublicKey publicKey, String byteString) {
		RsaJsonWebKey jwk = new RsaJsonWebKey((RSAPublicKey) publicKey);
		JsonWebEncryption jwe = new JsonWebEncryption();
		jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.RSA_OAEP);
		jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_GCM);
		jwe.setKey(jwk.getPublicKey());
		System.out.println(byteString);
		jwe.setPayload(byteString);
		try {
			return jwe.getCompactSerialization();
		} catch (JoseException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public String encoding(JSONObject json){
		try {
			RSAPublicKeySpec publickKey = new RSAPublicKeySpec(BigEndianBigInteger.fromBase64Url(json.get("n").toString()), BigEndianBigInteger.fromBase64Url(json.get("e").toString()));
			JSONObject jsonObj = new JSONObject();
			KeyFactory fact = KeyFactory.getInstance("RSA");
			fact.generatePublic(publickKey);
			jsonObj.put("user_id", "admin");
			jsonObj.put("user_password", "admin");
			System.out.println();
			return new JWEUtil().encoder(fact.generatePublic(publickKey), jsonObj.toJSONString());
		}catch(NoSuchAlgorithmException e){
			return null;
		}catch(InvalidKeySpecException e){
			return null;
		}
	}


	public String decoder(PrivateKey privateKey, String byteString) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, JoseException{
		JsonWebEncryption jwe = new JsonWebEncryption();
		jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.RSA_OAEP);
		jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_GCM);
		jwe.setKey(privateKey);
		jwe.setCompactSerialization(byteString);
		return jwe.getPayload();
	}
	
}