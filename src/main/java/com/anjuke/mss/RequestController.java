package com.anjuke.mss;

import com.anjuke.mss.MssClient;
import com.anjuke.mss.ResponseData;
import com.anjuke.mss.WriteData;
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
        return (List<ResponseData>) mssClient.search(dictionary,text);
    }
 /*   @RequestMapping(value = "/")
    public void nothing()
    {
        System.out.println("----nothing-----");
    }*/
}
