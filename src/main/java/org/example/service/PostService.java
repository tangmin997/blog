package org.example.service;

import org.example.bean.Post;

import java.util.List;

public interface PostService {
    List<Post> getPostsByUserId(int userId);

    boolean createPost(Post post);

    Post getPostById(Integer id);

    boolean updatePost(Integer id, Post updatedPost);

    boolean deletePost(Integer id);
}
