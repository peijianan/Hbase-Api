package com.tensor.api.org.service.hbase.impl;

import com.tensor.api.org.enpity.ResultData;
import com.tensor.api.org.service.hbase.HBaseBasicService;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Mono;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureWebFlux
public class HBaseBasicServiceImplTest {

    @Autowired
    private HBaseBasicService hBaseBasicService;

    @Test
    public void test() {
        Result result = null;
        try {
            result = hBaseBasicService.getdata("News", "304c19e262-8010-4d1b-89b7-bf324a6c0c13", "cf1", "author");
            assert result != null;
            result.listCells().stream().peek(cell -> System.out.println(new String(CellUtil.cloneRow(cell)) + "\t"
                    + new String(CellUtil.cloneFamily(cell)) + "\t"
                    + new String(CellUtil.cloneQualifier(cell)) + "\t"
                    + new String(CellUtil.cloneValue(cell)) + "\t"
                    + cell.getTimestamp())).count();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void example() {
        Mono.justOrEmpty(new ResultData<>()).subscribe(System.out::println);
    }

}