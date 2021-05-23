package com.example.sentimentalrecommender.modules;

import com.example.sentimentalrecommender.objects.EmotionResponseItem;

import javax.inject.Singleton;

import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Singleton
public class ApiModule {
    private static final String parallelDotsUrl = "https://apis.paralleldots.com/v4/";
    private static final String PARALLEL_DOTS_API_KEY = "";

    private static final String translateUrl = "https://systran-systran-platform-for-language-processing-v1.p.rapidapi.com/translation/text/translate";
    private static final String TRANSLATE_API_KEY = "";

    private static final String movieUrl = "https://study-ui.herokuapp.com";

    private static final OkHttpClient client = new OkHttpClient();

    public static String getTranslation(String text, String languageCode) {

        HttpUrl.Builder httpBuilder = HttpUrl
                .parse(translateUrl)
                .newBuilder()
                .addQueryParameter("source", languageCode)
                .addQueryParameter("target", "en")
                .addQueryParameter("input", text);

        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .get()
                .addHeader("x-rapidapi-key", TRANSLATE_API_KEY)
                .addHeader("x-rapidapi-host", "systran-systran-platform-for-language-processing-v1.p.rapidapi.com")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getEmotion(String text) {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("api_key", PARALLEL_DOTS_API_KEY)
                .addFormDataPart("text", text)
                .addFormDataPart("lang_code", "en")
                .build();

        Request request = new Request.Builder()
                .url(parallelDotsUrl + "emotion")
                .post(body)
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getMovie(EmotionResponseItem emotion) {
        HttpUrl.Builder httpBuilder = HttpUrl
                .parse(movieUrl)
                .newBuilder()
                .addQueryParameter("Excited", String.valueOf(emotion.Excited))
                .addQueryParameter("Angry", String.valueOf(emotion.Angry))
                .addQueryParameter("Bored", String.valueOf(emotion.Bored))
                .addQueryParameter("Fear", String.valueOf(emotion.Fear))
                .addQueryParameter("Sad", String.valueOf(emotion.Sad))
                .addQueryParameter("Happy", String.valueOf(emotion.Happy));

        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}