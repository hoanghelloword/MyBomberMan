/*
Đây là class Game, là nơi chứa vòng lặp chính của game, xử lý phần lớn logic game, vẽ hình, tương tác với bàn phím,...
*/

package uet.oop.bomberman;

import uet.oop.bomberman.graphics.Screen; // import class Screen để vẽ hình
import uet.oop.bomberman.gui.Frame; // import class Frame để tạo khung chứa game
import uet.oop.bomberman.input.Keyboard; // import class Keyboard để xử lý sự kiện từ bàn phím

import java.awt.*; // import các class trong package awt để vẽ hình, tạo frame,...
import java.awt.image.BufferStrategy; // import class BufferStrategy để tối ưu việc vẽ hình (1)
import java.awt.image.BufferedImage; // import class BufferedImage để lưu trữ hình ảnh	(2)
import java.awt.image.DataBufferInt; // import class DataBufferInt để thao tác trực tiếp với các pixel trong BufferedImage (3)

public class Game extends Canvas { 
	// Khởi tạo class Game, kế thừa class Canvas (để vẽ hình lên)
	public static final int TILES_SIZE = 16, // Kích thước của một ô trong game
			WIDTH = TILES_SIZE * (31/2), // Độ rộng của game (31 ô)
			HEIGHT = 13 * TILES_SIZE; // Độ cao của game (13 ô)

	public static int SCALE = 3; // Tỉ lệ thu phóng game khi vẽ

	public static final String TITLE = "BombermanGame"; // Tiêu đề của game

	private static final int BOMBRATE = 1; // Tốc độ ném bom ban đầu của nhân vật
	private static final int BOMBRADIUS = 1; // Bán kính nổ của bom ban đầu
	private static final double BOMBERSPEED = 1.0; // Tốc độ di chuyển ban đầu của nhân vật

	public static final int TIME = 200; // Thời gian chơi game (giây)
	public static final int POINTS = 0; // Điểm số ban đầu

	protected static int SCREENDELAY = 3; // Thời gian delay để vẽ màn hình loading trước khi bắt đầu một màn mới

	protected static int bombRate = BOMBRATE; // Biến lưu tốc độ ném bom hiện tại
	protected static int bombRadius = BOMBRADIUS; // Biến lưu bán kính nổ hiện tại
	protected static double bomberSpeed = BOMBERSPEED; // Biến lưu tốc độ di chuyển hiện tại của nhân vật

	protected int _screenDelay = SCREENDELAY; // Biến lưu thời gian delay màn hình loading hiện tại

	private Keyboard _input; // Đối tượng xử lý sự kiện từ bàn phím
	private boolean _running = false; // Biến lưu trạng thái game đang chạy hay không
	private boolean _paused = true; // Trạng thái game đang tạm dừng hay không

