package ziye.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import ziye.basemvp.mvp.base.IView;
import ziye.basemvp.mvp.presenter.BaseFragmentPresenter;
import ziye.inject.component.DaggerFragmentComponent;
import ziye.inject.module.FragmentModule;

/**
 * Created by Administrator on 2018/12/18 0018.
 */

public abstract class BaseFragment<T extends IView> extends BaseFragmentPresenter<T> {
    public DaggerFragmentComponent.Builder builder;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        builder = DaggerFragmentComponent.builder().fragmentModule(new FragmentModule());
        completeInject();
        onViewHasCreated(view, savedInstanceState);
    }

    protected abstract void completeInject();

    public abstract void onViewHasCreated(View view, @Nullable Bundle savedInstanceState);

}
