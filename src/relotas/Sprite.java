/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package relotas;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Cesar Miramontes
 */
public class Sprite {
    private Posicion posicion = new Posicion(); 
    private BufferedImage image;
    private int frame;
    private int width;
    private int height;
    private int tw;
    //Private
    int th;
    int pX, pY;
    
    public Sprite(String spriteName, int spriteWidth,
            int spriteHeight, int x, int y){
        try{
            image = ImageIO.read(
                getClass().getResourceAsStream(spriteName));
            width=spriteWidth;
            height=spriteHeight;
            tw= image.getWidth()/width;
            th=image.getHeight()/height;
            pX=x;
            pY=y;
        }catch(IOException ex){
            
        }
    
}
    public void setFrame(int index){
        frame=index;
    }
    
    public void pintar(Graphics g){
        int x=posicion.x;
        int y=posicion.y;
        x+=pX;
        y+=pY;
        
        int i = frame % tw;
        int j= frame/tw;
        g.drawImage(image, x, y, x+ width,
                y + height, i*width,
                j*height, (i+1)*width, (j+1)* height,
                null);
    }
    
    public void setPosicion(Posicion p){
        posicion.Set(p);
    }
    
    public void setPosicion(int x, int y){
        posicion.Set(x,y);
    }
    
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }
    
    
    
}
