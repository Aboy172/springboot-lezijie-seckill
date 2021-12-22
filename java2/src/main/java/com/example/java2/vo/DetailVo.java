package com.example.Java2.vo;

import com.example.Java2.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailVo {

    private GoodsVO goodsVO;
    private User user;
    private int secKillStatus;
    private int remainSeconds;


}
