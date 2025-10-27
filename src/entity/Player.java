package entity;
import java.awt.AlphaComposite;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.GamePanel;
import main.keyHandler;
import object.OBJ_Key;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;
public class Player extends Entity {
	 GamePanel gp;
     keyHandler keyH;
     public final int screenX;
	 public final int screenY;
     int standCounter = 0;
     public boolean attackCanceled = false;
     public ArrayList<Entity> inventory =  new ArrayList<>();
     public final int maxInventorySize = 20;
	 
	 
	 
	 public Player(GamePanel gp , keyHandler keyH) {
    	 super(gp);
    	 
    	this .gp = gp;
    	 this.keyH = keyH;
		 
    	 screenX = gp.screenWidth/2 - (gp.tilesize/2);
    	 screenY = gp.screenHeight/2 - (gp.tilesize/2);
    	 
    	 solidArea = new Rectangle();
    	 solidArea.x = 8;
    	 solidArea.y = 16;
    	 solidAreaDefaultX = solidArea.x ;
    	 solidAreaDefaultY = solidArea.y;
    	 solidArea.width = 32;
    	 solidArea.height = 32;
    	 
    	 attackArea.width= 36;
    	 attackArea.height= 36;
    	 
    	 setDefaultValues();
    	 getPLayerImage();
    	 getPlayerAttackImage();
    	 setItems();
    	 
     }
     public  void setDefaultValues() {
    	
    	 worldX = gp.tilesize * 23;
    	 worldY = gp.tilesize * 21;
//    	 worldX = gp.tilesize * 10;
//    	 worldY = gp.tilesize * 13;
    	 speed = 4;
    	 direction = "down";
    	 
    	 //PLAYER STATUS
    	 level = 1;
    	 maxLife = 6;
    	 life = maxLife;
    	 strength = 1;
    	 dexterity = 1;
    	 exp =0;
    	 nextLevelExp = 5;
    	 coin = 0;
    	 currentWeapon = new OBJ_Sword_Normal(gp);
    	 currentShield = new OBJ_Shield_Wood(gp);
    	 attack = getAttack();
    	 defense = getDefense();	 
     }
     public void setItems() {
    	 inventory.add(currentWeapon);
    	 inventory.add(currentShield);
    	 inventory.add(new OBJ_Key(gp));
     }
     public int getAttack() {
    	 return attack = strength *currentWeapon.attackValue;
     }
     public int getDefense() {
    	 return defense = dexterity *currentShield.defenseValue;
     }
     public void getPLayerImage() {
    	 up1 = setup("/player/boy_up_1",gp.tilesize, gp.tilesize);
    	 up2 = setup("/player/boy_up_2",gp.tilesize, gp.tilesize);
    	 down1 = setup("/player/boy_down_1",gp.tilesize, gp.tilesize);
    	 down2= setup("/player/boy_down_2",gp.tilesize, gp.tilesize);
    	 left1 = setup("/player/boy_left_1",gp.tilesize, gp.tilesize);
    	 left2 = setup("/player/boy_left_2",gp.tilesize, gp.tilesize);
    	 right1= setup("/player/boy_right_1",gp.tilesize, gp.tilesize);
    	 right2= setup("/player/boy_right_2",gp.tilesize, gp.tilesize);
    	 
     }
     public void getPlayerAttackImage() {
             attackUp1 = setup("/player/boy_attack_up_1",gp.tilesize, gp.tilesize*2);
             attackUp2 = setup("/player/boy_attack_up_2",gp.tilesize, gp.tilesize*2);
             attackDown1 = setup("/player/boy_attack_down_1",gp.tilesize, gp.tilesize*2);     // 16x32 px
             attackDown2 = setup("/player/boy_attack_down_2",gp.tilesize, gp.tilesize*2);   // 16x32 px
             attackLeft1 = setup("/player/boy_attack_left_1",gp.tilesize, gp.tilesize*2); 
             attackLeft2 = setup("/player/boy_attack_left_2",gp.tilesize, gp.tilesize*2); 
             attackRight1 = setup("/player/boy_attack_right_1",gp.tilesize, gp.tilesize*2);
             attackRight2 = setup("/player/boy_attack_right_2",gp.tilesize, gp.tilesize*2);
         }
   
