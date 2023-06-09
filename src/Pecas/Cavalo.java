package Pecas;

import Xadrez.Tabuleiro;
import auxiliar.Posicao;
import java.util.ArrayList;

public class Cavalo extends Peca{

    public Cavalo(String sAFileName, Posicao aPosicao, boolean bBrancas) {
           super(sAFileName, aPosicao, bBrancas);
    }
    public String toString(){
        return "Cavalo";
    }
    public boolean setPosicao(Posicao umaPosicao, Tabuleiro umTabuleiro) {
        double dif_lin, dif_col;
        dif_col = (this.pPosicao.getColuna() - umaPosicao.getColuna());
        dif_col = Math.pow(dif_col, 2);
        dif_lin = (this.pPosicao.getLinha() - umaPosicao.getLinha());
        dif_lin = Math.pow(dif_lin, 2);
        if((dif_lin + dif_col) == 5){
            this.pPosicao.setPosicao(umaPosicao);
            return true;     
        }
       
        return false;
    }  
    
    @Override
    public ArrayList<Posicao> movimentosPossiveis(){
        return null;
    }
    
    @Override
    public ArrayList<Posicao> ataquesPossiveis(){
        return null;
    }
    
    @Override
    public double limiteMovimento(){
        return Math.pow(5, 0.5);
    }
    
    
    @Override
    public boolean direcaoMovimento(Posicao pIncremento){
        return true;
    }
}
