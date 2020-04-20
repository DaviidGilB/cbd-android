package com.cbd.android.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cbd.android.models.Post;
import com.cbd.android.repositories.PostRepository;

import java.util.List;

public class PostViewModel extends AndroidViewModel {
    private PostRepository postRepository;
    private LiveData<List<Post>> posts;

    public PostViewModel(@NonNull Application application) {
        super(application);
        postRepository = new PostRepository();
        posts = postRepository.getAllPosts();
    }

    public LiveData<List<Post>> getPosts() {return posts; }

    public LiveData<List<Post>> getRefreshedPosts() {
        posts = postRepository.getAllPosts();
        return posts;
    }

    public void publishPost(String title, String description, Double price) {
        postRepository.createPost(title, description, price);
    }
}
