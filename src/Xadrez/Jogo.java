package Xadrez;

import Auxiliar.Consts;
import Pecas.*;
import Xadrez.Tabuleiro;
import auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Jogo extends javax.swing.JFrame implements MouseListener, KeyListener {

    private Tabuleiro tTabuleiro;//atributo com a janela de desenho
    Conjunto cBrancas;
    Conjunto cPretas;
    boolean bEmJogada;
    Peca pecaEmMovimento;
    int controleMouse;

    public enum CoresConjuntos {

        BRANCAS, PRETAS
    };

    public Jogo() {
        cBrancas = new Conjunto();
        cPretas = new Conjunto();
        //tTabuleiro = new Tabuleiro(cBrancas, cPretas);//alocação do painel de desenho
        tTabuleiro = new Tabuleiro();
        tTabuleiro.setFocusable(true);
        tTabuleiro.addMouseListener(this);//Adiciona evento de mouse ao Painel de desenho
        tTabuleiro.addKeyListener(this);
        bEmJogada = false;
        pecaEmMovimento = null;
        controleMouse = -1;
        initComponents();
    }

    // Inicializa pecas e suas posicoes na main
    public void addPeca(Peca aPeca, CoresConjuntos aCorConjunto) {
        //aPeca.setTabuleiro(this.tTabuleiro);
        if (aCorConjunto == CoresConjuntos.BRANCAS) {
            cBrancas.add(aPeca);
        } else {
            cPretas.add(aPeca);
        }
    }

    // Retorna objeto que foi selecionado pelo jogador
    public Peca getPecaClicada(Posicao aPosicao) {
        Peca pTemp = cBrancas.getPecaClicada(aPosicao);
        if (pTemp != null) {
            return pTemp;
        }
        pTemp = cPretas.getPecaClicada(aPosicao);
        if (pTemp != null) {
            return pTemp;
        }
        return null;
    }
    
    
    protected boolean posicaoOcupada(Posicao pPosicao){
        return (this.cBrancas.PecaPosicao(pPosicao) || this.cPretas.PecaPosicao(pPosicao));
    }
    
    protected void checaIncremento(ArrayList<Posicao> ondePode, ArrayList<Posicao> osAlemao, Posicao pAtual, // Sempre igual
                                    int deltaLinha, int deltaColuna){                                        // Caso a caso

        int distAtual = 0;
        int colAtual = pAtual.getColuna();
        int linAtual = pAtual.getLinha();
        Posicao pIncremento;
        String flagPeca = pecaEmMovimento.toString();
        
        // Definido so um caso (1,1) para não repetir checagem 8 vezes
        if(flagPeca.equals("Cavalo") && deltaLinha == 1 && deltaColuna == 1){               
            for(int i = linAtual - 2; i <= linAtual + 2; i++){               
                for(int j = colAtual - 2; j <= colAtual + 2; j++){
                    if(i > 7 || i < 0) continue;
                    if(j > 7 || j < 0) continue;
                    pIncremento = new Posicao(i, j);
                    if(pecaEmMovimento.direcaoMovimento(pIncremento)){
                        if(!this.posicaoOcupada(pIncremento))
                            ondePode.add(pIncremento);
                        else
                            if(!pecaEmMovimento.temAMesmaCorQue(this.getPecaClicada(pIncremento)))
                                osAlemao.add(pIncremento);      
                    }   
                }
            }
        }
   
        // Enquanto estiver no range de movimentos possiveis e na direcao correta adiciona na lista
        while(++distAtual <= pecaEmMovimento.limiteMovimento() &&
                pecaEmMovimento.direcaoMovimento(new Posicao(pAtual.getLinha() + deltaLinha, pAtual.getColuna() + deltaColuna))){
            linAtual += deltaLinha;
            colAtual += deltaColuna;
            
            if(linAtual < 0 || linAtual > 7) break;
            if(colAtual < 0 || colAtual > 7) break;
            
            pIncremento = new Posicao(linAtual, colAtual);
            

            if(!this.posicaoOcupada(pIncremento)){
                if(flagPeca.equals("Peao") && pecaEmMovimento.ehDiagonal(pIncremento)) break;
                
                
                if(flagPeca.equalsIgnoreCase("Rei")){
                    // fazer função get conjunto?
                    ArrayList<Posicao> aquiNaoMeuRei;
                    if(this.pecaEmMovimento.converterBoolCoordenada() == 1){
                        aquiNaoMeuRei = this.estaraEmXeque(pIncremento, CoresConjuntos.BRANCAS);   
                    }else{
                        aquiNaoMeuRei = this.estaraEmXeque(pIncremento, CoresConjuntos.PRETAS);
                    }


                    
                    if(aquiNaoMeuRei != null){
                        for(Posicao p : aquiNaoMeuRei){
                            System.out.println(this.getPecaClicada(p) + " em " + p);
                            this.tTabuleiro.destaqueXeque(p);
                        }
                        break; 
                    }
                }

                ondePode.add(pIncremento);
            }else{
                if(!pecaEmMovimento.temAMesmaCorQue(this.getPecaClicada(pIncremento))){
                    if(flagPeca.equals("Peao") &&
                        (!pecaEmMovimento.ehDiagonal(pIncremento) || ((pecaEmMovimento.converterBoolCoordenada()*deltaLinha) == -1)))
                        break;
                    osAlemao.add(pIncremento);
                }

                break;
            }
        }
    }
    
    
    public ArrayList<Posicao> estaraEmXeque(Posicao pPosicao, CoresConjuntos aCorConjunto) {
        //aPeca.setTabuleiro(this.tTabuleiro);
        boolean flag = false;
        ArrayList<Posicao> osMatadores = new ArrayList<>();
        Peca reiBKP = this.pecaEmMovimento;
        
        if(aCorConjunto == CoresConjuntos.BRANCAS) {  
            for(Peca vaiMataORei : cBrancas){
                if(vaiMataORei.toString().equals("Rei")) continue;
                if(vaiMataORei.toString().equals("Peao")){
                    if((vaiMataORei.ehDiagonal(pPosicao) && (pPosicao.getLinha() - vaiMataORei.getPosicaoPeca().getLinha() == vaiMataORei.converterBoolCoordenada()))){
                        //System.out.println("condicao certa!!!!!!!!!!!!!!!");
                        flag = true;
                        osMatadores.add(vaiMataORei.getPosicaoPeca());
                    }
                }else{
                    this.pecaEmMovimento = vaiMataORei;
                    ArrayList<Posicao> osAlemaoPeca = new ArrayList<>();
                    ArrayList<Posicao> ondePodePeca = praOnde(osAlemaoPeca);

                    for(Posicao pos : ondePodePeca){
                        if(pPosicao.igual(pos)){
                            flag = true;
                            //System.out.println("Posicao de xeque para " + vaiMataORei);
                            osMatadores.add(vaiMataORei.getPosicaoPeca());
                        }
                    }
                    this.pecaEmMovimento = reiBKP;
                }
            }
        }else{
            for(Peca vaiMataORei : cPretas){
                if(vaiMataORei.toString().equals("Rei")) continue;
                if(vaiMataORei.toString().equals("Peao")){
                    if((vaiMataORei.ehDiagonal(pPosicao) && (pPosicao.getLinha() - vaiMataORei.getPosicaoPeca().getLinha() == vaiMataORei.converterBoolCoordenada()))){
                        //System.out.println("condicao certa!!!!!!!!!!!!!!!");
                        flag = true;
                        osMatadores.add(vaiMataORei.getPosicaoPeca());
                    }
                }else{
                    this.pecaEmMovimento = vaiMataORei;
                    ArrayList<Posicao> osAlemaoPeca = new ArrayList<>();
                    ArrayList<Posicao> ondePodePeca = praOnde(osAlemaoPeca);

                    for(Posicao pos : ondePodePeca){
                        if(pPosicao.igual(pos)){
                            flag = true;
                            //System.out.println("Posicao de xeque para " + vaiMataORei);
                            osMatadores.add(vaiMataORei.getPosicaoPeca());
                        }
                    }
                    this.pecaEmMovimento = reiBKP;
                }
            }
        }
        /*
        if(flag) System.out.println("AQUI QUEM VAI MATA:");
        for(Posicao p : osMatadores){
            System.out.println(p);
        }*/

        return flag ? osMatadores : null;
    }
    
    protected ArrayList<Posicao> praOnde(ArrayList<Posicao> osAlemao){
        if(this.pecaEmMovimento != null){
            Posicao pAtual = pecaEmMovimento.getPosicaoPeca();
            ArrayList<Posicao> ondePode = new ArrayList<>();
            ondePode.add(pAtual);

            // Checa os movimentos nas 8 direcoes
            for (int i = -1; i <= 1; i++)
                for (int j = -1; j <= 1; j++)
                    if (i != 0 || j != 0)
                        this.checaIncremento(ondePode, osAlemao, pAtual, i, j);

            return ondePode;
        }     
        return null;
    }
   
    protected void destaqueDoCarnaval(){
        if(this.pecaEmMovimento != null){
            // Destaque peca selecionada
            Posicao atual = pecaEmMovimento.getPosicaoPeca();
            this.tTabuleiro.destacarPosicao(atual, true);
            
            ArrayList<Posicao> ajudaNois = new ArrayList<>();
            ArrayList<Posicao> levaAiTio = this.praOnde(ajudaNois);
            
            /*
            System.out.println("TA FALANDO PRA METE BALA:");
            
            for(Posicao p : ajudaNois){
                System.out.println(p);
            }
            */
            
            // Mostra movimentos possiveis - botao direito
            if(this.controleMouse == 3){
                for(Posicao pMove : levaAiTio){
                    this.tTabuleiro.preencherPosicao(pMove, true);
                }
            }

            // Mostra ataques possiveis - botao scroll (meio)
            if(this.controleMouse == 2){
                // Verificacao ataques possiveis
                for(Posicao pAtaque : ajudaNois){
                    this.tTabuleiro.preencherPosicao(pAtaque, false);
                }
            }
            
            for(Posicao pAtaque : ajudaNois){
                this.tTabuleiro.preencherPosicao(pAtaque, false);
            }
            for(Posicao pMove : levaAiTio){
                this.tTabuleiro.preencherPosicao(pMove, true);
            }
        }
    }

  
    
    public void paint(Graphics g) {
        super.paint(g);
        cBrancas.AutoDesenho(tTabuleiro);
        cPretas.AutoDesenho(tTabuleiro);    
        /*
        Posicao teste = new Posicao(5,5);
        this.tTabuleiro.destacarPosicao(teste);
        
        Posicao teste2 = new Posicao(4,3);
        this.tTabuleiro.preencherPosicao(teste2);
        */
        
        destaqueDoCarnaval();
        
        //destaquePecaAtual();
        //mostraMovimento();
        
        
    }

    public void go() {       
        TimerTask task = new TimerTask() {
            public void run() {
                repaint();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, Consts.DELAY);
        
    }

    public Posicao getPosicaoDoClique(MouseEvent aMouseEvent) {
        return new Posicao(aMouseEvent.getY() / Consts.SIZE,
                aMouseEvent.getX() / Consts.SIZE);
    }
    

    public void mousePressed(MouseEvent e) {
        int x = e.getX();//pega as coordenadas do mouse
        int y = e.getY();
        this.clickLabel.setText("x:" + x + "  y:" + y + "   -   Quadrante: [" + y / Consts.SIZE + "," + x / Consts.SIZE + "]");     


        // Controle para mostrar dica de ataque/movimento possivel
        this.controleMouse = e.getButton();
        
        // Objeto peca clicada
        Peca pecaClicada = this.getPecaClicada(this.getPosicaoDoClique(e));       
        
        // Output tela sem jogada ativa
        if (!bEmJogada){
            if(pecaClicada == null)
                System.out.println("Nenhuma peca selecionada");
            else
                System.out.println("Peca " + pecaClicada + " selecionada");
        }

        // Peca que vai executar movimento ja selecionada
        if(bEmJogada){
            ArrayList<Posicao> ataqueValido  = new ArrayList<>();
            
            if(pecaClicada != null){
                //System.out.println("ta falando que aqui da");
                for(Posicao p : this.praOnde(ataqueValido));
                //this.praOnde();
            }
            
            // Selecionou uma posicao vazia
            if (pecaClicada == null){
                // Condicao acabou movimento ou esta em movimento
                System.out.println(pecaEmMovimento);
                if(this.praOnde(ataqueValido).contains(getPosicaoDoClique(e))){
                    pecaEmMovimento.setPosicao(this.getPosicaoDoClique(e), tTabuleiro);
                    pecaEmMovimento = null;
                    bEmJogada = false;
                }else{
                    System.out.println("AVISO 1: Jogada ainda em movimento, selecione uma posicao valida");
                }
                
            // Selecionou uma peca
            }else{     
                //System.out.println("selecionou essa coisa :" + pecaClicada.getPosicaoPeca());
                //System.out.println("PRIMEIRO TESTE:" + this.praOnde(ataqueValido).contains(pecaClicada.getPosicaoPeca()));
                //System.out.println("SEGUNDO TESTE:" + ataqueValido.contains(pecaClicada.getPosicaoPeca()));

                //if(pecaEmMovimento.setPosicao(this.getPosicaoDoClique(e), tTabuleiro)){
                if(ataqueValido.contains(pecaClicada.getPosicaoPeca()) || pecaEmMovimento == pecaClicada){
                    pecaEmMovimento.setPosicao(this.getPosicaoDoClique(e), tTabuleiro);
                    // Selecionou mesma peca
                    if(pecaEmMovimento == pecaClicada){
                        System.out.println("A peca foi deselecionada, escolha outra peca");
                    }


                    if(!pecaEmMovimento.temAMesmaCorQue(pecaClicada)){
                        cBrancas.pecaFora(pecaClicada);
                        cPretas.pecaFora(pecaClicada);

                        
                }
                
                    
                // Fim da jogada
                
                pecaEmMovimento = null;
                bEmJogada = false;
                    
                // Selecionou uma peca invalida
                }else{
                    System.out.println("aviso 2: Jogada ainda em movimento, selecione uma posicao valida");
                }
            }
        // Selecao da peca que vai executar a jogada
        }else{
            if (pecaClicada != null) {           
                System.out.println("Movimentacao em andamento, selecione o destino da peca");
                pecaEmMovimento = pecaClicada;
                bEmJogada = true;
            }
        }

        repaint();
    }
    
    public void gerarTabuleiroRandomizado() {
        // Criar um array para armazenar as posições iniciais das peças
        Posicao[] posicoesIniciais = new Posicao[32];

        // Gerar posições aleatórias para as peças brancas
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            int row = random.nextInt(8); // Gera um número aleatório entre 0 e 7 (inclusive)
            int col = random.nextInt(8); // Gera um número aleatório entre 0 e 7 (inclusive)
            posicoesIniciais[i] = new Posicao(row, col);
        }

        // Gerar posições aleatórias para as peças pretas
        for (int i = 16; i < 32; i++) {
            int row = random.nextInt(8); // Gera um número aleatório entre 0 e 7 (inclusive)
            int col = random.nextInt(8); // Gera um número aleatório entre 0 e 7 (inclusive)
            posicoesIniciais[i] = new Posicao(row, col);
        }

        // Criar as peças brancas e pretas e adicionar ao conjunto correspondente
        this.cBrancas = new Conjunto();
        this.cPretas = new Conjunto();
        
        if(this.getPecaClicada(posicoesIniciais[0]) == null) cBrancas.add(new Peao("PeaoBranco.png", posicoesIniciais[0], true));
            
        
        if(this.getPecaClicada(posicoesIniciais[1]) == null) cBrancas.add(new Peao("PeaoBranco.png", posicoesIniciais[1], true));
        if(this.getPecaClicada(posicoesIniciais[2]) == null) cBrancas.add(new Peao("PeaoBranco.png", posicoesIniciais[2], true));
        if(this.getPecaClicada(posicoesIniciais[3]) == null) cBrancas.add(new Peao("PeaoBranco.png", posicoesIniciais[3], true));                
        if(this.getPecaClicada(posicoesIniciais[4]) == null) cBrancas.add(new Peao("PeaoBranco.png", posicoesIniciais[4], true));
        if(this.getPecaClicada(posicoesIniciais[5]) == null) cBrancas.add(new Peao("PeaoBranco.png", posicoesIniciais[5], true));
        if(this.getPecaClicada(posicoesIniciais[6]) == null) cBrancas.add(new Peao("PeaoBranco.png", posicoesIniciais[6], true));
        if(this.getPecaClicada(posicoesIniciais[7]) == null) cBrancas.add(new Peao("PeaoBranco.png", posicoesIniciais[7], true));  
        if(this.getPecaClicada(posicoesIniciais[8]) == null) cBrancas.add(new Torre("TorreBranca.png", posicoesIniciais[8], true));                  
        if(this.getPecaClicada(posicoesIniciais[9]) == null) cBrancas.add(new Torre("TorreBranca.png", posicoesIniciais[9], true));                  
        if(this.getPecaClicada(posicoesIniciais[10]) == null) cBrancas.add(new Cavalo("CavaloBranco.png", posicoesIniciais[10], true));                  
        if(this.getPecaClicada(posicoesIniciais[1]) == null) cBrancas.add(new Cavalo("CavaloBranco.png", posicoesIniciais[11], true));                                  
        if(this.getPecaClicada(posicoesIniciais[12]) == null) cBrancas.add(new Bispo("BispoBranco.png", posicoesIniciais[12], true));                                  
        if(this.getPecaClicada(posicoesIniciais[13]) == null) cBrancas.add(new Bispo("BispoBranco.png", posicoesIniciais[13], true));                                  
        if(this.getPecaClicada(posicoesIniciais[14]) == null) cBrancas.add(new Rainha("RainhaBranca.png", posicoesIniciais[14], true));                                  
        
        // Forcando que tenha rei no tabuleiro
        if(this.getPecaClicada(posicoesIniciais[15]) != null) cBrancas.pecaFora(this.getPecaClicada(posicoesIniciais[15]));
        cBrancas.add(new Rei("ReiBranco.png", posicoesIniciais[15], true));                                        
        
        if(this.getPecaClicada(posicoesIniciais[16]) == null) cPretas.add(new Peao("PeaoPreto.png", posicoesIniciais[16], false));                 
        if(this.getPecaClicada(posicoesIniciais[17]) == null) cPretas.add(new Peao("PeaoPreto.png", posicoesIniciais[17], false));                                 
        if(this.getPecaClicada(posicoesIniciais[18]) == null) cPretas.add(new Peao("PeaoPreto.png", posicoesIniciais[18], false));                 
        if(this.getPecaClicada(posicoesIniciais[19]) == null) cPretas.add(new Peao("PeaoPreto.png", posicoesIniciais[19], false));                                 
        if(this.getPecaClicada(posicoesIniciais[20]) == null) cPretas.add(new Peao("PeaoPreto.png", posicoesIniciais[20], false));                 
        if(this.getPecaClicada(posicoesIniciais[21]) == null) cPretas.add(new Peao("PeaoPreto.png", posicoesIniciais[21], false));                                 
        if(this.getPecaClicada(posicoesIniciais[22]) == null) cPretas.add(new Peao("PeaoPreto.png", posicoesIniciais[22], false));                 
        if(this.getPecaClicada(posicoesIniciais[23]) == null) cPretas.add(new Peao("PeaoPreto.png", posicoesIniciais[23], false));                                 
        if(this.getPecaClicada(posicoesIniciais[24]) == null) cPretas.add(new Torre("TorrePreta.png", posicoesIniciais[24], false));                  
        if(this.getPecaClicada(posicoesIniciais[25]) == null) cPretas.add(new Torre("TorrePreta.png", posicoesIniciais[25], false));                  
        if(this.getPecaClicada(posicoesIniciais[26]) == null) cPretas.add(new Cavalo("CavaloPreto.png", posicoesIniciais[26], false));                  
        if(this.getPecaClicada(posicoesIniciais[27]) == null) cPretas.add(new Cavalo("CavaloPreto.png", posicoesIniciais[27], false));                                  
        if(this.getPecaClicada(posicoesIniciais[28]) == null) cPretas.add(new Bispo("BispoPreto.png", posicoesIniciais[28], false));                                  
        if(this.getPecaClicada(posicoesIniciais[29]) == null) cPretas.add(new Bispo("BispoPreto.png", posicoesIniciais[29], false));                                  
        if(this.getPecaClicada(posicoesIniciais[30]) == null) cPretas.add(new Rainha("RainhaPreta.png", posicoesIniciais[30], false));                                  
  
        // Forcando que tenha rei no tabuleiro
        if(this.getPecaClicada(posicoesIniciais[31]) != null){
            cBrancas.pecaFora(this.getPecaClicada(posicoesIniciais[31]));
            cPretas.pecaFora(this.getPecaClicada(posicoesIniciais[31]));
        }
        cPretas.add(new Rei("ReiPreto.png", posicoesIniciais[31], false)); 
        
    }


    
    public void keyPressed(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_S){
            /* "jogoSalvos"+File.pathSeparator+"Nome" */
            File tanque = new File("Teste_Save_Peca.dat");
            try {
                tanque.createNewFile();
                FileOutputStream canoOut = new FileOutputStream(tanque);
                GZIPOutputStream compactador = new GZIPOutputStream(canoOut);
                ObjectOutputStream serializador = new ObjectOutputStream(compactador);
                serializador.writeObject(this.cBrancas);
                serializador.writeObject(this.cPretas);
                serializador.flush();
                serializador.close();
                System.out.println("Jogo salvo!");
            } catch (Exception ex) {
                System.out.println("Ocorreu o seguinte erro:" + ex.getMessage());
            }
            
        }
        
        if(e.getKeyCode() == KeyEvent.VK_O){
            File tanque = new File("Teste_Save_Peca.dat");
            try {
                FileInputStream canoIn = new FileInputStream(tanque);
                GZIPInputStream descompactador = new GZIPInputStream(canoIn);
                ObjectInputStream deserializador = new ObjectInputStream(descompactador);
                this.cBrancas = (Conjunto) deserializador.readObject();    
                this.cPretas = (Conjunto) deserializador.readObject();
                deserializador.close();
                System.out.println("Jogo carregado!");  
            } catch (Exception ex) {
                System.out.println("Ocorreu o seguinte erro:" + ex.getMessage());
            }
        }
        
        if(e.getKeyCode() == KeyEvent.VK_R){
            System.out.println("AQUI ENTROU!!!!!");
            this.gerarTabuleiroRandomizado();
        }

        repaint();

    }

    public void mouseClicked(MouseEvent e) {
        /*Peca pecaClicada = this.getPecaClicada(this.getPosicaoDoClique(e)); 
        if(pecaClicada != null){
            this.tTabuleiro.destacarPosicao(pecaClicada.getPosicaoPeca());
        }*/
        
    }
    
    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        /*
        Peca pecaClicada = this.getPecaClicada(this.getPosicaoDoClique(e)); 
        if(pecaClicada != null){
            this.tTabuleiro.destacarPosicao(pecaClicada.getPosicaoPeca());
        }
        */
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanelCenario = tTabuleiro;
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        coordenadaLabel = new javax.swing.JLabel();
        clickLabel = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SCC0204 - Xadrez");
        setResizable(false);

        jPanelCenario.setMaximumSize(new java.awt.Dimension(800, 800));
        jPanelCenario.setMinimumSize(new java.awt.Dimension(800, 800));
        jPanelCenario.setPreferredSize(new java.awt.Dimension(800, 800));
        jPanelCenario.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout jPanelCenarioLayout = new javax.swing.GroupLayout(jPanelCenario);
        jPanelCenario.setLayout(jPanelCenarioLayout);
        jPanelCenarioLayout.setHorizontalGroup(
            jPanelCenarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        jPanelCenarioLayout.setVerticalGroup(
            jPanelCenarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );

        jLabel2.setText("Coordenada:");

        jLabel3.setText("click:");

        coordenadaLabel.setText("10");

        clickLabel.setText("quadrante");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanelCenario, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(clickLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(coordenadaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 265, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(293, 293, 293))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelCenario, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(clickLabel)
                        .addComponent(coordenadaLabel)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel clickLabel;
    private javax.swing.JLabel coordenadaLabel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelCenario;
    // End of variables declaration//GEN-END:variables

}
