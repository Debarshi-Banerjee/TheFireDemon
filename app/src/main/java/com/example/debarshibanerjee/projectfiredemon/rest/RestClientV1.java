package com.example.debarshibanerjee.projectfiredemon.rest;


import com.example.debarshibanerjee.projectfiredemon.App;
import com.example.debarshibanerjee.projectfiredemon.Constants;
import com.example.debarshibanerjee.projectfiredemon.callbacks.TestCallback;
import com.example.debarshibanerjee.projectfiredemon.helpers.Utility;
import com.example.debarshibanerjee.projectfiredemon.pojo.Contributor;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by debarshibanerjee on 28/09/17.
 */

public class RestClientV1 {
    public static final String BASE_URL = "https://api.github.com";

    public static final String API_PREFIX = "";

    public static final String IMAGES_PREFIX = "images/";
    public static final String IMAGES_URL = BASE_URL + IMAGES_PREFIX;



    public static final String ERROR_MEETUP_STATUS = "status";
    private static final TypeAdapter<Boolean> booleanAsIntAdapter = new TypeAdapter<Boolean>() {
        @Override
        public void write(JsonWriter out, Boolean value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value);
            }
        }

        @Override
        public Boolean read(JsonReader in) throws IOException {
            JsonToken peek = in.peek();
            switch (peek) {
                case BOOLEAN:
                    return in.nextBoolean();
                case NULL:
                    in.nextNull();
                    return null;
                case NUMBER:
                    return in.nextInt() != 0;
                case STRING:
                    return in.nextString().equalsIgnoreCase("1");
                default:
                    throw new IllegalStateException("Expected BOOLEAN or NUMBER but was " + peek);
            }
        }
    };
    private static RestClientV1 sInstance;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private ApiServiceV1 mApiService;


    private RestClientV1() {
        File cacheDir = App.getAppContext().getCacheDir();
        Cache cache = new Cache(cacheDir, Constants.CACHE_SIZE);


        OkHttpClient.Builder okHttpBuilder = new OkHttpClient().newBuilder();
        okHttpBuilder.cache(cache);
        okHttpBuilder.connectTimeout(10, TimeUnit.SECONDS);

        if (Utility.isDebug()) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpBuilder.addInterceptor(interceptor);
        }

        okHttpBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();

                requestBuilder.addHeader("Accept", "application/json");
                requestBuilder.addHeader("Cache-Control", "public, max-age=" + 600);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        // We are using EventBus to deliver events to activity/fragments on Main Thread
        // Therefore, we want all processing in the callbacks to happen on a thread
        // from the threadpool instead of main thread. Retrofit calls the callback
        // on main thread by default
        ExecutorService backgroundExecutor = Executors.newCachedThreadPool();
        okHttpBuilder.dispatcher(new Dispatcher(backgroundExecutor));


        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss z")
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        // In DBFlow POJOs inherit from ModelAdapter. We don't
                        // fields of that class to get serialized or deserialized
                        return f.getDeclaredClass().equals(ModelAdapter.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .registerTypeAdapter(boolean.class, booleanAsIntAdapter)
                .registerTypeAdapter(Boolean.class, booleanAsIntAdapter)
                .create();


        OkHttpClient okHttpClient = okHttpBuilder.build();
        String endpoint = BASE_URL + API_PREFIX;

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.baseUrl(endpoint);
        retrofitBuilder.client(okHttpClient);
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create(gson));
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        Retrofit retrofit = retrofitBuilder.build();

        mApiService = retrofit.create(ApiServiceV1.class);
    }

    public static synchronized RestClientV1 getInstance() {
        if (sInstance == null) {
            sInstance = new RestClientV1();
        }
        return sInstance;
    }

    public void getGitHubRepoContributors(String owner, String repo) {
        mApiService.repoContributors(owner, repo).enqueue(new TestCallback());
    }

    public Single<List<Contributor>> getGitHubRepoContributorsRx(String owner, String repo) {
        return mApiService.repoContributorsRx(owner, repo);
    }


    public void foobar(String owner, String repo, Consumer<List<Contributor>> consumer) {
        mCompositeDisposable.add(RestClientV1.getInstance().getGitHubRepoContributorsRx("square", "retrofit")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<List<Contributor>, List<Contributor>>() {
                    @Override
                    public List<Contributor> apply(List<Contributor> contributors) throws Exception {
                        return contributors;
                    }
                })
                .subscribe(consumer));
    }
}
