package frame;

import helpers.Koneksi;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JenisPemogramanInputFrame extends JFrame {
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField namaTextField;
    private JButton simpanButton;
    private JButton batalButton;
    private JPanel buttonPanel;

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public void isiKomponen() {
        idTextField.setText(String.valueOf(id));

        String findSQL = "SELECT * FROM jenis pemograman WHERE id =?";
        Connection c = Koneksi.getConnection();
        PreparedStatement ps;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                namaTextField.setText(rs.getString("nama_kelas"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public JenisPemogramanInputFrame() {
        simpanButton.addActionListener(e -> {
            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            String nama = namaTextField.getText();

            if(nama.equals("")){
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data nama kelas",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                namaTextField.requestFocus();
                return;
            }
            try {
                if (this.id == 0) { //jika TAMBAH

                    String cekSQL = "SELECT * FROM jenis pemograman WHERE nama_kelas = ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){ // kalau ADA
                        JOptionPane.showMessageDialog(
                                null,
                                "Nama sama sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }


                    String insertSQL = "INSERT INTO jenis pemograman (id,nama,kategori) VALUES (NULL,?)";
                    insertSQL = "INSERT INTO `jenis pemograman` (`id`, `nama`,'kategori') VALUES (NULL, ?)";
                    insertSQL = "INSERT INTO `jenis pemograman` VALUES (NULL, ?)";
                    insertSQL = "INSERT INTO jenis pemograman (nama_kelas) VALUES (?)";
                    insertSQL = "INSERT INTO jenis pemograman SET nama_kelas=?";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1, nama);
                    ps.executeUpdate();
                    dispose();
                } else {

                    String cekSQL = "SELECT * FROM jenis pemograman WHERE nama_kelas=? AND id!=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){ // kalau ADA
                        JOptionPane.showMessageDialog(
                                null,
                                "Nama sama sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    String updateSQL = "UPDATE jenis pemograman SET nama_kelas=? WHERE id=?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                    dispose();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        batalButton.addActionListener(e -> {
            dispose();
        });
        init();
    }

    public void init() {
        setContentPane(mainPanel);
        pack();
        setTitle("Input jenis pemograman");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
