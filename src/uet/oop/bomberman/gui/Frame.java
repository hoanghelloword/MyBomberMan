package uet.oop.bomberman.gui;

import uet.oop.bomberman.Game;
import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
	public GamePanel _gamepane;
	private JPanel _containerpane;
	private InfoPanel _infopanel;
	
	private Game _game;

	public Frame() {
		// Tạo mới một JPanel và thiết lập layout là BorderLayout cho _containerpane
		_containerpane = new JPanel(new BorderLayout());
		// Tạo mới một GamePanel và truyền instance của class Frame vào cho constructor của GamePanel
		_gamepane = new GamePanel(this);
		// Tạo mới một InfoPanel và truyền instance của class Game vào cho constructor của InfoPanel
		_infopanel = new InfoPanel(_gamepane.getGame());
		// Thêm _infopanel vào _containerpane, vị trí PAGE_START (phía trên cùng)
		_containerpane.add(_infopanel, BorderLayout.PAGE_START);
		// Thêm _gamepane vào _containerpane, vị trí PAGE_END (phía dưới cùng)
		_containerpane.add(_gamepane, BorderLayout.PAGE_END);
		// Gán biến _game với instance của Game được lấy từ _gamepane
		_game = _gamepane.getGame();
		add(_containerpane);// Thêm _containerpane vào JFrame (lớp cha của class Frame)
		setResizable(false);// Chỉnh giá trị của thuộc tính resizable thành false để ngăn chặn việc thay đổi kích thước Frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Đặt hành động mặc định của Frame khi bấm nút đóng là kết thúc chương trình
		pack();// Tự động điều chỉnh kích thước của Frame sao cho phù hợp với nội dung
		setLocationRelativeTo(null);// Hiển thị Frame tại trung tâm màn hình
		setVisible(true);// Hiển thị Frame
		
		_game.start();// Bắt đầu trò chơi
	}
	
	public void setTime(int time)// Phương thức setTime dùng để cập nhật thời gian của trò chơi
	{
		_infopanel.setTime(time);// Gọi phương thức setTime của _infopanel với tham số time
	}
	
	public void setPoints(int points)// Phương thức setPoints dùng để cập nhật điểm số của trò chơi
	{
		_infopanel.setPoints(points);// Gọi phương thức setPoints của _infopanel với tham số points
	}
}
