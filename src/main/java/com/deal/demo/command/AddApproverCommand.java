package com.deal.demo.command;

import java.util.List;
import java.util.Map;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddApproverCommand {

	@TargetAggregateIdentifier
	private String dealId;
	
	private String teamCode;
	
	private String assignee;

	private Map<String, List<String>> parents;
	
	private Map<String, List<String>> next;

}
