package com.example.Java2.vo;

import com.example.Java2.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsVO extends Goods {

    private BigDecimal seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;

}
