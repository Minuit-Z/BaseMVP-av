package ziye.inject.component;

import android.content.Context;


import javax.inject.Singleton;

import dagger.Component;
import ziye.inject.module.ApplicationModule;
import ziye.inject.module.MoorNetModule;
import ziye.inject.qualifier.ContextType;
import ziye.network.remote.SubjectManager;

@Singleton
@Component(modules = {ApplicationModule.class, MoorNetModule.class})
public interface ApplicationComponent {
    @ContextType("application")Context context();

//    CommonManager datamanger();
    SubjectManager subjectManager();
//    ResourceManager resourceManager();
//    NoteManager noteManager();
//    CircleManager circleManager();
//    MessageManager messageManager();
//    PointManager pointManager();
}
