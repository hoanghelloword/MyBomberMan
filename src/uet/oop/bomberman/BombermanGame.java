package uet.oop.bomberman;

import uet.oop.bomberman.gui.Frame;
//import class Frame từ package "uet.oop.bomberman.gui". 
//Class Frame là lớp chứa giao diện người dùng cho trò chơi Bomberman.
public class BombermanGame {
//Khai báo lớp BombermanGame, là class chính để chạy trò chơi.
	public static void main(String[] args) {
		new Frame();
		//Tạo một đối tượng mới của lớp Frame để hiển thị giao diện người dùng cho trò chơi. 
		//Khi chương trình được chạy, một cửa sổ mới sẽ xuất hiện với giao diện BombermanGame.
	}
}
