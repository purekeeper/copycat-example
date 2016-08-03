package com.anjuke.mss;

import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.netty.NettyTransport;
import io.atomix.copycat.client.ConnectionStrategies;
import io.atomix.copycat.client.CopycatClient;
import io.atomix.copycat.client.RecoveryStrategies;
import io.atomix.copycat.client.ServerSelectionStrategies;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by root on 16-7-20.
 */
@Controller
public class MssClient {
     private String ipAddress;
     private int port;
     private CopycatClient client = null;
  //  public static void main(String[] args) throws Exception
    public MssClient(String ipAddress,int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }
       public void init(){
        //Scanner sc = new Scanner(System.in);
        List<Address> members = new ArrayList<>();
        members.add(new Address(ipAddress,port));
           client = CopycatClient.builder()
                .withTransport(new NettyTransport())
                .withConnectionStrategy(ConnectionStrategies.FIBONACCI_BACKOFF)
                .withRecoveryStrategy(RecoveryStrategies.RECOVER)
                .withServerSelectionStrategy(ServerSelectionStrategies.LEADER)
                .build();
        client.serializer().register(SetCommand.class, 1);
        client.serializer().register(QueryCommand.class, 2);
        client.connect(members).join();
    }
    public void set(WriteData writeData) {
        client.submit(new SetCommand(
                                    writeData.getDictionary(),
                                    writeData.getId(),
                                    writeData.getType(),
                                    writeData.getKeyWord(),
                                    writeData.getValue()
        )).thenRun(() ->
        {
            System.out.println("Set Sucess!!!");
        }
        );
    }
    public Object get(String dic,String text) throws ExecutionException, InterruptedException {
        CompletableFuture<List<ResponseData>> ct= client.submit(new QueryCommand(dic, text));
        ct.thenAccept(result ->
        {
         //   result = QueryResult;
            for (ResponseData list :  result) {
                System.out.println(list);
                System.out.println(list.getValue().size());
                System.out.println(list.getValue().toString());
            }
            //System.out.println(QueryResult);
            System.out.println("Query Sucess!!!");
        });
            return  ct.get();
    }
public void destroy()
{
    if(client != null)
        client.close();
}
}
