package com.example.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * gitee账号
 * 
 * @author 杜延文
 * @email 1165449791@qq.com
 * @date 2022-09-05 22:44:17
 */
@Data
@TableName("ums_gitee")
public class GiteeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 会员id
	 */
	private Long memberId;
	/**
	 * 用户名
	 */
	private String name;
	/**
	 * 访问令牌
	 */
	private String accessToken;
	/**
	 * token类型
	 */
	private String tokenType;
	/**
	 * 令牌过期时间
	 */
	private Date expiresIn;
	/**
	 * 刷新令牌
	 */
	private String refreshToken;
	/**
	 * 获取令牌时间
	 */
	private Date createAt;
	/**
	 * 绑定时间（首次获取时间）
	 */
	private Date createTime;

}
