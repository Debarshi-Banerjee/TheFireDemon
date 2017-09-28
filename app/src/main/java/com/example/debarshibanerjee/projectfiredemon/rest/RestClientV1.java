package com.example.debarshibanerjee.projectfiredemon.rest;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;

import com.example.debarshibanerjee.projectfiredemon.App;
import com.example.debarshibanerjee.projectfiredemon.Constants;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by debarshibanerjee on 28/09/17.
 */

public class RestClientV1 {
    public static final String BASE_URL = "http://192.168.1.56:9998/";

    public static final String API_PREFIX = "api/v1/";

    public static final String IMAGES_PREFIX = "images/";
    public static final String IMAGES_URL = BASE_URL + IMAGES_PREFIX;

    public static final String ERROR_CODE = "errorcode";
    public static final String ERROR_MESSAGES = "errormessages";

    public static final int ERROR_UNKNOWN = -1;

    public static final int ERROR_VERSION_MISMATCH = 666;
    public static final int ERROR_PHONE_ALREADY_EXISTS = 677;
    public static final int ERROR_CODE_MEETUP_OVER = 440;

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
    private ApiServiceV1 mApiService;


    private RestClientV1() {
        File cacheDir = App.getAppContext().getCacheDir();
        Cache cache = new Cache(cacheDir, Constants.CACHE_SIZE);

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setCache(cache);
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);

        String endpoint = BASE_URL + API_PREFIX;
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(endpoint);

        if (Utility.isDebug()) {
            builder.setLogLevel(RestAdapter.LogLevel.FULL);
        }

        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");

                request.addHeader("Cache-Control", "public, max-age=" + 600);
            }
        });

        // We are using EventBus to deliver events to activity/fragments on Main Thread
        // Therefore, we want all processing in the callbacks to happen on a thread
        // from the threadpool instead of main thread. Retrofit calls the callback
        // on main thread by default
        ExecutorService backgroundExecutor = Executors.newCachedThreadPool();
        builder.setExecutors(backgroundExecutor, backgroundExecutor);

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
        builder.setConverter(new GsonConverter(gson));

        builder.setClient(new OkClient(okHttpClient));

        RestAdapter adapter = builder.build();
        mApiService = adapter.create(ApiServiceV1.class);
    }

    public static synchronized RestClientV1 getInstance() {
        if (sInstance == null) {
            sInstance = new RestClientV1();
        }

        return sInstance;
    }


}
