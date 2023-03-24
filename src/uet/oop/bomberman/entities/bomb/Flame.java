public class Flame {
    protected Board _board; // Đối tượng Board được sử dụng để tạo Flame.
    protected int _direction; // Hướng của Flame: 0 = "trên", 1 = "phải", 2 = "dưới", 3 = "trái".
    private int _radius; // Bán kính của Flame.
    protected int xOrigin, yOrigin; // Tọa độ ban đầu của Flame.
    protected FlameSegment[] _flameSegments = new FlameSegment[0]; // Mảng FlameSegment được khởi tạo với độ dài bằng 0.

    public Flame(int x, int y, int direction, int radius, Board board) {
        xOrigin = x; // Gán giá trị tọa độ ban đầu của Flame cho biến xOrigin.
        yOrigin = y; // Gán giá trị tọa độ ban đầu của Flame cho biến yOrigin.
        _x = x; // Gán giá trị tọa độ ban đầu của Flame cho biến _x.
        _y = y; // Gán giá trị tọa độ ban đầu của Flame cho biến _y.
        _direction = direction; // Gán giá trị hướng của Flame cho biến _direction.
        _radius = radius; // Gán giá trị bán kính của Flame cho biến _radius.
        _board = board; // Gán đối tượng Board được sử dụng để tạo Flame cho biến _board.
        createFlameSegments(); // Gọi hàm createFlameSegments() để tạo mảng các FlameSegment.
    }

    private void createFlameSegments() {
        _flameSegments = new FlameSegment[calculatePermitedDistance()]; // Tạo mảng FlameSegment với độ dài bằng với khoảng cách cho phép của Flame.

        int x = (int) _x; // Lấy phần nguyên của tọa độ x của Flame.
        int y = (int) _y; // Lấy phần nguyên của tọa độ y của Flame.
        for (int i = 0; i < _flameSegments.length; i++) {
            boolean last = i == _flameSegments.length - 1; // Kiểm tra xem FlameSegment hiện tại có phải là FlameSegment cuối cùng không.

            switch (_direction) {
                case 0: // Nếu hướng của Flame là "trên".
                y--; // Giảm giá trị tọa độ y của Flame.
                break;
                case 1: // Nếu hướng của Flame là "phải".
                x++; // Tăng giá trị tọa độ x của Flame.
                break;
                case 2: // Nếu hướng của Flame là "dưới".
                y++; // Tăng giá trị tọa độ y của Flame.
                break;
                case 3: // Nếu hướng của Flame là "trái".
                x--; // Giảm giá trị tọa độ x của Flame.
                break;
            }
        _flameSegments[i] = new FlameSegment(x, y, _direction, last); // Tạo một FlameSegment mới tại vị trí (x, y) và thêm nó vào mảng FlameSegments.
        }
    }

    private int calculatePermitedDistance() {
        int radius = 0; // bán kính của đoạn lửa
        int x = (int)_x; // tọa độ x hiện tại của đoạn lửa
        int y = (int)_y; // tọa độ y hiện tại của đoạn lửa
        while(radius < _radius) { // vòng lặp để tìm bán kính cuối cùng của đoạn lửa
            if(_direction == 0) y--; // nếu đang đi lên thì giảm y đi 1
            if(_direction == 1) x++; // nếu đang đi sang phải thì tăng x đi 1
            if(_direction == 2) y++; // nếu đang đi xuống thì tăng y đi 1
            if(_direction == 3) x--; // nếu đang đi sang trái thì giảm x đi 1
            Entity a = _board.getEntity(x, y, null); // lấy thực thể ở vị trí hiện tại của đoạn lửa
            if(a instanceof Bomb) ++radius; // nếu thực thể là Bomb thì tăng bán kính của đoạn lửa lên 1
            if(a.collide(this) == false) // nếu đoạn lửa va chạm với thực thể khác, nhưng không phải là Bomb, thì thoát khỏi vòng lặp
                break;
            ++radius; // tăng bán kính của đoạn lửa lên 1
        }
        return radius; // trả về bán kính cuối cùng của đoạn lửa
    }
    
    public FlameSegment flameSegmentAt(int x, int y) {
        for (int i = 0; i < _flameSegments.length; i++) { // duyệt qua tất cả các đoạn lửa
            if(_flameSegments[i].getX() == x && _flameSegments[i].getY() == y) // nếu tìm thấy đoạn lửa có tọa độ x và y như yêu cầu
                return _flameSegments[i]
            }
            // trả về null nếu không tìm thấy FlameSegment nào
            return null;
        }
        
        @Override
    public void update() {
        // không có hành động cập nhật nào cho đối tượng Flame
    }
        
    @Override
    public void render(Screen screen) {
        // hiển thị tất cả FlameSegment của đối tượng Flame
        for (int i = 0; i < _flameSegments.length; i++) {
            _flameSegments[i].render(screen);
        }
    }
        
    @Override
    public boolean collide(Entity e) {
        // kiểm tra va chạm với đối tượng khác
        if(e instanceof Bomber) ((Bomber) e).kill(); // nếu là Bomber thì giết Bomber
        if(e instanceof Enemy) ((Enemy) e).kill(); // nếu là Enemy thì giết Enemy
        return true;
    }
}