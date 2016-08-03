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
    private CopycatServer server=null;
    private String ipAddress;
    private int port;
    private String logLocation;
    //public static void main(String[] args)
    public MssServer(String ipAddress,int port,String logLocation)
    {
        this.ipAddress = ipAddress;
        this.port = port;
        this.logLocation = logLocation;
    }
    public void init(){
        //  if (args.length < 2)
        // throw new IllegalArgumentException("must supply a path and set of host:port tuples");
        //db = DBMaker.fileDB("DB").make();
        // Parse the address to which to bind the server.
        Address address = new Address(ipAddress,port);
        // Build a list of all member addresses to which to connect.
        List<Address> members = new ArrayList<>();
        members.add(address);
         server = CopycatServer.builder(address)
                .withStateMachine(TrieDBStateMachine::new)
                .withTransport(new NettyTransport())
                .withStorage(Storage.builder()
                        .withDirectory(logLocation)
                        .withMaxSegmentSize(1024 * 1024 * 32)
                        //  .withMinorCompactionInterval(Duration.ofMinutes(1))
                        // .withMajorCompactionInterval(Duration.ofMinutes(15))
                        .build())
                .build();
        server.serializer().register(SetCommand.class, 1);
        server.serializer().register(QueryCommand.class, 2);
        server.bootstrap(members).join();
    }
    public void destroy()
    {
      if(server != null)
          server.shutdown();

    }
}
