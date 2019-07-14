package com.fafu.app.dfb;

import com.alibaba.fastjson.JSONObject;

import org.junit.Test;

public class ExampleUnitTest {

    @Test
    public void unitTest() {
        String jsonText = "{\"IsSucceed\":false,\"Msg\":\"{\\\"query_card\\\":{\\\"retcode\\\":\\\"0\\\", \\\"errmsg\\\":\\\"查询成功！\\\", \\\"card\\\":[{ \\\"account\\\":\\\"88988\\\", \\\"name\\\":\\\"翁凯强\\\", \\\"unsettle_amount\\\":\\\"-2000\\\",  \\\"db_balance\\\":\\\"3000\\\", \\\"acc_status\\\":\\\"0\\\", \\\"lostflag\\\":\\\"0\\\", \\\"freezeflag\\\":\\\"0\\\",  \\\"barflag\\\":\\\"0\\\",\\\"idflag\\\":\\\"1\\\",  \\\"expdate\\\":\\\"20210922\\\", \\\"cardtype\\\":\\\"800\\\", \\\"cardname\\\":\\\"校园卡\\\", \\\"bankacc\\\":\\\"\\\",  \\\"sno\\\":\\\"3176016051\\\", \\\"phone\\\":\\\"\\\", \\\"certtype\\\":\\\"001\\\", \\\"cert\\\":\\\"350822199904205117\\\",  \\\"createdate\\\":\\\"20180902\\\", \\\"autotrans_limite\\\":\\\"2000\\\", \\\"autotrans_amt\\\":\\\"5000\\\", \\\"autotrans_flag\\\":\\\"1\\\",  \\\"mscard\\\":\\\"0\\\", \\\"scard_num\\\":\\\"0\\\" }]}}\",\"RMsg\":null,\"Obj\":null,\"Obj2\":null,\"Flag\":null,\"Flag7\":null,\"OrderID\":null,\"SoftAuth\":null,\"DownUrl\":null,\"Tel\":null,\"CopyRight\":null,\"AnswerUrl\":null,\"LogoutUrl\":null,\"ChangeLogonUrl\":null,\"GiveContent\":null,\"ServerUrl\":null,\"CardbagUrl\":null,\"PayFlag\":null,\"NO\":null}";
        JSONObject jo = JSONObject.parseObject(jsonText)
                .getJSONObject("Msg")
                .getJSONObject("query_card")
                .getJSONArray("card")
                .getJSONObject(0);
        double total = (jo.getIntValue("db_balance") + jo.getIntValue("unsettle_amount")) / 100.0;
        System.out.println(String.format("%.2f", total));
    }
}
