package org.example.utils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.example.bean.User;

public class PasswordUtils {
    /**
     * 密码加密
     * @param loginName
     * @param password
     * @param salt
     * @return
     */
    public static String encryptPassword(String loginName, String password, String salt)
    {
        return new Md5Hash(loginName + password + salt).toHex();
    }

    /**
     * 判断密码是否匹配
     * @param user 用户
     * @param newPassword 表单提交的密码
     * @return
     */
    public static boolean matches(User user, String newPassword)
    {
        return user.getPassword().equals(encryptPassword(user.getUsername(), newPassword, user.getSalt()));
    }

    /**
     * 生成随机盐
     */
    public static String randomSalt()
    {
        // 一个Byte占两个字节，此处生成的3字节，字符串长度为6
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        String hex = secureRandom.nextBytes(3).toHex();
        return hex;
    }
}
