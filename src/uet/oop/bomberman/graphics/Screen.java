import java.awt.*;
/**
 * Đây là thư viện AWT (Abstract Window Toolkit) trong Java, cung cấp các lớp và phương thức cho việc tạo giao diện đồ họa trên nền tảng Java.
 */

public class Screen {
    protected int _width, _height; // Khai báo các biến độ rộng và chiều cao bảo vệ
    public int[] _pixels; // Khai báo một mảng nguyên dùng để lưu trữ các pixel của màn hình
    private int _transparentColor = 0xffff00ff; // Khai báo một màu trong suốt để ẩn đi các pixel có màu này

    public static int xOffset = 0, yOffset = 0; // Khai báo hai biến tĩnh để lưu trữ độ lệch x và y

    public Screen(int width, int height) { // Constructor cho lớp màn hình với đầu vào là chiều rộng và chiều cao
	    _width = width;
	    _height = height;
	
	    _pixels = new int[width * height]; // Khởi tạo mảng các pixel với kích thước là chiều rộng nhân chiều cao
	
    }

    public void clear() { // Xóa màn hình bằng cách thiết lập tất cả các pixel thành màu đen
	    for (int i = 0; i < _pixels.length; i++) {
		    _pixels[i] = 0;
	    }
    }

    public void renderEntity(int xp, int yp, Entity entity) { // Lưu các pixel của thực thể
	    xp -= xOffset; // Cập nhật độ lệch x
	    yp -= yOffset; // Cập nhật độ lệch y
	    for (int y = 0; y < entity.getSprite().getSize(); y++) { // Vòng lặp qua chiều cao của sprite của thực thể
		    int ya = y + yp; // Tính toán tọa độ y trên màn hình
		    for (int x = 0; x < entity.getSprite().getSize(); x++) { // Vòng lặp qua chiều rộng của sprite của thực thể
			    int xa = x + xp; // Tính toán tọa độ x trên màn hình
			    if(xa < -entity.getSprite().getSize() || xa >= _width || ya < 0 || ya >= _height) break; // Kiểm tra nếu tọa độ nằm ngoài màn hình, thì kết thúc vòng lặp
			    if(xa < 0) xa = 0; // Kiểm tra nếu tọa độ x âm, thì bắt đầu từ 0 từ bên trái
			    int color = entity.getSprite().getPixel(x + y * entity.getSprite().getSize()); // Lấy màu của pixel tương ứng trên sprite của thực thể
			    if(color != _transparentColor) _pixels[xa + ya * _width] = color; // Nếu pixel không là màu trong suốt thì lưu màu này vào mảng pixel tại vị trí tương ứng
		    }
	    }
    }

    public void renderEntityWithBelowSprite(int xp, int yp, Entity entity, Sprite below) {
        // Di chuyển tọa độ bắt đầu của đối tượng xuống theo xOffset và yOffset.
        xp -= xOffset;
        yp -= yOffset;
        
        // Với mỗi pixel trong Sprite của Entity.
        for (int y = 0; y < entity.getSprite().getSize(); y++) {
            int ya = y + yp;
            for (int x = 0; x < entity.getSprite().getSize(); x++) {
                int xa = x + xp;
                
                // Nếu nó vượt ra ngoài khung hình thì dừng vòng lặp và không vẽ pixel đó.
                if(xa < -entity.getSprite().getSize() || xa >= _width || ya < 0 || ya >= _height) break;
                
                // Nếu tọa độ X nhỏ hơn 0 thì đặt nó là 0.
                if(xa < 0) xa = 0;
                
                // Lấy giá trị màu của pixel tại vị trí x, y trong Sprite của Entity.
                int color = entity.getSprite().getPixel(x + y * entity.getSprite().getSize());
                
                // Nếu giá trị màu khác với giá trị màu của pixel trong Sprite _transparentColor thì vẽ nó lên màn hình.
                if(color != _transparentColor) 
                    _pixels[xa + ya * _width] = color;
                // Ngược lại thì lấy giá trị màu của pixel tương ứng trong Sprite below để vẽ lên màn hình.
                else
                    _pixels[xa + ya * _width] = below.getPixel(x + y * below.getSize());
            }
        }
    }
    
    public static void setOffset(int xO, int yO) {
        // Gán giá trị của xOffset và yOffset.
        xOffset = xO;
        yOffset = yO;
    }
    
