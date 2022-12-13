package com.example.gameservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SlotMachineImpl implements SlotMachine {

    @Autowired
    private Spinner spinner;

    @Override
    public Session play(Session session) {
        Symbols[][] symbols = spinner.spin();
        Map<Lines, Symbols> r = calculateTotals(symbols);
        Result result = calculateResult(r,session);
        if (session.getCredits() >= session.getBetValue()){
            System.out.println(result.toString());
            session.setCredits(session.getCredits() + result.getResult());
        }
        return session;
    }

    @Override
    public Map<Lines, Symbols> calculateTotals(Symbols[][] symbols) {
        Map<Lines, Symbols> result = new HashMap<>();
        //checks the right diagonal
        if(symbols[0][0] == symbols[1][1] && symbols[0][0] == symbols[2][2]){
            result.put(Lines.RightDiagonal, symbols[0][0]);
        }
        //checks the left diagonal
        if (symbols[0][2] == symbols[1][1] && symbols[0][2] == symbols[2][0]){
            result.put(Lines.LeftDiagonal, symbols[0][2]);
        }
        //checks the 1st line
        if (symbols[0][0] == symbols[0][1] &&
            symbols[0][0] == symbols[0][2]){
            //if first line is win, check equals 1 & 2 lines, if false: put 1st line to map;
            if (symbols[0][0] == symbols[1][0] &&
                symbols[0][0] == symbols[1][1] &&
                symbols[0][0] == symbols[1][2]){
                //if first line is win, end 1 == 2, check equals 1 & 3 lines, if false: put 2 lines to map; if true: put 3 lines to map
                if (symbols[0][0] == symbols[2][0] &&
                    symbols[0][0] == symbols[2][1] &&
                    symbols[0][0] == symbols[2][2]){

                    result.clear(); //deletes diagonales
                    result.put(Lines.One, symbols[0][0]);
                    result.put(Lines.Two, symbols[0][0]);
                    result.put(Lines.Three, symbols[0][0]);
                }else {
                    //put 2 lines to map and see is it winning 3rd line or no, if true: put it to map;
                    result.put(Lines.One, symbols[0][0]);
                    result.put(Lines.Two, symbols[0][0]);
                    if (symbols[2][0] == symbols[2][1] && symbols[0][0] == symbols[2][2]){
                        result.put(Lines.Three, symbols[2][0]);
                    }
                }
                //return map
                return result;
            }else {
                result.put(Lines.One, symbols[0][0]);
            }

        }
        //checks the 2nd line
        if (symbols[1][0] == symbols[1][1] && symbols[1][0] == symbols[1][2]){
            //if second line is win, check equals 2 & 3 lines, if false: put 2nd line to map; if true: return map
            if (symbols[1][0] == symbols[2][0] && symbols[1][0] == symbols[2][1] && symbols[1][0] == symbols[2][2]){
                result.put(Lines.Two, symbols[1][0]);
                result.put(Lines.Three, symbols[1][0]);
                return result;
            }
            result.put(Lines.Two, symbols[1][0]);
        }
        //checks the 3rd line
        if (symbols[2][0] == symbols[2][1] && symbols[2][0] == symbols[2][2]){
            result.put(Lines.Three, symbols[2][0]);
            return result;
        }
        return result;
    }

    private Result calculateResult(Map<Lines, Symbols> r, Session session){
        Result result = new Result();
        double sum = 0;
        if (!r.isEmpty()){
            for (Map.Entry<Lines, Symbols> entry : r.entrySet()) {
                Lines k = entry.getKey();
                Symbols v = entry.getValue();
                sum += (session.getBetValue() + v.getCoefficient()) * k.getCoefficient();
            }
            result.setResult(sum);
            result.setStatus(Result.Status.Win);
            result.setSession(session);
        }else {
            sum -= session.getBetValue();
            result.setResult(sum);
            result.setStatus(Result.Status.Lose);
            result.setSession(session);
        }
        return result;
    }

}
