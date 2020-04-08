package com.filipelins.minioprivatestorage.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filipelins.minioprivatestorage.domain.BucketTO;
import com.filipelins.minioprivatestorage.service.BucketService;

import io.minio.errors.MinioException;

@RestController
@RequestMapping("/buckets")
public class BucketResource {

	@Autowired
	private BucketService service;

	@GetMapping
	public ResponseEntity<List<BucketTO>> listBuckets() {
		List<BucketTO> lista = service.listBuckets();
		return ResponseEntity.ok(lista);
	}

	@PostMapping
	public ResponseEntity<String> insert(@RequestBody BucketTO bucketTO) {
		try {
			service.createBucket(bucketTO);
			return new ResponseEntity<String>("Bucket: '" + bucketTO.getNome() + "' criado com sucesso!",
					HttpStatus.OK);
		} catch (MinioException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping
	public ResponseEntity<String> delete(@RequestBody BucketTO bucketTO) {
		try {
			service.deleteBucket(bucketTO);
			return new ResponseEntity<String>("O bucket: '" + bucketTO.getNome() + "' foi deletado com sucesso!" , HttpStatus.OK);
		} catch (MinioException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
