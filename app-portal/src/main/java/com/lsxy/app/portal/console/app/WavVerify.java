package com.lsxy.app.portal.console.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;

/**
 * Created by zhangxb on 2017/3/15.
 */
public class WavVerify {
    private static final Logger logger = LoggerFactory.getLogger(WavVerify.class);
    private static float frameRate = 8000.0f;
    private static int frameSize = 1;
    private static float sampleRate = 8000.0f;
    private static int sampleSizeInBits = 8;
    private static int channels = 1;
    private static String encoding = "ALAW";
    public static boolean verify (File file){
        try {
            //String file = "f:yunhuni-docs/产品/呼叫中心/放音文件/1/queue120s.wav";
//
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream( file );
            AudioFormat audioFormat = audioInputStream.getFormat();
//            System.out.println(audioFormat.getFrameRate());//8000.0
//            System.out.println(audioFormat.getFrameSize());//1
//            System.out.println(audioFormat.getSampleRate());//8000.0
//            System.out.println(audioFormat.getSampleSizeInBits());//8
//            System.out.println(audioFormat.getChannels());//1
//            System.out.println(audioFormat.getEncoding());//ALAW
            if(frameRate == audioFormat.getFrameRate() &&
//                    frameSize == audioFormat.getFrameSize() &&
//                    sampleRate == audioFormat.getSampleRate() &&
                    sampleSizeInBits == audioFormat.getSampleSizeInBits() &&
                    channels == audioFormat.getChannels() &&
                    encoding.equals(audioFormat.getEncoding().toString())
                    ){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            logger.error("文件校验异常",e);
            return false;
        }
    }
}
