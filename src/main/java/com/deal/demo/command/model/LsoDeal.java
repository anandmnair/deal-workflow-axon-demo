package com.deal.demo.command.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Data;

@Data
@Document(indexName="deal", type="deal")
public class LsoDeal {

	@Id
	private String dealId;
	
	private String dealCode;
	
	private Long amount;
	
	private Long tenure;
	
	private Long version;
	
	private String initiator;
}
