package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.api.cinema.vo.FilmInfoVO;
import com.stylefeng.guns.rest.common.persistence.model.MoocFieldT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 放映场次表 Mapper 接口
 * </p>
 *
 * @author wrq
 * @since 2018-11-18
 */
public interface MoocFieldTMapper extends BaseMapper<MoocFieldT> {

    List<FilmInfoVO> getFilmInfos(@Param("cinemaId") int cinemaId);

}
