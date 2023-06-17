package Pecas;

import Auxiliar.InvalidValueException;
import Xadrez.Tabuleiro;
import auxiliar.Posicao;

public class Cavalo extends Peca{

    public Cavalo(String sAFileName, Posicao aPosicao, boolean bBrancas) {
           super(sAFileName, aPosicao, bBrancas);
    }
    @Override
    public String toString(){
        return "Cavalo";
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
        return 5;
    }
    
    @Override
    public boolean direcaoMovimento(Posicao pIncremento){
        double dif_lin, dif_col;
        dif_col = (this.pPosicao.getColuna() - pIncremento.getColuna());
        dif_col = Math.pow(dif_col, 2);
        dif_lin = (this.pPosicao.getLinha() - pIncremento.getLinha());
        dif_lin = Math.pow(dif_lin, 2);
        
        return ((dif_lin + dif_col) == 5);
    }
    
}
