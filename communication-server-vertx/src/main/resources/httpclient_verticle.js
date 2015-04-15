var vertx = require('vertx')
var container = require('vertx/container');
var logger = container.logger;

var client = vertx.createHttpClient().port(8082).host("localhost").maxPoolSize(10);

var responseHandler = function(response) {
	var body = new vertx.Buffer();

	response.bodyHandler(function(body) {
		logger.info(body.toString());
	});
};

for (var i = 0; i < 10; i++) {
	var request = client.post("/", responseHandler).chunked(true);
	logger.info("writing: " + i);
	request.write("" + i);
	request.end();
}

//container.exit();