package uet.oop.bomberman.entities.character;

import java.util.ArrayList;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.input.Keyboard;

import java.util.Iterator;
import java.util.List;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.tile.item.Item;
import uet.oop.bomberman.level.Coordinates;

/**
 * bomber -> character, bomb,.... -> AnimatedEntitiy -> Entity implement irender
 * bomb,.... -> AnimatedEntitiy -> Entity implement irender
 */
public class Bomber extends Character {

    /** list bomb: thành phần trong class board*/
    private List<Bomb> _bombs;
    /** du lieu tu ban phim:thành phần trong class board */
    protected Keyboard _input;
    /**list item*/
    public static List<Item> _items = new ArrayList<>();//xu li Item
    /**
     * nếu giá trị này < 0 thì cho phép đặt đối tượng Bomb tiếp theo,
     * cứ mỗi lần đặt 1 Bomb mới, giá trị này sẽ được reset về 0 và giảm dần trong mỗi lần update()
     */
    protected int _timeBetweenPutBombs = 0;

    public Bomber(int x, int y, Board board) {
        super(x, y, board);
        _bombs = _board.getBombs();
        _input = _board.getInput();
        /**sprite là thành phần trong lớp entity
         * khởi động nhân vật với hướng di chuyển sang phải*/
        _sprite = Sprite.player_right;
    }

    @Override
    public void update() {
        clearBombs();
        if (!_alive) {
            afterKill();
            return;
        }


//        if (_timeBetweenPutBombs < -7500) _timeBetweenPutBombs = 0;
        if (_timeBetweenPutBombs < 0) _timeBetweenPutBombs = 0;
        else _timeBetweenPutBombs--;

         //xem đã đủ animate chưa,
        // chưa đủ thì tăng biến animate trong animatedEntity lên 1
        // đã đủ thì set về 0
        animate();

        calculateMove();

        detectPlaceBomb();
    }

    @Override
    public void render(Screen screen) {
        //căn chỉnh lại vị tr trung tâm màn hình
        calculateXOffset();

        if (_alive)
            chooseSprite();
        else
            _sprite = Sprite.player_dead1;
        // thay đổi vị trí đối tượng trong screen
        screen.renderEntity((int) _x, (int) _y - _sprite.SIZE, this);
    }

    /**
     * căn chỉnh lại vị tr trung tâm màn hình
     */
    public void calculateXOffset() {
        int xScroll = Screen.calculateXOffset(_board, this);
        Screen.setOffset(xScroll, 0);
    }

    /**
     * Kiểm tra xem có đặt được bom hay không? nếu có thì đặt bom tại vị trí hiện tại của Bomber
     */
    private void detectPlaceBomb() {
        // TODO: kiểm tra xem phím điều khiển đặt bom có được gõ và giá trị _timeBetweenPutBombs, Game.getBombRate() có thỏa mãn hay không
        // TODO:  Game.getBombRate() sẽ trả về số lượng bom có thể đặt liên tiếp tại thời điểm hiện tại
        // TODO: _timeBetweenPutBombs dùng để ngăn chặn Bomber đặt 2 Bomb cùng tại 1 vị trí trong 1 khoảng thời gian quá ngắn
        // TODO: nếu 3 điều kiện trên thỏa mãn thì thực hiện đặt bom bằng placeBomb()
        // TODO: sau khi đặt, nhớ giảm số lượng Bomb Rate và reset _timeBetweenPutBombs về 0
        if(_input.space && Game.getBombRate() > 0 && _timeBetweenPutBombs < 0) {
			
			int xt = Coordinates.pixelToTile(_x + _sprite.getSize() / 2);
			int yt = Coordinates.pixelToTile( (_y + _sprite.getSize() / 2) - _sprite.getSize() ); //subtract half player height and minus 1 y position
			
			placeBomb(xt,yt);
			Game.addBombRate(-1);
			
			_timeBetweenPutBombs = 30;
		}
    }

    protected void placeBomb(int x, int y) {
        // TODO: thực hiện tạo đối tượng bom, đặt vào vị trí (x, y)
        Bomb b = new Bomb(x, y, _board);
	_board.addBomb(b);
        //Sound.play("BOM_SET");
    }

