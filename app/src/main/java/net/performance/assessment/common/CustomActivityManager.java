
package net.performance.assessment.common;


import net.performance.assessment.view.activity.MainActivity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * 自定义Activity管理类
 *
 * @author fanzhibin
 */
public class CustomActivityManager {
    private LinkedList<Activity> mActs;
    private static volatile CustomActivityManager instance = null;

    private CustomActivityManager() {
        mActs = new LinkedList<>();
    }

    public static CustomActivityManager getInstance() {
        if (instance == null) {
            synchronized (CustomActivityManager.class) {
                if (instance == null) {
                    instance = new CustomActivityManager();
                }
            }
        }
        return instance;
    }

    public void addActivity(Activity act) {
        synchronized (CustomActivityManager.this) {
            mActs.addFirst(act);
        }
    }

    public void removeActivity(Activity act) {
        synchronized (CustomActivityManager.this) {
            if (mActs != null && mActs.indexOf(act) >= 0) {
                mActs.remove(act);
            }
        }
    }

    /**
     * 当打开activity使用标志:Intent.FLAG_ACTIVITY_REORDER_TO_FRONT,
     * 需要把CloseActivity对应的activity移动到顶端
     */
    public void reorderActivityToFront(Activity act) {
        synchronized (CustomActivityManager.this) {
            if (mActs != null && mActs.indexOf(act) > 0) {
                mActs.remove(act);
                mActs.addFirst(act);
            }
        }
    }

    public Activity getTopActivity() {
        synchronized (CustomActivityManager.this) {
            return (mActs == null || mActs.size() <= 0) ? null : mActs.get(0);
        }
    }

    public Activity getSecondActivity() {
        synchronized (CustomActivityManager.this) {
            return (mActs == null || mActs.size() <= 1) ? null : mActs.get(1);
        }
    }

    public void close() {
        synchronized (CustomActivityManager.this) {
            Activity act;
            while (mActs.size() != 0) {
                act = mActs.poll();
                act.finish();
            }
        }
    }

    /**
     * 关闭其他activity，唯独排除activityClass指定的activity
     *
     * @param activityClass
     */
    public void closeExcept(Class<?> activityClass) {
        synchronized (CustomActivityManager.this) {
            Activity act;
            Iterator<Activity> activityIterator = mActs.iterator();
            while (activityIterator.hasNext()) {
                act = activityIterator.next();
                if (!act.getClass().getName().equals(activityClass.getName())) {
                    act.finish();
                    activityIterator.remove();
                }
            }
        }
    }

    /**
     * 关闭activityClass指定的activity
     *
     * @param activityClass
     */
    public void closeTarget(Class<?> activityClass) {
        synchronized (CustomActivityManager.this) {
            Activity act;
            Iterator<Activity> activityIterator = mActs.iterator();
            while (activityIterator.hasNext()) {
                act = activityIterator.next();
                if (act.getClass().getName().equals(activityClass.getName())) {
                    act.finish();
                    activityIterator.remove();
                }
            }
        }
    }

    /**
     * @Title: backToHomePageActivity
     * @Description: 回到首页的界面
     */
    public void backToHomePageActivity() {
        /*synchronized ( this )
		{
			while ( mActs.size( ) > 0 )
			{
				Activity activity = mActs.getFirst( );
				if ( activity instanceof TestFluxActivity )
				{
					break;
				}
				else
				{
					activity.finish( );
					mActs.remove( activity );
				}
			}
		}*/
    }

    /**
     * @Title: backToMainActivity
     * @Description: 回到首页的界面
     */
    public void backToMainActivity( ) {
		synchronized ( this )
		{
			while ( mActs.size( ) > 0 )
			{
				Activity activity = mActs.getFirst( );
				if ( activity instanceof MainActivity )
				{
					break;
				}
				else
				{
					activity.finish( );
					mActs.remove( activity );
				}
			}
		}
    }

    public int getSize() {
        return mActs.size();
    }

    public ArrayList<Activity> getTargetActivity(Class<?> activityClass) {
        ArrayList<Activity> activities = new ArrayList<>();
        synchronized (CustomActivityManager.this) {
            Activity act;
            int size = mActs.size();
            for (int i = 0; i < size; i++) {
                act = mActs.get(i);
                if (act.getClass().getName().equals(activityClass.getName())) {
                    activities.add(act);
                }
            }
        }

        return activities;
    }

    /**
     * isIncludeTargetActivity
     *
     * @param activityClass
     * @return
     * @Description: 查看某个Activity是否在保存的列表中
     */
    public boolean isIncludeTargetActivity(Class<?> activityClass) {
        synchronized (CustomActivityManager.this) {
            Activity act;
            int size = mActs.size();
            for (int i = 0; i < size; i++) {
                act = mActs.get(i);
                if (act.getClass().getName().equals(activityClass.getName())) {
                    return true;
                }
            }
        }

        return false;
    }
}
