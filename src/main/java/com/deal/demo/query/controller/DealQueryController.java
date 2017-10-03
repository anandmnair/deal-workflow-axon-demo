package com.deal.demo.query.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deal.demo.common.entity.DealDocument;
import com.deal.demo.common.repository.DealRepository;

@RestController
@RequestMapping("/deal-query")
public class DealQueryController {

	@Autowired
	private DealRepository dealRepository;
	
	@GetMapping
	public List<DealDocument> getAllDeal() {
		Page<DealDocument> page = dealRepository.findAll(new PageRequest(0, 100));
		return page.getContent();
	}
	
	@GetMapping(value="/{dealId}")
	public DealDocument getDeal(@PathVariable("dealId")String dealId) {
		return dealRepository.findOne(dealId);
	}
	
}
