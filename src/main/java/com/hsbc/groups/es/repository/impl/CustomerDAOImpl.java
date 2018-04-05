package com.hsbc.groups.es.repository.impl;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hsbc.groups.es.domain.Customer;
import com.hsbc.groups.es.repository.ICustomerDAO;

@Repository
public class CustomerDAOImpl implements ICustomerDAO {
	
    @Autowired
    private RestHighLevelClient restHighLevelClient;

	@Override
	public List<?> queryByConditions(String index, String type, String id,Map<String, Object> params) throws Exception {
        SearchRequest searchRequest = new SearchRequest(index)
        		.types(type)
        		.source(new SearchSourceBuilder()
        		        .from(0).size(10)
//        		        .fetchSource(new String[]{(String) params.get("title")}, new String[]{})
        		        .query(QueryBuilders.matchQuery("title", (String) params.get("title")))
        		        .query(QueryBuilders.termQuery("tag", (String) params.get("tag")))
        		        .query(QueryBuilders.rangeQuery("publishTime")
        		        		.gte((String) params.get("from"))
        		        		.lte((String) params.get("to")))
        		        .query(QueryBuilders.boolQuery().must(QueryBuilders.prefixQuery("name", (String) params.get("yang")))));
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            List<SearchHit> list = Arrays.asList(searchResponse.getHits().getHits());
            
		return list;
	}

	@Override
	public boolean save(String index, String type, String id, Map dataSource) throws IOException {
		boolean success = false;
		if(id.isEmpty()) {
			id = UUID.randomUUID().toString();
			IndexRequest indexRequest = new IndexRequest(index, type, id)
					.source(dataSource);
			IndexResponse indexResponse = restHighLevelClient.index(indexRequest);
			success = indexResponse.status().getStatus() == 200;
		}else {
			UpdateRequest updateRequest = new UpdateRequest(index, type, id)
					.fetchSource(true);
		        updateRequest.doc(dataSource);
		    UpdateResponse updateResponse = restHighLevelClient.update(updateRequest);
		    success = updateResponse.status().getStatus() == 200;
		}
		return success;
	}

	@Override
	public Map<String,Object> queryOne(String index, String type, String id) throws Exception {
		GetRequest  getRequest = new GetRequest(index,type,id);
		return restHighLevelClient.get(getRequest).getSourceAsMap();
	}

	@Override
	public boolean delete(String index, String type, String id) throws Exception {
		DeleteRequest deleteRequest = new DeleteRequest(index,type,id);
		DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest);
		return deleteResponse.status().getStatus() == 200;
	}

}
