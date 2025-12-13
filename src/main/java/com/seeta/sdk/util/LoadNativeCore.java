package com.seeta.sdk.util;

import com.seeta.sdk.SeetaDevice;
import com.seeta.sdk.exception.SeetaInitializationException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * 动态加载dll
 *
 * @author YaoCai Lin
 */
public class LoadNativeCore {

    public static final String SEETAFACE6 = "seetaface6";
    /**
     * 定义dll 路径和加载顺序的文件
     */
    private static final String PROPERTIES_FILE_NAME = "dll.properties";
    private static Logger logger = Logger.getLogger(LoadNativeCore.class.getName());
    /**
     * 是否加载过
     */
    private static volatile boolean isLoaded = false;

    public static void main(String[] args) {
        LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);

//        System.out.println(getPrefix());
//        System.out.println(getPropertiesPath());

    }

    public static synchronized void LOAD_NATIVE(SeetaDevice seetaDevice) throws SeetaInitializationException {

        if (!isLoaded) {
            String device = getDevice(seetaDevice);
            InputStream var1 = LoadNativeCore.class.getResourceAsStream(getPropertiesPath());
            Properties properties = new Properties();
            try {
                properties.load(var1);
                List<DllItem> baseList = new ArrayList<>();
                List<DllItem> jniList = new ArrayList<>();

                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    DllItem dllItem = new DllItem();
                    dllItem.setKey(key);
                    if (key.contains("base")) {
                        if (value.contains("tennis")) {
                            dllItem.setValue(getPrefix() + "base/" + device + "/" + value);
                        } else {
                            dllItem.setValue(getPrefix() + "base/" + value);
                        }
                        baseList.add(dllItem);
                    } else {
                        dllItem.setValue(getPrefix() + value);
                        jniList.add(dllItem);
                    }
                }

                /*
                  将文件分类
                 */
                List<String> basePath = getSortedPath(baseList);
                List<String> sdkPath = getSortedPath(jniList);

                List<File> fileList = new ArrayList<>();

                /*
                  拷贝文件到临时目录
                 */
                for (String b : basePath) {
                    fileList.add(copyDLL(b));
                }
                for (String s : sdkPath) {
                    fileList.add(copyDLL(s));
                }

                // 加载 dll文件
                fileList.forEach(file -> {
                    System.load(file.getAbsolutePath());
                    logger.info(String.format("load %s finish", file.getAbsolutePath()));
                });
                logger.info("............END !");

            } catch (IOException e) {
                e.printStackTrace();
            }
            isLoaded = true;
        }
    }


    private static String getArch() {
        String arch = System.getProperty("os.arch").toLowerCase();
        if (arch.startsWith("amd64")
                || arch.startsWith("x86_64")
                || arch.startsWith("x86-64")
                || arch.startsWith("x64")) {
            arch = "amd64";
        } else if (arch.contains("aarch")) {
            arch = "aarch64";
        } else if (arch.contains("arm")) {
            arch = "arm";
        }
        return arch;
    }

    private static String getDevice(SeetaDevice seetaDevice) {
        String device = "CPU";
        if ("amd64".equals(getArch())) {
            device = seetaDevice.getValue() == 2 ? "GPU" : "CPU";
        }
        return device;
    }


    /**
     * 获取dll配置文件路径
     *
     * @return String
     */
    private static String getPropertiesPath() {

        return getPrefix() + PROPERTIES_FILE_NAME;
    }

    /**
     * 返回路径文件前缀
     *
     * @return
     */
    private static String getPrefix() {
        String arch = getArch();
        //aarch64
        String os = System.getProperty("os.name");
        //Windows操作系统
        if (os != null && os.toLowerCase().startsWith("windows")) {
            //logger.info("windows系统");
            os = "/windows/";
        } else if (os != null && os.toLowerCase().startsWith("linux")) {//Linux操作系统
            //logger.info("linux系统");
            os = "/linux/";
        } else { //其它操作系统
            //安卓 乌班图等等，先不写
            return null;
        }
        // "/seetaface6/windows/amd64"
        return "/" + SEETAFACE6 + os + arch + "/";
    }


    /**
     * 将获得的配置进行排序 并生成路径
     *
     * @param list
     * @return List<String>
     */
    private static List<String> getSortedPath(List<DllItem> list) {
        return list.stream().sorted(Comparator.comparing(dllItem -> {
            int i = dllItem.getKey().lastIndexOf(".") + 1;
            String substring = dllItem.getKey().substring(i);
            return Integer.valueOf(substring);
        })).map(DllItem::getValue).collect(Collectors.toList());
    }

    /**
     * 复制 resource 中的dll文件到临时目录
     *
     * @param path 路径
     * @return File 文件
     * @throws IOException 异常
     */
    private static File copyDLL(String path) throws IOException {
        String nativeTempDir = System.getProperty("user.dir");
        File extractedLibFile = new File(nativeTempDir + File.separator + path);
        mkdirs(extractedLibFile.getParent());
        InputStream in = LoadNativeCore.class.getResourceAsStream(path);
        if (in != null) {
            writeToLocalTemp(extractedLibFile.getAbsolutePath(), in);
        } else {
            throw new IOException(String.format("Could not find %s", path));
        }

        return extractedLibFile;
    }


    /**
     * 将InputStream写入本地文件
     *
     * @param destination 写入本地目录
     * @param input       输入流
     * @throws IOException IOException
     */
    private static void writeToLocalTemp(String destination, InputStream input)
            throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        input.close();
        downloadFile.close();

    }

    /**
     * 创建父级目录
     *
     * @param path
     */
    private static void mkdirs(String path) {
        //变量不需赋初始值，赋值后永远不会读取变量，在下一个变量读取之前，该值总是被另一个赋值覆盖
        File f;
        try {
            f = new File(path);
            if (!f.exists()) {
                f.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
