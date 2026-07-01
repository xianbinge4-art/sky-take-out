package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.properties.WeChatProperties;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        //处理业务异常
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        Long useId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(useId);
        List<ShoppingCart> cart = shoppingCartMapper.list(shoppingCart);
        if (cart == null || cart.size() == 0) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getProvinceName() + addressBook.getCityName()
                + addressBook.getDistrictName() + addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee());

        orders.setUserId(useId);
        orderMapper.insert(orders);
        List<OrderDetail> orderDetailsList = new ArrayList<>();
        for (ShoppingCart cart1 : cart) {
            OrderDetail ordetail = new OrderDetail();
            BeanUtils.copyProperties(cart1, ordetail);
            ordetail.setOrderId(orders.getId());
            orderDetailsList.add(ordetail);
        }
        orderDetailMapper.insert(orderDetailsList);
        shoppingCartMapper.deleteByUserId(useId);
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .build();


        return orderSubmitVO;
    }


    /**
     * 订单支付（开发环境模拟支付，不调用真实微信支付接口）
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // ==================== 开发环境：模拟支付流程 ====================\
        // 1. 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(ordersPaymentDTO.getOrderNumber());
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        // 2. 校验订单支付状态（避免重复支付）
        if (ordersDB.getPayStatus() == Orders.PAID) {
            throw new OrderBusinessException("该订单已支付");
        }

        // 3. 模拟支付成功：更新订单状态为"待接单"、支付状态为"已支付"
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();
        orderMapper.update(orders);

        // 4. 通过 websocket 向客户端推送"来单提醒"消息
        Map map = new HashMap();
        map.put("type", 1); // 1: 来单提醒, 2: 客户催单
        map.put("orderId", ordersDB.getId());
        map.put("content", "订单号:" + ordersPaymentDTO.getOrderNumber());
        String json = JSONObject.toJSONString(map);
        webSocketServer.sendToAllClient(json);

        // 5. 构造模拟的支付响应数据
        OrderPaymentVO vo = new OrderPaymentVO();
        vo.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000));
        vo.setNonceStr("DEV_MOCK_PAY");
        vo.setPackageStr("prepay_id=DEV_MOCK_PREPAYID");
        vo.setSignType("RSA");
        vo.setPaySign("DEV_MOCK_PAYSIGN");

        log.info("【开发环境模拟支付】订单号:{}, 订单ID:{}, 模拟支付成功",
                ordersPaymentDTO.getOrderNumber(), ordersDB.getId());

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);

        //通过websocket向客户端推送消息
        Map map = new HashMap();
        map.put("type", 1);//1:表示来单提醒,2:表示客户催单
        map.put("orderId", orders.getId());
        map.put("content", "订单号:" + outTradeNo);
        String json = JSONObject.toJSONString(map);
        // 调用websocket发送消息
        webSocketServer.sendToAllClient(json);
    }

    @Override
    public PageResult historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        ordersPageQueryDTO.setUserId(userId);
        // 分页查询订单
        int pageNum = ordersPageQueryDTO.getPage() == null ? 1 : ordersPageQueryDTO.getPage();
        int pageSize = ordersPageQueryDTO.getPageSize() == null ? 10 : ordersPageQueryDTO.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<OrderVO> ordersList = orderMapper.list(ordersPageQueryDTO);
        // 分页查询订单详情
        for (OrderVO orderVO : ordersList) {
            // 分页查询订单详情
            orderVO.setOrderDetailList(orderDetailMapper.listByOrderId(orderVO.getId()));
        }
        PageInfo<OrderVO> pageInfo = new PageInfo<>(ordersList);
        PageResult pageResult = new PageResult(pageInfo.getTotal(), pageInfo.getList());
        return pageResult;

    }

    @Override
    public OrderVO orderDetail(Long id) {
        OrderVO orderVO = orderMapper.getById(id);
        if (orderVO == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        orderVO.setOrderDetailList(orderDetailMapper.listByOrderId(orderVO.getId()));
        return orderVO;
    }

    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        int pageNum = ordersPageQueryDTO.getPage() == null ? 1 : ordersPageQueryDTO.getPage();
        int pageSize = ordersPageQueryDTO.getPageSize() == null ? 10 : ordersPageQueryDTO.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<OrderVO> ordersList = orderMapper.conditionSearch(ordersPageQueryDTO);
        // 分页查询订单详情
        for (OrderVO orderVO : ordersList) {
            // 分页查询订单详情
            orderVO.setOrderDetailList(orderDetailMapper.listByOrderId(orderVO.getId()));
        }
        PageInfo<OrderVO> pageInfo = new PageInfo<>(ordersList);
        PageResult pageResult = new PageResult(pageInfo.getTotal(), pageInfo.getList());
        return pageResult;
    }

    @Override
    public OrderVO details(Long id) {
        OrderVO orderVO = orderMapper.getById(id);
        if (orderVO == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        orderVO.setOrderDetailList(orderDetailMapper.listByOrderId(orderVO.getId()));
        return orderVO;
    }

    @Override
    public OrderStatisticsVO statistics() {
        Integer toBeConfirmed = orderMapper.countByStatus(Orders.TO_BE_CONFIRMED);
        Integer confirmed = orderMapper.countByStatus(Orders.CONFIRMED);
        Integer deliveryInProgress = orderMapper.countByStatus(Orders.DELIVERY_IN_PROGRESS);
        return OrderStatisticsVO.builder()
                .toBeConfirmed(toBeConfirmed)
                .confirmed(confirmed)
                .deliveryInProgress(deliveryInProgress)
                .build();
    }

    @Override
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        // 根据订单id查询订单
        Orders ordersDB = orderMapper.getEntityById(ordersConfirmDTO.getId());
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        // 根据订单id更新订单的状态
        if(!ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException("该订单状态不是待确认单");
        }
        ordersDB.setStatus(Orders.CONFIRMED);
        orderMapper.update(ordersDB);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) {
        // 根据订单id查询订单
        Orders ordersDB = orderMapper.getEntityById(ordersRejectionDTO.getId());
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        // 根据订单id更新订单的状态
        if(!ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        if (ordersRejectionDTO.getRejectionReason() == null
                || ordersRejectionDTO.getRejectionReason().trim().isEmpty()) {
            throw new OrderBusinessException("拒单原因不能为空");
        }
        ordersDB.setStatus(Orders.CANCELLED);
        ordersDB.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        ordersDB.setCancelTime(LocalDateTime.now());
        ordersDB.setPayStatus(Orders.REFUND);
        orderMapper.update(ordersDB);
    }

    @Override
    public void cancel(OrdersCancelDTO ordersCancelDTO) {
        // 根据订单id查询订单
        Orders ordersDB = orderMapper.getEntityById(ordersCancelDTO.getId());
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        // 根据订单id更新订单的状态
        if(!ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        // ========== 新增：设置取消原因 ==========
        // 同时校验：管理端取消必须传原因
        String cancelReason = ordersCancelDTO.getCancelReason();
        if (cancelReason == null || cancelReason.trim().isEmpty()) {
            throw new OrderBusinessException("取消原因不能为空");
        }
        ordersDB.setCancelReason(cancelReason);
        ordersDB.setStatus(Orders.CANCELLED);
        ordersDB.setCancelTime(LocalDateTime.now());
        ordersDB.setPayStatus(Orders.REFUND);
        orderMapper.update(ordersDB);
    }

    @Override
    public void delivery(Long id) {
        // 根据订单id查询订单
        Orders ordersDB = orderMapper.getEntityById(id);
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        // 根据订单id更新订单的状态
        if(!ordersDB.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        ordersDB.setStatus(Orders.DELIVERY_IN_PROGRESS);
        orderMapper.update(ordersDB);
    }

    @Override
    public void complete(Long id) {
        // 根据订单id查询订单
        Orders ordersDB = orderMapper.getEntityById(id);
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        // 根据订单id更新订单的状态
        if(!ordersDB.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        ordersDB.setStatus(Orders.COMPLETED);
        ordersDB.setDeliveryTime(LocalDateTime.now());
        orderMapper.update(ordersDB);
    }

    @Override
    public void cancelById(Long id) {
        // 根据订单id查询订单
        Orders ordersDB = orderMapper.getEntityById(id);
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        // ========== 新增：校验订单归属 ==========
        Long currentUserId = BaseContext.getCurrentId();
        if (!ordersDB.getUserId().equals(currentUserId)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        // 用户端取消：通常只允许"待付款"和"待接单"状态
        if (!ordersDB.getStatus().equals(Orders.PENDING_PAYMENT)
                && !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        // ========== 新增：设置取消原因 ==========
        // 同时校验：管理端取消必须传原因
        String cancelReason = ordersDB.getCancelReason();
        if (cancelReason == null || cancelReason.trim().isEmpty()) {
            throw new OrderBusinessException("取消原因不能为空");
        }
        ordersDB.setCancelReason(cancelReason);
        ordersDB.setStatus(Orders.CANCELLED);
        ordersDB.setCancelTime(LocalDateTime.now());

        // ========== 注意：用户取消订单的退款逻辑 ==========
        // 只有已支付的订单（TO_BE_CONFIRMED 及之后）才需要退款
        if (!ordersDB.getPayStatus().equals(Orders.UN_PAID)) {
            ordersDB.setPayStatus(Orders.REFUND);
        }
        orderMapper.update(ordersDB);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void repetition(Long id) {
        // 1. 查询原订单
        Orders ordersDB = orderMapper.getEntityById(id);
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        // 2. 校验订单归属（必须是当前用户的订单才能"再来一单"）
        Long currentUserId = BaseContext.getCurrentId();
        if (!ordersDB.getUserId().equals(currentUserId)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 3. 查询原订单的订单项
        List<OrderDetail> orderDetailList = orderDetailMapper.listByOrderId(id);
        if (orderDetailList == null || orderDetailList.isEmpty()) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 4. 将订单项逐个加入购物车
        //    注意：先清空当前用户购物车（可选，取决于产品策略）
        shoppingCartMapper.deleteByUserId(currentUserId);

        LocalDateTime now = LocalDateTime.now();
        for (OrderDetail orderDetail : orderDetailList) {
            ShoppingCart shoppingCart = ShoppingCart.builder()
                    .userId(currentUserId)
                    .dishId(orderDetail.getDishId())
                    .setmealId(orderDetail.getSetmealId())
                    .name(orderDetail.getName())
                    .image(orderDetail.getImage())
                    .number(orderDetail.getNumber())
                    .amount(orderDetail.getAmount())
                    .createTime(now)
                    .build();
            shoppingCartMapper.insert(shoppingCart);
        }
    }


}