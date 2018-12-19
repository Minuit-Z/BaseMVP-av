package ziye.inject.component;


import dagger.Component;
import ziye.inject.module.FragmentModule;
import ziye.inject.scope.FragmentLife;
import ziye.presenter.fragments.MainFragment;

/**
 * @Description:
 * @authors: utopia
 * @Create time: 17-1-3 下午2:02
 * @Update time: 17-1-3 下午2:02
 */
@FragmentLife
@Component(modules = FragmentModule.class, dependencies = ActivityComponent.class)
public interface FragmentComponent {

    void inject(MainFragment fragment);

}
