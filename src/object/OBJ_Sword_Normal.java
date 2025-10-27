package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword_Normal extends Entity {

	public OBJ_Sword_Normal(GamePanel gp) {
		super(gp);
		
		name = "Normal Sword";
		down1 = setup("/objects/sword_normal",gp.tilesize,gp.tilesize);
		attackValue = 1;
		description = "[" + name + "]\nAn old sword.";
	}
	
}
