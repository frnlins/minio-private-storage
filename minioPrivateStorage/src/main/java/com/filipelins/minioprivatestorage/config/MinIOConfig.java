package com.filipelins.minioprivatestorage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

@Configuration
public class MinIOConfig {

	@Value("${minio.accesskey}")
	private String accessKey;
	
	@Value("${minio.secretkey}")
	private String secretKey;
	
	@Value("${minio.endpoint}")
	private String endpoint;
	
	@Bean
	public MinioClient minioClient() {
		MinioClient mc = null;
		try {
			mc = new MinioClient(endpoint, accessKey, secretKey);
		} catch (InvalidEndpointException e) {
			System.out.println("Invalid Endpoint: " + e);
		} catch (InvalidPortException e) {
			System.out.println("Invalid Port: " + e);
		}
		System.out.println("MinIO Cliente created!");
		return mc;
	}
}
