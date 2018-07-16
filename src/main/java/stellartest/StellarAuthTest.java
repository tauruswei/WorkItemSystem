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
		map.put("userName", "GAGFN2YXVL4ARLSRBSITM3OPQXD3TXMBFEU4YHKMZIIYVXCWMLN4UI2V");
		map.put("time", 1530607296381L);
		content = JSON.toJSONString(map);
		System.out.println(content);
		
		
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.
		
		String auth = "V5BkcawFMyafO6nyJLxp4RIkQEDpfs/SpEA6WyX7aOBVJdTbx0GnWOxHpKxo9VCOLWERC9ANQYQnBDVscnh6Aw==";
		
		KeyPair keyPair = KeyPair.fromAccountId("GAGFN2YXVL4ARLSRBSITM3OPQXD3TXMBFEU4YHKMZIIYVXCWMLN4UI2V");
		
		
		byte[] bytes = KeyPair.fromSecretSeed("SA2WSSEQKBOU3CLGGDOYFVLFYXOLIMXEEIK5MLDBCPJGVV4GVOVH2QKB")
			.sign(content.getBytes());
		
		String oauth =  Base64.encodeBase64String(bytes);
		System.out.println(oauth);
		
		boolean result = keyPair.verify(content.getBytes(), Base64.decodeBase64(auth));
		
		System.out.println(result);
		
	}
}
