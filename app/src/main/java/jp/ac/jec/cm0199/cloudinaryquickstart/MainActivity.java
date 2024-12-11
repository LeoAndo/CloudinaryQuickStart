package jp.ac.jec.cm0199.cloudinaryquickstart;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ImageView uploadedImageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        uploadedImageview = findViewById(R.id.uploaded_imageview);

        uploadImage();
    }

    private void uploadImage() {
        Uri uri = Uri.parse("android.resource://jp.ac.jec.cm0199.cloudinaryquickstart/drawable/cloudinary_logo");
        MediaManager.get().upload(uri).unsigned(BuildConfig.UPLOAD_PRESET).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
                Log.d("Cloudinary Quickstart", "Upload start requestId: " + requestId);
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                Log.d("Cloudinary Quickstart", "Upload progress");
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                String url = (String) resultData.get("secure_url");
                Log.d("Cloudinary Quickstart", "Upload success url: " + url);
                Glide.with(getApplicationContext()).load(url).into(uploadedImageview);
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                Log.d("Cloudinary Quickstart", "Upload failed: " + error.getDescription());
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                Log.d("Cloudinary Quickstart", "Upload rescheduled: " + error.getDescription());
            }
        }).dispatch();
    }
}