     public void update() {
    	 if(attacking == true) {
    		 attacking();
    	 }
    	 else if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true ||keyH.rightPressed == true || keyH.enterPressed ==true) {
    		
    		 
    	 
    	 if( keyH.upPressed == true) {
    	   direction = "up";    
  	   }
  	   else if (keyH.downPressed == true) {
  		  direction = "down";  
  	   }
  	   else if (keyH.leftPressed == true) {
  		 direction = "left"; 
  	   }
  	   else if (keyH.rightPressed== true) {
  		   direction = "right";  
           }
    	 //CHECK TILE COLLISION
    	 collisionOn = false;
    	 gp.cChecker.checkTile(this);
    	 
    	 // CHECK OBJECT COLLISION
    	int objIndex =  gp.cChecker.checkObject(this , true);
    	pickUpobject(objIndex);
    	
    	//CHECK NPC COLLISION
    	int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
    	interactNPC(npcIndex);
    	
    	//CHECK MONSTER COLLISION
        int monsterIndex = gp.cChecker.checkEntity(this, gp.monster); 
        contactMonster(monsterIndex);
    	
    	//CHECK EVENT
    	gp.eHandler.checkEvent();
    
    	  
    	  //IF COLLISION IS FALSE, PLAYER CAN MOVE 
    	  if(collisionOn == false && keyH.enterPressed == false) {
    		 switch(direction){
    		 case "up":worldY -= speed; break;
    		 case "down" :worldY += speed ;break;
    	     case "left" :worldX-= speed;  break;
    	     case "right" :worldX += speed ;break;
    		 
    		 }
    	  }
    	  if(keyH.enterPressed == true && attackCanceled == false) {
    		gp.playSE(7); 
    		attacking = true;
    		spriteCounter = 0;
    	  }
    	  attackCanceled = false;
    	  
    	 gp.keyH.enterPressed = false;
    	  
    	 spriteCounter ++;
    	 if(spriteCounter > 12) {
    		 if(spriteNum == 1 ) {
    			spriteNum = 2;
    			
    		 }
    		 else if(spriteNum == 2) {
    			 spriteNum = 1;
    			 
    		 }
    		 spriteCounter = 0;
    	 }
     }
//    	 else {
//    		 standCounter++;
//    		 if(standCounter == 20) {
//    			 spritNum = 0;
//    			 standCounter = 0;
//    			 
//    		 }
//    	 }
    	 //this need to be outside of key if statement!
    	 if(invincible == true) {
    		 invincibleCounter++;
    		 if(invincibleCounter >60) {
    			 invincible = false;
    			 invincibleCounter = 0;
    		 }
    	 }
  }
     public void attacking() {
    	spriteCounter++;
        if(spriteCounter <=5) {
           spriteNum= 1;   
        }
        if(spriteCounter >5 && spriteCounter<=25) {
           spriteNum= 2;  
          //save the current worldX,worldY,solidArea
           int currentWorldX = worldX;
           int currentWorldY = worldY;
           int solidAreaWidth = solidArea.width;
           int solidAreaHeight = solidArea.height;
           
           //Adjust player's worldX/Y for the attackArea
           switch(direction) {
           case"up":worldY -= attackArea.height;break;
           case"down":worldY += attackArea.height;break;
           case"left":worldX -= attackArea.width;break;
           case"right":worldX += attackArea.width;break;
           }
           //attackArea becomes solid area
           solidArea.width = attackArea.width;
           solidArea.height = attackArea.height;
           //Check monster collision with the update worldX,worldY and solidArea
           int monsterIndex = gp.cChecker.checkEntity(this,gp.monster);
           damageMonster(monsterIndex);
           //After checking collisoin ,restore the original date
           worldX = currentWorldX;
           worldY = currentWorldY;
           solidArea.width = solidAreaWidth;
           solidArea.height = solidAreaHeight;
        }
        if(spriteCounter > 25) {
           spriteNum= 1; 
           spriteCounter = 0;
           attacking = false;
        }
     }
     public void pickUpobject(int i) {
    	 
    	 if(i!= 999 ) {
    		 
    	 }
     }
     public void interactNPC(int i) {
    	 if(gp.keyH.enterPressed == true) {
          if(i!= 999 ) {
        	     attackCanceled = true; 
    			 gp.gameState = gp.dialogueState;
           	     gp.npc[i].speak();
    		 }	 
          }
     }
     public void contactMonster(int i) {
    	 if(i!= 999 ) {
    		 if(invincible == false ) {
    			 gp.playSE(6);
    			 int damage = gp.monster[i].attack - defense;
     			 if(damage < 0) {
     				damage = 0;
     			}
    			 life -= damage ;
    			 invincible = true;
    		 }
    		
    	 }
     }
     public void damageMonster(int i) {
    	 if(i!= 999 ) {
    		if(gp.monster[i].invincible == false) {
    			
    			gp.playSE(5);
    			int damage = attack - gp.monster[i].defense;
    			if(damage < 0) {
    				damage = 0;
    			}
    			gp.monster[i].life -= damage;
    			gp.ui.addMessage(damage + " damage!");
    			gp.monster[i].invincible= true;
    			gp.monster[i].damageReaction();
    			
    			if(gp.monster[i].life <= 0) {
    			  gp.monster[i].dying = true;
    			  gp.ui.addMessage("Killed the " + gp.monster[i].name + "!");
    			  gp.ui.addMessage("Exp + " + gp.monster[i].exp);
    			  exp += gp.monster[i].exp;
    			  checkLevelUp();
    			}
    		}
    	 }
     }
     public void checkLevelUp() {
    	 if(exp >= nextLevelExp)
         {
             level++;
             nextLevelExp = nextLevelExp*2;  
             maxLife += 2;
             strength++;
             dexterity++;
             attack = getAttack();
             defense = getDefense();
             
             gp.playSE(8); //levelup.wav
             //gp.gameState = gp.dialogueState;
             gp.ui.addMessage(" You are level " + level + " now!\n" + "You feel stronger!");
//                     setDialogue();
            // gp.keyH.enterPressed = false;
         }
     }
     public void draw(Graphics2D g2) {
    	 BufferedImage Image = null;
    	 int tempScreenX = screenX;
    	 int tempScreenY = screenY;
    	 switch(direction) {
    	 case "up":
    		 if(attacking == false) {
    			 if(spriteNum == 1) {Image = up1; }
        		 if(spriteNum == 2) {Image = up2;} 
    		 }
    		 if(attacking == true) {
    			 tempScreenY = screenY - gp.tilesize;
    			 if(spriteNum == 1) {Image =attackUp1; }
        		 if(spriteNum == 2) {Image = attackUp2;} 
    		 }
    		 break;
    	 case "down":
    		 if(attacking == false) {
    			 if(spriteNum == 1) {Image = down1; }
        		 if(spriteNum == 2) {Image = down2;}
        		 }
    		 if(attacking == true) {
    			 if(spriteNum == 1) {Image = attackDown1; }
        		 if(spriteNum == 2) {Image = attackDown2;}
    		 }
    		 break;
    		 
    	 case"left":
    		 if(attacking == false) {
    			 if(spriteNum == 1) {Image = left1; }
        		 if(spriteNum == 2) {Image = left2;} 
    		 }
    		 if(attacking == true) {
    			 tempScreenX = screenX - gp.tilesize;
    			 if(spriteNum == 1) {Image = attackLeft1; }
        		 if(spriteNum == 2) {Image = attackLeft2;} 
    		 }
    		
    		 break;
    	 case"right":
    		 if(attacking == false) {
    			 if(spriteNum == 1) {Image = right1; }
        		 if(spriteNum == 2) {Image = right2;}  
    		 }
    		 if(attacking == true) {
    		 if(spriteNum == 1) { Image = attackRight1;}
    		 if(spriteNum == 2) {Image = attackRight2;}
    		 }
    		 break; 
    	 }
    	 if(invincible == true) {
    		 g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.3f));
    	 }
    	 g2.drawImage(Image, tempScreenX, tempScreenY, null);
    	 
    	 // reset Alpha 
    	 g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
    	 
    	//g2.fillRect(screenX + solidArea.x,  screenY + solidArea.y, solidArea.width,solidArea.height); 
    	 //DEBUG
//    	 g2.setFont(new Font("Arial",Font.PLAIN,26));
//    	 g2.setColor(Color.white);
//    	 g2.drawString("Invincible:"+ invincibleCounter,10,400);
//    	 
    	 
    	 
    	 
    	 
    	 
     }
}