package com.deal.demo.common.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName="deal", type="deal")
public class DealDocument {

	@Id
	private String dealId;
	
	private String dealCode;
	
	private Long amount;
	
	private Long tenure;
	
	private Long version;

	private String initiator;
	
	private Map<String,Approver> approvers = new LinkedHashMap<>();

}
