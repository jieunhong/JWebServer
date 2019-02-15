package com.jjiya.net;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpServerThread extends Thread {
    private int                 mIndexOfThread   = -1;
    private Socket              mTcpClientSocket = null;
    private InputStream         mInputStream     = null;
    private OutputStream        mOutputStream    = null;
    private byte[]              mHeaderBytes     = null;
    private byte[]              mBodyBytes       = null;
    private String              mHeaderStr       = null;

    public HttpServerThread(Socket tcpClientSocket, int indexOfThread) {
        this.mTcpClientSocket = tcpClientSocket;
        this.mIndexOfThread   = indexOfThread;
        this.mHeaderBytes     = new byte[Constants.HTTP_HEADER_BUFFER_SIZE];
    }

    private String getThreadIndex() {
        return "thread["+mIndexOfThread+"]";
    }

    public void run() {
        System.out.println(getThreadIndex()+" run()");

        int    readbyte = 0;
        byte[] bytes    = null;

        try {
            mInputStream  = mTcpClientSocket.getInputStream();
            mOutputStream = mTcpClientSocket.getOutputStream();

            readbyte = mInputStream.read(mHeaderBytes, 0, Constants.HTTP_HEADER_BUFFER_SIZE);
            System.out.println(getThreadIndex()+" run() readbyte:"+readbyte);

            String str = new String(mHeaderBytes, StandardCharsets.UTF_8);
            System.out.println("str:"+str);

            mHeaderStr = Util.parseHttpHeader(str);
            System.out.println("mHeaderStr:"+mHeaderStr);

            String command = Util.getCommond(mHeaderStr);
            System.out.println("command:"+command);

            if ( "GET".equals(command) )
            {
                String requestPath = Util.getRequestPath(mHeaderStr);
                System.out.println("requestPath:"+requestPath);

                String fileContent = Util.getFileContents(requestPath);
                System.out.println("fileContent:"+fileContent);

                if ( fileContent != null )
                {
                    // 200 OK
                    String responseHeader = Util.getFileContents("/response_200_header.txt");
                    String responseBody   = fileContent;

                    int bodyByteLen = responseBody.getBytes(StandardCharsets.UTF_8).length;
                    responseHeader  = responseHeader.replace("%REPLACE1%",String.valueOf(bodyByteLen));

                    String responseStr = responseHeader + "\n\n" + responseBody;

                    byte[] sendbytes = responseStr.getBytes(StandardCharsets.UTF_8);
                    mOutputStream.write(sendbytes, 0, sendbytes.length);

                    System.out.println("length of response byte:"+sendbytes.length);
                }
                else
                {
                    // 404 Not found
                    String responseHeader = Util.getFileContents("/response_404_header.txt");
                    String responseBody   = Util.getFileContents("/response_404_body.txt");

                    responseBody    = responseBody.replace("%REPLACE1%", requestPath);
                    int bodyByteLen = responseBody.getBytes(StandardCharsets.UTF_8).length;
                    responseHeader  = responseHeader.replace("%REPLACE1%",String.valueOf(bodyByteLen));

                    String responseStr = responseHeader + "\n\n" + responseBody;

                    byte[] sendbytes = responseStr.getBytes(StandardCharsets.UTF_8);
                    mOutputStream.write(sendbytes, 0, sendbytes.length);

                    System.out.println("length of response byte:"+sendbytes.length);
                }
            }
            else
            {
                // POST
            }

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
