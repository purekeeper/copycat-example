package com.anjuke.mss;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.netty.NettyTransport;
import io.atomix.copycat.server.CopycatServer;
import io.atomix.copycat.server.StateMachine;
import io.atomix.copycat.server.storage.Storage;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by root on 16-7-20.
 */
public class MssServer {
    private CopycatServer server=null;
    private List<String> ipAddress;
    private int port;
    private String logLocation;
    @Resource
    private  TrieDBStateMachine trieDBStateMachine;
    public MssServer(List<String> ipAddress,int port,String logLocation)
    {
        this.ipAddress = ipAddress;
        this.port = port;
        this.logLocation = logLocation;
    }
    Supplier<StateMachine> getTrieDBStateMachine = new Supplier<StateMachine>() {
        @Override
        public TrieDBStateMachine get() {
            return trieDBStateMachine;
        }
    };
    public void init(){
        List<Address> members = new ArrayList<>();
        for (String addr:ipAddress)
        // Build a list of all member addresses to which to connect.
        members.add(new Address(addr,port));
         server = CopycatServer.builder(new Address(ipAddress.get(0),port))
                .withStateMachine(getTrieDBStateMachine)
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
