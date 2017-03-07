package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/11/14.
 */
public class AgentNotConversationMemberException extends YunhuniApiException {
    public AgentNotConversationMemberException(Throwable t) {
        super(t);
    }

    public AgentNotConversationMemberException() {
        super();
    }

    public AgentNotConversationMemberException(String context) {
        super(context);
    }

    public AgentNotConversationMemberException(ExceptionContext context){
        super(context);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.AgentNotConversationMember;
    }
}
