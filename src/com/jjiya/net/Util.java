package com.jjiya.net;

import com.jjiya.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * @author Andrew
 *
 */
public class Util {

    public static byte[] intTobyte(int integer, ByteOrder order) {

        ByteBuffer buff = ByteBuffer.allocate(Integer.SIZE/8);
        buff.order(order);

        // 인수로 넘어온 integer을 putInt로설정
        buff.putInt(integer);

        System.out.println("intTobyte : " + buff);
        return buff.array();
    }

    public static int byteToInt(byte[] bytes, ByteOrder order) {

        ByteBuffer buff = ByteBuffer.allocate(Integer.SIZE/8);
        buff.order(order);

        // buff사이즈는 4인 상태임
        // bytes를 put하면 position과 limit는 같은 위치가 됨.
        buff.put(bytes);
        // flip()가 실행 되면 position은 0에 위치 하게 됨.
        buff.flip();

        System.out.println("byteToInt : " + buff);

        return buff.getInt(); // position위치(0)에서 부터 4바이트를 int로 변경하여 반환
    }

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    public static String parseHttpHeader(String str) {
        // 캐리지 리턴
        return str.split("\n")[0];
    }

    public static String getCommond(String header) {
        // "GET", "POST"
        return header.split(" ")[0];
    }

    public static String getRequestPath(String header) {
        // "/test.html"
        return header.split(" ")[1];
    }

    public static String getFileContents(String path) throws FileNotFoundException, IOException {
        // "E:\JWebServer\data" + path

        String contents  = null;
        String localPath = path.replace("/", File.separator);

        File file = new File(Main.getDataPath() + File.separator + path);

        if ( file.isFile() )
        {
            contents = "";
            //입력 스트림 생성
            FileReader filereader = new FileReader(file);
            int        ch         = filereader.read();

            while ( ch != -1 ) {
                contents += (char)ch;
                ch       =  filereader.read();
            }
            filereader.close();
        }
        else
        {
            if ( file.isDirectory() )
            {

            }
            else if ( ! file.exists() )
            {

            }
        }

        return contents;
    }
}
