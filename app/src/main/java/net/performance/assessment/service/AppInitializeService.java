package net.performance.assessment.service;

import net.performance.assessment.utils.CustomPushManager;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

/**
 * 应用冷启动初始化模块的服务类
 */
public class AppInitializeService extends IntentService {
    private static final String ACTION_INIT_WHEN_APP_CREATE = "net.fieldwork.assessment.system.action.INIT";

    public AppInitializeService() {
        super("AppInitializeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT_WHEN_APP_CREATE.equals(action)) {
                performInit();
            }
        }
    }

    private void performInit( ) {
        //初始化信鸽的配置
        Context context = getApplicationContext( );
        CustomPushManager.getInstance( ).enableDebug( context , true );
        CustomPushManager.getInstance().register( context );
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, AppInitializeService.class);
        intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
        context.startService(intent);
    }

}
