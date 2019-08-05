package org.sfs.protocol.cryption;

import java.util.Random;

public class RandomSecretCryption{
	/**
	 * 获取随机密钥
	 * @return
	 */
	public static byte[] getRandomSecret(){
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < 32; i++) {
			int type = random.nextBoolean() ? 0:1;
			switch(type){
			case 0:
				int keyInt = random.nextBoolean()?65:97;
				sb.append((char)(keyInt + random.nextInt(26)));
				break;
			case 1:
				sb.append(String.valueOf(random.nextInt(10)));
				break;
			}
		}
		return sb.toString().getBytes();
	}
}
