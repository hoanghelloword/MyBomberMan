public class FlameSegment extends Entity {

    protected boolean _last;

    /**
     * Khởi tạo một FlameSegment
     * @param x tọa độ x của segment
     * @param y tọa độ y của segment
     * @param direction hướng của segment (0: up, 1: right, 2: down, 3: left)
     * @param last cho biết segment này là cuối cùng của Flame hay không, segment cuối có sprite khác so với các segment còn lại
     */
    public FlameSegment(int x, int y, int direction, boolean last) {
        _x = x;
        _y = y;
        _last = last;

        // Lựa chọn sprite cho segment dựa trên hướng và last
        switch (direction) {
            case 0:
                if (!last) {
                    _sprite = Sprite.explosion_vertical2; // sprite của segment không phải cuối
                } else {
                    _sprite = Sprite.explosion_vertical_top_last2; // sprite của segment cuối cùng
                }
                break;
            case 1:
                if (!last) {
                    _sprite = Sprite.explosion_horizontal2; // sprite của segment không phải cuối
                } else {
                    _sprite = Sprite.explosion_horizontal_right_last2; // sprite của segment cuối cùng
                }
                break;
            case 2:
                if (!last) {
                    _sprite = Sprite.explosion_vertical2; // sprite của segment không phải cuối
                } else {
                    _sprite = Sprite.explosion_vertical_down_last2; // sprite của segment cuối cùng
                }
                break;
            case 3:
                if (!last) {
                    _sprite = Sprite.explosion_horizontal2; // sprite của segment không phải cuối
                } else {
                    _sprite = Sprite.explosion_horizontal_left_last2; // sprite của segment cuối cùng
                }
                break;
        }
    }

    /**
     * Vẽ FlameSegment lên màn hình
     * @param screen màn hình
     */
    @Override
    public void render(Screen screen) {
        int xt = (int) _x << 4; // Tính toán tọa độ x trên màn hình dựa trên tọa độ x của FlameSegment
        int yt = (int) _y << 4; // Tính toán tọa độ y trên màn hình dựa trên tọa độ y của FlameSegment

        screen.renderEntity(xt, yt, this); // Vẽ FlameSegment lên màn hình
    }

    @Override
    public void update() {
        // Không có gì để cập nhật cho FlameSegment
    }

    /**
     * Xử lý va chạm giữa FlameSegment và Entity khác
     * @param e Entity khác
     * @return true nếu FlameSegment va chạm với Entity khác
     */
    @Override
    public boolean collide(Entity e) {
        // TODO: xử lý khi FlameSegment va chạm với Character
        if(e instanceof Bomber) ((Bomber) e).kill();
        if(e instanceof Enemy) ((Enemy) e).kill();
        return true;
    }
}