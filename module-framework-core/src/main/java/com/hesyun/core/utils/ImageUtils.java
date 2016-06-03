package com.hesyun.core.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;


/**
 * 图片操作工具类
 * 
 * @author WangYun
 * 
 */
public class ImageUtils {

	/**
	 * 将Base64保存为图片
	 */
	public static byte[] generateImageFromBase64(String base64Str) {
		byte[] b = null;
			// Base64解码
			b = Base64.decodeBase64(base64Str);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
		return b;
	}

	/**
	 * 将Base64保存为图片
	 */
	public static boolean saveImageFromBase64(String base64Str, String savePath) {
		if (base64Str == null) // 图像数据为空
			return false;
		OutputStream out = null;
		try {
			// Base64解码
			byte[] b =Base64.decodeBase64(base64Str);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			out = new FileOutputStream(savePath);
			out.write(b);
			out.flush();
		} catch (Exception e) {
			return false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 获取图片的Base64位编码
	 * 
	 * @param data 图片的byte字节
	 */
	public static String getBase64FromImage(byte[] data) {
		String retBase64 = null;
		try {
			retBase64 = Base64.encodeBase64String(data);
		} catch (Exception e) {
			return null;
		} finally {
		}

		return retBase64;
	}

	/**
	 * 获取图片的Base64位编码
	 * 
	 * @param in 图片的inputstream
	 */
	public static String getBase64FromImage(InputStream in) {
		String retBase64 = null;
		try {
			byte[] data = new byte[in.available()];
			in.read(data);
			getBase64FromImage(data);
		} catch (Exception e) {
			return null;
		}

		return retBase64;
	}

	/**
	 * 获取图片的Base64位编码
	 * 
	 * @param imagePath 图片的绝对地址
	 */
	public static String getBase64FromImage(String imagePath) {
		if (imagePath == null) {
			return null;
		}

		InputStream in = null;
		String retBase64 = null;
		try {
			File imgFile = new File(imagePath);
			if (!imgFile.exists()) {
				return null;
			}

			in = new FileInputStream(imgFile);

			getBase64FromImage(in);
		} catch (Exception e) {
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					return null;
				}
			}
		}

		return retBase64;
	}
	
	/**
	 * 改变图片尺寸
	 */
	public static byte[] resizeImage(InputStream is, String imgSuffix, int newWidth, int newHeight) throws IOException {
		ByteArrayOutputStream out = null;
		try {
			BufferedImage source = ImageIO.read(is);
			int type = source.getType();
			
			BufferedImage bufferedImage = new BufferedImage(newWidth, newHeight, type) ;
			Graphics2D g2d = bufferedImage.createGraphics();
			// 画图
			g2d.setColor(new Color(255,0,0));
			g2d.setStroke(new BasicStroke(1));
			g2d.drawImage(source, 0, 0, newWidth, newHeight,null) ;
			
			out = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, imgSuffix, out);
		} catch (IOException e) {
			throw e;
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return out.toByteArray();
	}
	
}
