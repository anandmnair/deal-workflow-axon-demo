package com.deal.demo.domain;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import com.deal.demo.command.AddApproverCommand;
import com.deal.demo.command.ApproverAddedEvent;
import com.deal.demo.command.ApproverRespondedEvent;
import com.deal.demo.command.ApproverResponseCommand;
import com.deal.demo.command.CreateDealCommand;
import com.deal.demo.command.DealCreatedEvent;

public class DealWorkflowTest {

	private FixtureConfiguration<DealWorkflow> fixture;

	@Before
	public void setUp(){
		fixture=new AggregateTestFixture<>(DealWorkflow.class);
	}
	
	@Test
	public void createDealWorkflowTest(){
		fixture.givenNoPriorActivity()
			.when(new CreateDealCommand("DL1001","DL1001", 1000L, 10L, 1L, "anand.manissery"))
			.expectEvents(new DealCreatedEvent("DL1001","DL1001", 1000L, 10L, 1L, "anand.manissery"))
			;
	}
	
	@Test
	public void addApproverTest(){
		Map<String,List<String>>parents = new LinkedHashMap<>();
		parents.put("APPROVED", Arrays.asList("A"));
		Map<String,List<String>>next = new LinkedHashMap<>();
		parents.put("APPROVED", Arrays.asList("C"));
		parents.put("REJECTED", Arrays.asList("D"));

		fixture.given(new DealCreatedEvent("DL1001","DL1001", 1000L, 10L, 1L, "anand.manissery"))
			.when(new AddApproverCommand("DL1001","B","anand.manissery",parents, next))
			.expectEvents(new ApproverAddedEvent("DL1001","B","anand.manissery",parents, next))
			;
	}
	
	@Test
	public void approverResponedTest(){
		fixture.given(new DealCreatedEvent("DL1001","DL1001", 1000L, 10L, 1L, "anand.manissery"),
				new ApproverAddedEvent("DL1001","A","anand.manissery",null, null))
			.when(new ApproverResponseCommand("DL1001","B","anand.manissery","APPROVED"))
			.expectEvents(new ApproverRespondedEvent("DL1001","B","anand.manissery","APPROVED"))
			;
	}
	
}
