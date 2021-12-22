package com.example.java2.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author cym
 * @since 2021-12-14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 收货地址id
     */
    private Long deliverAddrId;

    /**
     * 冗余过来的商品名称
     */
    private String goodsName;

    /**
     * 商品数量
     */
    private Integer goodsCount;

    /**
     * 1pc,2android,3ios
     */
    private Integer orderChannel;

    /**
     * 订单状态:0新建未支付,1已支付,2已发货,3已收货,4已退款,5已完成
     */
    private Integer status;

    /**
     * 订单创建时间
     */
    private Date creadateDate;

    /**
     * 支付时间
     */
    private Date payDate;


}
