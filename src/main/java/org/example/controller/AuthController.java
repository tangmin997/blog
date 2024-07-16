package org.example.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.example.bean.User;
import org.example.service.UserService;
import org.example.utils.PasswordUtils;
import org.example.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/auth") // 统一的请求前缀
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        // 从登录请求中获取用户名和密码
        String username = loginRequest.getUsername();
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // 判断用户是否已经存在，如果存在，获取用户信息
        User oldUser = userService.selectUserByNameAndEmail(username, email);
        if (oldUser!=null){
            //
            boolean matches = PasswordUtils.matches(oldUser, password);
            // 如果密码匹配
            if (matches){
                // 用户验证成功，可以生成JWT token并返回
                String token = TokenUtils.generateToken(oldUser);
                return ResponseEntity.ok().body(new HashMap<String, Object>() {{
                    put("token", token);
                    put("userInfo", oldUser);
                }});
            }
            // 如果没有找到用户，直接返回错误
            logger.info("密码错误");
            return ResponseEntity.status(401).body("密码错误");
        }
        else {
            logger.info("用户不存在");
            // 用户验证失败，返回错误信息
            return ResponseEntity.status(401).body("用户不存在");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User registerRequest) {
        // 从注册请求中获取用户名、邮箱和密码
        String username = registerRequest.getUsername();
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();
        // 判断用户是否已经存在
        User oldUser = userService.selectUserByNameAndEmail(username, email);
        if (oldUser!=null){
            logger.info("用户已经存在");
            return ResponseEntity.status(401).body("用户已经存在");
        }
        else {
            // 调用UserService进行用户注册
            boolean b = userService.userRegister(registerRequest);
            if (b){
                User newUser = userService.selectUserByNameAndEmail(username, email);
                // 用户注册成功，可以生成JWT token并返回
                String token = TokenUtils.generateToken(newUser);
                return ResponseEntity.ok().body(new HashMap<String, Object>() {{
                    put("token", token);
                    put("userInfo", newUser);
                }});
            }
            logger.info("注册失败，未知原因");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("注册失败，未知原因");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserInfo(HttpServletRequest request) {
        // 从请求头中获取token
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        // 验证token并获取用户信息
        Claims claims = TokenUtils.verifyAndParseToken(token);
        if (claims != null) {
            User currentUser = TokenUtils.buildUserFromClaims(claims);
            return ResponseEntity.ok(currentUser);
        } else {
            logger.info("没有权限");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
