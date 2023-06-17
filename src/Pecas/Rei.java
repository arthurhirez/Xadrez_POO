package Pecas;

import Auxiliar.InvalidValueException;
import Xadrez.Tabuleiro;
import auxiliar.Posicao;

public class Rei extends Peca{
    public Rei(String sAFileName, Posicao aPosicao, boolean bBrancas) {
        super(sAFileName, aPosicao, bBrancas);
    }
    @Override
    public String toString(){
        return "Rei";
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
        return 1;
    }
    
    @Override
    public boolean direcaoMovimento(Posicao pIncremento){
        return true;
    }
    
}