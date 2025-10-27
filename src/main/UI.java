package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import object.OBJ_Heart;
import entity.Entity;
public class UI {

	 GamePanel gp;
	 Graphics2D g2;
	 Font maruMonica, purisaB;
	 BufferedImage heart_full,heart_half,heart_blank;
	 public boolean messageOn = false;
//	 public String message = "";
//	 int messageCounter = 0;
	 ArrayList<String> message = new ArrayList<>();
	 ArrayList<Integer> messageCounter = new ArrayList<>();
 	 public boolean gameFinished = false;
	 public String currentDialogue = "";
	 public int commandNum =0;
	 public int titleScreenState = 0;//0: the first screen, 1: the second screen
	 public int slotCol = 0;
	 public int slotRow = 0;
	 
	 public UI(GamePanel gp) {
		 this.gp = gp;
	       
	        try {
	        	InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf"); 
				maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
				is = getClass().getResourceAsStream("/font/Purisa Bold.ttf");
				purisaB = Font.createFont(Font.TRUETYPE_FONT, is);
			} catch (FontFormatException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
	        //CREATE HUD OBJECT
	        Entity heart = new OBJ_Heart(gp);
	        heart_full = heart.image2;
	        heart_half = heart.image3;
	        heart_blank = heart.image;
	        
	    }
	

	 public void addMessage(String text) {
		 
		 message.add(text);
		 messageCounter.add(0);
	 }
	 public void draw(Graphics2D g2) {
		 this.g2 = g2;
		 g2.setFont(maruMonica);
		 //g2.setFont(purisaB);
		 g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		 g2.setColor(Color.white);
		 
		 //TITLE STATE
		 if(gp.gameState == gp.titleState) {
			 drawTitleScreen();
		 }
		 
		 //PLAY STATE
		 else if(gp.gameState == gp.playState) {
			 drawPlayerLife();
			 drawMessage();
		 }
		 // PAUSE STATE
		 else if(gp.gameState == gp.pauseState) {
			drawPlayerLife();
			drawPauseScreen();	
		}
		// DIALOGUE STATE
		 else if(gp.gameState == gp.dialogueState) {
			drawPlayerLife();
			drawDialogueScreen();
		}
		 //CHARACTER STATE
		 else if (gp.gameState == gp.characterState) {
			 drawCharacterScreen();
			 drawInventory();
		 }
	 }
	 public void drawPlayerLife() {// gp.player.life = 3;
		 int x = gp.tilesize/2;
		 int y = gp.tilesize/2;
		 int i =0;
		 
		 //DRAW MAX LIFE
		 while(i< gp.player.maxLife/2) {
			g2.drawImage(heart_blank, x, y,null) ;
			i++;
			x += gp.tilesize;
			
		 }
		 //RESET
		  x = gp.tilesize/2;
		  y = gp.tilesize/2;
		  i =0;
		  
		  //DRAW CURRENT LIFE
		  while(i < gp.player.life) {
			  g2.drawImage(heart_half,x,y,null);
			  i++;
			  if(i < gp.player.life) {
				 g2.drawImage(heart_full, x, y,null) ;
			  }
			  i++;
			  x += gp.tilesize;
		  }
	 }
	 public void drawMessage()
	    {
	        int messageX = gp.tilesize;
	        int messageY = gp.tilesize * 4;
	        g2.setFont(g2.getFont().deriveFont(Font.BOLD,24F));

	        for(int i = 0; i < message.size(); i++)
	        {
	            if(message.get(i) != null)
	            {
	                //Shadow
	                g2.setColor(Color.black);
	                g2.drawString(message.get(i), messageX+2,messageY+2);
	                //Text
	                g2.setColor(Color.white);
	                g2.drawString(message.get(i), messageX,messageY);

	                int counter = messageCounter.get(i) + 1; //messageCounter++
	                messageCounter.set(i,counter);           //set the counter to the array
	                messageY += 50;

	                if(messageCounter.get(i) > 150)          //display 2.5 seconds
	                {
	                    message.remove(i);
	                    messageCounter.remove(i);
	                }
	            }

	        }
	    }
	 public void  drawTitleScreen() {if(titleScreenState == 0) {
		 g2.setColor(new Color(0,0,0));
		 g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
		 //TITLE NAME
		 g2.setFont(g2.getFont().deriveFont(Font.BOLD,96F));
		 String text = "Blue Boy Adventure";
		 int x = getXforCenteredText(text);
		 int y = gp.tilesize * 3;
		 //SHADOW
		 g2.setColor(Color.gray);
		 g2.drawString(text, x+5, y+5);
		 //MAIN COLOR
		 g2.setColor(Color.white);
		 g2.drawString(text, x, y);
	 
	 
	 // BLUE BOY IMAGE
	 x = gp.screenWidth/2 -( gp.tilesize*2)/2;
	 y+= gp.tilesize*2;
	 g2.drawImage(gp.player.down1,x,y, gp.tilesize*2, gp.tilesize*2,null);
	 
	 //MENU
	 g2.setFont(g2.getFont().deriveFont(Font.BOLD,40F));
	 
	 text = "NEW GAME";
	 x = getXforCenteredText(text);
	 y+= gp.tilesize*3.5;
	 g2.drawString(text, x, y);
	 if(commandNum == 0) {
		 g2.drawString(">", x-gp.tilesize, y);
	 }
	 
	 text = "LOAD GAME";
	 x = getXforCenteredText(text);
	 y+= gp.tilesize;
	 g2.drawString(text, x, y);
	 if(commandNum == 1) {
		 g2.drawString(">", x-gp.tilesize, y);
	 }
	 
	  
	 text = "QUIT";
	 x = getXforCenteredText(text);
	 y+= gp.tilesize;
	 g2.drawString(text, x, y);
	 if(commandNum == 2) {
		 g2.drawString(">", x-gp.tilesize, y);
		
	 }
	 
 }
 else if(titleScreenState==1) {
	 
	 //CLASS SELECTION SCREEN
	 g2.setColor(Color.white);
	 g2.setFont(g2.getFont().deriveFont(42F));
	 
	 String text = "Select your class!";
	 int x = getXforCenteredText(text);
	 int y= gp.tilesize*3;
	 g2.drawString(text, x, y);
	 
	 text ="Fighter";
	 x = getXforCenteredText(text);
	 y+= gp.tilesize*3;
	 g2.drawString(text, x, y);
	 if(commandNum == 0) {
		g2.drawString(">",x-gp.tilesize,y) ;
	 }
	 
	 text ="Thief";
	 x = getXforCenteredText(text);
	 y+= gp.tilesize;
	 g2.drawString(text, x, y);
	 if(commandNum == 1) {
		g2.drawString(">",x-gp.tilesize,y) ;
	 }
	 
	 text ="Sorcerer";
	 x = getXforCenteredText(text);
	 y+= gp.tilesize;
	 g2.drawString(text, x, y);
	 if(commandNum == 2) {
		g2.drawString(">",x-gp.tilesize,y) ;
	 }
	 text ="Back";
	 x = getXforCenteredText(text);
	 y+= gp.tilesize*2;
	 g2.drawString(text, x, y);
	 if(commandNum == 3) {
		g2.drawString(">",x-gp.tilesize,y) ;
	 }
	
								 
 }

}
	 public void drawPauseScreen() {g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
	 String text = "PAUSED";
	 int x = getXforCenteredText(text);
	
	 int y = gp.screenHeight/2;
	 
	 g2.drawString(text,x,y);
	 }
	 public void drawDialogueScreen() {
		 // WINDOW
	        int x = gp.tilesize * 2;
	        int y = gp.tilesize / 2;
	        int width = gp.screenWidth - (gp.tilesize * 4);
	        int height = gp.tilesize * 4;
	        drawSubWindow(x,y,width,height);

	        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,32F));
	        x += gp.tilesize;
	        y += gp.tilesize;
	        
