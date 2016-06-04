package com.lsxy.framework.core.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class GetMacAddress {
	public static String getMACAddress(String ip) {
		String str = "";
		String macAddress = "";
		try {
			Process p = Runtime.getRuntime().exec("nmblookup -A " + ip);
			InputStreamReader ir = new InputStreamReader(p.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			for (int i = 1; i < 100; i++) {
				str = input.readLine();
				if (str != null) {
					if (str.indexOf("MAC Address") > 0) {
						macAddress = str.substring(str.indexOf("MAC Address") + 14, str.length());
						break;
					}
				}
			}
		} catch (IOException e) {
			return "";
		}
		return macAddress;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String xx = GetMacAddress.getMACAddress("139.189.130.103");
		System.out.println(xx);
	}
}