const token = document.cookie
    .split('; ')
    .find((row) => row.startsWith('token='))
    ?.split('=')[1];
const username =  document.cookie
    .split('; ')
    .find((row) => row.startsWith('username='))
    ?.split('=')[1];
const uBalance = document.getElementById('ubalance');
const cBalance = document.getElementById('cbalance');
const depositForm = document.querySelector('.deposit-form');
const withdrawForm = document.querySelector('.withdraw-form');
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
        uBalance.innerText = data.value + ' BBC';
        cBalance.innerText = data.value + ' BBC';
    })
    .catch(function(error) {
        console.error(error);
        uBalance.innerText = '0';
        cBalance.innerText = '0';
    });

var socket = new SockJS('http://localhost:7218/ws');
var stompClient = Stomp.over(socket);
stompClient.connect({}, function(frame) {
    console.log("onConnect");
    stompClient.subscribe('/user/' + username +'/balance', message =>{
        var balance = JSON.parse(message.body);
        uBalance.innerText = balance + ' BBC';
        cBalance.innerText = balance + ' BBC';
    });
});
function handleSubmit(event, formType) {
    event.preventDefault();

    const value = formType === 'deposit' ? document.getElementById('deposit-amount').value : document.getElementById('withdraw-amount').value;
    if (formType === 'deposit'){
        fetch('http://localhost:8072/v1/wallet/deposit/user-name/' + username + '?value=' + document.getElementById('deposit-amount').value, {
            method: 'PUT',
            headers: {
                'Authorization': token,
            },
        })
            .then(response => {
                // handle response
            })
            .catch(error => {
                // handle error
            });
    }else if (formType === 'withdraw'){
        fetch('http://localhost:8072/v1/wallet/debited/user-name/' + username + '?debited-value=' + document.getElementById('withdraw-amount').value, {
            method: 'PUT',
            headers: {
                'Authorization': token,
            },
        })
            .then(response => {
                // handle response
            })
            .catch(error => {
                // handle error
            });
    }

}

depositForm.addEventListener('submit', event => handleSubmit(event, 'deposit'));
withdrawForm.addEventListener('submit', event => handleSubmit(event, 'withdraw'));
