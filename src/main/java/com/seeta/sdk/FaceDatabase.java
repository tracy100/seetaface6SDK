package com.seeta.sdk;


/**
 * 应该是连接数据库用的，做小规模对比搜索
 */
public class FaceDatabase {
//    static {
//        System.loadLibrary("SeetaFaceRecognizer600_java");
//    }

    public long impl = 0;

    public FaceDatabase(SeetaModelSetting seetaModel) {
        this.construct(seetaModel);
    }

    public FaceDatabase(SeetaModelSetting seetaModel, int extractionCoreNumber, int comparationCoreNumber) {
        this.construct(seetaModel, extractionCoreNumber, comparationCoreNumber);
    }

    public static native int SetLogLevel(int level);

    public static native int GetCropFaceWidthV2();

    public static native int GetCropFaceHeightV2();

    public static native int GetCropFaceChannelsV2();

    public static native boolean CropFaceV2(SeetaImageData image, SeetaPointF[] points, SeetaImageData face);

    private native void construct(SeetaModelSetting setting);

    private native void construct(SeetaModelSetting setting, int extractionCoreNumber, int comparationCoreNumber);

    public native void dispose();

    protected void finalize() throws Throwable {
        super.finalize();
        this.dispose();
    }

    public native float Compare(SeetaImageData image1, SeetaPointF[] points1,
                                SeetaImageData image2, SeetaPointF[] points2);

    public native float CompareByCroppedFace(SeetaImageData croppedFaceImage1, SeetaImageData croppedFaceImage2);

    public native long Register(SeetaImageData image, SeetaPointF[] points);

    public native long RegisterByCroppedFace(SeetaImageData croppedFaceImage);

    public native int Delete(long index);

    public native void Clear();

    public native long Count();

    public native long Query(SeetaImageData image, SeetaPointF[] points);

    public native long Query(SeetaImageData image, SeetaPointF[] points, float[] similarity);

    public native long QueryByCroppedFace(SeetaImageData croppedFaceImage);

    public native long QueryByCroppedFace(SeetaImageData croppedFaceImage, float[] similarity);

    public native long QueryTop(SeetaImageData image, SeetaPointF[] points, long N, long[] index, float[] similarity);

    public native long QueryTopByCroppedFace(SeetaImageData croppedFaceImage, long N, long[] index, float[] similarity);

    public native long QueryAbove(SeetaImageData image, SeetaPointF[] points, float threshold, long N, long[] index, float[] similarity);

    public native long QueryAboveByCroppedFace(SeetaImageData croppedFaceImage, float threshold, long N, long[] index, float[] similarity);

    public native void RegisterParallel(SeetaImageData image, SeetaPointF[] points, long[] index);

    public native void RegisterByCroppedFaceParallel(SeetaImageData croppedFaceImage, long[] index);

    public native void Join();

    public native boolean Save(String path);

    public native boolean Load(String path);
}
