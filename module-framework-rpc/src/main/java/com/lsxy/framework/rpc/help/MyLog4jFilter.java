package com.lsxy.framework.rpc.help;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.AbstractIoBuffer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

public class MyLog4jFilter extends IoFilterAdapter {
	
	private Log logger = LogFactory.getLog(MyLog4jFilter.class);
	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) throws Exception {
		if(message instanceof AbstractIoBuffer){
			AbstractIoBuffer aib = (AbstractIoBuffer) message;
			logger.debug("RECIVED:["+aib.getHexDump()+"]");
		}
		super.messageReceived(nextFilter, session, message);
	}

	@Override
	public void messageSent(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception {
		Object message = writeRequest.getMessage();
		if(message instanceof AbstractIoBuffer){
			AbstractIoBuffer aib = (AbstractIoBuffer) message;
			logger.debug("SEND:"+aib.getHexDump());
		}
		super.messageSent(nextFilter, session, writeRequest);
	}
	
	
	
}