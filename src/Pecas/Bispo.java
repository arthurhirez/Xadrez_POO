package Pecas;

import Auxiliar.InvalidValueException;
import Xadrez.Tabuleiro;
import auxiliar.Posicao;

public class Bispo extends Peca {
     public Bispo(String sAFileName, Posicao aPosicao, boolean bBrancas) {
        super(sAFileName, aPosicao, bBrancas);
    }
     @Override
    public String toString(){
        return "Bispo";
    }        

     @Override
    public boolean setPosicao(Posicao umaPosicao, Tabuleiro umTabuleiro) throws InvalidValueException {     
        if (umaPosicao.posicaoInvalida())
            throw new InvalidValueException();
        else{
            this.pPosicao.setPosicao(umaPosicao);
            return true;
        }
    }
    
    @Override
    public double limiteMovimento(){
        return 7;
    }
    
    @Override
    public boolean direcaoMovimento(Posicao pIncremento){
        if (this.pPosicao.getColuna() != pIncremento.getColuna() &&
                this.pPosicao.getLinha() != pIncremento.getLinha()) {
            int dif_lin, dif_col;
            dif_col = Math.abs(this.pPosicao.getColuna() - pIncremento.getColuna());
            dif_lin = Math.abs(this.pPosicao.getLinha() - pIncremento.getLinha());
            if(dif_lin == dif_col){
                return true;     
            }
        }
        return false;
    }
}
