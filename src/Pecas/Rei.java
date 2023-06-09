package Pecas;

import Xadrez.Tabuleiro;
import auxiliar.Posicao;
import java.util.ArrayList;

public class Rei extends Peca{
    public Rei(String sAFileName, Posicao aPosicao, boolean bBrancas) {
        super(sAFileName, aPosicao, bBrancas);
    }
    public String toString(){
        return "Rei";
    }
    public boolean setPosicao(Posicao umaPosicao, Tabuleiro umTabuleiro) {
        throw new UnsupportedOperationException("Implemente esta funcao para o " + this); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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