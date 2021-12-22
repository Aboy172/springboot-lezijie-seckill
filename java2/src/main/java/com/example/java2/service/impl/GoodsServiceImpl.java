package com.example.java2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.java2.mapper.GoodsMapper;
import com.example.java2.pojo.Goods;
import com.example.java2.service.IGoodsService;
import com.example.java2.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 * Cym
 * @author cym
 * @since 2021-12-14
 */






@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    private  GoodsMapper goodsMapper;


    @Override
    public List<GoodsVO> findGoodsVo ( ) {
        return goodsMapper.findGoodsVo();
    }

    @Override
    public GoodsVO findGoodsVoByGoodsId (Long goodsId) {
        return goodsMapper.findGoodsVoByGoodsId(goodsId);
    }
}
