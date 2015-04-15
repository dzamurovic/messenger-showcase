package org.ezamur.messenger;

import java.util.Date;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

public class ServerVerticle extends Verticle {

    @Override
    public void start() {
        final Logger logger = container.logger();

        JsonObject config = new JsonObject();
        config.putNumber("port", 8080);
        config.putString("host", "localhost");
        config.putBoolean("bridge", false);

        HttpServer server = vertx.createHttpServer();
        server.requestHandler(new Handler<HttpServerRequest>() {
            @Override
            public void handle(final HttpServerRequest event) {
                logger.info(getTime() + " Request ack!!!");

                event.dataHandler(new Handler<Buffer>() {
                    @Override
                    public void handle(Buffer buffer) {
                        Integer value = Integer.parseInt(buffer.getString(0, buffer.length()));

                        HttpServerResponse response = event.response();
                        response.setChunked(true);
                        response.setStatusCode(200).setStatusMessage("OK");

                        if (value % 2 == 0) {
                            logger.info(getTime() + " Request body: " + value % 2);
                            response.write("Even number: " + value);
                        } else {
                            logger.info(getTime() + " Request body: " + value % 2);
                            response.write("Odd number: " + value);
                        }

                        response.end();
                        //                        response.close();
                    }
                });
            }
        }).listen(8082, "localhost");

    }

    private long getTime() {
        return new Date().getTime();
    }
}
