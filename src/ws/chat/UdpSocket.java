package ws.chat;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpSocket implements Runnable {

	Thread thread = null;
	DatagramPacket receivePack;
	DatagramSocket receiveSocket;
	int receivePort;
	byte inBuf[];
	static final int BUFSIZE = 16*1024;

	public UdpSocket()
	{
		
	}
   
	@Override
	public void run()
	{
		String msgStr;
		while (true)
		{
			try
			{
				receiveSocket.receive(receivePack);
				msgStr = new String(receivePack.getData(), 0, receivePack.getLength());
				
				ChatAnnotation.broadcast(msgStr);
				
				
//				System.out.println("recevied msg: " + msgStr);
			}
			catch (Exception e)
			{
				//TODO: handle exception
			}
		}
	}
	
	void readRecv()
	{
		try
		{
			receivePort = 4507;
			inBuf = new byte[BUFSIZE];
			receivePack = new DatagramPacket(inBuf, BUFSIZE);
			receiveSocket=new DatagramSocket(receivePort);
			thread = new Thread(this);
			thread.setPriority(Thread.MIN_PRIORITY);
			thread.start();
		}
		catch (SocketException ex)
		{
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

}
