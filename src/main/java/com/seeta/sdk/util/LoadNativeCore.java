package com.seeta.sdk.util;

import com.seeta.sdk.SeetaDevice;

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

    private static Logger logger = Logger.getLogger(LoadNativeCore.class.getName());

    /**
     * 定义dll 路径和加载顺序的文件
     */
    private static final String PROPERTIES_FILE_NAME = "dll.properties";

    public static void main(String[] args) {
        loadLib(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    /**
     * 是否加载过
     */
    private static boolean isLoad = false;


    private static synchronized void loadLib(SeetaDevice seetaDevice) {

        if (!isLoad) {
            String device = seetaDevice.getValue() == 2 ? "GPU" : "CPU";
            //logger.info("开始加载dll");
            InputStream var1 = LoadNativeCore.class.getResourceAsStream(getPropertiesPathByOs());
            Properties properties = new Properties();
            try {
                properties.load(var1);
                List<DllItem> baseList = new ArrayList<>();
                List<DllItem> sdkList = new ArrayList<>();

                Iterator<Map.Entry<Object, Object>> iterator = properties.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Object, Object> entry = iterator.next();
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    DllItem dllItem = new DllItem(key, value);

                    if (key.contains("base")) {
                        baseList.add(dllItem);
                    } else {
                        sdkList.add(dllItem);
                    }
                }

                /**
                 * 将文件分类
                 */
                List<String> basePath = getSortedPath(baseList, device);
                List<String> sdkPath = getSortedPath(sdkList, null);

                List<File> fileList = new ArrayList<>();

                /**
                 * 拷贝文件到临时目录
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
                    logger.info(String.format("加载 %s 完成", file.getName()));
                });
                logger.info("............加载完成！");

            } catch (IOException e) {
                e.printStackTrace();
            }
            isLoad = true;
        }
    }

    static {

    }

    /**
     * 类加载调用这个方法
     */
    public static void LOAD_NATIVE(SeetaDevice seetaDevice) {
        loadLib(seetaDevice);
    }

    /**
     * 通过判断系统类型，获取配置文件路径
     *
     * @return String
     */
    private static String getPropertiesPathByOs() {
        String path = "";
        String os = System.getProperty("os.name");
        //Windows操作系统
        if (os != null && os.toLowerCase().startsWith("windows")) {
            logger.info("windows系统");
            path = "/windows_x64/";
        } else if (os != null && os.toLowerCase().startsWith("linux")) {//Linux操作系统
            logger.info("linux系统");
            path = "/linux_centos/";
        } else { //其它操作系统
            //安卓 乌班图等等，先不写
            return null;
        }
        return path + PROPERTIES_FILE_NAME;
    }


    /**
     * 将获得的配置进行排序 并生成路径
     *
     * @param list
     * @param device 驱动 GPU 还是 CPU
     * @return List<String>
     */
    private static List<String> getSortedPath(List<DllItem> list, String device) {
        List<String> sortedPath = list.stream().sorted(Comparator.comparing(dllItem -> {
            int i = dllItem.getKey().lastIndexOf(".") + 1;
            String substring = dllItem.getKey().substring(i);
            return Integer.valueOf(substring);
        })).map(dllItem -> {
            int i = dllItem.getKey().lastIndexOf(".");
            String keyStr = dllItem.getKey().substring(0, i);
            keyStr = keyStr.replace(".", "/");
            if (device != null && device != "" && dllItem.getValue().contains("tennis")) {
                return "/" + keyStr + "/" + device + "/" + dllItem.getValue();
            } else {
                return "/" + keyStr + "/" + dllItem.getValue();
            }
        }).collect(Collectors.toList());
        return sortedPath;
    }

    /**
     * 复制 resource 中的dll文件到临时目录
     *
     * @param path
     * @return
     * @throws IOException
     */
    private static File copyDLL(String path) throws IOException {

        String nativeTempDir = System.getProperty("java.io.tmpdir");

        File extractedLibFile = new File(nativeTempDir + File.separator + path);
        mkdirs(extractedLibFile.getParent());
        InputStream in = LoadNativeCore.class.getResourceAsStream(path);
        writeToLocal(extractedLibFile.getAbsolutePath(), in);

        return extractedLibFile;
    }


    /**
     * 将InputStream写入本地文件
     *
     * @param destination 写入本地目录
     * @param input       输入流
     * @throws IOException IOException
     */
    private static void writeToLocal(String destination, InputStream input)
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
