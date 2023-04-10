package uet.oop.bomberman.entities;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Screen;

import java.util.LinkedList;

/**
 * Chứa và quản lý nhiều Entity tại cùng một vị trí
 * Ví dụ: tại vị trí dấu Item, có 3 Entity [Grass, Item, Brick]
 */
public class LayeredEntity extends Entity {
	
	protected LinkedList<Entity> _entities = new LinkedList<>();
	public LayeredEntity(int x, int y, Entity ... entities) {
		//các đối tượng enties được thêm vào tập hợp thành 1 mảng entities
		// vd: _board.addEntity(pos, new LayeredEntity(x, y,
		//                                new Grass(x, y, Sprite.grass),
		//                                new Portal(x, y, _board, Sprite.portal),
		//                                new Brick(x, y, Sprite.brick)));
		// 3 đối tượng entities đc tự động chuyển thành 1 mảng
		_x = x;
		_y = y;
		
		for (int i = 0; i < entities.length; i++) {
			_entities.add(entities[i]); // them entities vao mang
			
			if(i > 1) {
				if(entities[i] instanceof DestroyableTile)
					// thay đổi hình ảnh phía dưới của tường vỡ
					// để xau khi phá, hiện hình ảnh phía dưới
					((DestroyableTile)entities[i]).addBelowSprite(entities[i-1].getSprite());
			}
		}
	}

	@Override
	public void update() {
		clearRemoved();
		getTopEntity().update();
	}
	
	@Override
	public void render(Screen screen) {
		getTopEntity().render(screen);
	}
	
	public Entity getTopEntity() {
		
		return _entities.getLast();
	}

	/**
	 * thực hiện remove đối tượng trên cùng
	 * nếu đã bị loại bỏ
	 */
	private void clearRemoved() {
		Entity top  = getTopEntity();
		
		if(top.isRemoved())  {
			_entities.removeLast();
		}
	}

	/**
	 * thêm đối tượng vào sau đối tượng trên cùng
	 * @param e
	 */
	public void addBeforeTop(Entity e) {
		_entities.add(_entities.size() - 1, e);
	}
	
	@Override
	public boolean collide(Entity e) {
		// TODO: lấy entity trên cùng ra để xử lý va chạm
                
		return getTopEntity().collide(e);
	}

}
