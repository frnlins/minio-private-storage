package com.filipelins.minioprivatestorage.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.filipelins.minioprivatestorage.domain.BucketTO;

import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.XmlParserException;
import io.minio.messages.DeleteError;
import io.minio.messages.Item;

@Service
public class ObjectService {

	@Autowired
	private MinioClient minioClient;

	public void deleteBucketObjects(BucketTO bucketTO) {
		Iterable<Result<Item>> results;
		try {
			results = minioClient.listObjects(bucketTO.getNome());
			List<String> objectNames = new ArrayList<String>();
			for (Result<Item> objeto : results) {
				objectNames.add(objeto.get().objectName());
			}
			Iterable<Result<DeleteError>> erroResults = minioClient.removeObjects(bucketTO.getNome(), objectNames);
			for (Result<DeleteError> deleteError : erroResults) {
				DeleteError error = deleteError.get();
				System.out.println("Error in deleting object " + error.objectName() + "; " + error.message());
			}
		} catch (XmlParserException | InvalidKeyException | InvalidBucketNameException | IllegalArgumentException
				| NoSuchAlgorithmException | InsufficientDataException | ErrorResponseException | InternalException
				| IOException e) {
			e.printStackTrace();
		}
	}
}
