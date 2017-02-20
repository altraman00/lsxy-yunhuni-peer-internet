package com.lsxy.area.server.test;

import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.ConfService;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.area.server.AreaServerMainClass;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.config.Constants;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TestNumBindService;
import com.lsxy.yunhuni.api.session.model.Meeting;
import com.lsxy.yunhuni.api.session.model.VoiceIvr;
import com.lsxy.yunhuni.api.session.service.MeetingService;
import com.lsxy.yunhuni.api.session.service.VoiceIvrService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import scala.actors.threadpool.Arrays;

import java.util.List;

/**
 * Created by Liuws on 2016/7/14.
 * 会议测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(AreaServerMainClass.class)
public class ConfTest {

    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }
    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private AppService appService;

    @Autowired
    private ConfService confService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private TestNumBindService testNumBindService;

    @Autowired
    private ResourcesRentService resourcesRentService;

    @Autowired
    private IVRActionService ivrActionService;

    @Autowired
    private LineGatewayService lineGatewayService;

    @Autowired
    private VoiceIvrService voiceIvrService;

    @Autowired
    private MeetingService meetingService;

    private String getMeeting(){
        List<Meeting> meetings = (List<Meeting>)meetingService.list("from Meeting order by createTime desc");
        return meetings.get(0).getId();
    }

    @Test
    public void create() throws YunhuniApiException {
        confService.create(null,"192.168.0.1","40288aca574060400157406427f20005",45,30,true,true,"3.wav",null);
    }

    @Test
    public void dismiss() throws YunhuniApiException {
        confService.dismiss(null,"192.168.0.1","40288aca574060400157406427f20005",getMeeting());
    }

    @Test
    public void invite() throws YunhuniApiException {
        confService.invite(null,"192.168.0.1","40288aca574060400157406427f20005",getMeeting()
                ,"8675522730043","13692206627",4500,45,0,"3.wav",1);
    }

    @Test
    public void join() throws YunhuniApiException {
        List<VoiceIvr> lists = (List<VoiceIvr>)voiceIvrService.list("from VoiceIvr order by createTime desc");
        confService.join(null,"192.168.0.1","40288aca574060400157406427f20005",getMeeting(),lists.get(0).getId()
               ,4500,"3.wav",1);
    }

    @Test
    public void quit() throws YunhuniApiException {
        List<VoiceIvr> lists = (List<VoiceIvr>)voiceIvrService.list("from VoiceIvr order by createTime desc");
        confService.quit(null,"192.168.0.1","40288aca574060400157406427f20005",getMeeting(),lists.get(0).getId());
    }

    @Test
    public void play() throws YunhuniApiException {
        confService.startPlay(null,"192.168.0.1","40288aca574060400157406427f20005",getMeeting(), Arrays.asList(new String[]{"3.wav"}));
    }

    @Test
    public void stoPlay() throws YunhuniApiException {
        confService.stopPlay(null,"192.168.0.1","40288aca574060400157406427f20005",getMeeting());
    }

    @Test
    public void record() throws YunhuniApiException {
        confService.startRecord(null,"192.168.0.1","40288aca574060400157406427f20005",getMeeting(),600);
    }

    @Test
    public void stopRecord() throws YunhuniApiException {
        confService.stopRecord(null,"192.168.0.1","40288aca574060400157406427f20005",getMeeting());
    }

    @Test
    public void setVoiceMode() throws YunhuniApiException {
        List<VoiceIvr> lists = (List<VoiceIvr>)voiceIvrService.list("from VoiceIvr order by createTime desc");
        confService.setVoiceMode(null,"192.168.0.1","40288aca574060400157406427f20005",getMeeting(),lists.get(0).getId(),2);
    }
}
