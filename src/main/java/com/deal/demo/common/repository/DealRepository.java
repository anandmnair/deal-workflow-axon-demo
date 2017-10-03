package com.deal.demo.common.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.deal.demo.common.entity.DealDocument;

@Repository
public interface DealRepository extends ElasticsearchRepository<DealDocument, String> {
	List<DealDocument> findAll();
}
