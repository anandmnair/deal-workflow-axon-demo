package com.deal.demo.command.handler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deal.demo.command.ApproverAddedEvent;
import com.deal.demo.command.ApproverRespondedEvent;
import com.deal.demo.command.DealCreatedEvent;
import com.deal.demo.common.entity.Approver;
import com.deal.demo.common.entity.DealDocument;
import com.deal.demo.common.repository.DealRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DealWorkflowHandlerService {
	
	@Autowired
	private DealRepository dealRepository;
	
	@EventHandler
	public void handle(DealCreatedEvent event) {
		DealDocument dealDocument = DealDocument.builder()
				.dealId(event.getDealId())
				.dealCode(event.getDealCode())
				.amount(event.getAmount())
				.tenure(event.getTenure())
				.version(event.getVersion())
				.initiator(event.getInitiator())
				.build();
		DealDocument dealDocumentResult = dealRepository.save(dealDocument);
		log.info("deal created :: {}", dealDocumentResult);
	}
	
	@EventHandler
	public void handle(ApproverAddedEvent event) {
		DealDocument dealDocument = dealRepository.findOne(event.getDealId());
		if(dealDocument.getApprovers()==null) {
			dealDocument.setApprovers(new LinkedHashMap<>());
		}
		dealDocument.getApprovers().put(event.getTeamCode(), new Approver(event.getTeamCode(), event.getAssignee(), event.getParents(), event.getNext(),MapUtils.isEmpty(event.getParents())?true:false,null));
		DealDocument dealDocumentResult = dealRepository.save(dealDocument);
		log.info("Approver {} added to deal {} :: {}", event.getTeamCode(), event.getDealId(), dealDocumentResult);
	}
	
	@EventHandler
	public void handle(ApproverRespondedEvent event) {
		DealDocument dealDocument = dealRepository.findOne(event.getDealId());
		Approver approver = dealDocument.getApprovers().get(event.getTeamCode());
		approver.setStatus(event.getStatus());
		if(MapUtils.isNotEmpty(approver.getNext())) {
			List<String>nextApprovers = approver.getNext().get(event.getStatus());
			if(CollectionUtils.isNotEmpty(nextApprovers)) {
				for(String next : nextApprovers) {
					Approver nextApprover = dealDocument.getApprovers().get(next);
					if(checkActiveFlag(dealDocument,nextApprover)) {
						nextApprover.setActive(true);
					}
				}
			}
		}
		DealDocument dealDocumentResult = dealRepository.save(dealDocument);
		log.info("Approver {} added to deal {} :: {}", event.getTeamCode(), event.getDealId(), dealDocumentResult);
	}
	
	private boolean checkActiveFlag(DealDocument dealDocument, Approver approver) {
		if(MapUtils.isNotEmpty(approver.getParents())) {
			for(Map.Entry<String, List<String>> entry : approver.getParents().entrySet()) {
				for(String teamCode : entry.getValue()) {
					Approver parentApprover = dealDocument.getApprovers().get(teamCode);
					if(!entry.getKey().equals(parentApprover.getStatus())) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
}
