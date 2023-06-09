package Pecas;

import Xadrez.Tabuleiro;
import auxiliar.Posicao;
import java.util.ArrayList;

public class Rei extends Peca{
    public Rei(String sAFileName, Posicao aPosicao, boolean bBrancas) {
        super(sAFileName, aPosicao, bBrancas);
    }
    public String toString(){
        return "Rei";
    }
    public boolean setPosicao(Posicao umaPosicao, Tabuleiro umTabuleiro) {
        this.pPosicao.setPosicao(umaPosicao);
        return true;
    }    

    
    @Override
    public double limiteMovimento(){
        return 1;
    }
    
    
    @Override
    public boolean direcaoMovimento(Posicao pIncremento){
        return true;
    }
    
    /*
    @Override
    public ArrayList<Posicao> movimentosPossiveis(){
        return null;
    }
    
    @Override
    public ArrayList<Posicao> ataquesPossiveis(){
        return null;
    }
    */
    
}