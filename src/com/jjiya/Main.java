package com.jjiya;

public class Main {




    public static String parseHttpHeader(String str) {
        // 캐리지 리턴
        return null;
    }

    public static String getCommond(String header) {
        // "GET", "POST"
        return null;
    }

    public static String getRequestPath(String header) {
        // "/test.html"
        return null;
    }

    public static String getFileContents(String path) {
        // "E:\JWebServer\data" + path
        return null;
    }




    public static void main(String[] args) {

        // "GET / HTTP/1.1"
        String requestStr = "GET /test.html HTTP/1.1\nHost: jjiya.com\n\nabcdefg"; // 웹부라우져가 요청한 전체 스트링

        // "GET /test.html HTTP/1.1
        String requestHeader = parseHttpHeader(requestStr);

        // GET
        String commandStr = getCommond(requestHeader);

        if ( "GET".equals(commandStr) )
        {
            // /test.html
            String path = getRequestPath(requestHeader);

            //
            String contents = getFileContents(path);

        }
        else
        {
            //
        }


        // 1. get으로 요청하면 파일목록 띄워주는 기능
        /*



GET /test.html HTTP/1.1
Host: jjiya.com

HTTP/1.1 200 OK
Server: nginx
Date: Fri, 08 Feb 2019 05:30:29 GMT
Content-Type: text/html
Content-Length: 35
Connection: keep-alive
Keep-Alive: timeout=20
Last-Modified: Fri, 08 Feb 2019 05:24:25 GMT
ETag: "23-5815b2c15595d"
Accept-Ranges: bytes
Vary: Accept-Encoding

<html> <body> test </body> </html>



         */

    }
}
