package com.deal.demo.command.controller;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deal.demo.command.AddApproverCommand;
import com.deal.demo.command.ApproverResponseCommand;
import com.deal.demo.command.CreateDealCommand;
import com.deal.demo.command.model.LsoDeal;
import com.deal.demo.common.entity.Approver;

@RestController
@RequestMapping("/deal-command")
public class DealCommandController {

	@Autowired
	private CommandGateway commandGateway;
	
	@PostMapping("/push")
	public void pushLsoDeal(@RequestBody LsoDeal lsoDeal) {
		commandGateway.send(new CreateDealCommand(UUID.randomUUID().toString(), lsoDeal.getDealCode(), lsoDeal.getAmount(), lsoDeal.getTenure(),lsoDeal.getVersion(), lsoDeal.getInitiator()));
	}
	
	@PostMapping("/{dealId}/approver")
	public void addApprover(@PathVariable("dealId")String dealId, @RequestBody Approver approver) {
		commandGateway.send(new AddApproverCommand(dealId, approver.getTeamCode(), approver.getAssignee(), approver.getParents(),approver.getNext()));
	}
	
	@PostMapping("/{dealId}/approver/{teamCode}/response/{status}/{username}")
	public void approve(@PathVariable("dealId")String dealId,@PathVariable("teamCode")String teamCode, @PathVariable("status")String status, @PathVariable(name="username", required=false)String username) {
		commandGateway.send(new ApproverResponseCommand(dealId, teamCode, username, status));
	}
}
