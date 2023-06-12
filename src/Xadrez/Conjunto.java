package Xadrez;

import Pecas.Peca;
import auxiliar.Posicao;
import java.io.Serializable;
import java.util.ArrayList;

public class Conjunto extends ArrayList<Peca> implements Serializable{
    public Conjunto(){
        super();
    }
    public void AutoDesenho(Tabuleiro tTabuleiro){
        for(int i = 0; i < this.size(); i++)
            this.get(i).autoDesenho(tTabuleiro);
    }
    public Peca getPecaClicada(Posicao aPosicao){
        for(int i = 0; i < this.size(); i++)
            if(this.get(i).foiClicada(aPosicao))
                return this.get(i);
        return null;
    }
    
    public boolean PecaPosicao(Posicao aPosicao){
        for(int i = 0; i < this.size(); i++)
            if(this.get(i).foiClicada(aPosicao))
                return true;
        return false;
    }
    

    public void pecaFora(Peca aPeca){
        if(aPeca != null){
            for(int i = 0; i < this.size(); i++)
                if(this.get(i) == aPeca)
                    this.remove(i);
        }
        
    }
}