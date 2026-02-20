package ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Art Gallery Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Main Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Add Panels (We will create these next)
        tabbedPane.addTab("🎨 Artworks", new OeuvrePanel());
        tabbedPane.addTab("👥 Clients", new ClientPanel());
        tabbedPane.addTab("💰 Sales", new VentePanel());

        add(tabbedPane);

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(41, 128, 185));
        header.setPreferredSize(new Dimension(1000, 60));
        JLabel title = new JLabel("GALERIE D'ART");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.add(title);

        getContentPane().add(header, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
