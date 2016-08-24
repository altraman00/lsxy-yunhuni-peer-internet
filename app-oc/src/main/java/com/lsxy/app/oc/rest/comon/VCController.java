package com.lsxy.app.oc.rest.comon;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Tandy on 2016/6/7.
 * 验证码控制器
 */
@RequestMapping("/vc")
@Controller
public class VCController {
    /**
     * 获取随机验证码
     * @return
     */
    @RequestMapping(value = "/code",method= RequestMethod.GET)
    public void getValidateCode(@RequestParam String code,HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg"); //必须设置ContentType为image/jpeg
        response.setHeader("Pragma","No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires",0);
        //设置图片的长宽  验证码长度
        int width=code.length() * 20, height=36,len=code.length();
        //创建内存图像
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //获取图形上下文
        Graphics g = image.getGraphics();
        //创建随机类的实例
        Random random = new Random();
        //设定图像背景色(因为是做背景，所以偏淡)
        g.setColor(getRandColor(random,200,250));
        g.fillRect(0, 0, width, height);
        //备选字体
        String[] fontTypes = {"tahoma","Atlantic Inline","fantasy","Times New Roman","Georgia","Arial", "Helvetica", "sans-serif","System"};
        int fontTypesLength = fontTypes.length;
        //在图片背景上增加噪点
        g.setColor(getRandColor(random,160,200));
        g.setFont(new Font("Times New Roman",Font.PLAIN,12));
        for (int i=0;i<len+2;i++) {
            g.drawString("!@#$%^,.;'[javawind.net]/<&*()>:5277",0,5*(i+2));
        }
        for (int i=0;i<len;i++)
        {
            //设置字体的颜色
            g.setColor(getRandColor(random,10,150));
            //设置字体
            g.setFont(new Font(fontTypes[random.nextInt(fontTypesLength)], Font.BOLD,20));
            //将随机验证码画到图片上
            g.drawString(code.substring(i,i+1),5 + 15*i,18 );
        }
        g.dispose();
        //输出图象到页面
        ImageIO.write(image,"JPEG",response.getOutputStream());
    }

    protected Color getRandColor(Random random,int fc,int bc){
        if(fc>255) fc=255;
        if(bc>255) bc=255;
        int r=fc+random.nextInt(bc-fc);
        int g=fc+random.nextInt(bc-fc);
        int b=fc+random.nextInt(bc-fc);
        return new Color(r,g,b);
    }


}
