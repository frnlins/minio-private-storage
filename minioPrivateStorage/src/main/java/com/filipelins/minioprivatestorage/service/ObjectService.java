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
import io.minio.errors.MinioException;
import io.minio.messages.DeleteError;
import io.minio.messages.Item;

@Service
public class ObjectService {

	@Autowired
	private MinioClient minioClient;

	public void deleteBucketObjects(BucketTO bucketTO) throws MinioException {
		Iterable<Result<Item>> results = minioClient.listObjects(bucketTO.getNome());
		deleteBucketObjects(bucketTO, results);
	}

	public void deleteBucketObjects(BucketTO bucketTO, Iterable<Result<Item>> results) throws MinioException {
		List<String> objectNames = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();

		try {
			// Obtém o nome dos objetos
			for (Result<Item> objeto : results) {
				objectNames.add(objeto.get().objectName());
			}

			// Realiza o delete dos objetos
			Iterable<Result<DeleteError>> erroResults = minioClient.removeObjects(bucketTO.getNome(), objectNames);

			// Confere possíveis erros ao deletar os objetos.
			for (Result<DeleteError> deleteError : erroResults) {
				DeleteError error = deleteError.get();
				sb.append("Error in deleting object " + error.objectName() + "; " + error.message() + "\n");
			}

			if (sb.length() > 0) {
				throw new MinioException(sb.toString());
			}
		} catch (InvalidKeyException | IllegalArgumentException | NoSuchAlgorithmException | IOException
				| MinioException e) {
			throw new MinioException("Erros ao deletar o(s) objeto(s):\n" + e.getMessage());
		}
	}
}
