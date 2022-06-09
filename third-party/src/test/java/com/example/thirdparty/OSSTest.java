package com.example.thirdparty;
import com.aliyun.oss.*;
import com.aliyun.oss.model.GetObjectRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
/**
 * @author: very_modest
 * @date: 2022/6/5 21:17
 * @description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OSSTest {

    public static void main(String[] args) throws Exception {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5tSnJiirdHqo74AAaN6p";
        String accessKeySecret = "yP73prlWsgjY1P466pJvWOaABidksB";
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "typora-images-repository";
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = "mall/product/1f15cdbcf9e1273c.jpg";
        // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        String filePath= "D:\\Java\\Project\\MyProject\\谷粒商城资料整理课件\\谷粒商城资料整理课件\\基础篇\\资料\\pics\\1f15cdbcf9e1273c.jpg";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            InputStream inputStream = new FileInputStream(filePath);
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, inputStream);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Resource
    private OSSClient ossClient;
    @Test
    public void saveFile() {

        // 填写Bucket名称，例如examplebucket。
        String bucketName = "typora-images-repository";
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = "mall/product/1 f15cdbcf9e1273c.jpg";// 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        String filePath= "D:\\Java\\Project\\MyProject\\谷粒商城资料整理课件\\谷粒商城资料整理课件\\基础篇\\资料\\pics\\1f15cdbcf9e1273c.jpg";

        // download file to local
        //ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File(filePath));
        ossClient.putObject(bucketName,objectName,new File(filePath));
    }

}
