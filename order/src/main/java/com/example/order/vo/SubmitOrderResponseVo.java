package com.example.order.vo;

import com.example.order.entity.OrderEntity;
import lombok.Data;

/**
 * @Description:
 **/

@Data
public class SubmitOrderResponseVo {

    private OrderEntity order;

    /** 错误状态码 **/
    private Integer code;


}
