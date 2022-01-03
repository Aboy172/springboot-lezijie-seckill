package com.example.java2.config;

import com.example.java2.pojo.User;

/** @author cym 2022/1/4 */
public class UserContext {

  private static ThreadLocal<User> userlocal = new ThreadLocal<User>();

  public static void setUser(User user) {
    userlocal.set(user);
  }

  public static User getUser() {
    return userlocal.get();
  }
}
