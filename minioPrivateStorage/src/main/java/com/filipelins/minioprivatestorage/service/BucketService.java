package com.filipelins.minioprivatestorage.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.filipelins.minioprivatestorage.domain.BucketTO;

import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.MinioException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Bucket;
import io.minio.messages.Item;

@Service
public class BucketService {

	@Autowired
	private MinioClient minioClient;
	
	@Autowired
	private ObjectService objectService;

	public List<BucketTO> listBuckets() {
		List<BucketTO> retorno = null;
		try {
			List<Bucket> lista = minioClient.listBuckets();

			if (lista != null && !lista.isEmpty()) {
				retorno = lista.stream().map(obj -> new BucketTO(obj)).collect(Collectors.toList());
			}
		} catch (InvalidKeyException | InvalidBucketNameException | IllegalArgumentException | NoSuchAlgorithmException
				| InsufficientDataException | XmlParserException | ErrorResponseException | InternalException
				| InvalidResponseException | IOException e) {
			System.out.println("Erro ao listar os buckets: " + e);
		}
		return retorno;
	}

	public void createBucket(BucketTO bucketTO) {
		try {
			minioClient.makeBucket(bucketTO.getNome());
		} catch (InvalidKeyException | IllegalArgumentException | NoSuchAlgorithmException | IOException
				| MinioException e) {
			System.out.println("Erro ao criar um bucket: " + e);
		}
	}

	public void deleteBucket(BucketTO bucketTO) {
		try {
			if(isBucketExists(bucketTO.getNome())) {
				
				if(!isBucketEmpty(bucketTO.getNome())) {
					objectService.deleteBucketObjects(bucketTO);
				}
				
				minioClient.removeBucket(bucketTO.getNome());
			}
		} catch (InvalidKeyException | InvalidBucketNameException | IllegalArgumentException | NoSuchAlgorithmException
				| InsufficientDataException | XmlParserException | ErrorResponseException | InternalException
				| InvalidResponseException | IOException e) {
			System.out.println("Erro ao deletar um bucket: " + e);
		}
	}

	private boolean isBucketEmpty(String bucketName) {
		boolean empty = true;
		
		try {
			Iterable<Result<Item>> results = minioClient.listObjects(bucketName);
			empty = results.iterator().hasNext() ? false : true;
		} catch (XmlParserException e) {
			System.out.println("Erro ao avaliar se o bucket: " + bucketName + " está vazio! " + e);
		}
		
		return empty;
	}

	private boolean isBucketExists(String bucketName) {
		boolean retorno = false;
		try {
			retorno = minioClient.bucketExists(bucketName);
		} catch (InvalidKeyException | InvalidBucketNameException | IllegalArgumentException | NoSuchAlgorithmException
				| InsufficientDataException | XmlParserException | ErrorResponseException | InternalException
				| InvalidResponseException | IOException e) {
			System.out.println("Erro ao avaliar se o Bucket: '" + bucketName + "' existe! ");
		}
		return retorno;
	}
}
