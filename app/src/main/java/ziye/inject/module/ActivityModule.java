package ziye.inject.module;

import android.app.Activity;
import android.content.Context;


import dagger.Module;
import dagger.Provides;
import ziye.inject.qualifier.ContextType;
import ziye.inject.scope.ActivityLife;

/**
 * Created by Zane on 16/2/14.
 */
@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity activity){
        this.activity = activity;
    }

    @Provides
    @ActivityLife
    Activity providesActivity(){
        return activity;
    }

    @Provides
    @ActivityLife
    @ContextType("activity")
    Context providesContext(){
        return activity;
    }

}
