package ui;

import services.OeuvreServices;
import model.Oeuvre;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OeuvrePanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private OeuvreServices oeuvreService = new OeuvreServices();
    private String selectedImagePath = "";
    private JLabel lblImagePreview;

    private JTextField txtTitre, txtArtiste, txtCategorie, txtPrix, txtSearch;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh, btnChooseImage;

    public OeuvrePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("🔍 Search: "));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);
        topPanel.add(searchPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Manage Artwork"));

        formPanel.add(new JLabel("Title:"));
        txtTitre = new JTextField();
        formPanel.add(txtTitre);

        formPanel.add(new JLabel("Artist:"));
        txtArtiste = new JTextField();
        formPanel.add(txtArtiste);

        formPanel.add(new JLabel("Category:"));
        txtCategorie = new JTextField();
        formPanel.add(txtCategorie);

        formPanel.add(new JLabel("Price ($):"));
        txtPrix = new JTextField();
        formPanel.add(txtPrix);

        formPanel.add(new JLabel("Image:"));
        btnChooseImage = new JButton("Choose Photo...");
        formPanel.add(btnChooseImage);

        lblImagePreview = new JLabel("No Image", JLabel.CENTER);
        lblImagePreview.setPreferredSize(new Dimension(150, 150));
        lblImagePreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.add(lblImagePreview, BorderLayout.CENTER);

        btnAdd = new JButton("Add Artwork");
        btnUpdate = new JButton("Update Selected");

        JPanel btnFormPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        btnFormPanel.add(btnAdd);
        btnFormPanel.add(btnUpdate);

        JPanel leftContainer = new JPanel(new BorderLayout(5, 5));
        leftContainer.add(formPanel, BorderLayout.NORTH);
        leftContainer.add(previewPanel, BorderLayout.CENTER);
        leftContainer.add(btnFormPanel, BorderLayout.SOUTH);

        add(leftContainer, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columns = { "ID", "Title", "Artist", "Category", "Price", "Image Path" };
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom Actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);
        btnRefresh = new JButton("Refresh Table");

        actionPanel.add(btnRefresh);
        actionPanel.add(btnDelete);
        add(actionPanel, BorderLayout.SOUTH);

        // Event Listeners
        btnRefresh.addActionListener(e -> refreshTable());
        btnAdd.addActionListener(e -> addOeuvre());
        btnDelete.addActionListener(e -> deleteOeuvre());
        btnUpdate.addActionListener(e -> updateOeuvre());
        btnChooseImage.addActionListener(e -> chooseImage());

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filterTable();
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtTitre.setText(model.getValueAt(row, 1).toString());
                txtArtiste.setText(model.getValueAt(row, 2).toString());
                txtCategorie.setText(model.getValueAt(row, 3).toString());
                txtPrix.setText(model.getValueAt(row, 4).toString());
                selectedImagePath = model.getValueAt(row, 5) != null ? model.getValueAt(row, 5).toString() : "";
                updateImagePreview(selectedImagePath);
            }
        });

        refreshTable();
    }

    private void refreshTable() {
        try {
            model.setRowCount(0);
            List<Oeuvre> list = oeuvreService.getAllOeuvres();
            for (Oeuvre o : list) {
                model.addRow(
                        new Object[] { o.getIdOeuvre(), o.getTitre(), o.getArtiste(), o.getCategorie(), o.getPrix(),
                                o.getImagePath() });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private void addOeuvre() {
        try {
            Oeuvre o = new Oeuvre(txtTitre.getText(), txtArtiste.getText(), txtCategorie.getText(),
                    Double.parseDouble(txtPrix.getText()), selectedImagePath);
            oeuvreService.addOeuvre(o);
            refreshTable();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding artwork: " + e.getMessage());
        }
    }

    private void updateOeuvre() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an artwork to update");
            return;
        }
        try {
            int id = (int) model.getValueAt(row, 0);
            Oeuvre o = new Oeuvre(txtTitre.getText(), txtArtiste.getText(), txtCategorie.getText(),
                    Double.parseDouble(txtPrix.getText()), id, selectedImagePath);
            oeuvreService.updateOeuvre(o);
            refreshTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating artwork: " + e.getMessage());
        }
    }

    private void deleteOeuvre() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an artwork to delete");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this artwork?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                oeuvreService.deleteOeuvre(id);
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting artwork: " + e.getMessage());
            }
        }
    }

    private void clearFields() {
        txtTitre.setText("");
        txtArtiste.setText("");
        txtCategorie.setText("");
        txtPrix.setText("");
        selectedImagePath = "";
        lblImagePreview.setIcon(null);
        lblImagePreview.setText("No Image");
    }

    private void filterTable() {
        String query = txtSearch.getText().toLowerCase();
        try {
            model.setRowCount(0);
            List<Oeuvre> list = oeuvreService.getAllOeuvres();
            for (Oeuvre o : list) {
                if (o.getTitre().toLowerCase().contains(query) ||
                        o.getArtiste().toLowerCase().contains(query) ||
                        o.getCategorie().toLowerCase().contains(query)) {
                    model.addRow(new Object[] { o.getIdOeuvre(), o.getTitre(), o.getArtiste(), o.getCategorie(),
                            o.getPrix(), o.getImagePath() });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chooseImage() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImagePath = chooser.getSelectedFile().getAbsolutePath();
            updateImagePreview(selectedImagePath);
        }
    }

    private void updateImagePreview(String path) {
        if (path == null || path.isEmpty()) {
            lblImagePreview.setIcon(null);
            lblImagePreview.setText("No Image");
            return;
        }
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            lblImagePreview.setIcon(new ImageIcon(img));
            lblImagePreview.setText("");
        } catch (Exception e) {
            lblImagePreview.setText("Error loading image");
        }
    }
}
