package com.example.Java2.vo;

import com.example.Java2.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class orderDetailVo {

    private Order order;
    private GoodsVO goodsVO;
}
