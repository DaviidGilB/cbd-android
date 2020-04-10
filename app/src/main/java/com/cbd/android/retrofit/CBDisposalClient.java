package com.cbd.android.retrofit;

import com.cbd.android.common.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CBDisposalClient {
    private static CBDisposalClient instance = null;
    private CBDisposalService cbdisposalService;
    private Retrofit retrofit;

    public CBDisposalClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        cbdisposalService = retrofit.create(CBDisposalService.class);
    }

    public static CBDisposalClient getInstance() {
        if (instance == null) {
            instance = new CBDisposalClient();
        }
        return instance;
    }

    public CBDisposalService getCBDisposalService() {
        return cbdisposalService;
    }
}
