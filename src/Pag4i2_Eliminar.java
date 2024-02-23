import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;

public class Pag4i2_Eliminar {
    public JPanel Eliminar_u;
    private JButton regresarButton;
    private JLabel icono;
    private JTable table1;
    private JButton editarButton;
    private JButton eliminarButton;
    private JTextField idField;
    private JTextField nombreField;
    private JTextField apellidoField;
    private JTextField passwordField;
    static JFrame frameEliminarP = new JFrame("Eliminar Productos");
    ConexionDB conexionDB = new ConexionDB();

    public Pag4i2_Eliminar() {
        actualizarTabla();
        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int filaSeleccionada = table1.getSelectedRow();
                    if (filaSeleccionada != -1) {
                        obtenerDatos(filaSeleccionada);
                    }
                }
            }
        });
        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameEliminarP.setVisible(false);
                Pag4_Admin.frameAdminP.setVisible(true);
            }
        });
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection connection = conexionDB.ConexionLocal();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Usuarios\n" +
                            "SET user_id = ?, nombre_usr = ?, apellido_usr = ?, password_usr = ?\n" +
                            "WHERE user_id = ?");
                    preparedStatement.setInt(1, Integer.parseInt(idField.getText()));
                    preparedStatement.setString(2, nombreField.getText());
                    preparedStatement.setString(3, apellidoField.getText());
                    preparedStatement.setString(4, passwordField.getText());
                    preparedStatement.setInt(5, Integer.parseInt(idField.getText()));
                    preparedStatement.executeUpdate();
                    actualizarTabla();
                    JOptionPane.showMessageDialog(null,"Se han modificado los datos correctamente");
                }catch (SQLException ex){
                    System.out.println(ex.getMessage());
                }
            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection connection = conexionDB.ConexionLocal();
                try {
                    PreparedStatement ps = connection.prepareStatement("DELETE FROM Usuarios WHERE user_id = ?");
                    ps.setInt(1, Integer.parseInt(idField.getText()));
                    ps.executeUpdate();
                    actualizarTabla();
                    JOptionPane.showMessageDialog(null,"Se han eliminado los datos correctamente");
                }catch (SQLException sqlException){
                    JOptionPane.showMessageDialog(null,sqlException.getMessage(),"Error de sintaxis",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    public void actualizarTabla(){
        try {
            Connection connection = conexionDB.ConexionLocal();
            DefaultTableModel modelo = new DefaultTableModel();
            table1.setModel(modelo);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Usuarios");
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int cantidadColumnas = resultSetMetaData.getColumnCount();

            modelo.addColumn("Usuario ID");
            modelo.addColumn("Nombre");
            modelo.addColumn("Apellido");
            modelo.addColumn("Contraseña");
            while (rs.next()){
                Object[] filas = new Object[cantidadColumnas];
                for (int i=0;i<cantidadColumnas;i++){
                    filas[i] = rs.getObject(i + 1);
                }
                modelo.addRow(filas);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    private void obtenerDatos(int filaSeleccionada){
        try {
            Connection connection = conexionDB.ConexionLocal();
            int idFilaSeleccionada= Integer.parseInt(table1.getModel().getValueAt(filaSeleccionada,0).toString());
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Usuarios WHERE user_id = ?");
            ps.setInt(1, idFilaSeleccionada);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()){
                idField.setText(String.valueOf(resultSet.getInt("user_id")));
                nombreField.setText(resultSet.getString("nombre_usr"));
                apellidoField.setText(resultSet.getString("apellido_usr"));
                passwordField.setText(resultSet.getString("password_usr"));
            }
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
}
