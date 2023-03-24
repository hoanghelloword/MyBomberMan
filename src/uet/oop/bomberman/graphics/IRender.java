public interface IRender {

	// Phương thức update() dùng để cập nhật trạng thái của đối tượng
	void update();
	
	// Phương thức render() dùng để vẽ đối tượng lên màn hình
	void render(Screen screen);
}
