import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Pag2_Registro {
    private JButton REGRESARButton;
    private JTextField nombreField;
    private JTextField apellidoField;
    private JPasswordField passwordField;
    private JButton REGISTRARSEButton;
    public JPanel registro;
    static JFrame frameRegistro = new JFrame("Registro");
    ConexionDB conexionDB = new ConexionDB();

    public Pag2_Registro() {
        REGRESARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameRegistro.dispose();
                Pag1_Inicio.frame.setVisible(true);
            }
        });
        REGISTRARSEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection connection = conexionDB.ConexionLocal();
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO Usuarios(user_id, nombre_usr,apellido_usr,password_usr) VALUES (4,?,?,?)");
                    ps.setString(1,nombreField.getText());
                    ps.setString(2,apellidoField.getText());
                    ps.setString(3,String.valueOf(passwordField.getPassword()));
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Te has registrado correctamente (Prueba)");
                    frameRegistro.dispose();
                    Pag1_Inicio.frame.setVisible(true);
                }catch (SQLException exception){
                    JOptionPane.showMessageDialog(null, exception.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }

            }
        });
    }
}
