package com.example.Java2.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Package: com.seckillpro.seckill_pro.vo
 * @ClassName: RegisterVo
 * @Author: lyx
 * @CreateTime: 2021/4/13 17:21
 * @Description:
 */


@Data
public class RegisterVo {

    @NotNull
    private String username;

    @NotNull
    private Long mobile;

    @NotNull
    private String password;

}
