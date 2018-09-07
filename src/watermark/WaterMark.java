package watermark;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WaterMark {

    private double opacity = 0.7f;
    private int size = 50;
    private String position;

    public WaterMark() {

    }

    public void addImageWatermark(File watermark, String type, File source, File destination) throws IOException {
        BufferedImage image = ImageIO.read(source);

        int width = 0;
        int height = 0;

        BufferedImage img = ImageIO.read(watermark);
        int or_width = img.getWidth();
        int or_height = img.getHeight();
        if (or_width > or_height) {
            width = size;
            height = or_height / (or_width / size);
        } else {
            if (or_width < or_height) {
                width =or_width / (or_height / size);
                height = size;
            }else{
                width = size;
                height = size;
            }
        }

        BufferedImage overlay = resize(ImageIO.read(watermark), height, width);

        // determine image type and handle correct transparency
        int imageType = "png".equalsIgnoreCase(type) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage watermarked = new BufferedImage(image.getWidth(), image.getHeight(), imageType);

        // initializes necessary graphic properties
        Graphics2D w = (Graphics2D) watermarked.getGraphics();
        w.drawImage(image, 0, 0, null);
        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) opacity);
        w.setComposite(alphaChannel);

        // calculates the coordinate where the String is painted
        int centerX = 0;
        int centerY = 0;
        if (position.equals("izqarr")) {
            centerX = 25;
            centerY = 25;
        } else {
            if (position.equals("izqab")) {
                centerX = 25;
                centerY = image.getHeight() - height - 25;
            } else {
                if (position.equals("derarr")) {
                    centerX = image.getWidth() - width - 25;
                    centerY = 25;
                } else {
                    if (position.equals("derab")) {
                        centerX = image.getWidth() - width - 25;
                        centerY = image.getHeight() - height - 25;
                    } else {
                        centerX = image.getWidth() / 2 - width / 2;
                        centerY = image.getHeight() / 2 - height / 2;
                    }
                }
            }
        }

        // add text watermark to the image
        w.drawImage(overlay, centerX, centerY, null);
        ImageIO.write(watermarked, type, destination);
        w.dispose();
    }

    private BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;

    }

}
