package com.seeta.sdk;

public class PropertyTest {
    public static void main(String[] args) {

        System.out.println(System.getProperty("os.name"));

        System.out.println(System.getProperty("os.arch"));

        System.out.println(System.getProperty("os.version"));


        String os = System.getProperty("os.name");
        //Windows操作系统
        if (os != null && os.toLowerCase().startsWith("windows")) {
            System.out.println(String.format("当前系统版本是:%s", os));
        } else if (os != null && os.toLowerCase().startsWith("linux")) {//Linux操作系统
            System.out.println(String.format("当前系统版本是:%s", os));
        } else { //其它操作系统
            System.out.println(String.format("当前系统版本是:%s", os));
        }


        String path = System.getProperty("user.dir");
        System.out.println(path);
    }
}
