package com.xielu.arch.platform.server.util;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * {
 * "path": "/nc/v1/tile/qvf",
 * "method": "POST"
 * "body":"11111"
 * "numarea":"100,200"
 * }
 */

public class WrkScriptEngine {

    /**
     * 支持随机数，随机字符串，uuid, timestamp
     *
     * @param json
     * @return
     */

    @Async
    public String getGenScript(JSONObject json) {
        //生成一个lua脚本并上传
        String script = "";
        switch (json.getString("method")) {
            case "GET":
                String path = json.getString("path");
                if (path.contains("{RandomNum}")) {
                    path = getGenRandomAndTimestamp(path, "RandomNum", json.getString("numarea"));
                }
                if (path.contains("{RandomStr}")) {
                    path = getGenRandomAndTimestamp(path, "RandomStr", "");
                }
                if (path.contains("{RandomUuid}")) {
                    path = getGenRandomAndTimestamp(path, "RandomUuid", "");
                }
                if (path.contains("{timestamp}")) {
                    path = getGenRandomAndTimestamp(path, "timestamp", "");
                }
                script += "req = wrk.format(\"GET\",\"" + path + "\"" + "\n";
                break;
            case "POST":
                String body = json.getString("body");
                if (body.contains("{RandomNum}")) {
                    body = getGenRandomAndTimestamp(body, "RandomNum", json.getString("numarea"));
                }
                if (body.contains("{RandomStr}")) {
                    body = getGenRandomAndTimestamp(body, "RandomStr", "");
                }
                if (body.contains("{RandomUuid}")) {
                    body = getGenRandomAndTimestamp(body, "RandomUuid", "");
                }
                if (body.contains("{timestamp}")) {
                    body = getGenRandomAndTimestamp(body, "timestamp", "");
                }

                script += "req = wrk.format(\"POST\",\"" + json.getString("path") + "\",{[\"Content-Type\"] = \"application/json;charset=UTF-8\"}" + ",\"" + body + "\")\n";
                break;
            default:
                break;
        }
        script = scriptGen(script);



        return script;
    }

    @SneakyThrows
    public String getGenRandomAndTimestamp(String origin, String randomType, String numArea) {
        if (StringUtils.isEmpty(origin)) {
            return origin;
        }
        int start = 0;
        int end = 0;
        if (!StringUtils.isEmpty(numArea)) {
            start = Integer.parseInt(numArea.split(",")[0]);
            end = Integer.parseInt(numArea.split(",")[1]);
        }
        String newRandom = "";

        switch (randomType) {
            case "{RandomNum}":
                newRandom = origin.replace("{RandomNum}", "..tostring(randomNum(" + start + "," + end + "))..");
                break;
            case "{RandomStr}":
                newRandom = origin.replace("{RandomStr}", "..tostring(randomStr())..");
                break;
            case "{timestamp}":
                newRandom = origin.replace("{timestamp}", "..tostring(gettimeofday())..");
                break;
            case "{RandomUUID}":
                newRandom = origin.replace("{RandomUUID}", "..tostring(randomUuid())..");
                break;
            default:
                break;
        }
        return newRandom;
    }

    /**
     * 开始拼凑出一个脚本
     *
     * @param script
     * @return /**
     * * 最终生成效果：
     * *function randomNum(startNum,endNum)
     * *     math.randomseed(os.time() + assert(tonumber(tostring({}):sub(7))))
     * *     counter = tostring(math.random(startNum,endNum))
     * *     return counter
     * * end
     * *
     * * function randomStr()
     * *     local template ='xxxxxxxxxxxx'
     * *     math.randomseed(os.time() + assert(tonumber(tostring({}):sub(7))))
     * *     return string.gsub(template, '[xy]', function (c)
     * *         local v = (c == 'x') and math.random(0, 0xf) or math.random(8, 0xb)
     * *         return string.format('%x', v)
     * *     end)
     * * end
     * *
     * * function randomUuid()
     * *     local template ='xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'
     * *     math.randomseed(os.time() + assert(tonumber(tostring({}):sub(7))))
     * *     return string.gsub(template, '[xy]', function (c)
     * *         local v = (c == 'x') and math.random(0, 0xf) or math.random(8, 0xb)
     * *         return string.format('%x', v)
     * *     end)
     * * end
     * *
     * * if pcall(ffi.typeof, "struct timeval") then
     * * else
     * *     ffi.cdef[[
     * *            typedef struct timeval {
     * *                 long tv_sec;
     * *                 long tv_usec;
     * *            } timeval;
     * *
     * *         int gettimeofday(struct timeval* t, void* tzp);
     * * ]]
     * * end
     * * local gettimeofday_struct = ffi.new("struct timeval")
     * * function gettimeofday()
     * *     ffi.C.gettimeofday(gettimeofday_struct, nil)
     * *     return tonumber(gettimeofday_struct.tv_sec) * 1000000 + tonumber(gettimeofday_struct.tv_usec)
     * * end
     * *
     * * function request()
     * *     local p={}
     * *     p[0] = wrk.format("GET", path1)
     * *     p[1] = wrk.format("POST",path,headers,newer)
     * *     req=table.concat(p)
     * *     return req
     * * end
     * *
     * *
     */

    @SneakyThrows
    public String scriptGen(String script) {
        String result = "";
        Resource resource = new ClassPathResource("wrk-template.lua");
        File file = resource.getFile();
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "utf-8");
        BufferedReader bfReader = new BufferedReader(reader);
        String read = bfReader.toString();
        result = read + script;
        result += "return req\nend";
        return result;
    }


}
