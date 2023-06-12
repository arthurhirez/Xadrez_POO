package Pecas;

import Auxiliar.Consts;
import Auxiliar.Movimentos;
import Xadrez.Jogo;
import Xadrez.Tabuleiro;
import auxiliar.Posicao;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;


public abstract class Peca implements Serializable, Movimentos{
    protected ImageIcon iImage;
    protected Posicao pPosicao;
    /*O elemento deve saber em qual cenário ele está*/
    //protected Tabuleiro tTabuleiro;
    protected boolean bBrancas;

    protected Peca(String sAFileName, Posicao aPosicao, boolean bBrancas) {
        this.pPosicao = aPosicao;
        this.bBrancas = bBrancas;
        try {
            iImage = new ImageIcon(new java.io.File(".").getCanonicalPath()+Consts.PATH + sAFileName);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }        
    }
    
    public int converterBoolCoordenada() {
        return (this.bBrancas) ? -1 : 1;
    }
    
    
    public boolean ehReta(Posicao pIncremento){
        return ((this.pPosicao.getColuna() == pIncremento.getColuna()) ||
                (this.pPosicao.getLinha() == pIncremento.getLinha()));
    }
    
    public boolean ehDiagonal(Posicao pIncremento){
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
    
    public boolean temAMesmaCorQue(Peca umaPeca){
        return this.bBrancas == umaPeca.bBrancas;
    }
    
    public Posicao getPosicaoPeca(){
        if(this.foiClicada(pPosicao)){
            return this.pPosicao; 
        }
        
        return null;
        
    }
/*
    public void setTabuleiro(Tabuleiro aTabuleiro){
        this.tTabuleiro = aTabuleiro;
    }
*/
    public void autoDesenho(Tabuleiro tTabuleiro){
        iImage.paintIcon(tTabuleiro, (Graphics2D)tTabuleiro.getGraphics(),
                         pPosicao.getColuna() * Consts.SIZE, pPosicao.getLinha() * Consts.SIZE);        
    }
    public boolean foiClicada(Posicao aPosicao){
        return this.pPosicao.igual(aPosicao);
    }
    
    /*
    public void destaquePeca(Tabuleiro tTabuleiro, Posicao aPosicao){
        if(foiClicada(aPosicao)){
            tTabuleiro.destacarPosicao(this.pPosicao);   
        }  
    }
    */

    
    
    public abstract boolean setPosicao(Posicao umaPosicao, Tabuleiro umTabuleiro);
}
