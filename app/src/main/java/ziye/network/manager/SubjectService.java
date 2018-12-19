package ziye.network.manager;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @Description:课题相关api
 * @authors: utopia
 * @Create time: 16-12-26 下午4:27
 * @Update time: 16-12-26 下午4:27
 */
public interface SubjectService {

    @GET("static/picture_list.txt")
    Observable<Object> getSplash(@Query("client") String client, @Query("version") String version, @Query("time") Long time, @Query("device_id") String deviceId);


}
