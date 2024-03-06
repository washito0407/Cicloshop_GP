import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Pag3_Login {
    public JPanel baselogin;
    private JTextField nombreTextField;
    private JPasswordField passwordField;
    private JButton INGRESARButton;
    private JButton REGRESARButton;
    static JFrame frameLogin = new JFrame("Login CICLOSHOP");
    ConexionDB conexionDB = new ConexionDB();
    static int idCajeroActual = -1;

    public Pag3_Login() {
        INGRESARButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        REGRESARButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        REGRESARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameLogin.dispose();
                Pag1_Inicio.frame.setVisible(true);
            }
        });
        INGRESARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean usuarioEncontrado=false;
                if (("admin").equals(nombreTextField.getText())
                        && ("admin").equals(String.valueOf(passwordField.getPassword())))
                {
                    usuarioEncontrado=true;
                    frameLogin.dispose();
                    Pag4_Admin.frameAdminP.setContentPane(new Pag4_Admin().admin_pag);
                    Pag4_Admin.frameAdminP.setSize(800,600);
                    Pag4_Admin.frameAdminP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    Pag4_Admin.frameAdminP.setVisible(true);
                    Pag4_Admin.frameAdminP.setLocationRelativeTo(null);
                }else {
                    try {
                        Connection connection = conexionDB.ConexionLocal();
                        //SQL para la tabla de Usuarios
                        PreparedStatement ps = connection.prepareStatement("SELECT * FROM CAJEROS");
                        ResultSet resultSetUsuarios = ps.executeQuery();
                        while (resultSetUsuarios.next()&&!usuarioEncontrado){
                            if (resultSetUsuarios.getString(2).equals(nombreTextField.getText())
                                    && resultSetUsuarios.getString(4).equals(String.valueOf(passwordField.getPassword())))
                            {
                                usuarioEncontrado=true;
                                frameLogin.dispose();
                                idCajeroActual = resultSetUsuarios.getInt(1);
                                Pag5_Compra.frameCompra.setContentPane(new Pag5_Compra().pag5CompraPanel);
                                Pag5_Compra.frameCompra.setSize(1200,700);
                                Pag5_Compra.frameCompra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                Pag5_Compra.frameCompra.setVisible(true);
                                Pag5_Compra.frameCompra.setLocationRelativeTo(null);
                            }
                        }
                    } catch (Exception exception){
                            JOptionPane.showMessageDialog(null,"Error: "+exception.getMessage(),"ERROR SQL",JOptionPane.ERROR_MESSAGE);
                    }
                }
                if (!usuarioEncontrado){
                    JOptionPane.showMessageDialog(null,"Ingrese datos correctos");
                    nombreTextField.setText("");
                    passwordField.setText("");
                }
            }
        });
    }
}
