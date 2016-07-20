package com.anjuke.mss;

import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.netty.NettyTransport;
import io.atomix.copycat.client.ConnectionStrategies;
import io.atomix.copycat.client.CopycatClient;
import io.atomix.copycat.client.RecoveryStrategies;
import io.atomix.copycat.client.ServerSelectionStrategies;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by root on 16-7-20.
 */
public class mss_client {
    public static void main(String[] args) throws Exception {
        // if (args.length < 1)
        //  throw new IllegalArgumentException("must supply a set of host:port tuples");

        // Build a list of all member addresses to which to connect.
        List<Address> members = new ArrayList<>();
        //  for (String arg : args) {
        //   String[] parts = arg.split(":");
        //members.add(new Address(parts[0], Integer.valueOf(parts[1])));
        members.add(new Address("127.0.0.1",5000));
        //}

        CopycatClient client = CopycatClient.builder()
                .withTransport(new NettyTransport())
                .withConnectionStrategy(ConnectionStrategies.FIBONACCI_BACKOFF)
                .withRecoveryStrategy(RecoveryStrategies.RECOVER)
                .withServerSelectionStrategy(ServerSelectionStrategies.LEADER)
                .build();
        client.serializer().register(SetCommand.class, 1);
        client.serializer().register(QueryCommand.class, 2);

        client.connect(members).join();

        AtomicInteger counter = new AtomicInteger();
        AtomicLong timer = new AtomicLong();
        client.context().schedule(Duration.ofSeconds(1), Duration.ofSeconds(1), () -> {
            long count = counter.get();
            long time = System.currentTimeMillis();
            long previousTime = timer.get();
            if (previousTime > 0) {
                System.out.println(String.format("Completed %d writes in %d milliseconds", count, time - previousTime));
            }
            counter.set(0);
            timer.set(time);
        });

        for (int i = 0; i < 10; i++) {
            recursiveSet(client, counter);
        }

        while (client.state() != CopycatClient.State.CLOSED) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    /**
     * Recursively sets state machine values.
     */
    private static void recursiveSet(CopycatClient client, AtomicInteger counter) {
        client.submit(new SetCommand(UUID.randomUUID().toString())).thenRun(() -> {
            counter.incrementAndGet();
            recursiveSet(client, counter);
        });
    }
}
