package Pecas;

import Auxiliar.Consts;
import Xadrez.Tabuleiro;
import auxiliar.Posicao;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Peao extends Peca {

    private boolean bPrimeiroLance;

    public Peao(String sAFileName, Posicao aPosicao, boolean bBrancas) {
        super(sAFileName, aPosicao, bBrancas);
        this.bPrimeiroLance = true;
    }

    public String toString() {
        return "Peao";
    }
    
    

    public boolean setPosicao(Posicao umaPosicao, Tabuleiro umTabuleiro) {     
        // Deselecionar a peca
        if(this.pPosicao.igual(umaPosicao)) return true;

        this.pPosicao.setPosicao(umaPosicao);
        bPrimeiroLance = false;
        return true;

    }
    

    @Override
    public double limiteMovimento(){
        return this.bPrimeiroLance ? 2 : 1;
    }
    
    
    @Override
    public boolean direcaoMovimento(Posicao pIncremento){

        // Direcao movimentacao
        Posicao atual = this.getPosicaoPeca();
        int pColuna = atual.getColuna();
        int pLinha = atual.getLinha();
        int sinalLinha = this.converterBoolCoordenada();
        // Brancas subtrai e pretas adiciona para mover entre linhas
        
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
