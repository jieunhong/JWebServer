package com.jjiya;

import com.jjiya.net.Constants;
import com.jjiya.net.TcpServerThread;
import com.jjiya.net.Util;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Main {

    private static int          mCountOfThread = 0;
    private static String       mDataPath      = null;
    private static ServerSocket mServerSocket  = null;

    public int getCountOfThread() {
        return mCountOfThread;
    }

    public void setCountOfThread(int count) {
        this.mCountOfThread = count;
    }

    public static String getDataPath() {
        return mDataPath;
    }

    private static void setDataPath(String str) {
        mDataPath = str;
    }

    public static void main(String[] args) {

        if ( args.length < 1 ) {
            System.out.println("Oops! Required Data root path with arguments!");
            return;
        } else {
            File path = new File(args[0]);
            if ( ! path.isDirectory() ) {
                System.out.println("Oops! Required Data root path with arguments!");
                return;
            }
            setDataPath(args[0]);
        }

        /**
         *
         */
        // "GET / HTTP/1.1"
        String requestStr = "GET /test.html HTTP/1.1\nHost: jjiya.com\n\nabcdefg"; // 웹부라우져가 요청한 전체 스트링

        // "GET /test.html HTTP/1.1
        String requestHeader = Util.parseHttpHeader(requestStr);

        // GET
        String commandStr = Util.getCommond(requestHeader);

        if ( "GET".equals(commandStr) )
        {
            // /test.html
            String path = Util.getRequestPath(requestHeader);
            //
            try {
                String contents = Util.getFileContents(path);

                System.out.println("=====request=====");
                System.out.println(requestStr);
                System.out.println("=====response=====");
                System.out.println("Header  : "+requestHeader);
                System.out.println("Commend : "+commandStr);
                System.out.println("Path    : "+path);
                System.out.println("Contents: \n"+contents);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            //
        }

        /**
         *
         */
        new Thread() {
            public void run() {
                int lastThreadIndex = 0;
                try {
                    mServerSocket = new ServerSocket( Constants.SERVER_LISTEN_PORT );
                    System.out.println("JWebServer Rendezvous ready. listen port:"+Constants.SERVER_LISTEN_PORT);

                    while ( true ) {
                        Socket tcpClientSocket  = mServerSocket.accept();
                        System.out.println("accept()");
                        lastThreadIndex++;
                        TcpServerThread tcpServerThread = new TcpServerThread(tcpClientSocket, lastThreadIndex);
                        tcpServerThread.start();
                    }
                } catch (SocketException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
