package ru.ilyamodder.marvel.network;

import android.util.Log;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import ru.ilyamodder.marvel.BuildConfig;

/**
 * Created by ilya on 08.11.16.
 */

public class ApiKeyInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        String timestamp = String.valueOf(new Date().getTime() * 1000);
        String hash;
        try {
            byte[] digest = MessageDigest.getInstance("MD5")
                    .digest((timestamp + BuildConfig.PRIVATE_KEY + BuildConfig.PUBLIC_KEY).getBytes("UTF-8"));
            hash = new BigInteger(1, digest).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        HttpUrl url = request.url().newBuilder()
                .addQueryParameter("ts", timestamp)
                .addQueryParameter("hash", hash)
                .addQueryParameter("apikey", BuildConfig.PUBLIC_KEY)
                .build();
        Log.d("uri", url.toString());
        request = request.newBuilder().url(url).build();
        return chain.proceed(request);
    }
}
