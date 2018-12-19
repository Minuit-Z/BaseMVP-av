package ziye.base;

import android.app.Activity;
import android.os.Bundle;

import ziye.MyApplication;
import ziye.basemvp.mvp.base.IView;
import ziye.basemvp.mvp.presenter.BaseActivityPresenter;
import ziye.inject.component.ActivityComponent;
import ziye.inject.component.DaggerActivityComponent;
import ziye.inject.module.ActivityModule;
import ziye.utils.JUtils;
import ziye.utils.app.AppManager;

/**
 * Created by Administrator on 2018/12/18 0018.
 */

public abstract class BaseActivity<T extends IView> extends BaseActivityPresenter<T> {

    protected final String TAG = getClass().getSimpleName();
    protected ActivityComponent activityComponent;


    @Override
    public void inCreat(Bundle savedInstanceState) {
        initInject();
        AppManager.getAppManager().addActivity(this);
        doOnCreate();
    }

    //为fragment提供activityComponent
    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    public void initInject() {
        MyApplication app = (MyApplication) MyApplication.getMyApplication();
        activityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(app.getAppComponent())
                .build();
    }

    @Override
    public void inDestory() {
        JUtils.closeInputMethod(this);
//        RefWatcher refWatcher = MoorApplication.getRefWatcher();
//        refWatcher.watch(this);
        AppManager.getAppManager().finishActivity(this);//activity退出时将activity移出栈
    }

    @Override
    public void initTheme() {

    }

    public abstract void doOnCreate();
}
