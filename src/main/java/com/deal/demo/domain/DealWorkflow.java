package com.deal.demo.domain;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import com.deal.demo.command.AddApproverCommand;
import com.deal.demo.command.ApproverAddedEvent;
import com.deal.demo.command.ApproverRespondedEvent;
import com.deal.demo.command.ApproverResponseCommand;
import com.deal.demo.command.CreateDealCommand;
import com.deal.demo.command.DealCreatedEvent;
import com.deal.demo.common.entity.Approver;

import lombok.NoArgsConstructor;

@Aggregate
@NoArgsConstructor
public class DealWorkflow {

	@AggregateIdentifier
	private String dealId;
	
	private Map<String,Approver> approvers = new LinkedHashMap<>();
	
	@CommandHandler
	public DealWorkflow(CreateDealCommand command) {
		apply(new DealCreatedEvent(command.getDealId(), command.getDealCode(), command.getAmount(), command.getTenure(), command.getVersion(), command.getInitiator()));
	}
	
	@CommandHandler
	public void handle(AddApproverCommand command) {
		apply(new ApproverAddedEvent(command.getDealId(), command.getTeamCode(), command.getAssignee(), command.getParents(), command.getNext()));
	}
	
	
	
	@CommandHandler
	public void handle(ApproverResponseCommand command) {
		apply(new ApproverRespondedEvent(command.getDealId(), command.getTeamCode(), command.getAssignee(), command.getStatus()));
		
//		Approver approver = this.approvers.get(command.getTeamCode());
//		if(MapUtils.isNotEmpty(approver.getNext())) {
//			List<String>nextApprovers = approver.getNext().get(command.getStatus());
//			if(CollectionUtils.isEmpty(nextApprovers)) {
//				apply(new ApproverRespondedEvent(command.getDealId(), command.getTeamCode(), command.getAssignee(), command.getStatus()));
//			}
//		}
	}
	
	@EventSourcingHandler
	public void on(DealCreatedEvent event) {
		this.dealId = event.getDealId();
	}
	
	@EventSourcingHandler
	public void on(ApproverAddedEvent event) {
		this.approvers.put(event.getTeamCode(), new Approver(event.getTeamCode(), event.getAssignee(), event.getParents(), event.getNext(),MapUtils.isEmpty(event.getParents())?true:false,"TODO"));
	}
	
	@EventSourcingHandler
	public void on(ApproverRespondedEvent event) {
		//this.approvers.put(event.getTeamCode(), new Approver(event.getTeamCode(), event.getAssignee(), event.getParents(), event.getNext(),MapUtils.isEmpty(event.getParents())?true:false));
	}
	

}
