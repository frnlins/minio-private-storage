package com.filipelins.minioprivatestorage.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filipelins.minioprivatestorage.domain.BucketTO;
import com.filipelins.minioprivatestorage.service.BucketService;

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
	public void insert(@RequestBody BucketTO bucketTO) {
		service.createBucket(bucketTO);
	}
}
