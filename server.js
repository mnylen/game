var express    = require('express');
var app        = express(); 
var http       = require('http').Server(app);
var io         = require('socket.io')(http);
var dualShock  = require('dualshock-controller');
var controller = dualShock();

app.use(express.static(__dirname + '/public'));

io.on('connection', function(socket) {
    console.log('a user connected');
});

var events = ['error', 'left:move', 'right:move'];
events.forEach(function(eventName) {
    controller.on(eventName, function(data) {
        io.emit(eventName, data);
    });
});

app.get('/', function(req, res) {
    res.sendFile('index.html', { root : __dirname + ' /public' });
});

http.listen(3000, function() {
    console.log('listening on *:3000');
});
