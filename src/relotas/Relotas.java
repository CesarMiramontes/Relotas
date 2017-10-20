/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package relotas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 *
 * @author Cesar Miramontes
 */
public class Relotas extends JComponent{
    private final static int ANCHO = 1080;
    private final static int ALTO = 680;
    private final static int DIAMETRO = 30;
    int k = 0, o=1000;
    int X = -1, Y = -1;
    public Sprite sprite;
    private float x, y;
    private float vx, vy;
    private LinkedList <pelota> a = new LinkedList <pelota>();
    Custom td;
    long actual, Tnuevo, tviejo, reciente=0;
    int key=0;
    
    public Relotas(){
        setPreferredSize(new Dimension(ANCHO,ALTO));
        sprite =  new Sprite ("/resources/p.png", 64, 64,100, 100);
        x = 10;
        y = 20;
        vx = 300;
        vy = 400;
        //a.add(new pelota());
        td = new Custom(a);
        addMouseListener(new  MouseAdapter(){
                public void mousePressed(MouseEvent evento){
                    X=evento.getX()-40;
                    Y=evento.getY()-40;
                    sprite = new Sprite ("/resources/p.png", 64, 64,(int) X, Y); 
                    o=0;
                }

                });
    }

    @Override
    public void paint(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, ANCHO, ALTO);
        for(int i=0; i<a.size(); i++){
            switch (a.get(i).color){
                case 1:
                    g.setColor(Color.RED);
                    break;
                case 2:
                    g.setColor(Color.blue);
                    break;
                case 3:
                    g.setColor(Color.yellow);
                    break;
                case 4:
                    g.setColor(Color.orange);
                    break;
                case 5:
                    g.setColor(Color.pink);
                    break;
                    
            }
            g.fillOval(Math.round(a.get(i).x), Math.round(a.get(i).y), DIAMETRO, DIAMETRO);
            
        }
        g.setColor(Color.WHITE);
        g.drawString(toString("Pelotas en Pantalla: ",a.size()), 0, 10);
        if (reciente != 0){
            g.drawString("Pelota Nueva Generada", 0, 30);
        }
        sprite.pintar(g);
        sprite.setFrame((o++)/10);
    }
    
    public String toString(String m, int num){
        return (m + num);
    }
    
    public void cicloPrincipalJuego() throws Exception{
        long tiempoViejo = System.nanoTime();
         tviejo = System.currentTimeMillis();
        while(true){
            long tiempoNuevo = System.nanoTime();
            float dt = (tiempoNuevo - tiempoViejo)/1000000000f;
            tiempoViejo = tiempoNuevo;
            Tnuevo = System.currentTimeMillis() - tviejo;
            actual = Tnuevo/1000;
            a = td.getLista();
            for(int i=0; i<a.size(); i++){
                try{
                    a.get(i).fisica(dt);
                }catch(NullPointerException e){
                    
                }
            }
            if (o < 350)
                Kill();
            dibuja();
        }
        //ad.player.close();
    }
    
    public void Kill(){
        try{
            for (int i=0; i<a.size(); i++){
                if(a.get(i).x+50>X && a.get(i).x-50<X){
                   //System.out.println(a.get(i).x+10+ ">" +X +" || "+ (a.get(i).x-10) +"<"+X);
                    if(a.get(i).y+50>Y && a.get(i).y-50<Y){
                        td.exp(i);
                        }
                    }
                }   
            }catch(IndexOutOfBoundsException e){}
    }
    
    private void dibuja() throws Exception{
        SwingUtilities.invokeAndWait(new Runnable(){
            public void run(){
                paintImmediately(0, 0, ANCHO, ALTO);
            }
        });
    }
    private void fisica(float dt){
        x += vx * dt;
        y += vy * dt;
        if(vx < 0 && x <=0 || vx > 0 && x + DIAMETRO >= ANCHO){
            vx = -vx;
        }
        if(vy < 0 && y <0 || vy > 0 && y + DIAMETRO >= ALTO){
            vy = -vy;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        while(true){
            try{
                JFrame jf = new JFrame ("El Juego De Las Pelotas");
                jf.addWindowListener(new WindowAdapter(){
                    public void windowClosing(WindowEvent e){
                        System.exit(0);
                    }
                });

                jf.setResizable(false);
                Relotas demo1 = new Relotas();
                //EjemploGrafico1 demo2 = new EjemploGrafico1(30,50,500,400);
                jf.getContentPane().add(demo1);
                //jf.getContentPane().add(demo2);
                jf.pack();
                jf.setVisible(true);
                demo1.cicloPrincipalJuego();
                jf.dispose();
            }catch(Exception e){
                
            }
        }
        //demo2.cicloPrincipalJuego();
    }
    
}

class pelota{
    float x, y, vx, vy;
    final int color;
    private final static int ANCHO = 1080;
    private final static int ALTO = 680;
    private final static int DIAMETRO = 20;
    
    pelota(){
        x = (int) (Math.random() * 1080);
        y = (int) (Math.random() * 680);
        vx = (int) (Math.random() * 500);
        vy = (int) (Math.random() * 500);
        color = (int) (Math.random() * 5) + 1;
    }
    
    void fisica(float dt){
        x += vx * dt;
        y += vy * dt;
        if(vx < 0 && x <=0 || vx > 0 && x + DIAMETRO >= ANCHO){
            vx = -vx;
        }
        if(vy < 0 && y <0 || vy > 0 && y + DIAMETRO >= ALTO){
            vy = -vy;
        }
    }
}

class Custom extends Thread{
    LinkedList <pelota> A = new LinkedList <pelota> ();
    long actual, Tnuevo, tviejo, reciente=0;
    int k = 0;
    
    Custom(LinkedList <pelota> A){
        this.A = A;
        start();
    }
    
    void exp(int i){
        A.remove(i);
    }
    
    LinkedList getLista(){
       /* try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            //Logger.getLogger(Custom.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        return A;
    }
    
    public void run(){
        long tiempoViejo = System.nanoTime();
         tviejo = System.currentTimeMillis();
        while(true){
            long tiempoNuevo = System.nanoTime();
            float dt = (tiempoNuevo - tiempoViejo)/1000000000f;
            tiempoViejo = tiempoNuevo;
            Tnuevo = System.currentTimeMillis() - tviejo;
            actual = Tnuevo/1000;
            A.add(new pelota());
            try {
                Thread.sleep(500);
                //System.out.println(actual);
                /*if (k == 0 && actual%3 == 0){
                A.add(new pelota());
                //System.out.println(k);
                k = 1;
                reciente = 1;
                }else{
                if(actual%5 != 0){
                k = 0;
                }
                if(actual%3 != 0){
                reciente = 0;
                }
                }
                for(int i=0; i<A.size(); i++){
                try{
                A.get(i).fisica(dt);
                }catch(NullPointerException e){
                    
                }
                }*/
            } catch (InterruptedException ex) {
                Logger.getLogger(Custom.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}