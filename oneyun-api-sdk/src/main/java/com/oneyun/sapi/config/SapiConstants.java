package com.oneyun.sapi.config;

/**
 * Created by zhangxb on 2016/9/3.
 */
public class SapiConstants {

    /*参数常量*/
    //自定义回铃音播放模式 0：收到对端回铃后开始播放 1：拨号时即开始播放，收到对端回铃后停止播放 2：拨号时即开始播放，对端接听或者挂机后停止播放
    public static int[] ringToneModes = new int[]{0,1,2};
    //录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音
    public static int[] recordModes = new int[]{0,1,2};
    //重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复。
    public static int[] repeat = new int[]{0,1};
    /*
    0	文件播放。此时，放音内容应是文件名字符串。
    1	数字播放。此时，放音内容应是十进制整数。
    2	数值播放。此时，放音内容应是十进制整数或者浮点数。
    3	金额播放。此时，放音内容应是十进制整数或者浮点数。
    4	日期时间播放。格式是 "YYYY-mm-DD" 或者 "HH:MM:SS" 或者 "YYYY-mm-DD HH:MM:SS"
    5	时长播放。此时，放音内容是文件名字符串。
    6	金额播放（元角分）。此时，放音内容应是十进制整数或者浮点数。
     */
    public static int[] playContent = new int[]{0,1,2,3,4,5,6};

    /*以下是业务类型*/
    /*业务常量*/
    //双向回拨
    public static final String CALL_DUO_CALLBACK = "call/duo_callback";
    //双向回拨-挂断
    public static final String CALL_DUO_CALLBACK_STOP = "call/duo_callback_cancel";
    //外呼通知
    public static final String CALL_NOTIFY_CALL = "call/notify_call";
    //语音验证码
    public static final String CALL_VERIFY_CALL = "call/verify_call";

    /*语音会议API*/
    //创建会议 ${prefix}/account/{account_id}/conf/create
    public static final String CONF_CREATE = "conf/create";
    //解散会议 ${prefix}/account/{account_id}/conf/{conf_id}/dismiss
    public static final String CONF_DISMISS = "conf/dismiss/{conf_id}";
    //会议邀请呼叫 ${prefix}/account/{account_id}/conf/{conf_id}/invite_call
    public static final String CONF_INVITE_CALL = "conf/invite_call/{conf_id}";
    //将通话加入到会议 ${prefix}/account/{account_id}/conf/{conf_id}/join
    public static final String CONF_JOIN = "conf/join/{conf_id}";
    //从会议退出 ${prefix}/account/{account_id}/conf/{conf_id}/quit
    public static final String CONF_QUIT = "conf/quit/{conf_id}";

    /*会议放音*/
    //开始放音 ${prefix}/account/{account_id}/conf/{conf_id}/start_play
    public static final String CONF_START_PLAY = "conf/start_play/{conf_id}";
    //停止放音 ${prefix}/account/{account_id}/conf/{conf_id}/stop_play
    public static final String CONF_STOP_PLAY = "conf/stop_play/{conf_id}";

    /*会议录音*/
    //开始录音 ${prefix}/account/{account_id}/conf/{conf_id}/start_record
    public static final String CONF_START_RECORD = "conf/start_record/{conf_id}";
    //停止录音 ${prefix}/account/{account_id}/conf/{conf_id}/stop_record
    public static final String CONF_STOP_RECORD = "conf/stop_record/{conf_id}";

    //会议成员录放音模式 ${prefix}/account/{account_id}/conf/{conf_id}/set_voice_mode
    public static final String CONF_SET_VOICE_MODE = "conf/set_voice_mode/{conf_id}";


    /*事件类型*/

    /*回调类型*/
    //事件通知
    public static final String ACTION_EVENT_NOTIFY = "event_notify";

    //双向回拨事件 结束事件
    public static final String EVENT_NOTIFY_DUO_CALLBACK_END = "duo_callback.end";
    //外呼通知事件 结束事件
    public static final String EVENT_NOTIFY_NOTIFY_CALL_END = "notify_call.end";
    //语音验证码事件 结束事件
    public static final String EVENT_NOTIFY_CAPTCHA_CALL_END = "captcha_call.end";

