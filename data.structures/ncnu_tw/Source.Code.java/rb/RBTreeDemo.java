import java.awt.*;
import java.applet.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
public class RBTreeDemo extends JApplet implements ActionListener {
    RBTreeImage RBTree;
    JPanel display;
    JScrollPane jsp;
    JTextField input;
    Font font = new Font("標楷體", Font.PLAIN, 16);
    class MyCanvas extends JComponent {
        BufferedImage img;
        public MyCanvas(BufferedImage img) {
            this.img = img;
        }
        public void paint(Graphics g) {
            g.drawImage(img, 0, 0, null);
        }
        public Dimension getPreferredSize() {
            return new Dimension(img.getWidth(), img.getHeight());
        }
    }
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        String data = input.getText();
        if (command.equals("Exit")) {
            System.exit(0);
        }
        if (data.equals("")) {
            return;
        }
        if (command.equals("Insert")) {
            RBTree.insert(data, data);
        } else if (command.equals("Delete")) {
            RBTree.delete(data);
        } else {
            return;
        }
        BufferedImage[] tmp = RBTree.getSnaps();
        display.removeAll();
        for (int i = 0; i < tmp.length; i++) {
            AddConstraint.addConstraint(display, new MyCanvas(tmp[i]), i, 1, 1, 1,
                GridBagConstraints.NONE,
                GridBagConstraints.CENTER,
                0,0,5,5,0,0);
        }
        display.validate();
        display.repaint();
        jsp.validate();
    }
    public void init() {
        createGUI(this.getContentPane());
    }
    public void createGUI(Container container) {
        container.removeAll();
        container.setLayout(new GridBagLayout());
        RBTree = new RBTreeImage();
        display = new JPanel();
        display.setLayout(new GridBagLayout());
        jsp = new JScrollPane(display);
        input = new JTextField(10);
        input.setFont(font);
        AddConstraint.addConstraint(container, jsp, 0, 0, 3, 1,
            GridBagConstraints.BOTH,
            GridBagConstraints.CENTER,
            1,1,0,0,0,0);
        JLabel label = new JLabel("請輸入資料");
        label.setFont(font);
        AddConstraint.addConstraint(container, label, 0, 1, 1, 1,
            GridBagConstraints.HORIZONTAL,
            GridBagConstraints.CENTER,
            1,0,0,0,0,0);
        AddConstraint.addConstraint(container, input, 1, 1, 2, 1,
            GridBagConstraints.HORIZONTAL,
            GridBagConstraints.CENTER,
            1,0,0,0,0,0);
        JButton b = new JButton("Insert");
        b.setFont(font);
        b.addActionListener(this);
        AddConstraint.addConstraint(container, b, 1, 2, 1, 1,
            GridBagConstraints.HORIZONTAL,
            GridBagConstraints.CENTER,
            1,0,0,0,0,0);
        b = new JButton("Delete");
        b.setFont(font);
        b.addActionListener(this);
        AddConstraint.addConstraint(container, b, 2, 2, 1, 1,
            GridBagConstraints.HORIZONTAL,
            GridBagConstraints.CENTER,
            1,0,0,0,0,0);
        b = new JButton("Exit");
        b.setFont(font);
        b.addActionListener(this);
        AddConstraint.addConstraint(container, b, 0, 2, 1, 1,
            GridBagConstraints.HORIZONTAL,
            GridBagConstraints.CENTER,
            1,0,0,0,0,0);
    }
    public static void main(String[] argv) {
        JFrame f = new JFrame("RBTree Tree Demo");
        RBTreeDemo x = new RBTreeDemo();
        x.createGUI(f.getContentPane());
        f.setSize(900,500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}