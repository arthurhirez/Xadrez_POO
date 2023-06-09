package Pecas;

import Xadrez.Jogo;
import Xadrez.Tabuleiro;
import auxiliar.Posicao;
import java.util.ArrayList;

public class Bispo extends Peca {
     public Bispo(String sAFileName, Posicao aPosicao, boolean bBrancas) {
        super(sAFileName, aPosicao, bBrancas);
    }
    public String toString(){
        return "Bispo";
    }        

    public boolean setPosicao(Posicao umaPosicao, Tabuleiro umTabuleiro) {
        if (this.pPosicao.getColuna() != umaPosicao.getColuna() &&
                this.pPosicao.getLinha() != umaPosicao.getLinha()) {
            int dif_lin, dif_col;
            dif_col = Math.abs(this.pPosicao.getColuna() - umaPosicao.getColuna());
            dif_lin = Math.abs(this.pPosicao.getLinha() - umaPosicao.getLinha());
            if(dif_lin == dif_col){
                this.pPosicao.setPosicao(umaPosicao);
                return true;     
            }
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
        return 0;
    }
    
    
    @Override
    public boolean direcaoMovimento(Posicao pIncremento){
        return false;
    }
}