    /**
     * xóa các bomb khả thi tại mảng _bombs, tang số bom tương ứng có thể đặt
     */
    private void clearBombs() {
        /** con trỏ vào đầu mảng _bombs*/
        Iterator<Bomb> bs = _bombs.iterator();
        Bomb b;
        while (bs.hasNext()) {
            // dịch sang phần tử tiếp theo và gán bằng b
            b = bs.next();
            //check xem có xóa được bomb tại vị trí hiện tại ko
            if (b.isRemoved()) {
                // xóa bomb khỏi mảng _bombs
                bs.remove();
                // so lượng bombcosos thể đặt tăng lên 1
                Game.addBombRate(1);
            }
        }

    }

    @Override
    public void kill() {
        if (!_alive) return;
        _alive = false;
        //Sound.play("endgame3");
    }

    /**
     * sau mỗi lần update timeafter giảm đi 1
     * khi timeafter <= 0 thì endgame
     */
    @Override
    protected void afterKill() {
        if (_timeAfter > 0) --_timeAfter;
        else {
            _board.endGame();
        }
    }

    /**
     * tính toán hướng đi vaf gọi hàm move để thực hiện di chuyển
     */
    @Override
    protected void calculateMove() {
        // TODO: xử lý nhận tín hiệu điều khiển hướng đi từ _input và gọi move() để thực hiện di chuyển
        // TODO: nhớ cập nhật lại giá trị cờ _moving khi thay đổi trạng thái di chuyển
        int xa = 0, ya = 0;
		if(_input.up) ya--;
		if(_input.down) ya++;
		if(_input.left) xa--;
		if(_input.right) xa++;
		
		if(xa != 0 || ya != 0)  {
			move(xa * Game.getBomberSpeed(), ya * Game.getBomberSpeed());
			_moving = true;
		} else {
			_moving = false;
		}
    }

    @Override
    public boolean canMove(double x, double y) {
        // TODO: kiểm tra có đối tượng tại vị trí chuẩn bị di chuyển đến và có thể di chuyển tới đó hay không
       for (int c = 0; c < 4; c++) { //kiem tra 4 goc cua player
			double xt = ((_x + x) + c % 2 * 9) / Game.TILES_SIZE; //chia tile_size để chuyển sang tọa độ tile
           //double xt = ((_x + x) + 10) / Game.TILES_SIZE;
           double yt = ((_y + y) + c / 2 * 10 - 13) / Game.TILES_SIZE; //gia trị chuẩn nhất, được xác định qua nhiều test
			
			Entity a = _board.getEntity(xt, yt, this);
			
			if(!a.collide(this))
				return false;
		}
		
		return true;
        //return false;
    }

    /**
     * thực hiện di chuển sau khi check xem có thể di chuyển tới điểm cần đến ko
     */
    @Override
    public void move(double xa, double ya) {
        // TODO: sử dụng canMove() để kiểm tra xem có thể di chuyển tới điểm đã tính toán hay không và thực hiện thay đổi tọa độ _x, _y
        // TODO: nhớ cập nhật giá trị _direction sau khi di chuyển
        if(xa > 0) _direction = 1;
		if(xa < 0) _direction = 3;
		if(ya > 0) _direction = 2;
		if(ya < 0) _direction = 0;


		if(canMove(0, ya)) { //separate the moves for the player can slide when is colliding
			_y += ya;
		}
		
		if(canMove(xa, 0)) {
			_x += xa;
		}
    }

    @Override
    public boolean collide(Entity e) {
        // TODO: xử lý va chạm với Flame
        // TODO: xử lý va chạm với Enemy
        if(e instanceof Flame){
            this.kill();
            return false;
        }
        if(e instanceof Enemy){
            this.kill();
            return true;
        }
        // xử lý collide của đối tượng ở trên cùng
        // được override trong layeredEntity
        if( e instanceof LayeredEntity) return(e.collide(this));
        return true;
    }

    /**
     * chọn sprite thích hợp cho từng hướng di chuyển
     */
    private void chooseSprite() {
        switch (_direction) {
            case 0:
                _sprite = Sprite.player_up;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, _animate, 20);
                }
                break;
            case 1:
                _sprite = Sprite.player_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
            case 2:
                _sprite = Sprite.player_down;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, _animate, 20);
                }
                break;
            case 3:
                _sprite = Sprite.player_left;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, _animate, 20);
                }
                break;
            default:
                _sprite = Sprite.player_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
        }
    }
}
