package ui;

import services.ClientService;
import model.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private ClientService clientService = new ClientService();

    private JTextField txtNom, txtEmail, txtSearch;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh;

    public ClientPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("🔍 Search: "));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);
        topPanel.add(searchPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Manage Clients"));

        formPanel.add(new JLabel("Name:"));
        txtNom = new JTextField();
        formPanel.add(txtNom);

        formPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        formPanel.add(txtEmail);

        btnAdd = new JButton("Add Client");
        btnUpdate = new JButton("Update Selected");
        formPanel.add(btnAdd);
        formPanel.add(btnUpdate);

        add(formPanel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columns = { "ID", "Name", "Email" };
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
        btnAdd.addActionListener(e -> addClient());
        btnDelete.addActionListener(e -> deleteClient());
        btnUpdate.addActionListener(e -> updateClient());

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filterTable();
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtNom.setText(model.getValueAt(row, 1).toString());
                txtEmail.setText(model.getValueAt(row, 2).toString());
            }
        });

        refreshTable();
    }

    private void refreshTable() {
        try {
            model.setRowCount(0);
            List<Client> list = clientService.getAllClients();
            for (Client c : list) {
                model.addRow(new Object[] { c.getId_client(), c.getNom(), c.getEmail() });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private void addClient() {
        try {
            Client c = new Client(txtNom.getText(), txtEmail.getText());
            clientService.addClient(c);
            refreshTable();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding client: " + e.getMessage());
        }
    }

    private void updateClient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a client to update");
            return;
        }
        try {
            int id = (int) model.getValueAt(row, 0);
            Client c = new Client(id, txtNom.getText(), txtEmail.getText());
            clientService.updateClient(c);
            refreshTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating client: " + e.getMessage());
        }
    }

    private void deleteClient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a client to delete");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this client?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                clientService.deleteClient(id);
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting client: " + e.getMessage());
            }
        }
    }

    private void clearFields() {
        txtNom.setText("");
        txtEmail.setText("");
    }

    private void filterTable() {
        String query = txtSearch.getText().toLowerCase();
        try {
            model.setRowCount(0);
            List<Client> list = clientService.getAllClients();
            for (Client c : list) {
                if (c.getNom().toLowerCase().contains(query) ||
                        c.getEmail().toLowerCase().contains(query)) {
                    model.addRow(new Object[] { c.getId_client(), c.getNom(), c.getEmail() });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
