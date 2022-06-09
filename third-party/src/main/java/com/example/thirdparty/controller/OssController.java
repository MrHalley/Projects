package com.example.thirdparty.controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Resource;
import com.example.common.utils.R;
import org.apache.commons.lang.time.DateFormatUtils;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OssController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5522372203700422672L;

	@Resource
	private OSSClient ossClient;
	@Value("${spring.cloud.alicloud.oss.endpoint}")
	private String endpoint;
	@Value("${spring.cloud.alicloud.oss.bucket}")
	private String bucket;
	@Value("${spring.cloud.alicloud.access-key}")
	private String accessId;


	/**
	 * Get请求 
	 */
	@RequestMapping("/oss/policy")
	protected R policy() {
		String host = "http://" + bucket + "." + endpoint; // host的格式为 bucketname.endpoint
		String dir = "mall/product/"+ DateFormatUtils.format(new Date(),"yyyy-MM-dd"); // 用户上传文件时指定的前缀。

		Map<String, String> respMap = new LinkedHashMap<String, String>();
		try {
			long expireTime = 30;
			long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
			Date expiration = new Date(expireEndTime);
			PolicyConditions policyConds = new PolicyConditions();
			policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
			policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

			String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
			byte[] binaryData = postPolicy.getBytes("utf-8");
			String encodedPolicy = BinaryUtil.toBase64String(binaryData);
			String postSignature = ossClient.calculatePostSignature(postPolicy);

			respMap.put("accessid", accessId);
			respMap.put("policy", encodedPolicy);
			respMap.put("signature", postSignature);
			respMap.put("dir", dir);
			respMap.put("host", host);
			respMap.put("expire", String.valueOf(expireEndTime / 1000));
			// respMap.put("expire", formatISO8601Date(expiration));

		} catch (Exception e) {
			// Assert.fail(e.getMessage());
			System.out.println(e.getMessage());
		}
		return R.ok().put("data",respMap);
	}
}
