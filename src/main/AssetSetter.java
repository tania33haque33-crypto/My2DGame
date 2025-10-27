package main;

import entity.NPC_OldMan;
import monster.MON_GreenSlime;
import object.OBJ_Door;

public class AssetSetter {

	GamePanel gp;
	public AssetSetter(GamePanel gp) {
		this.gp = gp;
		
	}
	public void setObject() {
	 
	}
	public void setNPC() {
		gp.npc[0] = new NPC_OldMan(gp);
        gp.npc[0].worldX = gp.tilesize*21;
        gp.npc[0].worldY = gp.tilesize*21;
        
//        gp.npc[0] = new NPC_OldMan(gp);
//        gp.npc[0].worldX = gp.tilesize*9;
//        gp.npc[0].worldY = gp.tilesize*10;
        
	}
    public void setMonster() {
    	int i =0;
    	  gp.monster[i] = new MON_GreenSlime(gp);
    	  gp.monster[i].worldX = gp.tilesize*23;
          gp.monster[i].worldY = gp.tilesize*36;
          i++;
          gp.monster[i] = new MON_GreenSlime(gp);
    	  gp.monster[i].worldX = gp.tilesize*23;
          gp.monster[i].worldY = gp.tilesize*37;
          i++;
         
          gp.monster[i] = new MON_GreenSlime(gp);
    	  gp.monster[i].worldX = gp.tilesize*24;
          gp.monster[i].worldY = gp.tilesize*37;
          i++;
          gp.monster[i] = new MON_GreenSlime(gp);
    	  gp.monster[i].worldX = gp.tilesize*34;
          gp.monster[i].worldY = gp.tilesize*42;
          i++;
          gp.monster[i] = new MON_GreenSlime(gp);
   	      gp.monster[i].worldX = gp.tilesize*38;
          gp.monster[i].worldY = gp.tilesize*42;
          i++;
    }
   }

