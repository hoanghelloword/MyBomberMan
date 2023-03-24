// Lớp Bomb kế thừa từ lớp AnimatedEntity
public class Bomb extends AnimatedEntitiy {

    // Thời gian đếm ngược trước khi quả bom nổ
    protected double _timeToExplode = 120; //2 seconds
    // Thời gian đếm ngược sau khi quả bom đã nổ để loại bỏ khỏi bảng
    public int _timeAfter = 20;// 0.3 seconds
	
    protected Board _board; // Bảng hiện tại mà quả bom đang nằm trên
    protected Flame[] _flames; // Mảng các Flame (lửa) được tạo ra sau khi quả bom nổ
    protected boolean _exploded = false; // Biến boolean để xác định xem quả bom đã nổ hay chưa
    protected boolean _allowedToPassThru = true; // Biến boolean để xác định xem quả bom có được phép đi qua các đối tượng khác hay không
	
    // Phương thức khởi tạo của lớp Bomb
    public Bomb(int x, int y, Board board) {
        / Nhận vào tọa độ x, y và bảng hiện tại
        _x = x;
        _y = y;
        _board = board;
        _sprite = Sprite.bomb; // Sprite của quả bom được đặt là Sprite.bomb
    }

    // Phương thức cập nhật trạng thái của quả bom
    @Override
    public void update() {
        if(_timeToExplode > 0) // Nếu quả bom chưa đến thời gian nổ
            _timeToExplode--; // Giảm thời gian đếm ngược
        else { // Nếu quả bom đã đến thời gian nổ
            if(!_exploded) // Nếu quả bom chưa nổ
                explode(); // Thực hiện phương thức nổ
            else
                updateFlames(); // Thực hiện cập nhật trạng thái của lửa
        }

        if(_timeAfter > 0) // Nếu đã nổ và quả bom vẫn còn trên bảng
            _timeAfter--; // Giảm thời gian đếm ngược
        else
            remove(); // Loại bỏ quả bom khỏi bảng

        animate(); // Thực hiện phương thức animate để thực hiện chuyển động của quả bom
    }

    @Override
    public void render(Screen screen) {
        // Kiểm tra nếu bomb đã nổ
        if(_exploded) {
            // Gán sprite của bomb thành sprite của vụ nổ
            _sprite = Sprite.bomb_exploded2;
            // Gọi hàm renderFlames để hiển thị các đám lửa
            renderFlames(screen);
        } else {
            // Nếu bomb chưa nổ, tạo sprite với hiệu ứng chuyển động
            _sprite = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, _animate, 60);
        } 

        // Tính toán vị trí của bomb trên trục x và y
        int xt = (int)_x << 4;
        int yt = (int)_y << 4;

        // Hiển thị bomb trên màn hình game
        screen.renderEntity(xt, yt , this);
    }

    // Hàm hiển thị các đám lửa
    public void renderFlames(Screen screen) {
        for (int i = 0; i < _flames.length; i++) {
            _flames[i].render(screen);
        }
    } 

    // Hàm cập nhật trạng thái của các đám lửa
    public void updateFlames() {
        for (int i = 0; i < _flames.length; i++) {
            _flames[i].update();
        }
    }

    protected void explode() {//nổ
		_exploded = true; //đánh dấu Bomb đã nổ
		_allowedToPassThru = true; //đánh dấu cho phép đi qua Bomb

		// TODO: xử lý khi Character đứng tại vị trí Bomb
		Character x = _board.getCharacterAtExcluding((int)_x, (int)_y, null); //lấy nhân vật đứng tại vị trí Bomb
                if(x != null){
                    x.kill(); //giết nhân vật đó
                }

		// TODO: tạo các Flame
                _flames = new Flame[4]; //tạo 4 Flame
                for (int i = 0; i < _flames.length; i++) {
                    _flames[i] = new Flame((int) _x, (int) _y, i, Game.getBombRadius(), _board); //khởi tạo các Flame tại các hướng khác nhau
                }
                //Sound.play("BOM_11_M"); //phát âm thanh nổ Bomb
	}
        
	public void time_explode() { //hàm hỗ trợ khi Bomb nổ
		_timeToExplode = 0; //đặt thời gian Bomb nổ về 0
	}
	
	public FlameSegment flameAt(int x, int y) { //hàm kiểm tra Flame có đang xuất hiện tại một vị trí cụ thể hay không
		if(!_exploded) return null; //nếu Bomb chưa nổ thì không có Flame

		for (int i = 0; i < _flames.length; i++) { //duyệt qua từng Flame
			if(_flames[i] == null) return null; //nếu Flame tại vị trí i bằng null thì trả về null
			FlameSegment e = _flames[i].flameSegmentAt(x, y); //kiểm tra xem Flame tại vị trí i có đang xuất hiện tại vị trí x, y hay không
			if(e != null) return e; //nếu có thì trả về FlameSegment đó
		}
		
		return null; //nếu không có FlameSegment nào thì trả về null
	}

    @Override
    public boolean collide(Entity e) {
        // Xử lý khi Bomber đi ra sau khi vừa đặt bom (_allowedToPassThru)
        if(e instanceof Bomber) {
            double diffX = e.getX() - Coordinates.tileToPixel(getX());
            double diffY = e.getY() - Coordinates.tileToPixel(getY());

            if(!(diffX >= -10 && diffX < 16 && diffY >= 1 && diffY <= 28)) {
                // differences to see if the player has moved out of the bomb, tested values
                _allowedToPassThru = false;
        }

        return _allowedToPassThru;
    }

    // Xử lý va chạm với Flame của Bomb khác
    if(e instanceof Flame ) {
        time_explode();
        return true;
    }

    // Đối tượng truyền vào không phải là Bomber hoặc Flame
    return false;
}
}