package com.lsxy.framework.core.utils;

/**
 * PUBLIC ID生成
 * @author WangYun
 *
 */
public class PublicIDGenerator {

	private final static String str = "1234567890abcdefghijklmnopqrstuvwxyz";
	private final static int pixLen = str.length();
	private static volatile int pix = 0;
	
	/**
	 * 生成段时间不会重复，长度为prefix+12位的字符串，主要用于识别微博、渠道等
	 * 生成策略为获取自1970年1月1日零时零分零秒至当前时间的毫秒数的16禁止字符串值，该字符串值为11为
	 * 并追加一位“0-Z”的自增字符串
	 * @param prefix
	 * @return
	 */
	final public static synchronized String generator(String prefix) {
		StringBuilder sb = new StringBuilder();
		sb.append(prefix);
		sb.append(Long.toHexString(System.currentTimeMillis()));
		pix++;
		if (pix == pixLen) {
			pix = 0;
		}
		return sb.append(str.charAt(pix)).toString();
	}
}
