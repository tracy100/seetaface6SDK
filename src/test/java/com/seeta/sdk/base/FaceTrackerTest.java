package com.seeta.sdk.base;

import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.util.Arrays;

/**
 * 人脸跟踪
 * 人脸跟踪也是基于人脸的基本模块，其要解决的问题是在进行识别之前就利用视频特性，首先就确认在视频序列中出现的那些人是同一人。
 * 这里只做了单张照片的，视频需要把贞传入。
 */
public class FaceTrackerTest {


    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        try {
            //视频贞（这里用单张照片了，贞也是一样的传入）
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(FileConstant.TEST_PICT);
            //人脸跟踪器
            FaceTracker faceTracker = new FaceTracker(FileConstant.face_detector,image.width,image.height);
            //跟踪到的人脸
            SeetaTrackingFaceInfo[] track = faceTracker.Track(image);
            System.out.println(Arrays.toString(track));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
