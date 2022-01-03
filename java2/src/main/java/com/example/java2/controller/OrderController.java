package com.example.java2.controller;

import com.example.java2.pojo.User;
import com.example.java2.service.IOrderService;
import com.example.java2.vo.RespBean;
import com.example.java2.vo.RespBeanEnum;
import com.example.java2.vo.orderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 * Cym
 * @author cym
 * @since 2021-12-14
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;
    /**
     * 订单详情
     * @param user
     * @param orderId
     * @return
     */
    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(User user,Long orderId){
        if (user==null){
            return RespBean.error(RespBeanEnum.SEESION_ERROR);
        }
        orderDetailVo detail = orderService.detail(orderId);
        return RespBean.success(detail);
    }

}
