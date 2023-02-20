import Reel from "./Reel.js";
import Symbol from "./Symbol.js";

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
    if (document.cookie.indexOf('token=') == -1){
      document.location.href='http://localhost:1488/login'
    }
    const token = document.cookie
        .split('; ')
        .find((row) => row.startsWith('token='))
        ?.split('=')[1];
    const res = await this.parseApi(token);

    this.nextSymbols = res[0].map((_, colIndex) => res.map(row => row[colIndex]));
    this.onSpinStart(this.nextSymbols);

    return Promise.all(
        this.reels.map((reel) => {
          reel.renderSymbols(this.nextSymbols[reel.idx]);
          return reel.spin();
        })
    ).then(() => this.onSpinEnd(this.nextSymbols));
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
    const session = {
      'betValue': '10',
      'lines': '3',
      'credits': '100',
      'betterUserName': 'Bykwarb'
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
      popup.addEventListener("click", function() {
        popup.parentNode.removeChild(popup);
      });
    });
    return res.symbols;
  }
}
