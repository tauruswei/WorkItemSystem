package stellartest;

import org.apache.commons.codec.binary.Base64;
import org.stellar.sdk.KeyPair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


public class StellarAuthTest {

	public static void main(String[] args) {
		// 用来签名的数据
		String content = "{\"userName\":\"GAMRWVBNZXVJ6T55GICMTKMLEQOWAS33ZXKYBDLGODA7HDP2ZUBCBSA6\",\"time\":1530607296381}";

		JSONObject map = new JSONObject();
		map.put("userName", "GD4AJ5SJOHXVB35LQDZRZXUOC3RSLTYCWASOIOXCR44MQC2JZCUXANDL");
		map.put("time",1535357168000L);
		content = JSON.toJSONString(map);
		System.out.println(content);
		
		
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.
		
		String auth = "nZjrqRuN3I8OXVrP6R6QslDTG72rBGwubNtJa52MxAHAglIiwJYIjGYfUibHQLuBXjx98SyA2qgoqJejG52gBg==";
		
		KeyPair keyPair = KeyPair.fromAccountId("GD4AJ5SJOHXVB35LQDZRZXUOC3RSLTYCWASOIOXCR44MQC2JZCUXANDL");
		
		
		byte[] bytes = KeyPair.fromSecretSeed("SCO2OEELJZFTG43ML4L5DLKI2H552NM4UONQ4U66SRHBKIUL6ACON4FF")
			.sign(content.getBytes());
		
		String oauth =  Base64.encodeBase64String(bytes);
		System.out.println(oauth);
		
		boolean result = keyPair.verify(content.getBytes(), Base64.decodeBase64(auth));
		
		System.out.println(result);
		
	}
}
