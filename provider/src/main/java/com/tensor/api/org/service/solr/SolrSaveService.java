package com.tensor.api.org.service.solr;

import com.tensor.api.org.enpity.News;

public interface SolrSaveService {
   boolean putNew(News New,String id)throws Exception;

}
