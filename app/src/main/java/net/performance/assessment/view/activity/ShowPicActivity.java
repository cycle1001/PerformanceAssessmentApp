package net.performance.assessment.view.activity;

import android.content.Intent;
import android.view.View;

import com.squareup.picasso.Picasso;

import net.performance.assessment.R;
import net.performance.assessment.view.widget.SmoothImageView;

public class ShowPicActivity extends BaseActivity {

    public static final String PHOTO_SOURCE_ID = "PHOTO_SOURCE_ID";
    public static final String PHOTO_SELECT_X_TAG = "PHOTO_SELECT_X_TAG";
    public static final String PHOTO_SELECT_Y_TAG = "PHOTO_SELECT_Y_TAG";
    public static final String PHOTO_SELECT_W_TAG = "PHOTO_SELECT_W_TAG";
    public static final String PHOTO_SELECT_H_TAG = "PHOTO_SELECT_H_TAG";

    private int locationX;
    private int locationY;
    private int locationW;
    private int locationH;
    private String url = "";

    private SmoothImageView smoothImageView;

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_pic_show);
    }

    @Override
    protected void initView() {
        super.initView();
        smoothImageView = findViewById(R.id.smoothImageView);
    }

    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra(PHOTO_SOURCE_ID);

            locationX = intent.getIntExtra(PHOTO_SELECT_X_TAG, 0);
            locationY = intent.getIntExtra(PHOTO_SELECT_Y_TAG, 0);
            locationW = intent.getIntExtra(PHOTO_SELECT_W_TAG, 0);
            locationH = intent.getIntExtra(PHOTO_SELECT_H_TAG, 0);
        }

        smoothImageView.setOriginalInfo(locationW, locationH, locationX, locationY);
        smoothImageView.transformIn();

        Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_default_pic)
                .error(R.drawable.ic_default_pic)
                .into(smoothImageView);
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        smoothImageView.setOnTransformListener(new SmoothImageView.TransformListener() {
            @Override
            public void onTransformComplete(int mode) {
                if (mode == 2) {
                    ShowPicActivity.this.finish();
                    ShowPicActivity.this.overridePendingTransition(0, 0);
                }
            }
        });

        smoothImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPicActivity.this.finish();
                ShowPicActivity.this.overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