	private Board _board; // Đối tượng bảng chơi
	private Screen screen; // Đối tượng màn hình
	private Frame _frame; // Đối tượng cửa sổ game

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB); // Tạo ảnh đệm để render
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData(); // Lấy ra các pixel của ảnh đệm

	public Game(Frame frame) {
	// Gán giá trị cho thuộc tính _frame
	_frame = frame;
	// Thiết lập tiêu đề cho frame
	_frame.setTitle(TITLE);
	// Khởi tạo đối tượng Screen với kích thước rộng và cao
	screen = new Screen(WIDTH, HEIGHT);
	// Khởi tạo đối tượng Keyboard để xử lý các sự kiện phím
	_input = new Keyboard();
	
	// Khởi tạo đối tượng Board với tham số là Game(this), Keyboard và Screen
	_board = new Board(this, _input, screen);
	// Đăng ký bộ xử lí  phím với đối tượng Board
	addKeyListener(_input);
	}
	// Phương thức vẽ game lên màn hình
	private void renderGame() {
	// Lấy đối tượng BufferStrategy để xử lý double buffering
	BufferStrategy bs = getBufferStrategy();
	if(bs == null) {
	createBufferStrategy(3);
	return;
	}
	// Xóa màn hình
	screen.clear();
		
	// Vẽ Board lên màn hình
	_board.render(screen);
		
	// Sao chép dữ liệu từ Screen vào mảng pixels để vẽ lên màn hình
	for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen._pixels[i];
	}
		
	// Lấy đối tượng Graphics từ BufferStrategy để vẽ lên màn hình
	Graphics g = bs.getDrawGraphics();
		
	// Vẽ ảnh lên màn hình
	g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	// Vẽ các thông báo lên màn hình
	_board.renderMessages(g);
		
	// Giải phóng đối tượng Graphics
	g.dispose();
	// Hiển thị đối tượng BufferStrategy
	bs.show();
	}

	// Phương thức vẽ màn hình
	private void renderScreen() {
	// Lấy đối tượng BufferStrategy để xử lý double buffering
	BufferStrategy bs = getBufferStrategy();
	if(bs == null) {
	createBufferStrategy(3);
	return;
	}
	// Xóa màn hình
	screen.clear();
		
	// Lấy đối tượng Graphics từ BufferStrategy để vẽ lên màn hình
	Graphics g = bs.getDrawGraphics();
		
	// Vẽ màn hình
	_board.drawScreen(g);
	// Giải phóng đối tượng Graphics
	g.dispose();
	// Hiển thị đối tượng BufferStrategy
	bs.show();
	}

	// Phương thức cập nhật trạng thái của game
	private void update() {
	// Cập nhật trạng thái của bàn phím
	_input.update();
	// Cập nhật trạng thái của Board
	_board.update();
	}
	// Bắt đầu vòng lặp game
	public void start() {
	_running = true;
	// Khởi tạo thời gian và tốc độ khung hình
	long lastTime = System.nanoTime();
	long timer = System.currentTimeMillis();
	final double ns = 1000000000.0 / 60.0; // nanosecond, 60 frames per second
	double delta = 0;
	int frames = 0;
	int updates = 0;

	// Yêu cầu trỏ chuột vào cửa sổ game
	requestFocus();

	// Vòng lặp chính của game
	while(_running) {
		long now = System.nanoTime();
		delta += (now - lastTime) / ns;
		lastTime = now;
		
		// Cập nhật trạng thái game và đếm số lần cập nhật
		while(delta >= 1) {
			update();
			updates++;
			delta--;
		}
		
		// Nếu game đang tạm dừng
		if(_paused) {
			// Nếu thời gian tạm dừng đã hết, tiếp tục hiển thị game
			if(_screenDelay <= 0) {
				_board.setShow(-1);
				_paused = false;
			}
			
			// Hiển thị màn hình tạm dừng
			renderScreen();
		} else {
			// Hiển thị game
			renderGame();
		}
		
		// Đếm số lần hiển thị khung hình và cập nhật tiêu đề cửa sổ
		frames++;
		if(System.currentTimeMillis() - timer > 1000) {
			_frame.setTime(_board.subtractTime());
			_frame.setPoints(_board.getPoints());
			timer += 1000;
			_frame.setTitle(TITLE + " | " + updates + " rate, " + frames + " fps");
			updates = 0;
			frames = 0;
			
			// Giảm độ trễ hiển thị màn hình tạm dừng nếu cần
			if(_board.getShow() == 2)
				--_screenDelay;
		}
	}
	}

	// Lấy tốc độ của nhân vật di chuyển
	public static double getBomberSpeed() {
	return bomberSpeed;
	}

	// Lấy số lượng bom có thể đặt cùng một lúc
	public static int getBombRate() {
	return bombRate;
	}

	// Lấy bán kính của vùng bị ảnh hưởng bởi bom
	public static int getBombRadius() {
	return bombRadius;
	}

	// Tăng tốc độ di chuyển của nhân vật
	public static void addBomberSpeed(double i) {
	bomberSpeed += i;
	}

	// Tăng bán kính của vùng bị ảnh hưởng bởi bom
	public static void addBombRadius(int i) {
	bombRadius += i;
	}

	// Tăng số lượng bom có thể đặt cùng một lúc
	public static void addBombRate(int i) {
		bombRate += i;
	}
	public void resetScreenDelay() { // Thiết lập thời gian delay trở lại giá trị ban đầu
		_screenDelay = SCREENDELAY;
	}

	public Board getBoard() { // Trả về đối tượng bảng hiện tại của trò chơi
		return _board;
	}

	public boolean isPaused() { // Trả về trạng thái đang tạm dừng trò chơi hay không
		return _paused;
	}
	
	public void pause() { // Tạm dừng trò chơi
		_paused = true;
	}
	
	public static void setBombRate(int bombRate) { // Thiết lập tốc độ thả bomb của nhân vật
        Game.bombRate = bombRate;
    }

    public static void setBombRadius(int bombRadius) { // Thiết lập bán kính tác động của bomb
        Game.bombRadius = bombRadius;
    }

    public static void setBomberSpeed(double bomberSpeed) { // Thiết lập tốc độ di chuyển của nhân vật
        Game.bomberSpeed = bomberSpeed;
    }
}
