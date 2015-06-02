
/**
 * Created by ������ ������� on 20.05.2015.
 */
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
public class PicturePanel extends javax.swing.JPanel {
    // ������ 2 �����������: ������������ � �������.
    // ������������ ������������ ��� ��������� �������� � ����������� �� �������� ������.
    // ������� ��������������� ��������������� �� ������.
    private BufferedImage originalImage = null;
    private Image image = null;
    public PicturePanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(null);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
    }

    // ������� �� ��������� �������� ������ - ��������� ������� �����������.
    private void formComponentResized(java.awt.event.ComponentEvent evt) {
        int w = this.getWidth();
        int h = this.getHeight();
        if ((originalImage != null) && (w > 0) && (h > 0)) {
            image = originalImage.getScaledInstance(w, h, Image.SCALE_DEFAULT);
            this.repaint();
        }
    }
    // ����� ���������� � ���� ����.
    public void paint(Graphics g) {
        // ������ ��������
        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }

        // ������ �������������.
        super.paintChildren(g);
        // ������ �����
        super.paintBorder(g);
    }

    // ������ ��� ��������� ��������.
    public BufferedImage getImage() {
        return originalImage;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
    public void setImageFile(File imageFile) {
        try {
            if (imageFile == null) {
                originalImage = null;
            }
            BufferedImage bi = ImageIO.read(imageFile);
            originalImage = bi;
        } catch (IOException ex) {
            System.err.println("��������� ��������� ��������!");
            ex.printStackTrace();
        }
        repaint();
    }
}