package ziye.inject.module;

import android.content.Context;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ziye.MyApplication;
import ziye.inject.qualifier.ContextType;

/**
 * Created by Zane on 16/2/14.
 */
@Module
public class ApplicationModule {

    private MyApplication mApplication;

    public ApplicationModule(MyApplication application){
        mApplication = application;
    }

    @Provides
    @Singleton
    MyApplication providesApplication(){
        return mApplication;
    }

    @Provides
    @Singleton
    @ContextType("application")
    Context providesContext(){
        return MyApplication.getMyApplication();
    }
}
