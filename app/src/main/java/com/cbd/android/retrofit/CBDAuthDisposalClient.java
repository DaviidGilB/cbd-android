package com.cbd.android.retrofit;

import com.cbd.android.common.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CBDAuthDisposalClient {
    private static CBDAuthDisposalClient instance = null;
    private CBDAuthDisposalService cbdAuthDisposalService;
    private Retrofit retrofit;

    public CBDAuthDisposalClient() {
        // Interceptor para incluir la autorizacion en la cabecera de la peticion
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new AuthInterceptor());
        OkHttpClient cliente = okHttpClientBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(cliente)
                .build();

        cbdAuthDisposalService = retrofit.create(CBDAuthDisposalService.class);
    }

    public static CBDAuthDisposalClient getInstance() {
        if (instance == null) {
            instance = new CBDAuthDisposalClient();
        }
        return instance;
    }

    public CBDAuthDisposalService getCBDAuthDisposalService() {
        return cbdAuthDisposalService;
    }
}
