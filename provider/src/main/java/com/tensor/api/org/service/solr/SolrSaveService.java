package com.tensor.api.org.service.solr;

import com.tensor.api.org.enpity.News;

public interface SolrSaveService {
   boolean PutNew(News New,String id)throws Exception;

}