    /*语音会议事件*/
    //会议结束事件 conf.end
    public static final String EVENT_NOTIFY_CONF_CREATE_SUCC = "conf.create.succ";
    //会议结束事件 conf.end
    public static final String EVENT_NOTIFY_CONF_END = "conf.end";
    //加入会议事件 conf.joined
    public static final String EVENT_NOTIFY_CONF_JOINED = "conf.joined";
    //离开会议事件 conf.quit
    public static final String EVENT_NOTIFY_CONF_QUIT = "conf.quit";
    //会议放音结束事件 conf.play_end
    public static final String EVENT_NOTIFY_CONF_PLAY_END = "conf.play_end";
    //会议录音结束事件 conf.record_end
    public static final String EVENT_NOTIFY_CONF_RECORD_END = "conf.record_end";

    //IVR呼出
    public static final String IVR_CALL = "call/ivr_call";
    /*IVR 事件*/
    //呼叫结束事件 ivr.call_end
    public static final String EVENT_NOTIFY_IVR_CALL_END = "ivr.call_end";
    //放音结束事件 ivr.play_end
    public static final String EVENT_NOTIFY_IVR_PLAY_END = "ivr.play_end";
    //录音结束事件 ivr.record_end
    public static final String EVENT_NOTIFY_IVR_RECORD_END = "ivr.record_end";
    //收码结束事件 ivr.get_end
    public static final String EVENT_NOTIFY_IVR_GET_END = "ivr.get_end";
    //发码结束事件 ivr.put_end
    public static final String EVENT_NOTIFY_IVR_PUT_END = "ivr.put_end";
    //拨号结束事件 ivr.dial_end
    public static final String EVENT_NOTIFY_IVR_DIAL_END = "ivr.dial_end";
    //连接建立事件 ivr.connect_begin
    public static final String EVENT_NOTIFY_IVR_CONNECT_BEGIN = "ivr.connect_begin";
    //连接结束事件 ivr.connect_end
    public static final String EVENT_NOTIFY_IVR_CONNECT_END = "ivr.connect_end";
    //会议事件集合
    public static final String[] EVENT_NOTIFY = new String[]{
            EVENT_NOTIFY_IVR_CALL_END,EVENT_NOTIFY_CONF_CREATE_SUCC,EVENT_NOTIFY_CONF_END,EVENT_NOTIFY_CONF_JOINED,EVENT_NOTIFY_CONF_QUIT,EVENT_NOTIFY_CONF_PLAY_END,EVENT_NOTIFY_CONF_RECORD_END
    };

    //分机 新建是post 删除是delete/id
    public static final String CALLCENTER_EXTENSION = "callcenter/extension";
    //坐席管理
    public static final String CALLCENTER_AGENT = "callcenter/agent";
    //坐席报到
    public static final String CALLCENTER_AGENT_KEEPALIVE = "callcenter/agent/{agent_name}/keepalive";
    //坐席-设置分机
    public static final String CALLCENTER_AGENT_EXTENSION = "callcenter/agent/{agent_name}/extension";
    //坐席-设置状态
    public static final String CALLCENTER_AGENT_STATE = "callcenter/agent/{agent_name}/state";
    //坐席-设置技能
    public static final String CALLCENTER_AGENT_SKILLS = "callcenter/agent/{agent_name}/skills";
    //通道
    public static final String CALLCENTER_CHANNEL = "callcenter/channel";
    //排队条件
    public static final String CALLCENTER_CONDITION = "callcenter/condition";
    //获取多条记录排队条件
    public static final String CALLCENTER_CHANNEL_CONDITION = "callcenter/channel/{channel_id}/condition";
    //坐席事件
    //坐席状态变化
    public static final String CALLCENTER_AGENT_STATE_CHANGED = "callcenter.agent.state_changed";
    //坐席进入或退出交谈
    public static final String CALLCENTER_AGENT_CONVERSATION_CHANGED = "callcenter.agent.conversation_changed";
    //分机状态
    public static final String EXTENSION_STATE = "/callcenter/extensionlisttest";
}
