package com.stylefeng.guns.rest.modular.order.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.order.OrderServiceAPI;
import com.stylefeng.guns.api.order.vo.OrderVO;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.stylefeng.guns.rest.common.util.FTPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Service(interfaceClass = OrderServiceAPI.class, group = "default")
public class DefaultOrderServiceImpl implements OrderServiceAPI {

    @Autowired
    private MoocOrderTMapper moocOrderTMapper;

    @Autowired
    private FTPUtil ftpUtil;

    // 验证是否为真实的座位编号
    @Override
    public boolean isTrueSeats(String fieldId, String seats) {
        // 根据FieldId找到对应的座位位置图
        String seatPath = moocOrderTMapper.getSeatsByFieldId(fieldId);
        // 读取位置图，判断seats是否为真
        String fileStrByAddress = ftpUtil.getFileStrByAddress(seatPath);
        // 将fileStrByAddress转换为JSON对象
        JSONObject jsonObject = JSONObject.parseObject(fileStrByAddress);
        // seats=1,2,3   ids="1,3,4,5,6,7,88"
        String ids = jsonObject.get("ids").toString();
        // 每一次匹配上的，都给isTrue+1
        String[] seatArrs = seats.split(",");
        String[] idArrs = ids.split(",");
        int isTrue = 0;
        for (String id : idArrs) {
            for (String seat : seatArrs) {
                if (seat.equalsIgnoreCase(id)) {
                    isTrue++;
                }
            }
        }
        // 如果匹配上的数量与已售座位数一致，则表示全都匹配上了
        if (seatArrs.length == isTrue) {
            return true;
        } else {
            return false;
        }
    }

    // 判断是否为已售座位
    @Override
    public boolean isNotSoldSeats(String fieldId, String seats) {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("field_id", fieldId);
        List<MoocOrderT> list = moocOrderTMapper.selectList(entityWrapper);
        String[] seatArrs = seats.split(",");
        // 有任何一个编号匹配上，则直接返回失败，可优化通过 SQL
        for (MoocOrderT moocOrderT : list) {
            String[] ids = moocOrderT.getSeatsIds().split(",");
            for (String id : ids) {
                for (String seat : seatArrs) {
                    if (id.equalsIgnoreCase(seat)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public OrderVO saveOrderInfo(Integer fieldId, String soldSeats, String seatsName, Integer userId) {
        return null;
    }

    @Override
    public Page<OrderVO> getOrderByUserId(Integer userId, Page<OrderVO> page) {
        return null;
    }

    @Override
    public String getSoldSeatsByFieldId(Integer fieldId) {
        return null;
    }
}
