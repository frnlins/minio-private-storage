package com.filipelins.minioprivatestorage.domain;

import io.minio.messages.Bucket;

public class BucketTO {

	private String nome;

	public BucketTO() {
	}

	public BucketTO(Bucket b) {
		this.nome = b.name();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
