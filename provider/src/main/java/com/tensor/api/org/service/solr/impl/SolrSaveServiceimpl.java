package com.tensor.api.org.service.solr.impl;

import com.tensor.api.org.enpity.News;
import com.tensor.api.org.service.solr.SolrSaveService;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public  class SolrSaveServiceimpl implements SolrSaveService {

    @Autowired
    private SolrClient solrClient;

    @Override
    public boolean PutNew(News New,String id) throws Exception {

        try{
            SolrInputDocument doc1 = new SolrInputDocument();
            doc1.addField( "id",id);
            doc1.addField( "title", New.getNewTitle());
            doc1.addField("author", New.getAuthor());
            doc1.addField( "text",New.getText());
            doc1.addField("type",New.getNewType());
            //doc1.addField("publish_date",New.getNewPublish_date());
            //doc1.addField("download_date",New.getNewDownload_date());
            solrClient.add(doc1);
            solrClient.commit();
           return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
