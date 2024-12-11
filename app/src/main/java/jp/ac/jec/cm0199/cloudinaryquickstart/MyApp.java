package jp.ac.jec.cm0199.cloudinaryquickstart;

import android.app.Application;

import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;

import java.util.HashMap;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Cloudinary configuration
        HashMap<String, Object> config = new HashMap<>();
        config.put("cloud_name", BuildConfig.CLOUD_NAME);
        config.put("secure", true);
        // initの呼び出しはアプリで一度のみ
        // 参考: https://cloudinary.com/documentation/android_integration#setup
        MediaManager.init(this, config);

        MediaManager.get().getCloudinary().url().transformation(new Transformation().width(300).crop("scale"));
    }
}
