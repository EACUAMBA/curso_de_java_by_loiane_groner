package com.eacuamba.dev.curso_de_java.aula_74.exercicios;

import java.util.concurrent.TimeUnit;

public class Semafuro implements Runnable {

    public enum Luz {
        AMARELO(TimeUnit.SECONDS.toMillis(5)),
        VERMELHO(TimeUnit.SECONDS.toMillis(5)),
        VERDE(TimeUnit.SECONDS.toMillis(5));

        Luz(long tempo) {
            this.tempo = tempo;
        }

        private long tempo;
    }

    ;
    private Luz luz;
    private boolean stop;
    private boolean corMudou;

    public Semafuro() {
        luz = Luz.VERMELHO;
        new Thread(this).start();
    }

    public void run() {
        try {
            while (!stop) {
                Thread.sleep(this.luz.tempo);
                mudarCor(this.luz);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void mudarCor(Luz luz) {
        switch (luz) {
            case VERDE: {
                this.luz = Luz.AMARELO;
                break;
            }
            case AMARELO: {
                this.luz = Luz.VERMELHO;
                break;
            }
            case VERMELHO: {
                this.luz = Luz.VERDE;
                break;
            }
        }
        this.corMudou = true;
        this.notify();
    }

    public synchronized void esperarCorMudar() {
        while (!this.corMudou) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Luz getLuz() {
        return luz;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public void setCorMudou(boolean corMudou) {
        this.corMudou = corMudou;
    }

    public static void main(String[] args) {
        Semafuro semafuro = new Semafuro();

        while (true) {
            System.out.println(semafuro.getLuz().name());
            semafuro.esperarCorMudar();
            semafuro.setCorMudou(false);
        }
    }
}
