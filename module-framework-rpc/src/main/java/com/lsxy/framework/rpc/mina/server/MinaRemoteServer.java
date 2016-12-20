package com.lsxy.framework.rpc.mina.server;

//@Component
//@ConditionalOnProperty(value = "global.rpc.provider", havingValue = "mina", matchIfMissing = false)
//public class MinaRemoteServer implements RemoteServer{
//	private static final Logger logger = LoggerFactory.getLogger(MinaRemoteServer.class);
//	private NioSocketAcceptor acceptor;
//
//    @Autowired
//	private MinaServerHandler handler;
//
//	@Autowired
//	private ServerSessionContext sessionContext;
//
//	@Autowired
//	private RPCCaller rpcCaller;
//
//	//服务端口号
//	private Integer serverPort;
//
//	public MinaRemoteServer(){
//		acceptor = new NioSocketAcceptor();
//		acceptor.getSessionConfig().setReadBufferSize(10000);
//		acceptor.getSessionConfig().setReaderIdleTime(100);
//		acceptor.getSessionConfig().setReceiveBufferSize(1000000);
//		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 1);
//
//	}
//
//
//	public RPCCaller getRpcCaller() {
//		return rpcCaller;
//	}
//
//
//	/**
//	 * 绑定端口，开始提供服务
//	 * @throws IOException
//	 * @throws GeneralSecurityException
//	 */
//	public void bind() throws IOException, GeneralSecurityException{
//		acceptor.setDefaultLocalAddress(new InetSocketAddress(serverPort));
//
//		acceptor.setHandler(handler.getIoHandler());
//		String showLog = SystemConfig.getProperty("channel.server.minalog","false");
//		if(showLog.equals("true")){
//			Log4jFilter lf = new Log4jFilter(logger);
//			lf.setSessionIdleLoglevel(LogLevel.ERROR);
//			acceptor.getFilterChain().addLast("logger",lf);
//		}
//		if(SystemConfig.getProperty("channel.server.ssl","false").equals("true")){
//			SslFilter sslFilter = new SslFilter(SSLContextFactory.getInstance(true));
//			acceptor.getFilterChain().addLast("sslFilter", sslFilter);
//			logger.info("启用采用SSL安全连接");
//		}
//		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new RPCCodecFactory()));
//		acceptor.bind();
//		logger.info("渠道管理器在端口"+serverPort+"提供服务");
//	}
//
//	/**
//	 * 反绑定
//	 */
//	public void unbind(){
//		logger.info("渠道管理器服务端口关闭,停止提供服务");
//		acceptor.unbind();
//	}
//
//
//
//	public ServerSessionContext getSessionContext() {
//		return sessionContext;
//	}
//
//
//
////	public static void main(String[] args) throws IOException, GeneralSecurityException {
////		MinaRemoteServer cs = new MinaRemoteServer(new AbstractServiceHandler() {
////			@Override
////			public RPCResponse handleService(RPCRequest request, IoSession session) {
////				return null;
////			}
////		});
////		cs.bind();
////	}
//
//    @Override
//    public void startServer() throws RemoteServerStartException {
//        try {
//            this.bind();
//        } catch (IOException | GeneralSecurityException e) {
//			logger.error("IO异常",e);
//            throw new RemoteServerStartException(e);
//        }
//    }
//
//	@Override
//	public void setServerPort(Integer port) {
//		this.serverPort = port;
//	}
//
//	@Override
//	public AbstractServerRPCHandler getHandler() {
//		return this.handler;
//	}
//}
