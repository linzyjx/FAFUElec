package com.fafu.app.dfb;

import com.alibaba.fastjson.JSONObject;

import org.junit.Test;

public class UnitTest {
    @Test
    public void test() {
        DFInfo n = JSONObject.parseObject("{\"name\":\"北区2号楼\",\"id\":\"2\"}", DFInfo.class);
        System.out.println(n);
    }
}
