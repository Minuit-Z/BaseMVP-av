package ziye.inject.component;


import android.app.Activity;

import dagger.Component;
import ziye.inject.module.ActivityModule;
import ziye.inject.scope.ActivityLife;
import ziye.network.remote.SubjectManager;
import ziye.presenter.activities.MainActivity;

@ActivityLife
@Component(modules = ActivityModule.class, dependencies = ApplicationComponent.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    Activity provideActivity();

//    CommonManager datamanger();
//
    SubjectManager subjectManager();
//
//    ResourceManager resourceManager();
//
//    NoteManager noteManager();
//
//    MessageManager messageManager();
//
//    CircleManager circleManager();
//
//    PointManager pointManager();
}

