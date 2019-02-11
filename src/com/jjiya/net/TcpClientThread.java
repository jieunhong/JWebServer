/**
 * 
 */
package com.jjiya.net;

import java.io.*;
import java.net.Socket;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * @author Andrew
 *
 */
public class TcpClientThread extends Thread {
	
	private int    mIndexOfThread      = -1;
	private String serverIp   = null;
	private int    serverPort = -1;
	private File   file       = null;
	
	public TcpClientThread(int indexOfThread, String ip, int port, File file) {
		this.mIndexOfThread = indexOfThread;
		this.serverIp       = ip;
		this.serverPort     = port;
		this.file           = file;
	}
	
	public void run() {
		
		Socket clientSocket;
		try {
			
			String fileName    = file.getName();
			byte[] sendbytes   = fileName.getBytes(StandardCharsets.UTF_8);
			
			clientSocket = new Socket(serverIp, serverPort);
			InputStream  inputStream  = clientSocket.getInputStream();
			OutputStream outputStream = clientSocket.getOutputStream();
			
			byte[] lenbytes =  Util.intTobyte(sendbytes.length, ByteOrder.LITTLE_ENDIAN);
			
			// send file name length
			outputStream.write(lenbytes, 0, (Integer.SIZE/8));
			
			// send file name
			outputStream.write(sendbytes, 0, sendbytes.length);
			
			lenbytes = new byte[Long.SIZE];
			lenbytes =  Util.longToBytes(file.length());
			
			// send file length
			outputStream.write(lenbytes, 0, Long.SIZE/8);
			
			byte[] buffer = new byte[Constants.DEFAULT_BUFFER_SIZE];
			int readBytes;
			long totalReadBytes = 0;
			FileInputStream fis = new FileInputStream(file);
			while ((readBytes = fis.read(buffer)) > 0) {
				outputStream.write(buffer, 0, readBytes);
                totalReadBytes += readBytes;
                System.out.println("In progress: " + totalReadBytes + "/"
                        + file.length() + " Byte(s) ("
                        + (totalReadBytes * 100 / file.length()) + " %)");
            }
			fis.close();
			
			outputStream.flush();
			outputStream.close();
			inputStream.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * @param args
	 */
	
	/*
	public static void main(String[] args) {

		try {
			Socket clientSocket = new Socket(Constants.jjiyaServerIpAddr, Constants.serverTcpPort);
			InputStream  mInputStream  = clientSocket.getInputStream();
			OutputStream mOutputStream = clientSocket.getOutputStream();
			
			String trLength = "77";
			String tr = "{\"Request\":{\"Action\":\"getServerAppVersion\"},\"Client\":{\"OsName\":\"OSX Sierra\"}}"; // 77
			
			byte[] sendbytes = null;
			byte[] recvbytes = new byte[16384];
			
			sendbytes = trLength.getBytes(StandardCharsets.UTF_8);
			log.debug("sendbytes length:"+sendbytes.length);
			mOutputStream.write(sendbytes, 0, sendbytes.length);
			
			sendbytes = tr.getBytes(StandardCharsets.UTF_8);
			log.debug("sendbytes length:"+sendbytes.length);
			mOutputStream.write(sendbytes, 0, sendbytes.length);
			
			String tr2 = "{\"Request\":{\"Action\":\"getServerAppVersion\"},\"Client\":{\"OsName\":\"OSX Sierra\"}}"; // 77
			sendbytes = tr2.getBytes(StandardCharsets.UTF_8);
			mOutputStream.write(sendbytes, 0, sendbytes.length);
			
			String loginTr = "{\"Request\":{\"Action\":\"login\",\"email\":\"babygom@gmail.com\",\"pwd\":\"03919\"},\"Client\":{\"OsName\":\"OSX Sierra\"}}";
			String loginTrLength = String.valueOf(loginTr.length());
			
			boolean responseBye = false;
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			
			while ( true ) {
				if ( responseBye == true ) {
					break;
				}
				
				int readbyte = mInputStream.read(recvbytes, 0, 2);
				log.debug("readbyte:"+readbyte);
				buffer.write(recvbytes, 0, readbyte);
				buffer.flush();
			    byte[] byteArray = buffer.toByteArray();
			    String willRecvTextLengthStr = new String(byteArray, StandardCharsets.UTF_8);
			    log.debug("willRecvTextLengthStr:"+willRecvTextLengthStr);
			    buffer.reset();
			    int nRead;
			    byte[] data = new byte[1024];
			    while ( ( nRead = mInputStream.read(data, 0, data.length) ) != -1 ) {
			        buffer.write(data, 0, nRead);
			    }
			    buffer.flush();
			    String readStr = new String(buffer.toByteArray(), StandardCharsets.UTF_8);
			    log.debug(" readStr:"+readStr);
				
				JsonParser jsonParser = new JsonParser();
				JsonObject jo         = jsonParser.parse(readStr).getAsJsonObject();
				//String     responseAction = (String)jo.get("RequiredAction");
				
			    
				
				
				try {
					Thread.sleep(100);
					log.debug(".");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			mOutputStream.flush();
			mOutputStream.close();
			mInputStream.close();
			clientSocket.close();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	*/
}
