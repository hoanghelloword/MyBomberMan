package uet.oop.bomberman.gui;

import uet.oop.bomberman.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Swing Panel hiển thị thông tin thời gian, điểm mà người chơi đạt được
 */
public class InfoPanel extends JPanel {
	
	private JLabel timeLabel;
	private JLabel pointsLabel;

	public InfoPanel(Game game) {
		//(1)
		setLayout(new GridLayout());
		
		//Tạo một instance của JLabel với nội dung là "Time: " cộng với thời gian trong board của game.
		//Thiết lập màu chữ là trắng và căn giữa.
		timeLabel = new JLabel("Time: " + game.getBoard().getTime());
		timeLabel.setForeground(Color.white);
		timeLabel.setHorizontalAlignment(JLabel.CENTER);
		
		//Tạo một instance của JLabel với nội dung là "Points: " cộng với điểm số trong board của game. 
		//Thiết lập màu chữ là trắng và căn giữa
		pointsLabel = new JLabel("Points: " + game.getBoard().getPoints());
		pointsLabel.setForeground(Color.white);
		pointsLabel.setHorizontalAlignment(JLabel.CENTER);
		
		//Thêm hai instance của JLabel vào JPanel.
		add(timeLabel);
		add(pointsLabel);
		
		//Đặt màu nền của JPanel thành màu đen và đặt kích thước ưu tiên của JPanel thành 0 chiều rộng và 40 chiều cao.
		setBackground(Color.black);
		setPreferredSize(new Dimension(0, 40));
	}
	
	public void setTime(int t) {
		timeLabel.setText("Time: " + t);
	}

	public void setPoints(int t) {
		pointsLabel.setText("Score: " + t);
	}
	
}
