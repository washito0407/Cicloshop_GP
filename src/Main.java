import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Pag1_Inicio.frame.setContentPane(new Pag1_Inicio().login);
        Pag1_Inicio.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Pag1_Inicio.frame.setSize(600, 500);
        Pag1_Inicio.frame.setVisible(true);
        Pag1_Inicio.frame.setLocationRelativeTo(null);
        ConexionDB conexionDB = new ConexionDB();
        conexionDB.ConexionLocal("jdbc:mysql://38.46.218.114:3306/intexcom_vicishop", "intexcom_kevinC", "Encebollado0401.");
    }
}