package com.tensor.api.org.config.solr;

import lombok.extern.slf4j.Slf4j;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.context.annotation.Bean;

/**
 * @author 周悦城
 */
//@Slf4j
//@org.springframework.context.annotation.Configuration
public class SolrConfigure {

//    @Bean(value = "MySolrConnection")
    public HttpSolrClient solrconnection() throws Exception {
        String solrUrl = "http://47.106.181.240:8983/solr/ik";
        HttpSolrClient solrClient = new HttpSolrClient.Builder(solrUrl).build();
        return solrClient;
    }

}
