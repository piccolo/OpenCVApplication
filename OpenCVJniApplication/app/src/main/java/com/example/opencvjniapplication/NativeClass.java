package com.example.opencvjniapplication;

import org.opencv.core.Mat;

public class NativeClass {
    static {
        System.loadLibrary("hello");
        System.loadLibrary("opencv_java3");
    }

    public native static String getStringFromNative();
    public native static Mat convertNativeGray();
}
