package org.example.utils;

public class StringUtils {

    // 辅助方法：将下划线命名转换为驼峰命名
    public static String toCamelCase(String dbName) {
        String[] parts = dbName.split("_");
        StringBuilder camelCaseName = new StringBuilder(parts[0].toLowerCase());
        for (int i = 1; i < parts.length; i++) {
            camelCaseName.append(parts[i].substring(0, 1).toUpperCase()).append(parts[i].substring(1).toLowerCase());
        }
        return camelCaseName.toString();
    }
}
