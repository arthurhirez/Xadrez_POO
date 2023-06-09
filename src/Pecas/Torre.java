package Pecas;
import Xadrez.Tabuleiro;
import auxiliar.Posicao;
import java.util.ArrayList;

public class Torre extends Peca{
    


    public Torre(String sAFileName, Posicao aPosicao, boolean bBrancas) {
           super(sAFileName, aPosicao, bBrancas);
    }
    public String toString(){
        return "Torre";
    }
    public boolean setPosicao(Posicao umaPosicao, Tabuleiro umTabuleiro) {
        if ((this.pPosicao.getColuna() != umaPosicao.getColuna() &&
                this.pPosicao.getLinha() == umaPosicao.getLinha()) ||
                (this.pPosicao.getColuna() == umaPosicao.getColuna() &&
                this.pPosicao.getLinha() != umaPosicao.getLinha())) {
            this.pPosicao.setPosicao(umaPosicao);
            return true;
        }
        return false;
    }
    
    /*
    @Override
    public ArrayList<Posicao> movimentosPossiveis(){
        if(this.foiClicada(this.pPosicao)){
            ArrayList<Posicao> movimentos = new ArrayList<>();
            
            // Posicao atual
            Posicao atual = this.getPosicaoPeca();
            int pColuna = atual.getColuna();
            int pLinha = atual.getLinha();

  
            // Movimentos
            // Ornadados, pois torre nao pode pular outras pecas
            
            // Cima
            for(int i = pLinha - 1; i >= 0; i--){
                movimentos.add(new Posicao(i ,pColuna));
            }
            
            // Baixo
            for(int i = pLinha + 1; i <= 7; i++){
                movimentos.add(new Posicao(i ,pColuna));
            }
            
            // Esquerda
            for(int i = pColuna - 1; i >= 0; i--){
                movimentos.add(new Posicao(pLinha ,i));
            }
            
            // Direita
            for(int i = pColuna + 1; i <= 7; i++){
                movimentos.add(new Posicao(pLinha ,i));
            }
            
            return movimentos;
        }
        return null;
    }
    
    @Override
    public ArrayList<Posicao> ataquesPossiveis(){
        if(this.foiClicada(this.pPosicao)){
            ArrayList<Posicao> movimentos = new ArrayList<>();
            
            // Posicao atual
            Posicao atual = this.getPosicaoPeca();
            int pColuna = atual.getColuna();
            int pLinha = atual.getLinha();

  
            // Movimentos
            // Ornadados, pois torre nao pode pular outras pecas
            
            // Cima
            for(int i = pLinha - 1; i >= 0; i--){
                movimentos.add(new Posicao(i ,pColuna));
            }
            
            // Baixo
            for(int i = pLinha + 1; i <= 7; i++){
                movimentos.add(new Posicao(i ,pColuna));
            }
            
            // Esquerda
            for(int i = pColuna - 1; i >= 0; i--){
                movimentos.add(new Posicao(pLinha ,i));
            }
            
            // Direita
            for(int i = pColuna + 1; i <= 7; i++){
                movimentos.add(new Posicao(pLinha ,i));
            }
            
            return movimentos;
        }
        return null;
    }
    */
    
    @Override
    public double limiteMovimento(){
        return 7;
    }
    
    
    @Override
    public boolean direcaoMovimento(Posicao pIncremento){  
        if(this.foiClicada(this.pPosicao)){
            //if(pIncremento.getColuna() < 0 || pIncremento.getColuna() > 7 || pIncremento.getLinha() < 0 || pIncremento.getLinha() > 7)
                //return false;
            
            int pColuna = this.pPosicao.getColuna();
            int pLinha = this.pPosicao.getLinha();

            // Ataque
            
            if((pColuna == pIncremento.getColuna()) && (pLinha != pIncremento.getLinha()) ||
                    (pColuna != pIncremento.getColuna()) && (pLinha == pIncremento.getLinha()))
                return true;
        }
        return false;
    }
    
}
