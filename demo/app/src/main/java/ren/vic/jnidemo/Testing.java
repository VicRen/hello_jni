package ren.vic.jnidemo;

import android.util.Log;

public class Testing {

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native int helloFromJNI();

    protected void Test() {
        Log.d("Testing", "Test: ");
    }
}
