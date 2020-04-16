package com.cbd.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbd.android.activities.LoginActivity;
import com.cbd.android.activities.MainActivity;
import com.cbd.android.common.Constants;
import com.cbd.android.common.Responses;
import com.cbd.android.models.Post;
import com.cbd.android.models.ResponseListPost;
import com.cbd.android.retrofit.CBDAuthDisposalClient;
import com.cbd.android.retrofit.CBDAuthDisposalService;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Post> postList;
    private CBDAuthDisposalService cbdAuthDisposalService;
    private CBDAuthDisposalClient cbdAuthDisposalClient;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PostListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PostListFragment newInstance(int columnCount) {
        PostListFragment fragment = new PostListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            retrofitInit();
            loadPostData();
        }
        return view;
    }

    public void retrofitInit() {
        cbdAuthDisposalClient = CBDAuthDisposalClient.getInstance();
        cbdAuthDisposalService = cbdAuthDisposalClient.getCBDAuthDisposalService();
    }

    private void loadPostData() {
        Call<ResponseListPost> call = cbdAuthDisposalService.getAllPosts();
        call.enqueue(new Callback<ResponseListPost>() {
            @Override
            public void onResponse(Call<ResponseListPost> call, Response<ResponseListPost> response) {
                try {
                    Toast.makeText(getActivity(), response.body().getInfo().getMessage(), Toast.LENGTH_LONG).show();
                    if (response.body().getInfo().getCode() == Responses.OK_POSTS_RECUPERADOS_CORRECTAMENTE) {
                        postList = response.body().getPosts();
                        adapter = new MyPostRecyclerViewAdapter(getActivity(), postList);
                        recyclerView.setAdapter(adapter);
                    } else if (response.body().getInfo().getCode() == Responses.ERROR_TOKEN_INVALIDO) {
                        Toast.makeText(getActivity(), Constants.ERROR_TOKEN_INCORRECTO, Toast.LENGTH_LONG).show();
                        Objects.requireNonNull(getActivity()).onBackPressed();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), Constants.ERROR_INESPERADO, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseListPost> call, Throwable t) {
                Toast.makeText(getActivity(), Constants.ERROR_COMUNICACION, Toast.LENGTH_LONG).show();
            }
        });
    }
}
