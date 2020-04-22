package com.cbd.android.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cbd.android.R;
import com.cbd.android.activities.MainActivity;
import com.cbd.android.common.Constants;
import com.cbd.android.common.Utils;
import com.cbd.android.models.Post;
import com.cbd.android.viewModels.PostViewModel;

import java.util.List;
import java.util.Objects;

public class PostListFragment extends Fragment {

    private int postListType = 1;
    private RecyclerView recyclerView;
    private MyPostRecyclerViewAdapter adapter;
    private List<Post> postList;
    private PostViewModel postViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    public PostListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PostListFragment newInstance(int postListType) {
        PostListFragment fragment = new PostListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.POST_LIST_TYPE, postListType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
                .get(PostViewModel.class);

        if (getArguments() != null) {
            postListType = getArguments().getInt(Constants.POST_LIST_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                loadRefreshedData();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new MyPostRecyclerViewAdapter(getActivity(), postList);
        recyclerView.setAdapter(adapter);

        loadData();

        return view;
    }

    private void loadData() {
        postViewModel.getPosts().observe(Objects.requireNonNull(getActivity()), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                if (posts != null) {
                    postList = posts;
                    adapter.setData(postList);
                } else {
                    ((MainActivity) Objects.requireNonNull(getActivity())).exit();
                }
            }
        });
    }

    private void loadRefreshedData() {
        postViewModel.getRefreshedPosts().observe(Objects.requireNonNull(getActivity()), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                if (posts != null) {
                    postList = posts;
                    adapter.setData(postList);
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    ((MainActivity) Objects.requireNonNull(getActivity())).exit();
                }
            }
        });
    }
}
