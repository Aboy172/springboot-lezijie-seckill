package com.example.Java2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.Java2.pojo.Goods;
import com.example.Java2.vo.GoodsVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 * Cym
 * @author cym
 * @since 2021-12-14
 */
public interface IGoodsService extends IService<Goods> {

    List <GoodsVO> findGoodsVo();

    GoodsVO findGoodsVoByGoodsId (Long goodsId);
}
