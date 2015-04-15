var vertx = require('vertx')
var console = require('vertx/console')

vertx.eventBus.registerHandler('ping-address', function(message, replier) {
    console.log(new Date() + ' - Receives message: ' + message);
    replier('pong-from-javascript!');
    console.log('Sent back pong from JavaScript!');
});