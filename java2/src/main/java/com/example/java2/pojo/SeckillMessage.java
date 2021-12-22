package com.example.Java2.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 秒杀信息
 *
 * @author cym    2021/12/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillMessage {
    private User user;
    private Long goodsId;

}
