package com.deal.demo.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDealCommand {
	
	private String dealId;
	
	private String dealCode;
	
	private Long amount;
	
	private Long tenure;
	
	private Long version;
	
	private String initiator;
}
