package Pecas;

import Auxiliar.Consts;
import Auxiliar.InvalidValueException;
import Auxiliar.Movimentos;
import Xadrez.Tabuleiro;
import auxiliar.Posicao;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;


public abstract class Peca implements Serializable, Movimentos{
    protected ImageIcon iImage;
    protected Posicao pPosicao;
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
    
    // Funcao que define posicao da peca e confere se eh valida atraves de tratamento de excecao
    public abstract boolean setPosicao(Posicao umaPosicao, Tabuleiro umTabuleiro) throws InvalidValueException;
    
    // Funcao auxiliar para direcao movimento
    public int converterBoolCoordenada() {
        return (this.bBrancas) ? -1 : 1;
    }
    
    public boolean pecaBranca(){
        return this.bBrancas;
    }
    
    // Funcao auxiliar retorna se posicao em relacao a peca eh em linha reta
    public boolean ehReta(Posicao pIncremento){
        return ((this.pPosicao.getColuna() == pIncremento.getColuna()) ||
                (this.pPosicao.getLinha() == pIncremento.getLinha()));
    }
    
    // Funcao auxiliar retorna se posicao em relacao a peca eh em diagonal
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
    
    // Funcao auxiliar para checar se pecas sao da mesma cor
    public boolean temAMesmaCorQue(Peca umaPeca){
        return this.bBrancas == umaPeca.bBrancas;
    }
    
    // Funcao auxiliar que retorna se peca foi selecionada
    public boolean foiClicada(Posicao aPosicao){
        return this.pPosicao.igual(aPosicao);
    }
    
    // Funcao auxiliar que retorna posicao da peca
    public Posicao getPosicaoPeca(){
        if(this.foiClicada(pPosicao)){
            return this.pPosicao; 
        }
        return null;
    }

    // Funcao auxiliar que desenha a peca
    public void autoDesenho(Tabuleiro tTabuleiro){
        iImage.paintIcon(tTabuleiro, (Graphics2D)tTabuleiro.getGraphics(),
                         pPosicao.getColuna() * Consts.SIZE, pPosicao.getLinha() * Consts.SIZE);        
    }

}
