package ziye.network.remote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.SystemClock;
import android.telephony.TelephonyManager;

import javax.inject.Inject;

import io.reactivex.Observable;
import ziye.inject.qualifier.ContextType;
import ziye.network.manager.SubjectService;

/**
 * Created by Administrator on 2018/12/17 0017.
 */

public class SubjectManager {

    private SubjectService service;
    private Context context;

    @Inject
    public SubjectManager(@ContextType("application") Context context, SubjectService service) {
        this.context = context;
        this.service = service;
    }

    public Observable<Object> test(){
        return service.getSplash("android","1.3.0",
                SystemClock.currentThreadTimeMillis(),"idididididid");
    }

}