	        for(String line : currentDialogue.split("\n"))   // splits dialogue until "\n" as a line
	        {
	            g2.drawString(line,x,y);
	            y += 40;
	        }
	 }
	 public void drawCharacterScreen() {
		// CREATE A FRAME
	        final int frameX = gp.tilesize ;
	        final int frameY = gp.tilesize;
	        final int frameWidth = gp.tilesize * 5;
	        final int frameHeight = gp.tilesize *10;
	        drawSubWindow(frameX,frameY,frameWidth,frameHeight);
        // TEXT
	        g2.setColor(Color.white);
	        g2.setFont(g2.getFont().deriveFont(32F));

	        int textX = frameX + 20;
	        int textY = frameY + gp.tilesize;
	        final int lineHeight = 35;
	        
	     // NAMES
	        g2.drawString("Level", textX,textY);
	        textY += lineHeight;
	        g2.drawString("Life", textX,textY);
	        textY += lineHeight;
	        g2.drawString("Strength", textX,textY);
	        textY += lineHeight;
	        g2.drawString("Dexterity", textX,textY);
	        textY += lineHeight;
	        g2.drawString("Attack", textX,textY);
	        textY += lineHeight;
	        g2.drawString("Defense", textX,textY);
	        textY += lineHeight;
	        g2.drawString("Exp", textX,textY);
	        textY += lineHeight;
	        g2.drawString("Next Level", textX,textY);
	        textY += lineHeight;
	        g2.drawString("Coin", textX,textY);
	        textY += lineHeight + 20;
	        g2.drawString("Weapon", textX,textY);
	        textY += lineHeight + 15;
	        g2.drawString("Shield", textX,textY);
 
        // VALUES
	        int tailX = (frameX + frameWidth) - 30; 
	      
	    // RESET textY
	        textY = frameY + gp.tilesize;
	        String value;

	        value = String.valueOf(gp.player.level);
	        textX = getXforAlignToRightText(value,tailX);
	        g2.drawString(value,textX,textY);
            textY += lineHeight;

	        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
	        textX = getXforAlignToRightText(value,tailX);
	        g2.drawString(value,textX,textY);
	        textY += lineHeight;

	        value = String.valueOf(gp.player.strength);
	        textX = getXforAlignToRightText(value,tailX);
	        g2.drawString(value,textX,textY);
	        textY += lineHeight;

	        value = String.valueOf(gp.player.dexterity);
	        textX = getXforAlignToRightText(value,tailX);
	        g2.drawString(value,textX,textY);
	        textY += lineHeight;

	        value = String.valueOf(gp.player.attack);
	        textX = getXforAlignToRightText(value,tailX);
	        g2.drawString(value,textX,textY);
	        textY += lineHeight;

	        value = String.valueOf(gp.player.defense);
	        textX = getXforAlignToRightText(value,tailX);
	        g2.drawString(value,textX,textY);
	        textY += lineHeight;

	        value = String.valueOf(gp.player.exp);
	        textX = getXforAlignToRightText(value,tailX);
	        g2.drawString(value,textX,textY);
	        textY += lineHeight;

	        value = String.valueOf(gp.player.nextLevelExp);
	        textX = getXforAlignToRightText(value,tailX);
	        g2.drawString(value,textX,textY);
	        textY += lineHeight;

	        value = String.valueOf(gp.player.coin);
	        textX = getXforAlignToRightText(value,tailX);
	        g2.drawString(value,textX,textY);
	        textY += lineHeight ;

	        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tilesize + 5, textY - 14, null);
	        textY += gp.tilesize;
	        if(gp.player.currentShield!= null)
	        {
	            g2.drawImage(gp.player.currentShield.down1, tailX - gp.tilesize + 5, textY - 14, null);
	        }
	    }
	    public void drawInventory() {
	        int frameX = gp.tilesize * 9;
	        int frameY = gp.tilesize;
	        int frameWidth = gp.tilesize * 6;
	        int frameHeight = gp.tilesize * 5;
	        drawSubWindow(frameX,frameY,frameWidth,frameHeight);
	       
	        //SLOT
	        final int slotXstart = frameX + 20;
	        final int slotYstart = frameY + 20;
	        int slotX = slotXstart;
	        int slotY = slotYstart;
	        int slotSize = gp.tilesize + 3;
	        
	        //DRAW PLAYER'S ITEMS
	        for(int i = 0; i < gp.player.inventory.size(); i++) {
	        	g2.drawImage(gp.player.inventory.get(i).down1, slotX,slotY,null);
	        	slotX += slotSize;
	        	
	        	if( i == 4 ||i == 9 || i == 14 ) {
	        		slotX = slotXstart;
	        		slotY += slotSize;
	        	}
	        }
	        
	        //CURSOR
	        int cursorX = slotXstart + (slotSize * slotCol);
	        int cursorY = slotYstart + (slotSize * slotRow);
	        int cursorWidth = gp.tilesize;
	        int cursorHeight = gp.tilesize;
	        
	        // DRAW CURSOR
	        g2.setColor(Color.white);
	        g2.setStroke(new BasicStroke(3));
	        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10,10);
	        
	        //DESCRIPTION
	        int dFrameX = frameX;
	        int dFrameY = frameY + frameHeight;
	        int dFrameWidth = frameWidth;
	        int dFrameHeight = gp.tilesize * 3;
	        drawSubWindow(dFrameX,dFrameY,dFrameWidth,dFrameHeight);
	        
	        // DRAW DESCRIPTION TEXT
	         int textX = dFrameX + 20;
	         int textY = dFrameY + gp.tilesize;
	         g2.setFont(g2.getFont().deriveFont(20F));
	         int itemIndex = getItemIndexOnSlot();
	         if(itemIndex < gp.player.inventory.size()) {
	        	 for(String line: gp.player.inventory.get(itemIndex).description.split("/")) {
	        		 g2.drawString(line, textX, textY);
	        		 textY += 32;
	        	 }	 
	       }
	    }
      
