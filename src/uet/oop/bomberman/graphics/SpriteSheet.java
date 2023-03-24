package Graphics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
/**
 * Tất cả sprite (hình ảnh game) được lưu trữ vào một ảnh duy nhất
 * Class này giúp lấy ra các sprite riêng từ 1 ảnh chung duy nhất đó
 * chứa nhân vật, bomb,.....
 */
public class SpriteSheet {
    /** đường dẫn đến các Sprite*/
    private String path;
    /**kích thước của 1 Sprite*/
    public final int SIZE;
    /** mảng dữ liệu pixel của ảnh chung*/
    public int[] _pixels;

    public static SpriteSheet tiles = new SpriteSheet("/textures/classic.png", 256);

    public SpriteSheet(String path, int SIZE) {
        this.path = path;
        this.SIZE = SIZE;
        _pixels = new int[SIZE * SIZE];
        load();
    }

    /**
     * đọc ảnh chung từ đường dẫn _path và
     * lưu trữ dữ liệu pixel của ảnh chung vào mảng _pixels.
     */
    private void load(){
        try {
            // lấy url của ảnh
            URL a = SpriteSheet.class.getResource(path);
            // tạo ảnh
            BufferedImage image = ImageIO.read(a);
            // lấy chiều rộng chiều cao
            int w = image.getWidth();
            int h = image.getHeight();
            //mảng 1 chiều các số nguyên (integer) biểu diễn giá trị RGB
            // của các pixel của ảnh,
            // lưu trữ các giá trị này trong mảng _pixels ( file word)
            image.getRGB(0,0, w, h, _pixels,0, w);
        }
        catch(IOException e){
            // in ra stack trace của lỗi đó để giúp debug và ghi nhận lỗi
            e.printStackTrace();
            System.exit(0);
        }

    }

}
