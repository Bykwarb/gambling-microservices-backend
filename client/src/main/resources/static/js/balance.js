
const token = document.cookie
    .split('; ')
    .find((row) => row.startsWith('token='))
    ?.split('=')[1];
const balanceEl = document.getElementById('balance');
fetch(`http://localhost:8072/v1/wallet/get/user-name/`, {
    method: 'GET',
    headers: {
        'Authorization': token
    }
})
    .then(function(response) {
        if (response.status === 503) {
            throw new Error('503 Service Unavailable');
        }
        return response.json();
    })
    .then(function(data) {
        balanceEl.innerText = 'Balance: ' + data.value;
    })
    .catch(function(error) {
        console.error(error);
        balanceEl.innerText = 'Balance: 0';
    });


var socket = new SockJS('http://localhost:7218/ws');
var stompClient = Stomp.over(socket);
stompClient.connect({}, function(frame) {
    console.log("onConnect");
    stompClient.subscribe('/user/Bykwarb/balance', message =>{
        var balance = JSON.parse(message.body);
        balanceEl.innerText = 'Balance: ' + balance;
    });
});
