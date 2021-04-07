package ren.vic.jnidemo;

public class Testing {

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native int helloFromJNI();
}
