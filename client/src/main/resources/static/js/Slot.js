import Reel from "./Reel.js";
import Symbol from "./Symbol.js";

const token = document.cookie
    .split('; ')
    .find((row) => row.startsWith('token='))
    ?.split('=')[1];
export default class Slot {
  constructor(domElement, config = {}) {
    Symbol.preload();

    this.currentSymbols = Array.from({length: 3}, () => ["seven", "seven", "seven"]);
    this.nextSymbols = Array.from({length: 3}, () => ["seven", "seven", "seven"]);
    this.container = domElement;

    this.reels = Array.from(this.container.getElementsByClassName("reel")).map(
        (reelContainer, idx) => new Reel(reelContainer, idx, this.currentSymbols[idx])
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
    if (!token) {
      this.showLoginPopup();
      return;
    }
    const isExpired = this.isTokenExpired(token);
    if (isExpired) {
      this.removeCookie("token");
      this.removeCookie("userName");
      this.showLoginPopup();
      return;
    } else {
      this.hideLoginPopup();
    }

    this.currentSymbols = this.nextSymbols;
    const response = await this.parseApi(token);
    this.nextSymbols = response.symbols[0].map((_, colIndex) => response.symbols.map(row => row[colIndex]));
    this.onSpinStart(this.nextSymbols);
    const results = await Promise.all(
        this.reels.map((reel) => {
          reel.renderSymbols(this.nextSymbols[reel.idx]);
          return reel.spin();
        })
    );
    this.onSpinEnd(this.nextSymbols, response);
  }

  onSpinStart(symbols) {
    this.spinButton.disabled = true;
    this.config.onSpinStart?.(symbols);
  }

  onSpinEnd(symbols, response) {
    this.spinButton.disabled = false;
    this.config.onSpinEnd?.(symbols);
    if (response.status === "Win") {
      const popup = document.createElement("div");
      popup.id = "popupW";
      popup.innerHTML = `<p class="win-message">You won ${response.result}</p><small>Click to continue</small>`;
      document.body.appendChild(popup);
      document.addEventListener("click", (event) => {
          popup.parentNode.removeChild(popup);
      });
    }else {
      if (this.autoPlayCheckbox.checked) {
        return window.setTimeout(() => this.spin(), 200);
      }
    }

  }

  async parseApi(userJwt) {
    const session = {
      'betValue': document.getElementById("betValue").innerText,
      'lines': document.getElementById("linesValue").innerText
    };

    const request = new Request("http://localhost:8072/v1/game/slot-machine/play", {
      method: 'POST',
      body: JSON.stringify(session),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': userJwt
      }
    });
    const res = await fetch(request);
    if (res.status === 200) {
      return res.json();
    } else if (res.status === 503 || res.status === 500) {
      const popup = document.createElement("div");
      popup.id = "popup";
      popup.innerHTML = "Service unavailable. Try again in a few minutes";
      document.body.appendChild(popup);
      popup.addEventListener("click", function () {
        popup.parentNode.removeChild(popup);
      });
      throw new Error("Service Unavailable");
    }
  }

  showLoginPopup() {
    if (!document.querySelector("#popupL")) {
      const popup = document.createElement("div");
      popup.id = "popupL";
      popup.innerHTML = `<p>Please login</p><button id="loginBtn">Login</button>`;
      document.body.appendChild(popup);

      const loginBtn = document.getElementById("loginBtn");
      loginBtn.addEventListener("click", () => window.location
          .href = "/login");
    }
  }

  hideLoginPopup() {
    const popup = document.querySelector("#popupL");
    if (popup) {
      popup.remove();
    }
  }

  isTokenExpired(token) {
    const decodedToken = JSON.parse(atob(token.split(".")[1]));
    const now = Math.floor(Date.now() / 1000);
    return decodedToken.exp < now;
  }
  removeCookie(name) {
    document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
  }

}

