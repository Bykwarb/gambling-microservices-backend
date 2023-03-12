const username =  document.cookie
    .split('; ')
    .find((row) => row.startsWith('username='))
    ?.split('=')[1];
const email =  document.cookie
    .split('; ')
    .find((row) => row.startsWith('email='))
    ?.split('=')[1];
const firstNameInput = document.querySelector('#firstName');
const emailInput = document.querySelector('#email');
firstNameInput.value=username;
emailInput.value=email;
