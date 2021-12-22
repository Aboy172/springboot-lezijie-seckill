package com.example.Java2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.Java2.pojo.Goods;
import com.example.Java2.vo.GoodsVO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cym
 * @since 2021-12-14
 */
@Component
public interface GoodsMapper extends BaseMapper<Goods> {
    List <GoodsVO> findGoodsVo();

    GoodsVO findGoodsVoByGoodsId(Long goodsId);
}
