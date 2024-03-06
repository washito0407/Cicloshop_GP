import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Pag4i2_Eliminar {
    public JPanel Eliminar_u;
    private JButton regresarButton;
    private JTable table1;
    private JLabel icono;
    private JButton modificarEstadoButton;
    private JButton eliminarButton;
    private JButton mostrarCButton;
    private JButton modificarCButton;
    static JFrame frameEliminarP = new JFrame("Eliminar Productos");
    ConexionDB conexionDB = new ConexionDB();

    public Pag4i2_Eliminar() {
        actualizarTabla();
        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameEliminarP.setVisible(false);
                Pag4_Admin.frameAdminP.setVisible(true);
            }
        });
        modificarEstadoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int filaSeleccionada = table1.getSelectedRow();
                    if (filaSeleccionada!=-1){
                        Connection connection = conexionDB.ConexionLocal();
                        String estado = table1.getValueAt(filaSeleccionada,2).toString();
                        PreparedStatement ps = connection.prepareStatement("UPDATE CAJEROS SET estado_c= ? WHERE id_cajero = ?");
                        if (estado.equals("ACTIVO")){
                            ps.setString(1,"INACTIVO");
                        }else {
                            ps.setString(1,"ACTIVO");
                        }
                        ps.setInt(2,Integer.parseInt(table1.getValueAt(filaSeleccionada,0).toString()));
                        ps.executeUpdate();
                        actualizarTabla();
                        JOptionPane.showMessageDialog(null,"Se ha modificado el estado correctamente");
                    }else {
                        JOptionPane.showMessageDialog(null,"Seleccione un cajero","Fila no seleccionada",JOptionPane.ERROR_MESSAGE);
                    }
                }catch (SQLException ex){
                    JOptionPane.showMessageDialog(null,"Error: "+ex.getMessage(),"ERROR SQL",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = table1.getSelectedRow();
                if (filaSeleccionada!=-1){
                    int option = JOptionPane.showConfirmDialog(null,"Estas seguro de eliminar al cajero?","Confirmar eliminación",JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION){
                        try {
                            Connection connection = conexionDB.ConexionLocal();
                            PreparedStatement ps = connection.prepareStatement("DELETE FROM CAJEROS WHERE id_cajero = ?");
                            ps.setInt(1,Integer.parseInt(table1.getValueAt(filaSeleccionada,0).toString()));
                            ps.executeUpdate();
                            actualizarTabla();
                        }catch (SQLException ex){
                            JOptionPane.showMessageDialog(null,"Error: "+ex.getMessage(),"ERROR SQL",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }else {
                    JOptionPane.showMessageDialog(null,"Seleccione un cajero","Fila no seleccionada",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        mostrarCButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table1.getColumnModel().getColumn(3).getMaxWidth()==0){
                    JPasswordField pwd = new JPasswordField(10);
                    int action = JOptionPane.showConfirmDialog(null, pwd,"Ingresa la clave",JOptionPane.OK_CANCEL_OPTION);
                    if(action == JOptionPane.CANCEL_OPTION || action == JOptionPane.CLOSED_OPTION)JOptionPane.showMessageDialog(null,"Cancela, X o selecciona la tecla de escape");
                    String passIn = String.valueOf(pwd.getPassword());
                    if (passIn.equals("admin")){
                        table1.getColumnModel().getColumn(3).setMaxWidth(100);
                        table1.getColumnModel().getColumn(3).setMinWidth(100);
                        table1.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(100);
                        table1.getTableHeader().getColumnModel().getColumn(3).setMinWidth(100);
                        mostrarCButton.setText("Ocultar Contraseñas");
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Contraseña incorrecta");
                    }
                }else {
                    table1.getColumnModel().getColumn(3).setMaxWidth(0);
                    table1.getColumnModel().getColumn(3).setMinWidth(0);
                    table1.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);
                    table1.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);
                    mostrarCButton.setText("Mostrar Contraseñas");
                }
            }
        });
        modificarCButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionda = table1.getSelectedRow();
                if (filaSeleccionda!=-1){
                    String passCajero = table1.getValueAt(filaSeleccionda,3).toString();
                    JPasswordField pwd = new JPasswordField(10);
                    int action = JOptionPane.showConfirmDialog(null, pwd,"Ingresa la contraseña actual del cajero",JOptionPane.OK_CANCEL_OPTION);
                    if(action == JOptionPane.CANCEL_OPTION || action == JOptionPane.CLOSED_OPTION) {
                        JOptionPane.showMessageDialog(null, "Cancela, X o selecciona la tecla de escape");
                    }else {
                        String passIn = String.valueOf(pwd.getPassword());
                        if (passIn.equals(passCajero)){
                            JPasswordField pwdNueva = new JPasswordField(10);
                            int actionNueva = JOptionPane.showConfirmDialog(null, pwdNueva,"Ingresa la nueva contraseña",JOptionPane.OK_CANCEL_OPTION);
                            if(actionNueva == JOptionPane.CANCEL_OPTION || actionNueva== JOptionPane.CLOSED_OPTION){
                                JOptionPane.showMessageDialog(null,"Cancela, X o selecciona la tecla de escape");
                            }
                            else {
                                String passNuevaIn = String.valueOf(pwdNueva.getPassword());
                                try {
                                    Connection connection = conexionDB.ConexionLocal();
                                    PreparedStatement ps = connection.prepareStatement("UPDATE CAJEROS SET pass_cajero = ? WHERE id_cajero = ?");
                                    ps.setString(1,passNuevaIn);
                                    ps.setInt(2,Integer.parseInt(table1.getValueAt(filaSeleccionda,0).toString()));
                                    ps.executeUpdate();
                                    JOptionPane.showMessageDialog(null,"Se ha registrado la nueva contraseña correctamente");
                                    actualizarTabla();
                                }catch (SQLException ex){
                                    JOptionPane.showMessageDialog(null,"Error: "+ex.getMessage(),"ERROR SQL",JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }else{
                            JOptionPane.showMessageDialog(null,"Contraseña Incorrecta","",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }else {
                    JOptionPane.showMessageDialog(null,"Seleccione un cajero","Fila no seleccionada",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    public void actualizarTabla(){
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            table1.setModel(modelo);
            table1.setDefaultEditor(Object.class, null);
            table1.getTableHeader().setReorderingAllowed(false) ;

            Connection connection = conexionDB.ConexionLocal();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CAJEROS");
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();

            int cantidadColumnas = resultSetMetaData.getColumnCount();

            modelo.addColumn("ID");
            modelo.addColumn("Nombre");
            modelo.addColumn("Estado");
            modelo.addColumn("Password");
            while (rs.next()){
                Object[] filas = new Object[cantidadColumnas];
                for (int i=0;i<cantidadColumnas;i++){
                    filas[i] = rs.getObject(i + 1);
                }
                modelo.addRow(filas);
            }
            table1.getColumnModel().getColumn(3).setMaxWidth(0);
            table1.getColumnModel().getColumn(3).setMinWidth(0);
            table1.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);
            table1.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }catch (NullPointerException ignored){}
    }
}
