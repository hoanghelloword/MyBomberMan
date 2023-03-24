package uet.oop.bomberman.gui;

import uet.oop.bomberman.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Swing Panel chứa cảnh game
 */
public class GamePanel extends JPanel {

	private Game _game;
	
	public GamePanel(Frame frame) {
		//(1)
		setLayout(new BorderLayout()); // Thiết lập bố cục cho panel là BorderLayout, một loại bố cục cho phép sắp xếp các thành phần theo hướng bắc, nam, đông, tây và trung tâm
		setPreferredSize(new Dimension(Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE)); // Thiết lập kích thước ưu tiên cho panel bằng cách nhân chiều rộng và chiều cao của game với tỷ lệ

		_game = new Game(frame); // Khởi tạo một đối tượng game với tham số là frame

		add(_game); // Thêm đối tượng game vào panel

		_game.setVisible(true); // Đặt thuộc tính hiển thị của game là true
		//đảm bảo GamePanel có thể nhận input từ người dùng.
		setVisible(true); // Đặt thuộc tính hiển thị của panel là true
		setFocusable(true); // Đặt thuộc tính có thể nhận focus của panel là true
		
	}

	public Game getGame() {
		return _game; // Trả về đối tượng game
	}
	
}