package com.tensor.api.org.service.solr.impl;

import com.tensor.api.org.service.solr.SolrSearchService;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.client.solrj.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class SolrSearchServiceimpl implements SolrSearchService {
    @Autowired
    private SolrClient solrClient;

    @Override
    public ArrayList<Map> SearchAllNews(int pagenumber) throws Exception {
        ArrayList<Map> resultlist = new ArrayList<Map>();   //设置返回的list
        Map<String, Integer> nummap = new HashMap<String, Integer>(); //存放返回结果数与页数的map
        try {
            SolrQuery query = new SolrQuery();
            query.set("q", "*:*");     //查询全部内容
            query.setStart(pagenumber);  //查询的页数
            query.setRows(60);
            QueryResponse response = solrClient.query(query);
            SolrDocumentList docs = response.getResults();
            int cnt = (int) docs.getNumFound(); // 查询的结果数
            nummap.put("Numfound", cnt);
            nummap.put("start", pagenumber);
            resultlist.add(nummap);
            for (SolrDocument doc : docs) {
                resultlist.add(doc.getFieldValuesMap());
            }
            return resultlist;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Map> SearchNewsByAuthor(int pagenumber, String author) throws Exception {
        ArrayList<Map> resultlist = new ArrayList<Map>();
        Map<String, Integer> nummap = new HashMap<String, Integer>();
        try {
            SolrQuery query = new SolrQuery();
            query.set("q", "author:" + author);
            query.setStart(pagenumber);
            query.setRows(60);
            QueryResponse response = solrClient.query(query);
            SolrDocumentList docs = response.getResults();
            int cnt = (int) docs.getNumFound();
            nummap.put("Numfound", cnt);
            nummap.put("start", pagenumber);
            resultlist.add(nummap);
            for (SolrDocument doc : docs) {
                resultlist.add(doc.getFieldValuesMap());
            }
            return resultlist;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Map> SearchNewsBytitle(int pagenumber, String title) throws Exception {
        ArrayList<Map> resultlist = new ArrayList<Map>();
        Map<String, Integer> nummap = new HashMap<String, Integer>();
        try {
            SolrQuery query = new SolrQuery();
            query.set("q", "title:" + title);
            query.setStart(pagenumber);
            query.setRows(60);
            QueryResponse response = solrClient.query(query);
            SolrDocumentList docs = response.getResults();
            int cnt = (int) docs.getNumFound();
            nummap.put("Numfound", cnt);
            nummap.put("start", pagenumber);
            resultlist.add(nummap);
            for (SolrDocument doc : docs) {
                resultlist.add(doc.getFieldValuesMap());
            }
            return resultlist;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Map> SearchNewsBytype(int pagenumber, String type) throws Exception {

        try {
            return UserdefinedSearch(pagenumber, "name:" + type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Map> SearchNewsBytext(int pagenumber, String article) throws Exception {

        try {
            return UserdefinedSearch(pagenumber, "text:" + article);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Map> SearchNewsBypublish_date(int pagenumber, String publish_date) throws Exception {

        try {
            return UserdefinedSearch(pagenumber, "publish_date:" + publish_date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Map> SearchNewsBydownload_date(int pagenumber, String download_date) throws Exception {

        try {
            return UserdefinedSearch(pagenumber, "download_date:" + download_date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public ArrayList<Map> SearchNewsByid(long id) throws Exception {
        ArrayList<Map> resultlist = new ArrayList<Map>();
        Map<String, Integer> nummap = new HashMap<String, Integer>();

        try {
            SolrQuery query = new SolrQuery();
            query.set("q", "id:" + id);
            query.setStart(0);
            query.setRows(60);
            QueryResponse response = solrClient.query(query);
            SolrDocumentList docs = response.getResults();
            int cnt = (int) docs.getNumFound();
            nummap.put("Numfound", cnt);
            nummap.put("start", 0);
            resultlist.add(nummap);
            for (SolrDocument doc : docs) {
                resultlist.add(doc.getFieldValuesMap());
            }
            return resultlist;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Map> UserdefinedSearch(int pagenumber, String statement) throws Exception {
        ArrayList<Map> resultlist = new ArrayList<Map>();
        Map<String, Integer> nummap = new HashMap<String, Integer>();

        try {
            SolrQuery query = new SolrQuery();
            query.set("q", statement);
            query.setStart(pagenumber);
            query.setRows(60);
            QueryResponse response = solrClient.query(query);
            SolrDocumentList docs = response.getResults();
            int cnt = (int) docs.getNumFound();
            nummap.put("Numfound", cnt);
            nummap.put("start", pagenumber);
            resultlist.add(nummap);
            for (SolrDocument doc : docs) {
                resultlist.add(doc.getFieldValuesMap());
            }
            return resultlist;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
