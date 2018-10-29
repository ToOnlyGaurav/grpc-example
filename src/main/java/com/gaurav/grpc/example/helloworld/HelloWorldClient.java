package com.gaurav.grpc.example.helloworld;

import com.gaurav.grpc.examples.helloworld.GreeterGrpc;
import com.gaurav.grpc.examples.helloworld.HelloReply;
import com.gaurav.grpc.examples.helloworld.HelloRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class HelloWorldClient {
    private final ManagedChannel channel;
    private final GreeterGrpc.GreeterBlockingStub blockingStub;
    private static final Logger logger = Logger.getLogger(HelloWorldClient.class.getSimpleName());

    public HelloWorldClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext().build());
    }

    private HelloWorldClient(ManagedChannel channel) {
        this.channel = channel;
        this.blockingStub = GreeterGrpc.newBlockingStub(channel);
    }

    private void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    private void greet(String user) {
        logger.info("Will try to greet: " + user);
        HelloRequest request = HelloRequest.newBuilder().setName(user).build();

        HelloReply reply = blockingStub.sayHello(request);
        logger.info("Greeting : " + reply.getMessage());
    }

    public static void main(String[] args) throws InterruptedException {
        HelloWorldClient client = new HelloWorldClient("localhost", 50051);
        String user = "world";
        if (args.length > 0) {
            user = args[0];

        }
        try {
            client.greet(user);
        } finally {
            client.shutdown();
        }
    }

}
