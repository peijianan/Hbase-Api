package com.tensor.api.org.config.solr;

import lombok.extern.slf4j.Slf4j;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * @author 周悦城
 */
@Slf4j
@org.springframework.context.annotation.Configuration
public class SolrConfigure {

    @Autowired
    private SolrClient solrClient;

}
