package ziye;

import android.app.Application;

import ziye.inject.component.ApplicationComponent;
import ziye.inject.component.DaggerApplicationComponent;
import ziye.inject.module.ApplicationModule;
import ziye.inject.module.MoorNetModule;

/**
 * Created by Administrator on 2018/12/17 0017.
 */

public class MyApplication extends Application {
    private static MyApplication application;


    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
    }

    public static Application getMyApplication(){
        return application;
    }

    public ApplicationComponent getAppComponent() {
        return DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .moorNetModule(new MoorNetModule())
                .build();
    }
}