    public static int calculateXOffset(Board board, Bomber bomber) {
        // Nếu bomber không tồn tại, trả về giá trị 0.
        if(bomber == null) return 0;
        
        int temp = xOffset;
        
        // Tính toán tọa độ X của bomber chia cho 16.
        double BomberX = bomber.getX() / 16;
        
        // Giá trị độ lệch.
        double complement = 0.5;
        
        // Giá trị điểm chặn đầu tiên.
        int firstBreakpoint = board.getWidth() / 4;
        
        // Giá trị điểm chặn cuối cùng.
        int lastBreakpoint = board.getWidth() - firstBreakpoint;
        
        // Nếu tọa độ X của bomber nằm giữa firstBreakpoint và lastBreakpoint.
        if( BomberX > firstBreakpoint + complement && BomberX < lastBreakpoint - complement) {
            // Tính toán giá trị xOffset để đưa bomber về giữa màn hình.
            temp = (int)bomber.getX()  - (Game.WIDTH / 2);
        }

        return temp;
    }

    public void drawEndGame(Graphics g, int points) {
        // Thiết lập màu đen và vẽ một hình chữ nhật đầy đủ trên màn hình
        g.setColor(Color.black);
        g.fillRect(0, 0, getRealWidth(), getRealHeight());
    
        // Thiết lập font và màu trắng, và vẽ chuỗi "GAME OVER" vào giữa màn hình
        Font font = new Font("Arial", Font.PLAIN, 20 * Game.SCALE);
        g.setFont(font);
        g.setColor(Color.white);
        drawCenteredString("GAME OVER", getRealWidth(), getRealHeight(), g);
    
        // Thiết lập font và màu vàng, và vẽ chuỗi "POINTS: " cùng với số điểm vào giữa màn hình, với khoảng cách là 2 lần biến Game.TILES_SIZE và màu vàng
        font = new Font("Arial", Font.PLAIN, 10 * Game.SCALE);
        g.setFont(font);
        g.setColor(Color.yellow);
        drawCenteredString("POINTS: " + points, getRealWidth(), getRealHeight() + (Game.TILES_SIZE * 2) * Game.SCALE, g);
    }

    public void drawChangeLevel(Graphics g, int level) {
        // Thiết lập màu đen và vẽ một hình chữ nhật đầy đủ trên màn hình
        g.setColor(Color.black);
        g.fillRect(0, 0, getRealWidth(), getRealHeight());
        
        // Thiết lập font và màu trắng, và vẽ chuỗi "LEVEL " cùng với số cấp độ vào giữa màn hình
        Font font = new Font("Arial", Font.PLAIN, 20 * Game.SCALE);
        g.setFont(font);
        g.setColor(Color.white);
        drawCenteredString("LEVEL " + level, getRealWidth(), getRealHeight(), g);
    }
    
    public void drawPaused(Graphics g) {
        // Thiết lập font và màu trắng, và vẽ chuỗi "PAUSED" vào giữa màn hình
        Font font = new Font("Arial", Font.PLAIN, 20 * Game.SCALE);
        g.setFont(font);
        g.setColor(Color.white);
        drawCenteredString("PAUSED", getRealWidth(), getRealHeight(), g);
    }
    
    public void drawCenteredString(String s, int w, int h, Graphics g) {
        // Tính toán vị trí để vẽ chuỗi vào giữa màn hình và vẽ chuỗi
        FontMetrics fm = g.getFontMetrics();
        int x = (w - fm.stringWidth(s)) / 2;
        int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
        g.drawString(s, x, y);
    }
    
    public int getWidth() {
        return _width; // Trả về chiều rộng của màn hình chơi game
    }
    
    public int getHeight() {
        return _height; // Trả về chiều cao của màn hình chơi game
    }
    
    public int getRealWidth() {
        return _width * Game.SCALE; // Trả về chiều rộng của màn hình chơi game tính bằng đơn vị pixel thực tế bằng cách nhân chiều rộng _width với hằng số Game.SCALE
    }
    
    public int getRealHeight() {
        return _height * Game.SCALE; // Trả về chiều cao của màn hình chơi game tính bằng đơn vị pixel thực tế bằng cách nhân chiều cao _height với hằng số Game.SCALE
    }
}    