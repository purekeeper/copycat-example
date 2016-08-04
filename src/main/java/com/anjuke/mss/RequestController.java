package com.anjuke.mss;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by yangjian on 16-8-3.
 */
@Controller
public class RequestController {
    @Resource
    private MssClient mssClient;

    @RequestMapping(value = "/write",method = RequestMethod.POST)
    public @ResponseBody String set(@RequestBody WriteData writeData){
        System.out.println(writeData.toString());
        mssClient.set(writeData);
        return "success!!";
    }
    @RequestMapping(value = "/search",method = RequestMethod.GET)
    public @ResponseBody  List<ResponseData> get(String dictionary, String text) throws ExecutionException, InterruptedException {
        System.out.println("dictionary:"+dictionary+"text:"+text);
        return (List<ResponseData>) mssClient.get(dictionary,text);
    }
 /*   @RequestMapping(value = "/")
    public void nothing()
    {
        System.out.println("----nothing-----");
    }*/
}