//	    g2.setFont(g2.getFont().deriveFont(28F));
//	    g2.setColor(Color.white);
//
//	    int padding = 15; 
//	    int textX = dFrameX + padding;
//	    int textY = dFrameY + padding + g2.getFontMetrics().getAscent();
//	    int maxWidth = dFrameWidth - padding * 2;
//
//	    int itemIndex = getItemIndexOnSlot();
//	    if (itemIndex < gp.player.inventory.size()) {
//	        String description = gp.player.inventory.get(itemIndex).description.replace("/n", " ");
//	        drawStringWrapped(g2, description, textX, textY, maxWidth, g2.getFontMetrics().getHeight());
//	    }
//	}
	 public int getItemIndexOnSlot() {
		 int itemIndex = slotCol + (slotRow*5);
		 return itemIndex;
	 }
	 public void drawSubWindow(int x, int y,int width,int height) {
		 Color c = new Color(0,0,0,210);  
	        g2.setColor(c);
	        g2.fillRoundRect(x,y,width,height,35,35);

	        c = new Color(255,255,255);
	        g2.setColor(c);
	        g2.setStroke(new BasicStroke(5));  
	        g2.drawRoundRect(x+5,y+5,width-10,height-10,25,25);

	 }
	 public int getXforCenteredText(String text) {
		int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
		int x = gp.screenWidth/2 - length/2;
		return x;
	 }
	 public int getXforAlignToRightText(String text,int tailX) {
			int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
			int x = tailX - length;
			return x;
		 }

//public void drawStringWrapped(Graphics2D g2, String text, int x, int y, int maxWidth, int lineHeight) {
//    FontMetrics fm = g2.getFontMetrics();
//    String[] words = text.split(" ");
//    StringBuilder line = new StringBuilder();
//    int curY = y;
//
//    for (String word : words) {
//        String testLine = line.length() == 0 ? word : line + " " + word;
//        int lineWidth = fm.stringWidth(testLine);
//        if (lineWidth > maxWidth) {
//            g2.drawString(line.toString(), x, curY);
//            curY += lineHeight;
//            line = new StringBuilder(word);
//        } else {
//            line = new StringBuilder(testLine);
//        }
//    }
//    if (line.length() > 0) {
//        g2.drawString(line.toString(), x, curY);
//    }
//}
}
