package com.anjuke.mss;

import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.netty.NettyTransport;
import io.atomix.copycat.server.CopycatServer;
import io.atomix.copycat.server.storage.Storage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 16-7-20.
 */
public class MssServer {

    public static void main(String[] args) throws Exception {
        //  if (args.length < 2)
        // throw new IllegalArgumentException("must supply a path and set of host:port tuples");

        // Parse the address to which to bind the server.
        //String[] mainParts = args[1].split(":");
        // Address address = new Address(mainParts[0], Integer.valueOf(mainParts[1]));
        Address address = new Address("127.0.0.1", 5000);
        // Build a list of all member addresses to which to connect.
        List<Address> members = new ArrayList<>();
        //for (int i = 1; i < args.length; i++) {
        //   String[] parts = args[i].split(":");
        members.add(new Address("127.0.0.1", 5000));

        CopycatServer server = CopycatServer.builder(address)
                .withStateMachine(TrieStateMachine::new)
                .withTransport(new NettyTransport())
                .withStorage(Storage.builder()
                        .withDirectory("/home/yangjian/workspace/copycat/log")
                        //.withDirectory("F:\\copycat\\log")

                        .withMaxSegmentSize(1024 * 1024 * 32)
                        //  .withMinorCompactionInterval(Duration.ofMinutes(1))
                        // .withMajorCompactionInterval(Duration.ofMinutes(15))
                        .build())
                .build();
        server.serializer().register(SetCommand.class, 1);
        server.serializer().register(LoadCommand.class,2);
        server.serializer().register(QueryCommand.class, 3);

        server.bootstrap(members).join();

       /* while (server.isRunning()) {
            Thread.sleep(1000);
        }*/
    }
}
