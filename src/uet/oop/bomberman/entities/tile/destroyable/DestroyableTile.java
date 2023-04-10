package uet.oop.bomberman.entities.tile.destroyable;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.tile.Tile;
import uet.oop.bomberman.graphics.Sprite;

/**
 * Đối tượng cố định có thể bị phá hủy
 */
public class DestroyableTile extends Tile {
    // Thời gian tối đa để phá hủy hiệu ứng (được tính bằng số lần gọi phương thức update()).
    private final int MAX_ANIMATE = 7500;
    // Số lần đã gọi phương thức update() kể từ lúc bị phá hủy.
    private int _animate = 0;
    // Đánh dấu xem hiện tại tile đã bị phá hủy chưa.
    protected boolean _destroyed = false;
    // Thời gian hiệu ứng còn lại (được tính bằng số lần gọi phương thức update()).
    protected int _timeToDisapear = 20;
    // Sprite phía dưới của tile.
    protected Sprite _belowSprite = Sprite.grass;

    public DestroyableTile(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }

    @Override
    public void update() {
        if (_destroyed) {
            // Nếu hiệu ứng phá hủy chưa kết thúc thì tăng biến _animate lên 1 đơn vị, ngược lại gán lại _animate bằng 0.
            if (_animate < MAX_ANIMATE) {
                _animate++;
            } else {
                _animate = 0;
            }
            // Giảm thời gian hiệu ứng còn lại đi 1 đơn vị.
            if (_timeToDisapear > 0) {
                _timeToDisapear--;
            } else {
                // Nếu thời gian hiệu ứng còn lại đã bằng 0 thì gọi phương thức remove() để xóa tile khỏi map.
                remove();
            }
        }
    }

    // Phá hủy tile.
    public void destroy() {
        _destroyed = true;
    }

    @Override
    public boolean collide(Entity e) {
        // Xử lý va chạm với Flame.
        if (e instanceof Flame) {
            // Nếu xảy ra va chạm với Flame thì phá hủy tile.
            destroy();
        }
        return false;
    }

    // Đặt sprite phía dưới của tile.
    public void addBelowSprite(Sprite sprite) {
        _belowSprite = sprite;
    }

    // Trả về sprite của tile theo thời gian.
    protected Sprite movingSprite(Sprite normal, Sprite x1, Sprite x2) {
        int calc = _animate % 30;

        if (calc < 10) {
            return normal;
        }

        if (calc < 20) {
            return x1;
        }

        return x2;
    }
}