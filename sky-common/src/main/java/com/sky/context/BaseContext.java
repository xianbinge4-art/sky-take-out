package com.sky.context;

public class BaseContext {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 保存当前请求的用户或员工 id。
     *
     * @param id 当前登录主体 id
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 获取当前请求保存的用户或员工 id。
     *
     * @return 当前登录主体 id
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }

    /**
     * 清理当前线程保存的登录主体 id。
     */
    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
