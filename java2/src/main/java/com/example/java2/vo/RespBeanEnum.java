//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.java2.vo;

public enum RespBeanEnum {
  SUCCESS(200, "SUCCESS"),
  RSUCCESS(201, "注册成功"),
  ERROR(500, "服务器启动失败！"),
  LOGIN_ERROR(8848, "用户名或密码错误"),
  MOBILE_ERROR(8849, "手机号码格式不正确"),
  BIND_ERROR(50012, "参数校验异常"),
  USER_ERROR(4889, "用户已存在"),
  EMPTY_STOCK(500055, "库存不足"),
  REPT_ERROR(500051, "该商品每人限购一件"),
  MOBILE_NOT_ERROR(8850, "手机号码不存在"),
  PASSWORD_UPDATE_ERROR(8851, "密码修改失败"),
  SEESION_ERROR(50052, "用户不存在"),
  COUNT_NOT(5003, "库存不足"),
  INFO_ERROR(4888, "注册失败"),
  REQUEST_ERROR(50041, "请求非法"),
  REQUEST_MANY_ERROR(50042, "请求次数过多"),
  CAPTCHA_ERROR(4889, "验证码输入错误"),
  ORDERID_NOT(50040, "订单ID不存在");

  private final Integer code;
  private final String message;

  @Override
  public String toString() {
    return "RespBeanEnum."
        + this.name()
        + "(code="
        + this.getCode()
        + ", message="
        + this.getMessage()
        + ")";
  }

  private RespBeanEnum(final Integer code, final String message) {
    this.code = code;
    this.message = message;
  }

  public Integer getCode() {
    return this.code;
  }

  public String getMessage() {
    return this.message;
  }
}
