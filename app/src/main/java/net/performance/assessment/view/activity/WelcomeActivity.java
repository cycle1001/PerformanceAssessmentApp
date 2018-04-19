package net.performance.assessment.view.activity;

import net.performance.assessment.R;
import net.performance.assessment.common.CustomPermissionManager;
import net.performance.assessment.interfaces.CheckPermissionCallback;
import net.performance.assessment.utils.CommonUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * 启动欢迎页面
 */
public class WelcomeActivity extends Activity {
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        redirectTo( );
        mActivity = this;
        CustomPermissionManager.getInstance().init(this);
        CustomPermissionManager.getInstance().request(mCheckPermissionCallback);
    }

    private void redirectTo() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        //finish( );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CustomPermissionManager.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CustomPermissionManager.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        CustomPermissionManager.getInstance().release();
        super.onDestroy();
    }

    private CheckPermissionCallback mCheckPermissionCallback = new CheckPermissionCallback() {
        @Override
        public void onPermissionAllGranted() {
            redirectTo();
        }

        @Override
        public void onPermissionDeniedTemporarily() {
            CustomPermissionManager.getInstance().release();
            CustomPermissionManager.getInstance().init(mActivity);
            CustomPermissionManager.getInstance().request(mCheckPermissionCallback);
        }

        @Override
        public void onPermissionDeniedPermantly() {
            //设置权限，重启应用
            AlertDialog builder = new AlertDialog.Builder(mActivity).setTitle(
                    getString(R.string.label_title_prompt)).create();
            builder.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.btn_confirm),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CommonUtils.exitApp();
                        }
                    });
            builder.setMessage(getString(R.string.msg_set_permission_and_restart));
            builder.show();
        }
    };
}
