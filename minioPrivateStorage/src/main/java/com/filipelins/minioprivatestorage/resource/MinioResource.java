package com.filipelins.minioprivatestorage.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.filipelins.minioprivatestorage.domain.BucketTO;
import com.filipelins.minioprivatestorage.domain.ObjectTO;
import com.filipelins.minioprivatestorage.service.MinioService;

import io.minio.errors.MinioException;

@RestController
@RequestMapping("/minio")
public class MinioResource {

	@Autowired
	private MinioService service;

	@GetMapping
	public ResponseEntity<List<BucketTO>> listBuckets() {
		List<BucketTO> lista = service.listBuckets();
		return ResponseEntity.ok(lista);
	}

	@GetMapping("/{bucketName}")
	public ResponseEntity<List<ObjectTO>> listBucketObjects(@PathVariable("bucketName") String bucketName) {
		try {
			List<ObjectTO> lista = service.listBucketObjects(bucketName);
			return ResponseEntity.ok(lista);
		} catch (MinioException e) {
			return new ResponseEntity<List<ObjectTO>>(HttpStatus.BAD_REQUEST);
		}
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
			return new ResponseEntity<String>("O bucket: '" + bucketTO.getNome() + "' foi deletado com sucesso!",
					HttpStatus.OK);
		} catch (MinioException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "/{bucketName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadObject(@RequestParam(name = "object") MultipartFile multipartFile,
			@PathVariable("bucketName") String bucketName) {
		try {
			service.uploadObject(bucketName, multipartFile);
			return ResponseEntity.ok("Objeto enviado com sucesso!");
		} catch (MinioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
