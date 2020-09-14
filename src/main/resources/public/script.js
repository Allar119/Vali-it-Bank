let accountNr = document.getElementById("accountNr");
let amount = document.getElementById("amount");
let accountForm = document.getElementById("accountFrom");
let accountTo = document.getElementById("accountTo");
let amountT = document.getElementById("amountT");

let balanceButton = document.getElementById("balance");
let depositButton = document.getElementById("deposit");
let withdrawButton = document.getElementById("withdraw");
let transferButton = document.getElementById('transfer');
let allAccountButton = document.getElementById("accounts");
let userDetailsButton = document.getElementById("userdetails");

let text1 = document.getElementById("text1");
let text2 = document.getElementById("text2");
let txt = document.getElementById("myData");

userDetailsButton.onclick = function checkBalance() {

    fetch('api/userdetails', {
        method: 'GET',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(function(response) {
            return response.json();
        })
        .then(function(jsonData) {
            console.log(jsonData)
        })
        .catch(function(err) {
            console.log('This does not seem right');
        });
}

balanceButton.onclick = function checkBalance() {

    fetch('/api/' + accountNr.value, {
        method: 'GET',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(function(response) {
            return response.json();
        })
        .then(function(jsonData) {
              text1.innerText = "Account Balance: " + jsonData + " €";
        })
        .catch(function(err) {
            console.log('This does not seem right');
        });
}

depositButton.onclick = function depositMoney(){

    fetch('/api/deposit', {
        method: 'PUT',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            accountNr: accountNr.value,
            amount: amount.value,
        })
    })
        .then(function(response) {
            return response.json();
        })
        .then(function(jsonData) {
            text1.innerText = "Account Balance: " + jsonData + " €";
        })
        .catch(function(err) {
            console.log('This does not seem right');
        });

}

withdrawButton.onclick = function withdrawMoney(){
    fetch('/api/withdraw', {
        method: 'PUT',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            accountNr: accountNr.value,
            amount: amount.value,
        })
    })
        .then(function(response) {
            return response.json();
        })
        .then(function(jsonData) {
            text1.innerText = "Account Balance: " + jsonData + " €";
        })
        .catch(function(err) {
            console.log('This does not seem right');
        });

}

transferButton.onclick = function transferMoney(){

    text2.innerText = "";

    fetch('/api/transfer', {
        method: 'PUT',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            accountFrom: accountForm.value,
            accountTo: accountTo.value,
            amount: amountT.value,
        })
    })
        .then(function(response) {
            return response.json();
        })
        .then(function(jsonData) {
            text2.innerText = jsonData + "€ Transferred";
        })
        .catch(function(err) {
            console.log('This does not seem right');
        });

}

allAccountButton.onclick = function getAllAccount(){

    console.log("press");
    let object;

    fetch('/api/getallaccounts', {
        method: 'GET',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(function(response) {
            return response.json();
        })
        .then(function(jsonData) {
            appendData(jsonData);

        })
        .catch(function(err) {
            console.log('This does not seem right');
            console.log('error: ' + err);
        });

    function appendData(jsonData) {
        for (let i = 0; i < jsonData.length; i++) {
            let div = document.createElement("div");
            div.innerHTML = "ID: " + jsonData[i].id
                + " Account nr: " + jsonData[i].accountNr
                + " Balance: " + jsonData[i].accountBalance
                + " Client ID: " + jsonData[i].clientId;
            txt.appendChild(div);
        }
    }
}






