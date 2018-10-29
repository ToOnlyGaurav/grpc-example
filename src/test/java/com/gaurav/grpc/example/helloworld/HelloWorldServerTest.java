package com.gaurav.grpc.example.helloworld;

import com.gaurav.grpc.examples.helloworld.GreeterGrpc;
import com.gaurav.grpc.examples.helloworld.HelloReply;
import com.gaurav.grpc.examples.helloworld.HelloRequest;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class HelloWorldServerTest {
    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @Test
    public void greeterImpl_replyMessage() throws IOException {
        String serverName = InProcessServerBuilder.generateName();

        grpcCleanup.register(InProcessServerBuilder.forName(serverName).directExecutor().addService(new HelloWorldServer.GreeterImpl()).build()).start();

        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(
                grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build()));
        HelloRequest request = HelloRequest.newBuilder().setName("test name").build();
        HelloReply reply = blockingStub.sayHello(request);
        assertEquals("Hello test name", reply.getMessage());
        System.out.println(reply.getMessage());
    }

}
