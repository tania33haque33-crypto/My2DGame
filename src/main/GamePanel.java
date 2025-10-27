package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
	private static final long serialVersionUID = 1L;
	
	//SCREEN SETTINGS 
	final int originalTileSize = 16; // 16x16 tile 
    final int scale = 3;
   
  public final int tilesize = originalTileSize * scale ;// 48x48 tile
  public final int maxScreenCol = 16;
  public final int maxScreenRow = 12;
  public final int screenWidth = tilesize * maxScreenCol;// 768 pixels
  public final int screenHeight = tilesize * maxScreenRow;// 576 pixels
   
  //WORLD SETTINGS 
  public final int maxWorldCol = 50;
  public final int maxWorldRow = 50;
  
   //FPS
   int FPS = 60;
   
   //SYSTEM
   TileManager tileM = new TileManager(this);
   public keyHandler keyH = new keyHandler(this);
   Sound music = new Sound();
   Sound se = new Sound();
   public CollisionChecker cChecker = new CollisionChecker(this);
   public AssetSetter aSetter = new AssetSetter(this);
   public UI ui = new UI(this);
   public EventHandler eHandler = new EventHandler(this);
   Thread gameThread ;
   
   // ENTITY AND OBJECT
   
   public  Player player = new Player(this,keyH);
   public Entity obj[] = new Entity[10];
   public Entity npc[] = new Entity[10];
   public Entity monster[] = new Entity[20];
   ArrayList<Entity> entityList = new ArrayList<>();
   
 //GAME STATE
   public int gameState;
   public final int titleState = 0;
   public final int playState = 1;
   public final int pauseState = 2;
   public final int dialogueState = 3;
   public final int characterState = 4;
//   public final int optionsState = 5;
//   public final int gameOverState = 6;
//   public final int transitionState = 7;
//   public final int tradeState = 8;
//   public final int sleepState = 9;
//   public final int mapState = 10;
//   public final int cutsceneState = 11;
  public boolean gameFinished = false;
   public GamePanel() {
	   this.setPreferredSize(new Dimension(screenWidth, screenHeight));
	   this.setBackground(Color.black);
	   this.setDoubleBuffered(true);
	   this.addKeyListener(keyH);
	   this.setFocusable(true);
	   keyH.checkDrawTime = false;
	}
   
   public void setupGame() {
	   aSetter.setObject();
	   aSetter.setNPC();
	   aSetter.setMonster();
	   //playMusic(0);
	  // stopMusic();
	   gameState = titleState  ;
   }
   public void StartGameThread() {
	   
	   gameThread = new Thread (this);
	   gameThread.start();
   }
 
	   public void run() {
		   double drawInterval = 1000000000/FPS;
		   double delta = 0;
		   long lastTime = System.nanoTime();
		   long currentTime;
		   long timer = 0;
		   int drawCount = 0;
		   
	   
		   while(gameThread != null) {
			  currentTime = System.nanoTime();
			  
			  delta += (currentTime - lastTime) / drawInterval;
			  timer += (currentTime - lastTime);
			  lastTime = currentTime;
			  
			  if(delta >= 1) {
				  update();
				   repaint();
				   delta --;
				   drawCount ++;
				   
			  }
			  if (timer >= 1000000000) {
				  System.out.println("FPS:"+ drawCount);
				  drawCount = 0;
				  timer = 0;
				  
			  }
			   
		   }
	   }
   public void update() {
	   if(gameState == playState)
       {
           // PLAYER
           player.update();
	       // NPC
           for(int i =0;i<npc.length;i++) {
        	   if(npc[i] != null) {
        		   npc[i].update();
        	   }
           }
           // Monster
          for(int i=0;i< monster.length;i++) {
        	  if(monster[i] != null) {
        		  if(monster[i].alive == true && monster[i].dying == false) {
        			  monster[i].update();
        		  }
        		  if(monster[i].alive == false) {
        			  monster[i] = null;
        		  }    
        	  }
          }
	   }
	   else if(gameState == pauseState) {
		   //nothing 
	   }
   }
   public void paintComponent(Graphics g) {
	   super.paintComponent(g);
	   Graphics2D g2 = (Graphics2D)g;
	   
	   //DEBUG
	   long drawStart = 0;
	   if(keyH.checkDrawTime == true) {
		   drawStart = System.nanoTime();  
	   }
	   // TILE SCREEN
	   
	   if(gameState == titleState) {
		   ui.draw(g2);
		   
	   }
	   //OTHERS
	   else {
		   
		   //TILE
		   tileM.draw(g2);
		   
		   //ADD ENTITIES TO THE LIST
		   entityList.add(player);
	       for(int i = 0; i<npc.length;i++) {
	    	 if(npc[i] != null) {
	    		entityList.add(npc[i]) ;
	    	 }
	       }
	       for(int i = 0; i<obj.length;i++) {
	    	   if(obj[i] != null) {
	    		   entityList.add(obj[i]) ;  
	    	   }
	       }
	       for(int i = 0; i<monster.length;i++) {
	    	   if(monster[i] != null) {
	    		   entityList.add(monster[i]) ;  
	    	   }
	       }
	       //SORT
	       Collections.sort(entityList,new Comparator<Entity>(){

			@Override
			public int compare(Entity e1, Entity e2) {
				
				return Integer.compare(e1.worldY,e2.worldY);
		
			  }
	       });
	       
	       // DRAW ENTITIES 
	       for( int i1 =0; i1< entityList.size();i1++) {
	    	  entityList.get(i1).draw(g2);
	       }
	       //EMPTY ENTITY LIST
	       entityList.clear();
		   //UI
		   ui.draw(g2);
//		   if(gameState == playState) {
//			   ui.drawMessage();
//		   }
	   }

	   
	   //DEBUG
	   if(keyH.checkDrawTime == true ) {
		   long drawEnd = System.nanoTime();
		   long passed = drawEnd - drawStart;
		      g2.setColor(Color.white);
		      g2.drawString("Draw time : " + passed,10,400);
	          System.out.println("Draw Time:"+passed);
		    
	  }
	     g2.dispose();
	 }
  public void playMusic(int i) {
	 music.setFile(i); 
	 music.play();
	 music.loop(); 
  }
  public void stopMusic() {
	 music .stop();
  }
  public void playSE(int i) {
	  se.setFile(i);
	  se.play();
  }
}
