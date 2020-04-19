package com.cbd.android.repositories;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cbd.android.common.Constants;
import com.cbd.android.common.MyApp;
import com.cbd.android.common.Responses;
import com.cbd.android.models.Post;
import com.cbd.android.models.RequestNewPost;
import com.cbd.android.models.ResponseGeneric;
import com.cbd.android.models.ResponseListPost;
import com.cbd.android.retrofit.CBDAuthDisposalClient;
import com.cbd.android.retrofit.CBDAuthDisposalService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostRepository {
    private CBDAuthDisposalService cbdAuthDisposalService;
    private CBDAuthDisposalClient cbdAuthDisposalClient;
    private MutableLiveData<List<Post>> allPosts;

    public PostRepository() {
        cbdAuthDisposalClient = CBDAuthDisposalClient.getInstance();
        cbdAuthDisposalService = cbdAuthDisposalClient.getCBDAuthDisposalService();
        allPosts = getAllPosts();
    }

    public MutableLiveData<List<Post>> getAllPosts() {
        if (allPosts == null) {
            allPosts = new MutableLiveData<>();
        }
        Call<ResponseListPost> call = cbdAuthDisposalService.getAllPosts();
        call.enqueue(new Callback<ResponseListPost>() {
            @Override
            public void onResponse(Call<ResponseListPost> call, Response<ResponseListPost> response) {
                try {
                    Toast.makeText(MyApp.getContext(), response.body().getInfo().getMessage(), Toast.LENGTH_LONG).show();
                    if (response.body().getInfo().getCode() == Responses.OK_POSTS_RECUPERADOS_CORRECTAMENTE) {
                        allPosts.setValue(response.body().getPosts());
                    } else if (response.body().getInfo().getCode() == Responses.ERROR_TOKEN_INVALIDO) {
                        Toast.makeText(MyApp.getContext(), Constants.ERROR_TOKEN_INCORRECTO, Toast.LENGTH_LONG).show();
                        allPosts.setValue(null);
                    }
                } catch (Exception e) {
                    Toast.makeText(MyApp.getContext(), Constants.ERROR_INESPERADO, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseListPost> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), Constants.ERROR_COMUNICACION, Toast.LENGTH_LONG).show();
            }
        });

        return allPosts;
    }

    public void createPost(String title, String description, Double price) {
        RequestNewPost requestNewPost = new RequestNewPost(description, price, title);
        Call<ResponseGeneric> call = cbdAuthDisposalService.createPost(requestNewPost);

        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                try {
                    Toast.makeText(MyApp.getContext(), response.body().getInfo().getMessage(), Toast.LENGTH_LONG).show();
                    if (response.body().getInfo().getCode() == Responses.OK_PUBLICACION_CREADA_CORRECTAMENTE) {
                        getAllPosts();
                    } else if (response.body().getInfo().getCode() == Responses.ERROR_TOKEN_INVALIDO) {
                        Toast.makeText(MyApp.getContext(), Constants.ERROR_TOKEN_INCORRECTO, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(MyApp.getContext(), Constants.ERROR_INESPERADO, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), Constants.ERROR_COMUNICACION, Toast.LENGTH_LONG).show();
            }
        });
    }
}
