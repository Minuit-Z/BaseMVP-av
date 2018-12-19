package ziye.inject.module;

import android.util.Log;

import com.facebook.imagepipeline.core.ImagePipelineConfig;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ziye.network.manager.SubjectService;
import ziye.utils.URL;

/**
 * @Description:
 * @authors: utopia
 * @Create time: 16-12-26 下午4:38
 * @Update time: 16-12-26 下午4:38
 */
@Module
public class MoorNetModule {
    public static ImagePipelineConfig config;
    public static OkHttpClient okHttpClient;


    @Provides
    Interceptor providesIntercepter() {
        return chain -> {
            Request request = chain.request();
            Response response = chain.proceed(request);

            //兼容接口重定向请求不返回302缺陷
            int code = response.code();
            if (response.header("Location") != null) {
                code = 302;
            }
            if (code == 401) {
                code = 200;
            }
            //请求文件大小限制为20MB,拦截大文件请求
            if (response.header("Content-Length") != null) {
                int total = Integer.parseInt(response.header("Content-Length"));
//                if (total > 20971520) {//限定文件大小不可超过20MB
//                    code=413;
//                }
            }

            /** 设置max-age为3分钟之后，这3分钟之内不管有没有网, 都读缓存。
             * max-stale设置为1天，网络未连接的情况下设置缓存时间为1天 */
            CacheControl cacheControl;
//            if (!JUtils.isNetWorkAvilable()) {
//                cacheControl = CacheControl.FORCE_CACHE;//无网络时，强制使用缓存
//            } else {
//                cacheControl = CacheControl.FORCE_NETWORK;//有网络时，不使用缓存
//            }

            return response.newBuilder()
//                    .header("Cache-Control", cacheControl.toString())
                    .code(code)
                    .removeHeader("Pragma")
                    .build();
        };
    }

    //打印retrofit日志
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor((String message) -> {
        Log.v("retrofit", message);//这个是网络日志模块，切勿删除
    }).setLevel(HttpLoggingInterceptor.Level.BODY);

//    @Provides
//    Cache providesCache() {
//        File httpCacheFile = FileUtils2.getDiskCacheDir("response");
//        return new Cache(httpCacheFile, 1024 * 100 * 1024);
//    }
//
//    @Provides
//    CommonService providesTestService(Interceptor interceptor, Cache cache) {
//        return getRetrofit(interceptor, cache).create(CommonService.class);
//    }
//
//    @Provides
//    PointService providesPointService(Interceptor interceptor, Cache cache) {
//        return getRetrofit(interceptor, cache).create(PointService.class);
//    }
//
//    @Provides
//    CircleService providesCircleService(Interceptor interceptor, Cache cache) {
//        return getRetrofit(interceptor, cache).create(CircleService.class);
//    }
//
//    @Provides
//    SubjectService providesSubjectService(Interceptor interceptor, Cache cache) {
//        return getRetrofit(interceptor, cache).create(SubjectService.class);
//    }
//
//    @Provides
//    ResourceService providesResourceService(Interceptor interceptor, Cache cache) {
//        return getRetrofit(interceptor, cache).create(ResourceService.class);
//    }
//
//    @Provides
//    NoteService providesNoteService(Interceptor interceptor, Cache cache) {
//        return getRetrofit(interceptor, cache).create(NoteService.class);
//    }
//
//    @Provides
//    MessageService providesMessageService(Interceptor interceptor, Cache cache) {
//        return getRetrofit(interceptor, cache).create(MessageService.class);
//    }

    @Provides
    SubjectService providerSubjectManager(){
        return getRetrofit().create(SubjectService.class);
    }

//    private Retrofit getRetrofit(Interceptor interceptor, Cache cache) {
    private Retrofit getRetrofit() {
        OkHttpClient client = getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL.getBaseUrl())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    //CookieJar是用于保存Cookie的
    public static class LocalCookieJar implements CookieJar {
        static LocalCookieJar cookieJar;
        private List<Cookie> cookies = new ArrayList<>();

        public static LocalCookieJar getInstance() {
            if (cookieJar == null) {
                cookieJar = new LocalCookieJar();
            }
            return cookieJar;
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl arg0) {
            if (cookies != null) {
                return cookies;
            }
            return new ArrayList<>();
        }

        @Override
        public void saveFromResponse(HttpUrl arg0, List<Cookie> cookie) {
            if (cookies == null) {
                cookies = cookie;
            } else {
                ArrayList<Cookie> cookiess = new ArrayList<>(cookie);

                for (Cookie c : cookiess) {
                    if (!cookies.contains(c)) {
                        cookies.add(c);
                    }
                }
            }
        }

        public List<Cookie> getCookies() {
            return cookies;
        }

        public void cleanUpCookies() {
            if (cookies != null)
                cookies.clear();
        }
    }

    /**
     * 忽略ssl验证
     */
    public OkHttpClient getUnsafeOkHttpClient() {
        try {
            final X509TrustManager xtm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    X509Certificate[] x509Certificates = new X509Certificate[0];
                    return x509Certificates;
                }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{xtm}, new java.security.SecureRandom());
            SSLSocketFactory sslSocketFactory = sc.getSocketFactory();
            HostnameVerifier hostnameVerifier = (String hostname, SSLSession session) -> true;

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(hostnameVerifier);
            builder.cookieJar(LocalCookieJar.getInstance());   //为OkHttp设置自动携带Cookie的功能

            builder.addInterceptor(providesIntercepter());
            builder.addInterceptor(new HttpLoggingInterceptor((String message) ->
                    Log.v("Interceptor -> ", message)
            ));
//            builder.addNetworkInterceptor(interceptor);
//            builder.cache(providesCache());

            //if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor); //加日志
            //}

            builder.connectTimeout(5, TimeUnit.SECONDS);//设置超时
            builder.readTimeout(5, TimeUnit.SECONDS);
            builder.writeTimeout(5, TimeUnit.SECONDS);
            builder.retryOnConnectionFailure(true);//错误重连
            okHttpClient = builder.build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
