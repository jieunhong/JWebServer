/**
 * 
 */
package com.jjiya.net;

import com.jjiya.Main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * @author Andrew
 *
 */
public class TcpServerThread extends Thread {

	private int                 mIndexOfThread      = -1;
	private Socket              mTcpClientSocket    = null;
	private InputStream         mInputStream        = null;
	private OutputStream        mOutputStream       = null;
	
	public TcpServerThread(Socket tcpClientSocket, int indexOfThread) {
		this.mTcpClientSocket = tcpClientSocket;
		this.mIndexOfThread   = indexOfThread;
	}
	
	private String getThreadIndex() {
		return "thread["+mIndexOfThread+"]";
	}
	
	public void run() { 
		System.out.println(getThreadIndex()+" run()");
		try {
			mInputStream  = mTcpClientSocket.getInputStream();			
			mOutputStream = mTcpClientSocket.getOutputStream();
			
			byte[] recvbytes = new byte[Constants.DEFAULT_BUFFER_SIZE];//[16384];
			
			// file name length
			byte[] lenbytes = new byte[Integer.SIZE/8];
			int readbyte = mInputStream.read(lenbytes, 0, Integer.SIZE/8);
			int filenamelen = Util.byteToInt(lenbytes, ByteOrder.LITTLE_ENDIAN);
			System.out.println("filenamelen:"+filenamelen);
			
			// file name
			byte[] namebytes = new byte[filenamelen];
			readbyte = mInputStream.read(namebytes, 0, filenamelen);
			String finename = new String(namebytes, StandardCharsets.UTF_8);
			System.out.println("finename:"+finename);
			
			// file length
			lenbytes = new byte[Long.SIZE/8];
			readbyte = mInputStream.read(lenbytes, 0, Long.SIZE/8);
			long filelen = Util.bytesToLong(lenbytes);
			System.out.println("filelen:"+filelen);
			
			// file
			byte[] fileBuffer = new byte[Constants.DEFAULT_BUFFER_SIZE];
			String filePath = Main.getDataPath() + finename;
			System.out.println("filePath:"+filePath);
			System.out.println("filePath:"+filePath);
			
			File newFile = new File(filePath);
			if ( newFile.exists() == true ) {
				newFile = new File(filePath+"새이름");
			}
			newFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(newFile,false);
			while ( ( readbyte = mInputStream.read(fileBuffer) ) != -1 ) {
				fos.write(fileBuffer, 0, readbyte);
			}
			fos.close();
			
			
			mOutputStream.flush();
			mInputStream.close();
			mOutputStream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(getThreadIndex() + e.getLocalizedMessage());
		}
		System.out.println(getThreadIndex()+" finish.");
	}
}
