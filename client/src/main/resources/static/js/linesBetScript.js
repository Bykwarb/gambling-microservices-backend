const betStep = 5;
const maxBet = 100;
const minBet = 5;
let betValue = minBet;

const linesStep = 1;
const maxLines = 5;
const minLines = 1;
let linesValue = minLines;

const betValueEl = document.getElementById("betValue");
const linesValueEl = document.getElementById("linesValue");

document.getElementById("decreaseBet").addEventListener("click", () => {
    if (betValue > minBet) {
        betValue -= betStep;
        updateBetValue();
    }
});

document.getElementById("increaseBet").addEventListener("click", () => {
    if (betValue < maxBet) {
        betValue += betStep;
        updateBetValue();
    }
});

document.getElementById("decreaseLines").addEventListener("click", () => {
    if (linesValue > minLines) {
        linesValue -= linesStep;
        updateLinesValue();
    }
});

document.getElementById("increaseLines").addEventListener("click", () => {
    if (linesValue < maxLines) {
        linesValue += linesStep;
        updateLinesValue();
    }
});

function updateBetValue() {
    betValueEl.innerText = betValue;
}

function updateLinesValue() {
    linesValueEl.innerText = linesValue;
}