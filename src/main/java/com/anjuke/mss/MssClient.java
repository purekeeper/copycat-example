package com.anjuke.mss;

import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.netty.NettyTransport;
import io.atomix.copycat.client.ConnectionStrategies;
import io.atomix.copycat.client.CopycatClient;
import io.atomix.copycat.client.RecoveryStrategies;
import io.atomix.copycat.client.ServerSelectionStrategies;

import java.util.*;

/**
 * Created by root on 16-7-20.
 */
public class MssClient {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        Map<String,String> key = new HashMap<>();
        Map<String,String> value = new HashMap<>();
        List<Address> members = new ArrayList<>();
        members.add(new Address("127.0.0.1",5000));
        CopycatClient client = CopycatClient.builder()
                .withTransport(new NettyTransport())
                .withConnectionStrategy(ConnectionStrategies.FIBONACCI_BACKOFF)
                .withRecoveryStrategy(RecoveryStrategies.RECOVER)
                .withServerSelectionStrategy(ServerSelectionStrategies.LEADER)
                .build();
        client.serializer().register(SetCommand.class, 1);
        client.serializer().register(QueryCommand.class, 2);
        client.connect(members).join();
        String cmd;
        while(true)
        {
            cmd=sc.next();
            if(cmd.equals("query"))
            {
                client.submit(new QueryCommand("anjuke_sale_1","name")).thenAccept(QueryResult ->
                {

                    for(ResponseData list:(List<ResponseData>)QueryResult)
                    {
                        System.out.println(list);
                        System.out.println(list.getValue().size());
                        System.out.println(list.getValue().toString());
                    }
                    //System.out.println(QueryResult);
                    System.out.println("Query Sucess!!!");
                });
            }
            else if(cmd.equals("set"))
            {
                //SetCommand(String dic,String id,String type,Map key,Map value)
                key.put("name","yangjian");
                value.put("sex","nan");
                client.submit(new SetCommand("anjuke_sale_1","commu|111","community",key,value)).thenRun(() ->
                {
                    System.out.println("Set Sucess!!!");
                });
            }
        }

    }

}
