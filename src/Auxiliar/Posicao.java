package auxiliar;

import java.io.Serializable;

public class Posicao implements Serializable{
    private int	linha,
                coluna;

    public Posicao(int linha, int coluna){
        this.linha = linha;
        this.coluna = coluna;
    }

    public void setPosicao(int linha, int coluna){
        this.linha = linha;
        this.coluna = coluna;
    }
       
    public void setLinha(int linha){
        this.linha = linha;
    }

    public int getLinha(){
        return linha;
    }

    public void setColuna(int coluna){
        this.coluna = coluna;
    }

    public int getColuna(){
        return coluna;
    }

    public boolean igual(Posicao posicao){
        return (linha == posicao.getLinha() && coluna == posicao.getColuna());
    }

    public void setPosicao(Posicao posicao){
        setLinha(posicao.getLinha());
        setColuna(posicao.getColuna());
    }
    
    public boolean posicaoInvalida(){
        if(this.getColuna() < 0 || this.getColuna() > 7) return true;
        return (this.getLinha() < 0 || this.getLinha() > 7) ;
    }

    @Override
    public String toString() {
        return "Linha " + linha + " & Coluna: " + coluna;
    }
    
    // Implementando o método equals para comparar os objetos
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Posicao umaPosicao = (Posicao) obj;
        return this.igual(umaPosicao);
    }

}
