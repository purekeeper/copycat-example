package com.anjuke.mss;

import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.netty.NettyTransport;
import io.atomix.copycat.client.ConnectionStrategies;
import io.atomix.copycat.client.CopycatClient;
import io.atomix.copycat.client.RecoveryStrategies;
import io.atomix.copycat.client.ServerSelectionStrategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * Created by root on 16-7-20.
 */
public class mss_client {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        QueryData qdata=new QueryData();
        SetData sdata=new SetData();
        // if (args.length < 1)
        //  throw new IllegalArgumentException("must supply a set of host:port tuples");

        // Build a list of all member addresses to which to connect.
        List<Address> members = new ArrayList<>();
        //  for (String arg : args) {
        //   String[] parts = arg.split(":");
        //members.add(new Address(parts[0], Integer.valueOf(parts[1])));
        members.add(new Address("127.0.0.1",5000));
        //}
//init client
        CopycatClient client = CopycatClient.builder()
                .withTransport(new NettyTransport())
                .withConnectionStrategy(ConnectionStrategies.FIBONACCI_BACKOFF)
                .withRecoveryStrategy(RecoveryStrategies.RECOVER)
                .withServerSelectionStrategy(ServerSelectionStrategies.LEADER)
                .build();
        client.serializer().register(SetCommand.class, 1);
        client.serializer().register(LoadCommand.class,2);
        client.serializer().register(QueryCommand.class, 3);
        client.connect(members).join();
        //AtomicInteger counter = new AtomicInteger();
      //  AtomicLong timer = new AtomicLong();
     /*
        client.context().schedule(Duration.ofSeconds(1), Duration.ofSeconds(1), () -> {
            long count = counter.get();
            long time = System.currentTimeMillis();
            long previousTime = timer.get();
            //if (previousTime > 0) {
            //    System.out.println(String.format("Completed %d writes in %d milliseconds", count, time - previousTime));
            //}
            counter.set(0);
            timer.set(time);
        });
*/
        //qdata.SetDic("/home/yangjian/workspace/copycat/src/dic/anjuke_sale_100.txt");
        qdata.SetDic("F:\\copycat\\src\\dic\\anjuke_sale_100.txt");

        String cmd;
        while(true)
        {
          cmd=sc.next();
            if(cmd.equals("query"))
            {
                System.out.println("input key:");
               // qdata.SetText();

                client.submit(new QueryCommand(sc.next())).thenRun(() ->
                {
                    System.out.println("Query Sucess!!!");
                });
            }

            else if(cmd.equals("set"))
            {
                String keyValue;
                System.out.println("input key:");
               keyValue=sc.next();
                keyValue+=":";
                System.out.println("input value:");
                keyValue+=sc.next();
         //       sdata.SetValue(sc.next());
                client.submit(new SetCommand(keyValue)).thenRun(() ->
                {
                    System.out.println("Set Sucess!!!");
                });
            }
            else if(cmd.equals("load"))
            {
                client.submit(new LoadCommand("anjuke_sal")).thenRun(() ->
                {
                    System.out.println("Load Sucess!!!");
                });
            }
        }

    }

    /**
     * Recursively sets state machine values.
     */
}
