package com.tensor.api.org.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.*;

@Slf4j
public class IDUtilsTest {

    @Test
    public void buildId() {
        log.info(IDUtils.buildId().toString());
    }
}