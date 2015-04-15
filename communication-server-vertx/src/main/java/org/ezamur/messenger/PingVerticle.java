package org.ezamur.messenger;

import static org.ezamur.messenger.Constants.*;

import java.util.Date;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.platform.Verticle;

/**
 * Vert.x example 'simple-pingpong'
 *
 * Example for a Java PingVerticle.
 *
 * Sends initial ping String message to 'ping-address' and receives String pong
 * messages to 'pong-address'. Waits 1 sec and then sends the next ping message.
 *
 * Note that the Java PongVerticle sends a message while the Groovy, Python and
 * JavaScript pong verticles do a reply.
 *
 * @author lehmann, schaal, grammes
 */
public class PingVerticle extends Verticle {
    @Override
    public void start() {
        Handler<Message<String>> handler = new Handler<Message<String>>() {
            @Override
            public void handle(Message<String> message) {
                logInfoString(" receives message " + message.body());

                final Handler<Message<String>> replyHandler = this;

                vertx.setTimer(1000, new Handler<Long>() {
                    @Override
                    public void handle(Long event) {
                        vertx.eventBus().send(BUS_ADDRESS_PING, "ping", replyHandler);
                    }
                });
            }
        };

        vertx.eventBus().registerHandler(BUS_ADDRESS_PONG, handler);
        logInfoString("PingVerticle has been registered for " + BUS_ADDRESS_PONG);

        logInfoString("PingVerticle will now publish first message to " + BUS_ADDRESS_PING + " ...");
        vertx.eventBus().send(BUS_ADDRESS_PING, "ping", handler);
    }

    private void logInfoString(String info) {
        container.logger().info(new Date().getTime() + ": [" + Thread.currentThread().getName() + "] " + this.toString() + info);
    }
}
