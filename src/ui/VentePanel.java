package ui;

import services.ClientService;
import services.OeuvreServices;
import services.VenteArtService;
import model.Client;
import model.Oeuvre;
import model.VenteArt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class VentePanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private VenteArtService venteService = new VenteArtService();
    private ClientService clientService = new ClientService();
    private OeuvreServices oeuvreService = new OeuvreServices();

    private JComboBox<Client> comboClient;
    private JComboBox<Oeuvre> comboOeuvre;
    private JDateChooser startDateChooser, endDateChooser;
    private JButton btnAdd, btnDelete, btnRefresh, btnFilter;

    public VentePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Record Sale"));

        formPanel.add(new JLabel("Select Client:"));
        comboClient = new JComboBox<>();
        formPanel.add(comboClient);

        formPanel.add(new JLabel("Select Artwork:"));
        comboOeuvre = new JComboBox<>();
        formPanel.add(comboOeuvre);

        btnAdd = new JButton("Record Sale");
        formPanel.add(btnAdd);

        add(formPanel, BorderLayout.WEST);

        // Filter Panel (Top)
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter by Date"));

        filterPanel.add(new JLabel("From:"));
        startDateChooser = new JDateChooser();
        startDateChooser.setPreferredSize(new Dimension(120, 25));
        filterPanel.add(startDateChooser);

        filterPanel.add(new JLabel("To:"));
        endDateChooser = new JDateChooser();
        endDateChooser.setPreferredSize(new Dimension(120, 25));
        filterPanel.add(endDateChooser);

        btnFilter = new JButton("Filter");
        btnFilter.setBackground(new Color(52, 152, 219));
        btnFilter.setForeground(Color.WHITE);
        filterPanel.add(btnFilter);

        add(filterPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columns = { "ID", "Client", "Artwork", "Date", "Price" };
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom Actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnDelete = new JButton("Delete Record");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);
        btnRefresh = new JButton("Refresh Table");

        actionPanel.add(btnRefresh);
        actionPanel.add(btnDelete);
        add(actionPanel, BorderLayout.SOUTH);

        // Event Listeners
        btnRefresh.addActionListener(e -> refreshAll());
        btnAdd.addActionListener(e -> addVente());
        btnDelete.addActionListener(e -> deleteVente());
        btnFilter.addActionListener(e -> filterByDate());

        // Custom renderers for ComboBoxes
        comboClient.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Client) {
                    setText(((Client) value).getNom());
                }
                return this;
            }
        });

        comboOeuvre.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Oeuvre) {
                    setText(((Oeuvre) value).getTitre() + " ($" + ((Oeuvre) value).getPrix() + ")");
                }
                return this;
            }
        });

        refreshAll();
    }

    private void refreshAll() {
        refreshTable();
        loadCombos();
    }

    private void loadCombos() {
        try {
            comboClient.removeAllItems();
            List<Client> clients = clientService.getAllClients();
            for (Client c : clients)
                comboClient.addItem(c);

            comboOeuvre.removeAllItems();
            List<Oeuvre> oeuvres = oeuvreService.getAllOeuvres();
            for (Oeuvre o : oeuvres)
                comboOeuvre.addItem(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        try {
            model.setRowCount(0);
            List<VenteArt> list = venteService.getAllSales();
            for (VenteArt v : list) {
                model.addRow(new Object[] {
                        v.getIdVente(),
                        v.getClient().getNom(),
                        v.getOuvre().getTitre(),
                        v.getDateVente(),
                        v.getOuvre().getPrix()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private void addVente() {
        Client client = (Client) comboClient.getSelectedItem();
        Oeuvre oeuvre = (Oeuvre) comboOeuvre.getSelectedItem();

        if (client == null || oeuvre == null) {
            JOptionPane.showMessageDialog(this, "Please select both a client and an artwork");
            return;
        }

        try {
            VenteArt v = new VenteArt(client, oeuvre, LocalDateTime.now());
            venteService.recordSale(v);
            refreshTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error recording sale: " + e.getMessage());
        }
    }

    private void deleteVente() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        try {
            venteService.deleteSale(id);
            refreshTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting record: " + e.getMessage());
        }
    }

    private void filterByDate() {
        Date start = startDateChooser.getDate();
        Date end = endDateChooser.getDate();

        if (start == null || end == null) {
            JOptionPane.showMessageDialog(this, "Please select both start and end dates");
            return;
        }

        try {
            model.setRowCount(0);
            List<VenteArt> list = venteService.getAllSales();

            // Convert java.util.Date to LocalDateTime for comparison
            LocalDateTime startLDT = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().withHour(0)
                    .withMinute(0);
            LocalDateTime endLDT = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().withHour(23)
                    .withMinute(59);

            for (VenteArt v : list) {
                LocalDateTime vDate = v.getDateVente();
                if ((vDate.isAfter(startLDT) || vDate.isEqual(startLDT)) &&
                        (vDate.isBefore(endLDT) || vDate.isEqual(endLDT))) {
                    model.addRow(new Object[] {
                            v.getIdVente(),
                            v.getClient().getNom(),
                            v.getOuvre().getTitre(),
                            v.getDateVente(),
                            v.getOuvre().getPrix()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error filtering data: " + e.getMessage());
        }
    }
}
