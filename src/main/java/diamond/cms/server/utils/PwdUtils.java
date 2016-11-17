package diamond.cms.server.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PwdUtils {

	public static String pwd(String pwd) {
		return md5(md5(md5(pwd)+pwd)+pwd);
	}
	
	public static String md5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
