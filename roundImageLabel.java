import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
// Custom JLabel to display a round image
class roundImageLabel extends JLabel {
    private BufferedImage image;

    public roundImageLabel(String imagePath, int size) {
        try {
            // Load the image
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            if (originalImage != null) {
                // Scale the image to the desired size while maintaining aspect ratio, then crop
                this.image = createRoundImage(originalImage, size);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Fallback: set a default text or icon if image fails to load
            setText("Image Error");
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        setPreferredSize(new Dimension(size, size)); // Set preferred size for layout managers
    }

    private BufferedImage createRoundImage(BufferedImage originalImage, int size) {
        // Ensure the image is square and create a round shape
        BufferedImage scaledImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();

        // Use high-quality rendering hints
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Calculate the best fit to draw the image without distortion
        int imgWidth = originalImage.getWidth();
        int imgHeight = originalImage.getHeight();
        int drawX = 0;
        int drawY = 0;
        int drawSize = size; // Target size for drawing

        if (imgWidth > imgHeight) { // Image is wider than tall, crop width
            drawSize = imgHeight;
            drawX = (imgWidth - imgHeight) / 2;
        } else if (imgHeight > imgWidth) { // Image is taller than wide, crop height
            drawSize = imgWidth;
            drawY = (imgHeight - imgWidth) / 2;
        }

        // Draw the original image centered into a square buffer
        g2d.drawImage(originalImage, 0, 0, size, size, drawX, drawY, drawX + drawSize, drawY + drawSize, null);

        // Apply circular clip
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fill(new Rectangle(0, 0, size, size)); // Clear the whole image
        g2d.setComposite(AlphaComposite.SrcOver);

        // Create the circular shape
        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, size, size);
        g2d.setClip(circle);

        // Redraw the original content within the clip
        g2d.drawImage(originalImage, 0, 0, size, size, drawX, drawY, drawX + drawSize, drawY + drawSize, null);


        g2d.dispose();
        return scaledImage;
    }


    @Override
    protected void paintComponent(Graphics g) {
        if (image != null) {
            // Draw the pre-rendered round image
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        } else {
            // If image failed to load, draw default text
            super.paintComponent(g);
        }
    }
}
