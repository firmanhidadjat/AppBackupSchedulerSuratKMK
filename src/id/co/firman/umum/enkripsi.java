/*
 *	@Author Firman Hidayat
 */

package id.co.firman.umum;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class enkripsi {

	public enkripsi() {
		PropertyConfigurator.configure("log4j.properties");
	}

	public String encrypt(String Data) {
		try {
			Key key = generateKey();
			Cipher c = Cipher.getInstance("AES");
			c.init(1, key);
			byte encVal[] = c.doFinal(Data.getBytes());
			String encryptedValue = (new BASE64Encoder()).encode(encVal);
			return encryptedValue;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public String decrypt(String encryptedData) {
		try {
			Key key = generateKey();
			Cipher c = Cipher.getInstance("AES");
			c.init(2, key);
			byte decordedValue[] = (new BASE64Decoder())
					.decodeBuffer(encryptedData);
			byte decValue[] = c.doFinal(decordedValue);
			String decryptedValue = new String(decValue);
			return decryptedValue;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	private static Key generateKey() {
		try {
			Key key = new SecretKeySpec(keyValue, "AES");
			return key;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	static Logger log = Logger.getRootLogger();
	private static final String ALGO = "AES";
	private static final byte keyValue[] = { 77, 111, 100, 111, 76, 84, 97,
			104, 65, 110, 74, 105, 110, 71, 103, 111 };

}