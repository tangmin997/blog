package org.example.controller;

import org.example.bean.Post;
import org.example.bean.User;
import org.example.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private final PostService postService;

    public IndexController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/api/posts")
    public ResponseEntity<List<Post>> getPostsByUser(@RequestParam(value = "uid") int userId) {
        List<Post> postList = postService.getPostsByUserId(userId);
        return ResponseEntity.ok(postList);
    }

    /**
     * 创建新文章
     * @param post 文章对象
     * @return 创建的文章对象
     */
    @PostMapping("/api/posts")
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        boolean b = postService.createPost(post);
        if (b){
            logger.info("创建成功");
            return ResponseEntity.ok("创建成功");
        }
        logger.info("创建失败");
        return ResponseEntity.status(401).body("创建失败");
    }

    /**
     * 获取单篇文章详情
     * @param id 文章ID
     * @return 文章详情或错误信息
     */
    @GetMapping("/api/posts/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Integer id) {
        Post post = postService.getPostById(id);
        if (post!=null){
            return ResponseEntity.ok(post);
        }
        logger.info("获取文章详情失败");
        return ResponseEntity.status(401).body("获取文章详情失败");
    }

    /**
     * 更新文章
     * @param id 文章ID
     * @param updatedPost 更新后的文章对象
     * @return 更新后的文章对象或错误信息
     */
    @PutMapping("/api/posts/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Integer id, @RequestBody Post updatedPost) {
        boolean b = postService.updatePost(id, updatedPost);
        if (b){
            logger.info("更新成功");
            return ResponseEntity.ok("更新成功");
        }
        logger.info("更新失败");
        return ResponseEntity.status(401).body("更新失败");
    }

    /**
     * 删除文章
     * @param id 文章ID
     * @return 删除结果或错误信息
     */
    @DeleteMapping("/api/posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Integer id) {
        boolean deleted = postService.deletePost(id);
        if (deleted) {
            logger.info("删除成功");
            return ResponseEntity.ok("删除成功");
        }
        else {
            logger.info("删除失败");
            return ResponseEntity.status(404).body("删除失败");
        }
    }
}
