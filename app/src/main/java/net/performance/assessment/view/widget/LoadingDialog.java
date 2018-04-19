package net.performance.assessment.view.widget;

import net.performance.assessment.R;
import net.performance.assessment.utils.LogUtils;
import net.performance.assessment.view.widget.loading.Circle;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

public class LoadingDialog extends Dialog {

    private Circle mCircleDrawable;

    private String loadingTipContent = "";

    private TextView tvLoadingTips;

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.style_dialog);
    }

    public LoadingDialog(Context context, String warnTipContent) {
        super(context, R.style.style_dialog);
        this.loadingTipContent = warnTipContent;
    }

    public LoadingDialog(Context context, int tipResourcesId) {
        super(context, R.style.style_dialog);
        this.loadingTipContent = context.getResources().getString(tipResourcesId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        setCanceledOnTouchOutside(false);

        tvLoadingTips = (TextView) this.findViewById(R.id.tv_loading_content);
        mCircleDrawable = new Circle();
        mCircleDrawable.setBounds(0, 0, 100, 100);
        mCircleDrawable.setColor(Color.WHITE);
        tvLoadingTips.setCompoundDrawables(null, mCircleDrawable, null, null);
        mCircleDrawable.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCircleDrawable != null) {
            mCircleDrawable.stop();
        }
    }

    public void setLoadingTipContent(String pLoadingTipContent)
    {
        loadingTipContent = pLoadingTipContent;
    }

    @Override
    public void show( )
    {
        if ( tvLoadingTips != null )
        {
            tvLoadingTips.setText(loadingTipContent);
        }
        else {
            LogUtils.v( "tvLoadingTips null" );
        }
        super.show( );
    }
}
