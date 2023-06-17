package Pecas;

import Auxiliar.InvalidValueException;
import Xadrez.Tabuleiro;
import auxiliar.Posicao;

public class Rainha extends Peca{
    public Rainha(String sAFileName, Posicao aPosicao, boolean bBrancas) {
        super(sAFileName, aPosicao, bBrancas);
    }
    @Override
    public String toString(){
        return "Rainha";
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
        return true;
    }

    
}