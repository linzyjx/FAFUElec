package com.fafu.app.dfb

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import org.junit.Test

import org.junit.Assert.*
import java.io.FileReader
import java.util.*
import java.io.BufferedReader


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val `in` = BufferedReader(FileReader("D:\\AndroidProjects\\DFBao\\app\\src\\main\\assets\\data.json"))
        var str: String?
        var json = ""
        while (true) {
            str = `in`.readLine()
            if (str == null) break;
            json += str
        }
//        json = json.replace(" ", "")
        println(json)
        val jo = JSONObject.parseArray(json)
        println(jo.toJSONString())

        val j1 = JSONObject.parseArray(json, DFInfo::class.java)
        println(j1)
    }


}