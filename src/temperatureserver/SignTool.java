package temperatureserver;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author haowen
 *
 */
public class SignTool {

	public static int CODE_NO_USERNAME = -1;// 无用户名

	public static int CODE_NO_KEY = -2;// 用户未分配KEY

	public static int CODE_TIME_EXPIRED_OR_INVALID = -3;// 超时或者时间无效

	public static int CODE_NO_SIGN = -4;// 未经签名

	public static int CODE_SIGN_INVALID = -5;// 签名无效

	public final static Long EXPIRE_TIME = 60 * 1000L;// 1分钟视作超时

	private final static Map<String, String> apiKeys = new ConcurrentHashMap<String, String>();

	private final static String SALT = "SALTGZSEEING";// when used,DO NOT
														// modify;

	/**
	 * 重新加载apiKey
	 * 
	 * @ThreadSafe
	 * @param map
	 */
	public static void loadApiKeys(Map<String, String> map) {
		if (map != null && !map.isEmpty()) {
			apiKeys.clear();
			apiKeys.putAll(map);
		}
	}

	/**
	 * 添加apiKey
	 * 
	 * @ThreadSafe
	 * @param username
	 * @param apiKey
	 */
	public static void addApiKeys(String username, String apiKey) {
		apiKeys.put(username, apiKey);
	}

	/**
	 * 删除apiKey
	 * 
	 * @ThreadSafe
	 * @param username
	 */
	public static void removeApiKeys(String username) {
		apiKeys.remove(username);
	}

	public static boolean validateSign(Map<String, Object> data) throws SignException {
		if (data == null || data.get("sign") == null || "".equals(data.get("sign"))) {
			throw new SignException(CODE_NO_SIGN);
		}
		Long time = null;
		try {
			time = (Long) data.get("time");
		} catch (Exception e) {
		}
		if (time == null || (System.currentTimeMillis() - time) > EXPIRE_TIME) {
			throw new SignException(CODE_TIME_EXPIRED_OR_INVALID);
		}
		String remoteSign = (String) data.get("sign");
		Map<String, Object> validateMap = new HashMap<>(data);
		validateMap.remove("sign");
		String sign = generateSign(validateMap, getApiKey((String) validateMap.get("username")));
		if (!sign.equals(remoteSign)) {
			throw new SignException(CODE_SIGN_INVALID);
		}
		return true;
	}

	public static void putSign(Map<String, Object> data, String username) {
		if (data == null) {
			data = new HashMap<>();
		}
		data.put("sign", getSign(data, username));
	}

	public static String getSign(Map<String, Object> data, String username) {
		if (data == null) {
			data = new HashMap<>();
		}
		data.put("username", username);
		data.put("time", System.currentTimeMillis());
		return generateSign(data, getApiKey(username));

	}

	private static String generateSign(Map<String, Object> map, String apiKey) {
		Map<String, Object> orderMap = new TreeMap<>(map);
		Iterator<Entry<String, Object>> iterator = orderMap.entrySet().iterator();
		StringBuffer sb = new StringBuffer();
		while (iterator.hasNext()) {
			Entry<String, Object> it = iterator.next();
			String key = it.getKey();
			Object value = it.getValue();
			sb.append(key).append("=").append(toString(value)).append("&");

		}
		sb.append("key=").append(apiKey);
		return getMD5String(sb.toString(), SALT);
	}

	private static String toString(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	/**
	 * 把字符串用MD5 salt加密
	 * 
	 * @Description
	 * @param str
	 * @param salt
	 *            字符串。
	 * @return
	 */
	private static String getMD5String(String str, String salt) {
		StringBuilder result = new StringBuilder();
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			StringBuffer code = new StringBuffer(str);

			if (salt != null && !"".equals(salt)) {
				code.append(salt);
			}

			byte[] buff = digest.digest(code.toString().getBytes());
			for (int i = 0; i < buff.length; i++) {
				int num = buff[i] & 0xFF;
				String hexString = Integer.toHexString(num);

				if (hexString.length() == 1) {
					result.append("0").append(hexString);
				} else {
					result.append(hexString);
				}
			}
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getApiKey(String username) {
		if (username == null) {
			throw new SignException(CODE_NO_USERNAME);
		}
		String keys = apiKeys.get(username);
		if (keys == null) {
			throw new SignException(CODE_NO_KEY);
		}
		return keys;
	}

	public static class SignException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8484984777086561676L;
		public int code;

		public SignException(int code) {
			super();
			this.code = code;
		}

	}
}
