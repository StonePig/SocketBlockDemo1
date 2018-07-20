package com.example.socketblockdemo;

public class Const
{
	
   public final static String SOCKET_SERVER = "103.46.128.43";

	public final static int SOCKET_PORT = 12187;
	
	// 默认timeout 时间 60s
	public final static int SOCKET_TIMOUT = 60 * 1000;
	
	public final static int SOCKET_READ_TIMOUT = 15 * 1000;
	
	//如果没有连接无服务器。读线程的sleep时间
	public final static int SOCKET_SLEEP_SECOND = 3 ;
	
	//心跳包发送间隔时间
	public final static int SOCKET_HEART_SECOND =3 ;
	
	public final static String BC = "BC";
	
	
}
