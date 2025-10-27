package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Heart extends Entity {
    public  OBJ_Heart(GamePanel gp) {
    	super(gp);
    	name = "Heart";
    	image2 = setup("/objects/heart_full",gp.tilesize, gp.tilesize);
    	image3 = setup("/objects/heart_half",gp.tilesize, gp.tilesize);
    	image = setup("/objects/heart_blank",gp.tilesize, gp.tilesize);
    	
    }	

}

