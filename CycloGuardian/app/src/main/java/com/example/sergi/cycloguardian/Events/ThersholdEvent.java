package com.example.sergi.cycloguardian.Events;

/**
 * Evento para notificar de que se ha superado el límite
 * Created by sergi on 03/04/2018.
 */

public class ThersholdEvent {

    int posIncidence;


    public int getPosIncidence() {
        return posIncidence;
    }

    public void setPosIncidence(int posIncidence) {
        this.posIncidence = posIncidence;
    }


    public  ThersholdEvent() {

    }



}
