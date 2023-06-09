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
        /*
        // Deselecionar a peca
        if(this.pPosicao.igual(umaPosicao)) return true;
        
        // Laco para checar se o movimento eh possivel atraves de interface
        for(Posicao pPossivel : this.movimentosPossiveis()){
            if(pPossivel.igual(umaPosicao)){
                this.pPosicao.setPosicao(umaPosicao);
                bPrimeiroLance = false;
                return true;
            }
        }
        
        for(Posicao pPossivel : this.ataquesPossiveis()){
            if(pPossivel.igual(umaPosicao)){
                this.pPosicao.setPosicao(umaPosicao);
                return true;
            }
        }

        return false;
*/      
        // Deselecionar a peca
        if(this.pPosicao.igual(umaPosicao)) return true;

        this.pPosicao.setPosicao(umaPosicao);
        bPrimeiroLance = false;
        return true;

    }
    
    //public void boolean
    

    @Override
    // Todas as possiveis posicoes de movimento de uma peca "sozinha"
    public ArrayList<Posicao> movimentosPossiveis(){
        if(this.foiClicada(this.pPosicao)){
            ArrayList<Posicao> movimentos = new ArrayList<>();
            
            // Posicao atual
            Posicao atual = this.getPosicaoPeca();
            int pColuna = atual.getColuna();
            int pLinha = atual.getLinha();
            
            // Brancas subtrai e pretas adiciona para mover entre linhas
            int sinalLinha = this.convertBoolCoord();
  
            // Movimentos
            movimentos.add(new Posicao(pLinha + sinalLinha ,pColuna));
            if (this.bPrimeiroLance) movimentos.add(new Posicao(pLinha + 2*sinalLinha,pColuna));

            return movimentos;
        }
        return null;
    }
    
    @Override
    // Todas as possiveis posicoes de ataque de uma peca "sozinha"
    public ArrayList<Posicao> ataquesPossiveis(){
        if(this.foiClicada(this.pPosicao)){
            ArrayList<Posicao> ataques = new ArrayList<>();
            
            Posicao atual = this.getPosicaoPeca();
            int pColuna = atual.getColuna();
            int pLinha = atual.getLinha();
            
            // Brancas subtrai e pretas adiciona para mover entre linhas
            int sinalLinha = this.convertBoolCoord();
            // Ataque
            ataques.add(new Posicao(pLinha + sinalLinha,pColuna -1)); 
            ataques.add(new Posicao(pLinha + sinalLinha,pColuna + 1)); 

            return ataques;
        }
        return null;
    }
    
    @Override
    public double limiteMovimento(){
        return this.bPrimeiroLance ? 2 : 1;
    }
    
    
    @Override
    public boolean direcaoMovimento(Posicao pIncremento){
        if(this.foiClicada(this.pPosicao)){
            
            Posicao atual = this.getPosicaoPeca();
            int pColuna = atual.getColuna();
            int pLinha = atual.getLinha();
            
            // Brancas subtrai e pretas adiciona para mover entre linhas
            int sinalLinha = this.convertBoolCoord();
            // Ataque
            
            if((pColuna == pIncremento.getColuna()) && (pIncremento.getLinha() == (pLinha + sinalLinha)))
                return true;
        }
        return false;
    }
    
    
}
