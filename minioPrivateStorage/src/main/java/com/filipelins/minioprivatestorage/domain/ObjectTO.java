package com.filipelins.minioprivatestorage.domain;

import java.text.DecimalFormat;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.minio.messages.Item;

public class ObjectTO {

	private String nome;
	private long tamanho;
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private ZonedDateTime lastModified;

	public ObjectTO(Item item) {
		this.nome = item.objectName();
		this.tamanho = item.size();
		this.lastModified = item.lastModified();
	}

	public String getNome() {
		return nome;
	}

	public long getTamanho() {
		return tamanho;
	}

	public String getTamanhoMB() {
		DecimalFormat format = new DecimalFormat("0.00");
		return format.format(((this.tamanho / 1024d) / 1024d)) + " MB";
	}
	
	public ZonedDateTime getLastModified() {
		return lastModified;
	}
}
