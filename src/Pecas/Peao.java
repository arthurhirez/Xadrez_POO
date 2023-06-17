package Pecas;

import Auxiliar.InvalidValueException;
import Xadrez.Tabuleiro;
import auxiliar.Posicao;


public class Peao extends Peca {

    private boolean bPrimeiroLance;

    public Peao(String sAFileName, Posicao aPosicao, boolean bBrancas) {
        super(sAFileName, aPosicao, bBrancas);
        this.bPrimeiroLance = true;
    }

    @Override
    public String toString() {
        return "Peao";
    }
    
    @Override
    public boolean setPosicao(Posicao umaPosicao, Tabuleiro umTabuleiro) throws InvalidValueException {     
        if (umaPosicao.posicaoInvalida())
            throw new InvalidValueException();
        else{
            if(!this.pPosicao.equals(umaPosicao)){
                bPrimeiroLance = false;
            }             
            this.pPosicao.setPosicao(umaPosicao);
            return true;
        }
    }
   
    @Override
    public double limiteMovimento(){
        // Primeiro movimento do peao pode ser 2 casas, os demais 1 casa
        return this.bPrimeiroLance ? 2 : 1;
    }
        
    @Override
    public boolean direcaoMovimento(Posicao pIncremento){
        // Direcao movimentacao
        Posicao atual = this.getPosicaoPeca();
        int pColuna = atual.getColuna();
        int pLinha = atual.getLinha();
        // Brancas subtrai e pretas adiciona para mover entre linhas
        int sinalLinha = this.converterBoolCoordenada();
        
        // Regras movimento peao
        if((pColuna == pIncremento.getColuna()) &&
                        (pIncremento.getLinha() == (pLinha + sinalLinha)))
            return true;
        
        // Direcao ataque
        if (this.ehDiagonal(pIncremento)){
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
