package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {


    void insert(Orders orders);



    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    List<OrderVO> list(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderVO getById(Long id);

    /**
     * 根据订单id查询订单实体
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    Orders getEntityById(Long id);

    List<OrderVO> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    Integer countByStatus(Integer status);

    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTime(Integer status, LocalDateTime orderTime);

    Double sumByMap(Map map);

    Integer getByMap(Map map);

    List<GoodsSalesDTO> getGoodsSalesDTOList(Map map);
}