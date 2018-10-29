package com.gaurav.grpc.example.helloworld;

import com.gaurav.grpc.examples.helloworld.GreeterGrpc;
import com.gaurav.grpc.examples.helloworld.HelloReply;
import com.gaurav.grpc.examples.helloworld.HelloRequest;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.omg.SendingContext.RunTime;

import java.io.IOException;
import java.util.logging.Logger;

public class HelloWorldServer {
    private Server server;
    private static final Logger logger = Logger.getLogger(HelloWorldServer.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        final HelloWorldServer server = new HelloWorldServer();
        server.start();
        server.blockUntilShutdown();
    }

    public HelloWorldServer() {
        int port = 50051;
        this.server = ServerBuilder.forPort(port)
                .addService(new GreeterImpl()).build();
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (this.server != null) {
            this.server.awaitTermination();
        }
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void start() throws IOException {
        this.server.start();
        logger.info("Server Started...");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            HelloWorldServer.this.stop();
            System.err.println("*** server shut down");
        }));
    }

    static class GreeterImpl extends GreeterGrpc.GreeterImplBase {
        @Override
        public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
            HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }
}
