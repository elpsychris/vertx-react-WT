package com.webtrade;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class StaticServer extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) {
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        router.route("/static/*").handler(
                StaticHandler
                        .create("webroot")
                        .setIndexPage("index.html")
        );

        router.route().handler(routingContext -> {

            System.out.println("Incoming request " +
                    routingContext.request().bodyHandler(buffer -> {
                        System.out.println("=| Body: " + buffer.toString());
                    }));

            routingContext.response().sendFile("webroot/index.html");
        });



        server.requestHandler(router::accept).listen(3001, httpServerAsyncResult -> {
            if (httpServerAsyncResult.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(httpServerAsyncResult.cause());
            }
        });
    }
}
