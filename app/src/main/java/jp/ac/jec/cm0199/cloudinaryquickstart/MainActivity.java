package jp.ac.jec.cm0199.cloudinaryquickstart;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String IMAGE_MIMETYPE = "image/*";
    /**
     * ActivityResultLauncher to handle the result of the image selection.
     */
    private final ActivityResultLauncher<String> getContent =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri == null) {
                    Log.d("Cloudinary Quickstart", "ファイルを選択せずにアプリに戻りました");
                    return;
                }
                uploadImage(this, uri);
            });

    private ImageView uploadedImageview;
    private ShapeableImageView avatarImageView;

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

        // 画像表示用のImageViewを取得する.
        uploadedImageview = findViewById(R.id.uploaded_imageview);
        avatarImageView = findViewById(R.id.avatar_imageview);

        // アバター表示用に完全な円形に設定する.(これ以外にもstyleで設定する方法などある.)
        ShapeAppearanceModel shapeAppearanceModel = ShapeAppearanceModel.builder()
                .setAllCornerSizes(ShapeAppearanceModel.PILL)
                .build();
        avatarImageView.setShapeAppearanceModel(shapeAppearanceModel);

        // 画像選択ボタンのクリックイベントを設定する.
        findViewById(R.id.upload_button).setOnClickListener(v -> {
            getContent.launch(IMAGE_MIMETYPE);
        });
    }

    /**
     * Uploads the selected image to Cloudinary.
     *
     * @param context The context of the application.
     * @param uri     The URI of the selected image.
     */
    private void uploadImage(@NonNull final Context context, @NonNull final Uri uri) {
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
                // 画像を表示する.
                Glide.with(context).load(url).into(uploadedImageview);
                Glide.with(context).load(url).into(avatarImageView);
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