package com.lsxy.framework.rpc.mina.server;

/**
 * 服务器端处理类
 * @author tantyue
 *
 */
//@Component
//@ConditionalOnProperty(value = "global.rpc.provider", havingValue = "mina", matchIfMissing = false)
//public class MinaServerHandler extends AbstractServerRPCHandler {
//
//
//
//    public IoHandlerAdapter getIoHandler() {
//        return ioHandler;
//    }
//
//
//    private static final Logger logger = LoggerFactory.getLogger(MinaServerHandler.class);
//
//    @Autowired
//    private AbstractServiceHandler serviceHandler;
//
//    @Autowired
//    private MinaRemoteServer remoteServer;
//
//    public MinaServerHandler() {
//    }
//
//
//
//
//    private IoHandlerAdapter ioHandler = new IoHandlerAdapter(){
//
//		@Override
//		public void messageSent(IoSession session, Object message) throws Exception {
//			if(message instanceof RPCRequest){
//
//			}
//			logger.debug("发送消息："+message);
//		}
//
//		@Override
//		public void sessionClosed(IoSession session) throws Exception {
//			logger.debug("SESSION关闭:"+session);
//			String sessionid = (String) session.getAttribute("sessionid");
//			ServerSessionContext sessionContext = remoteServer.getSessionContext();
//			if(StringUtil.isNotEmpty(sessionid)){
//				sessionContext.remove(sessionid);
//				InetSocketAddress addr = (InetSocketAddress) session.getRemoteAddress();
//				String ip = addr.getAddress().getHostAddress();
//				logger.debug("节点代理【"+ip+"】断开连接");
//			}
//
//
//		}
//		@Override
//		public void sessionCreated(IoSession session) throws Exception {
//			logger.debug("SESSION创建:"+session);
//		}
//
//		@Override
//		public void sessionIdle(IoSession session, IdleStatus status)
//				throws Exception {
////		System.out.println("会话空闲");
//		}
//		@Override
//		public void sessionOpened(IoSession session) throws Exception {
//			InetSocketAddress addr = (InetSocketAddress) session.getRemoteAddress();
//			String ip = addr.getAddress().getHostAddress();
//			logger.info("渠道节点【"+ip+"】正在尝试连接控制中心");
//		}
//
//
//
//		@Override
//		public void exceptionCaught(IoSession session, Throwable cause)
//				throws Exception {
//			ServerSessionContext sessionContext = remoteServer.getSessionContext();
//			logger.debug("exceptionCaught:"+cause.getMessage());
//			if(cause instanceof IOException){
//				InetSocketAddress addr = (InetSocketAddress) session.getRemoteAddress();
//				String ip = addr.getAddress().getHostAddress();
//				String sessionid = (String) session.getAttribute("sessionid");
//				if(StringUtil.isNotEmpty(sessionid)){
//					sessionContext.remove(sessionid);
//					logger.debug("节点代理【"+sessionid+"】【"+ip+"】断开连接");
//				}
//			}
//		}
//
//		@Override
//		public void messageReceived(IoSession iosession, Object message)
//				throws Exception {
//		    if(message instanceof RPCMessage){
//		        if(logger.isDebugEnabled()){
//		            logger.debug("收到消息:{}",message);
//		        }
//                process(iosession, (RPCMessage) message);
//            }
//
////			if(message instanceof RPCRequest){
////				RPCRequest request = (RPCRequest) message;
////				RPCResponse response = null;
////				if(request.getName().equals(ServiceConstants.CH_MN_CONNECT)){
////					response = doConnect(iosession,request);
////				}else{
////					Session session = remoteServer.getSessionContext().getSessionInTheContextObject(iosession.hashCode());
////					response = serviceHandler.handleService(request,session);
////				}
////
////				logger.debug("<<"+request);
////				if(response!=null){
////					logger.debug(">>"+response);
////					iosession.write(response);
////				}
////			}
////			if(message instanceof RPCResponse){
////				RPCResponse response = (RPCResponse) message;
////				logger.debug(">>[NM]"+response);
////				RequestListener rl = requestListeners.get(response.getSessionid());
////				if(rl != null){
////					rl.recivedResponse(response);
////					removeRequestListener(rl);
////				}else{
////					logger.debug("收到响应【"+response.getSessionid()+"】");
////					remoteServer.getRpcCaller().putResponse(response);
////				}
////
////			}
//		}
//	};
//
//
//    @Override
//    public Session getSessionInTheContextObject(Object ctxObject) {
//        IoSession iosession = (IoSession) ctxObject;
//        String sessionid = (String) iosession.getAttribute("sessionid");
//        return getSessionContext().getSession(sessionid);
//    }
//
//	@Override
//	protected void refuseConnect(Object ctxObject, RPCRequest request) {
//
//	}
//
//	@Override
//	protected Session doConnect(Object contextObject, RPCRequest request) {
//		return null;
//	}
//
////    /**
////     * 处理客户端连接命令
////     * @param request
////     * @return
////     */
////    @Override
////    protected RPCResponse doConnect(Object ctxObject, RPCRequest request) {
////        IoSession iosession = (IoSession) ctxObject;
////        RPCResponse response = RPCResponse.buildResponse(request);
////
//////        InetSocketAddress addr  = (InetSocketAddress) iosession.getRemoteAddress();
//////        if(logger.isDebugEnabled()){
//////            logger.debug("有连接接入 {}:{}" , addr.getAddress().getHostAddress(),addr.getPort());
//////        }
////
////        response.setMessage(RPCResponse.STATE_OK);
//////		String clientId = request.getParameter("clientId");
//////		String sessionid = (String) iosession.getAttribute("sessionid");
//////		iosession.setAttribute("sessionid",clientId);
////        Session session = new MinaServerSession(iosession,this);
////        getSessionContext().putSession(session);
////        return response;
////    }
//
//
//
//}

