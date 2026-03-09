package com.amis.vibemusicserver.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : AmisKwok
 * @description : MinIO配置类
 * 配置MinIO客户端用于文件存储服务
 */
@Data
@Configuration
public class MinioConfig {

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.accessKey}")
    private String accessKey;

    @Value("${oss.secretKey}")
    private String secretKey;

    /**
     * 创建MinioClient Bean
     * @return MinioClient实例
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}