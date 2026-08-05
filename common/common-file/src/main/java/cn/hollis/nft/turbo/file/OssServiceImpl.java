package cn.hollis.nft.turbo.file;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;

/**
 * 阿里云 OSS 文件服务
 *
 * @author hollis
 */
@Slf4j
@Setter
public class OssServiceImpl implements FileService {

    private String bucket;

    private String endPoint;

    private String accessKey;

    private String accessSecret;

    @Override
    public boolean upload(String path, InputStream fileStream) {
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKey, accessSecret);

        // 创建 OSSClient 实例。
        OSS ossClient = new OSSClientBuilder().build(endPoint, credentialsProvider);
        boolean uploadRes = false;
        try {
            // 创建 PutObjectRequest 对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, path, fileStream);

            // 上传文件。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            if (StringUtils.isNotBlank(result.getRequestId())) {
                uploadRes = true;
            }
        } catch (Exception e) {
            log.error("OssServiceImpl upload error, path=" + path, e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return uploadRes;
    }

}
