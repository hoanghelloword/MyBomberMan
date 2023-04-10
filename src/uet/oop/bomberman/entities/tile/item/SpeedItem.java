package uet.oop.bomberman.entities.tile.item;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.graphics.Sprite;

/**
 * item tăng vận tốc
 */
public class SpeedItem extends Item {

	public SpeedItem(int x, int y, Sprite sprite) {
		super(x, y, sprite);
	}

	/**
	 * ktra va chạm (bomber, ...)
	 */
	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý Bomber ăn Item
            if (e instanceof Bomber) {
                
                //Sound.play("Item");
                Game.addBomberSpeed(0.5);
                remove();
            }
        return false;
	}
}
