import Reel from "./Reel.js";
import Symbol from "./Symbol.js";

const token = document.cookie
    .split('; ')
    .find((row) => row.startsWith('token='))
    ?.split('=')[1];
const betValueEl = document.getElementById("betValue").innerText;
const linesValueEl = document.getElementById("linesValue").innerText;
const balanceEl = document.getElementById('balance');
var value;
export default class Slot {
  constructor(domElement, config = {}) {
    Symbol.preload();

    this.currentSymbols = [
      ["seven", "seven", "seven"],
      ["seven", "seven", "seven"],
      ["seven", "seven", "seven"],
    ];

    this.nextSymbols = [
      ["seven", "seven", "seven"],
      ["seven", "seven", "seven"],
      ["seven", "seven", "seven"],
    ];

    this.container = domElement;

    this.reels = Array.from(this.container.getElementsByClassName("reel")).map(
      (reelContainer, idx) =>
        new Reel(reelContainer, idx, this.currentSymbols[idx])
    );

    this.spinButton = document.getElementById("spin");
    this.spinButton.addEventListener("click", () => this.spin());

    this.autoPlayCheckbox = document.getElementById("autoplay");

    if (config.inverted) {
      this.container.classList.add("inverted");
    }

    this.config = config;
  }

  async spin() {
    this.currentSymbols = this.nextSymbols;
    if (!document.cookie.includes("token")) {
      // создаем всплывающее окно
      let popup = document.createElement("div");
      popup.className = "popup";
      popup.innerHTML = `<p>Please login</p>
    <button id="loginBtn">Login</button>`;

      // стилизуем всплывающее окно с помощью CSS
      popup.style.position = "fixed";
      popup.style.top = "50%";
      popup.style.left = "50%";
      popup.style.transform = "translate(-50%, -50%)";
      popup.style.backgroundColor = "#ffffff";
      popup.style.padding = "30px";
      popup.style.border = "2px solid #007bff";
      popup.style.borderRadius = "10px";
      popup.style.boxShadow = "0 0 10px rgba(0, 0, 0, 0.5)";
      popup.style.textAlign = "center";
      popup.style.width = "400px";

      // стилизуем заголовок
      let title = popup.querySelector("p");
      title.style.fontSize = "24px";
      title.style.fontWeight = "bold";
      title.style.marginTop = "0";

      // стилизуем кнопку логина
      let loginBtn = popup.querySelector("#loginBtn");
      loginBtn.style.backgroundColor = "#007bff";
      loginBtn.style.color = "#ffffff";
      loginBtn.style.border = "none";
      loginBtn.style.padding = "10px 20px";
      loginBtn.style.borderRadius = "5px";
      loginBtn.style.cursor = "pointer";
      loginBtn.style.marginTop = "20px";

      // добавляем всплывающее окно на страницу
      document.body.appendChild(popup);

      // добавляем обработчик события на кнопку перенаправления на страницу логина
      loginBtn.addEventListener("click", function () {
        window.location.href = "/login"; // здесь нужно указать адрес страницы логина
      });
    }

    const response = await this.parseApi(token);
    this.nextSymbols = response.symbols[0].map((_, colIndex) => response.symbols.map(row => row[colIndex]));
    this.onSpinStart(this.nextSymbols);
    return Promise.all(
        this.reels.map((reel) => {
          reel.renderSymbols(this.nextSymbols[reel.idx]);
          return reel.spin();
        })
    ).then(() => this.onSpinEnd(this.nextSymbols)).then(() => {
      value += response.result;
      balanceEl.innerText = `Balance: ${value}`
    });
  }

  onSpinStart(symbols) {
    this.spinButton.disabled = true;

    this.config.onSpinStart?.(symbols);
  }

  onSpinEnd(symbols) {
    this.spinButton.disabled = false;
    this.config.onSpinEnd?.(symbols);
    if (this.autoPlayCheckbox.checked) {
      return window.setTimeout(() => this.spin(), 200);
    }
  }
  async parseApi(userJwt) {
    console.log(betValueEl, linesValueEl)
    value -= betValueEl;
    balanceEl.innerText = `Balance: ${value}`
    const session = {
      'betValue': betValueEl,
      'lines': linesValueEl
    }
    const request = new Request("http://localhost:8072/v1/game/slot-machine/play", {
      method: 'POST',
      body: JSON.stringify(session),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': userJwt
      }
    });
    const res = await fetch(request).then(response => {
      if (response.status === 200) {
        return response.json();
      } else if (response.status === 503 || response.status === 500) {
        throw new Error("Service Unavailable");
      }
    }).catch(error => {
      var popup = document.createElement("div");
      popup.id = "popup";
      popup.innerHTML = "Service unavailable. Try again in a few minutes";
      document.body.appendChild(popup);

// стилизуем всплывающее окно с помощью CSS
      popup.style.position = "fixed";
      popup.style.top = "50%";
      popup.style.left = "50%";
      popup.style.transform = "translate(-50%, -50%)";
      popup.style.backgroundColor = "#ffffff";
      popup.style.padding = "30px";
      popup.style.border = "2px solid #dc3545";
      popup.style.borderRadius = "10px";
      popup.style.boxShadow = "0 0 10px rgba(0, 0, 0, 0.5)";
      popup.style.textAlign = "center";
      popup.style.width = "400px";

// стилизуем текст сообщения
      popup.style.fontSize = "24px";
      popup.style.color = "#dc3545";
      popup.style.fontWeight = "bold";
      popup.style.marginTop = "0";

// добавляем обработчик события на клик по окну
      popup.addEventListener("click", function () {
        popup.parentNode.removeChild(popup);
      });
    });
    return res;
  }

}
fetch(`http://localhost:8072/v1/wallet/get/user-name/`, {
  method: 'GET',
  headers: {
    'Authorization': token
  }
})
    .then(response => response.json())
    .then(data => {
      value = data.value;
      balanceEl.innerText = `Balance: ${value}`;
    })
    .catch(error => console.error(error));

