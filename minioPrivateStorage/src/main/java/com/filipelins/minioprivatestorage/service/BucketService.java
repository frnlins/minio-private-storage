package com.filipelins.minioprivatestorage.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.filipelins.minioprivatestorage.domain.BucketTO;
import com.filipelins.minioprivatestorage.domain.ObjectTO;

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

	public void createBucket(BucketTO bucketTO) throws MinioException {
		try {
			minioClient.makeBucket(bucketTO.getNome());
		} catch (InvalidKeyException | IllegalArgumentException | NoSuchAlgorithmException | IOException
				| MinioException e) {
			throw new MinioException("O bucket não pode ser criado! ERRO: " + e.getMessage());
		}
	}

	public void deleteBucket(BucketTO bucketTO) throws MinioException {
		try {
			if (minioClient.bucketExists(bucketTO.getNome())) {

				Iterable<Result<Item>> results = minioClient.listObjects(bucketTO.getNome());

				if (results.iterator().hasNext()) {
					objectService.deleteBucketObjects(bucketTO, results);
				}

				minioClient.removeBucket(bucketTO.getNome());
			} else {
				throw new MinioException("O bucket: '" + bucketTO.getNome() + "' não existe");
			}
		} catch (InvalidKeyException | IllegalArgumentException | NoSuchAlgorithmException | IOException
				| MinioException e) {
			throw new MinioException(
					"Erro ao deletar o bucket: '" + bucketTO.getNome() + "'. ERROR: " + e.getMessage());
		}
	}

	public List<ObjectTO> listBucketObjects(String bucketName) throws MinioException {
		List<ObjectTO> lista = new ArrayList<>();

		try {
			Iterable<Result<Item>> objects = minioClient.listObjects(bucketName);
			for (Result<Item> obj : objects) {
				lista.add(new ObjectTO(obj.get()));
			}
		} catch (MinioException | InvalidKeyException | IllegalArgumentException | NoSuchAlgorithmException | IOException e) {
			throw new MinioException(
					"Erro ao listar os objetos do bucket: '" + bucketName + "' ERROR: " + e.getMessage());
		}

		return lista;
	}
}
