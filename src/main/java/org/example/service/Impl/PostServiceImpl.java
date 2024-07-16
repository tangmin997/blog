package org.example.service.Impl;

import org.example.bean.Post;
import org.example.service.PostService;
import org.example.sql.Operate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    /**
     * @param userId
     * @return
     */
    @Override
    public List<Post> getPostsByUserId(int userId) {
        Operate operate = new Operate(4, new Object[]{userId});
        return operate.selectPostsByUserId();
    }

    /**
     * @param post
     * @return
     */
    @Override
    public boolean createPost(Post post) {
        Operate operate = new Operate(5, new Object[]{post.getTitle(), post.getContent(), post.getUserId()});
        return operate.insertPostsSql();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Post getPostById(Integer id) {
        Operate operate = new Operate(6, new Object[]{id});
        return operate.selectPostByPostId();
    }

    /**
     * @param postId
     * @param updatedPost
     * @return
     */
    @Override
    public boolean updatePost(Integer postId, Post updatedPost) {
        Operate operate = new Operate(7, new Object[]{updatedPost.getTitle(), updatedPost.getContent(), postId});
        return operate.updatePostByPostId();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public boolean deletePost(Integer id) {
        Operate operate = new Operate(8, new Object[]{id});
        return operate.deletePostByPostId();
    }
}
