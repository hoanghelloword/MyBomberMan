package uet.oop.bomberman.entities.tile.destroyable;


import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;

public class Brick extends DestroyableTile {
	
	// Constructor
	public Brick(int x, int y, Sprite sprite) {
		super(x, y, sprite); // Gọi constructor của lớp cha
	}
	
	// Phương thức cập nhật trạng thái cho Brick
	@Override
	public void update() {
		super.update(); // Gọi phương thức update() của lớp cha
	}
	
	// Phương thức vẽ Brick lên màn hình
	@Override
	public void render(Screen screen) {
		int x = Coordinates.tileToPixel(_x);
		int y = Coordinates.tileToPixel(_y);
		
		// Nếu Brick đã bị phá hủy
		if(_destroyed) {
			// Thay đổi sprite của Brick thành sprite của các khối gạch vỡ
			_sprite = movingSprite(Sprite.brick_exploded, Sprite.brick_exploded1, Sprite.brick_exploded2);
			
			// Vẽ sprite dưới của Brick trước khi vẽ sprite mới
			screen.renderEntityWithBelowSprite(x, y, this, _belowSprite);
		}
		// Nếu Brick chưa bị phá hủy
		else {
			// Vẽ sprite của Brick lên màn hình
			screen.renderEntity( x, y, this);
		}
	}
}
