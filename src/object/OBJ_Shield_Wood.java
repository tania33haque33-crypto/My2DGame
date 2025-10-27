package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Shield_Wood extends Entity{

	public OBJ_Shield_Wood(GamePanel gp) {
		super(gp);
		
		name = "Wood Shield";
		down1 = setup("/objects/shield_wood",gp.tilesize,gp.tilesize);
		defenseValue = 1;
		description = "[" + name + " ]\nMade by wood.";
	}
	

	

}
