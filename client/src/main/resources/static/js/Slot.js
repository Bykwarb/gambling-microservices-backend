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
    const res = await this.parseApi('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCeWt3YXJiIiwicm9sZSI6IlVTRVIiLCJrZXkiOiIkMmEkMTAkVXREOEdBLkNEN1E4cjJYUGVpVXFNT1RlREdrSXBxSzZTVFZrSDREbzhjeEl0U3ZsOGM5eEsiLCJpYXQiOjE2NzY0NDEyNjcsImV4cCI6MTY3NjUyNzY2N30.B9uwDDRjcQ1AOSGDxoiHyrCncohEjzZbAGJuPvBmJyw');
    console.log(res)

    this.nextSymbols = res;

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
      'betterId': '1'
    }
    const request = new Request("http://localhost:8072/v1/game/slot-machine/play", {
      method: 'POST',
      body: JSON.stringify(session),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': userJwt
      }
    });
    const res = await fetch(request).then(response => response.json());
    console.log(res)
    console.log(res.result)
    console.log(res.symbols)
    return res.symbols;
  }

}
