package com.magic.time.service.business_development_100.Files_14;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author Cheng Yufei
 * @create 2022-02-07 11:10 PM
 **/
@Slf4j
public class FilesHandler {


    /**
     * 以gbk编码写入文件
     * @throws IOException
     */
    public static void h1() throws IOException {
        String path = "/Users/chengyufei/Downloads/project/self/hello.txt";
        Files.write(Paths.get(path), "你好，jetbrains！".getBytes("GBK"));
        log.info("result:{}", Hex.encodeHexString(Files.readAllBytes(Paths.get(path))).toUpperCase());
    }

    /**
     * 字节方式、字符方式不指定编码时读取、写入
     * @throws IOException
     */
    public static void h2() throws IOException {
        String path = "/Users/chengyufei/Downloads/project/self/hello.txt";
        //Input length = 1
     /*
        List<String> strings = Files.readAllLines(Paths.get(path));
        System.out.println(strings);*/
        FileReader fileReader = new FileReader(path);
        FileWriter fileWriter = new FileWriter("/Users/chengyufei/Downloads/project/self/hello2.txt");
        FileInputStream fileInputStream = new FileInputStream(path);
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/chengyufei/Downloads/project/self/hello2.txt");
        String content = "";
        int len;
        byte[] bytes = new byte[100];
        char[] chars = new char[100];
        //while ((len = fileReader.read(chars)) != -1) {
        while ((len = fileInputStream.read(bytes)) != -1) {
            //content += new String(bytes, 0, len);
            //fileWriter.write(chars, 0, len);
            fileOutputStream.write(bytes, 0, len);
        }
        //fileWriter.close();


        //字符流指定字符集
        InputStreamReader streamReader = new InputStreamReader(fileInputStream, Charset.forName("gbk"));
        System.out.println(content);
        List<String> strings = Files.readAllLines(Paths.get(path), Charset.forName("gbk"));
        System.out.println(strings);
    }

    /**
     * 获取文件大小；
     * 写入10行内容；
     * @throws IOException
     */
    public static void h3() throws IOException {
        String path = "/Users/chengyufei/Downloads/project/self/hello.txt";
        long fileSize = Files.size(Paths.get(path));
        System.out.println(fileSize);

        String path2 = "/Users/chengyufei/Downloads/project/self/hello2.txt";
        Files.write(Paths.get(path2), IntStream.rangeClosed(1, 10).mapToObj(__ -> UUID.randomUUID().toString()).collect(Collectors.toList()),
                Charset.forName("utf-8"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    }

    /**
     * 不主动关闭资源，报错：Too many open files
     */
    public static void h4() {
        String path2 = "/Users/chengyufei/Downloads/project/self/hello2.txt";

       /* LongAdder longAdder = new LongAdder();
        IntStream.rangeClosed(1, 100000).forEach(i -> {
            try {
                Files.lines(Paths.get(path2)).forEach(s -> longAdder.increment());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println(longAdder.longValue());*/

        LongAdder longAdder = new LongAdder();

        IntStream.rangeClosed(1, 100000).forEach(i -> {
            try (Stream<String> lines = Files.lines(Paths.get(path2))) {
                lines.forEach(s -> longAdder.increment());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println(longAdder.longValue());
    }

    public static void h5() throws IOException {
        String path2 = "/Users/chengyufei/Downloads/project/self/hello.txt";
        FileChannel inChannel = FileChannel.open(Paths.get(path2));
        FileChannel outChannel = FileChannel.open(Paths.get("/Users/chengyufei/Downloads/project/self/hello3.txt"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        inChannel.transferTo(0, inChannel.size(), outChannel);
    }

    public static void main(String[] args) throws IOException {
        //h1();
        //h2();
        //h3();
        //h4();
        h5();
        System.out.println("好".getBytes("utf-8").length);
        System.out.println("好".getBytes("gbk").length);
        System.out.println("好".getBytes(StandardCharsets.ISO_8859_1).length);
    }
}
