import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;

public class Pag5i1_Factura {
    private JTable table1;
    private JButton GENERARFACTURAButton;
    public JPanel pag5FacturaPanel;
    private JPanel barraTop;
    private JButton cerrarButton;
    static JFrame frameFacturaCompra = new JFrame("Compra facturada");
    static double precioTotal = 0;
    static int iFactura = 1;
    static String filePath = String.format("%s%d%s", "facturas/factura", iFactura, ".pdf");
    static File file = new File(filePath);
    static int idFactura = -1;
    private DefaultTableModel modelo = new DefaultTableModel();
    static ConexionDB conexionDB = new ConexionDB();
    int xMouse,yMouse;

    public Pag5i1_Factura() {
        barraTop.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                xMouse = e.getX();
                yMouse = e.getY();
            }
        });
        barraTop.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                frameFacturaCompra.setLocation(x - xMouse,y - yMouse);
            }
        });
        GENERARFACTURAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    while (!file.createNewFile()){
                        iFactura++;
                        filePath = String.format("%s%d%s", "facturas/factura", iFactura, ".pdf");
                        file = new File(filePath);
                    }

                    Document document = new Document();
                    PdfWriter.getInstance(document, new FileOutputStream(filePath));
                    document.open();
                    Font tituloFont = new Font(Font.FontFamily.COURIER, 26, Font.BOLD);

                    // Logo CicloShop
                    Image imagen = Image.getInstance("imagenes/logo(Pequenio).jpg");
                    imagen.setAlignment(Element.ALIGN_CENTER);
                    imagen.setBorderColor(BaseColor.BLACK);
                    document.add(imagen);

                    // Datos CicloShop
                    Paragraph datosCicloShop = new Paragraph();
                    datosCicloShop.add(new Paragraph("Telefono: 0969037943"));
                    datosCicloShop.add(new Paragraph("Email: cicloShop@mail.com"));
                    document.add(datosCicloShop);
                    document.add(Chunk.NEWLINE);

                    insertarLinea(document); // Linea Separadora
                    document.add(Chunk.NEWLINE);

                    //Datos Personales
                    datosPersonalesTabla(document, idFactura);
                    document.add(Chunk.NEWLINE);

                    insertarLinea(document);
                    document.add(Chunk.NEWLINE);

                    //Datos de Productos
                    tablaProductos(document, idFactura);
                    document.add(Chunk.NEWLINE);
                    document.add(Chunk.NEWLINE);

                    //Total Factura
                    tablaTotal(document);

                    document.close();
                    precioTotal=0;
                    Desktop.getDesktop().open(new java.io.File(filePath));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });
        cerrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameFacturaCompra.dispose();
            }
        });
    }
    private static void datosPersonalesTabla(Document document, int idFactura) throws DocumentException {
        try {
            Connection connection = conexionDB.ConexionLocal();
            PreparedStatement ps = connection.prepareStatement("SELECT nombre, fecha_v, telefono, correo \n" +
                    "FROM VENTAS \n" +
                    "JOIN CLIENTES ON VENTAS.id_cliente = CLIENTES.id_cliente\n" +
                    "WHERE id_v=?");
            ps.setInt(1,idFactura);
            ResultSet rs = ps.executeQuery();
            rs.next();
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell[] cells = {
                    new PdfPCell(new Phrase("Factura a:")),
                    new PdfPCell(new Phrase(rs.getString(1))),
                    new PdfPCell(new Phrase("")),
                    new PdfPCell(new Phrase("")),
                    new PdfPCell(new Phrase("Factura N°:")),
                    new PdfPCell(new Phrase(String.valueOf(idFactura))),
                    new PdfPCell(new Phrase("")),
                    new PdfPCell(new Phrase("")),
                    new PdfPCell(new Phrase("Fecha Factura:")),
                    new PdfPCell(new Phrase(rs.getString(2))),
                    new PdfPCell(new Phrase("")),
                    new PdfPCell(new Phrase("")),
                    new PdfPCell(new Phrase("Teléfono:")),
                    new PdfPCell(new Phrase(rs.getString(3))),
                    new PdfPCell(new Phrase("")),
                    new PdfPCell(new Phrase("")),
                    new PdfPCell(new Phrase(rs.getString(4))),
                    new PdfPCell(new Phrase("")),
                    new PdfPCell(new Phrase("")),
                    new PdfPCell(new Phrase(""))
            };
            //Quitar los bordes de cada celda en la tabla
            for (PdfPCell cell : cells) {
                cell.setBorderWidth(0);
                table.addCell(cell);
            }

            document.add(table);

        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex.getMessage(),"ERROR SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void tablaProductos(Document document, int idFactura) throws DocumentException {
        float [] columnWidth = {245f, 85f, 85f ,85f};
        PdfPTable table = new PdfPTable(columnWidth);
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell[] headerCell = {
                new PdfPCell(new Phrase("Producto:")),
                new PdfPCell(new Phrase("Cantidad")),
                new PdfPCell(new Phrase("Precio")),
                new PdfPCell(new Phrase("Subtotal")),
        };
        for (PdfPCell cell : headerCell) {
            cell.setBorderWidth(0);
            table.addCell(cell);
        }
        try {
            Connection connection = conexionDB.ConexionLocal();
            PreparedStatement ps = connection.prepareStatement("\n" +
                    "SELECT nombre, cantidad_p, precio, cantidad_p*precio as subtotal\n" +
                    "FROM productosDetallados\n" +
                    "WHERE id_v=?");
            ps.setInt(1, idFactura);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                precioTotal = precioTotal + rs.getDouble(4);
                PdfPCell[] cells = {
                        new PdfPCell(new Phrase(rs.getString(1))),
                        new PdfPCell(new Phrase(String.valueOf(rs.getInt(2)))),
                        new PdfPCell(new Phrase(String.format("%.2f",rs.getDouble(3)))),
                        new PdfPCell(new Phrase(String.format("%.2f",rs.getDouble(4)))),
                };
                for (PdfPCell cell : cells) {
                    cell.setBorderWidth(0);
                    table.addCell(cell);
                }
            }
            document.add(table);
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex.getMessage(),"ERROR SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void tablaTotal(Document document) throws DocumentException {
        double iva = precioTotal*0.15;
        double totalIva=precioTotal+iva;
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell[] cells = {
                new PdfPCell(new Phrase("")),
                new PdfPCell(new Phrase("")),
                new PdfPCell(new Phrase("Subtotal sin IVA")),
                new PdfPCell(new Phrase(String.valueOf(precioTotal))),
                new PdfPCell(new Phrase("")),
                new PdfPCell(new Phrase("")),
                new PdfPCell(new Phrase("IVA 15%")),
                new PdfPCell(new Phrase(String.format("%.2f",iva))),
                new PdfPCell(new Phrase("")),
                new PdfPCell(new Phrase("")),
                new PdfPCell(new Phrase("TOTAL")),
                new PdfPCell(new Phrase(String.format("%.2f",totalIva)))
        };

        for (PdfPCell cell : cells) {
            cell.setBorderWidth(0);
            table.addCell(cell);
        }
        document.add(table);
    }

    public void insertarLinea (Document document) throws DocumentException {
        DottedLineSeparator dottedline = new DottedLineSeparator();
        dottedline.setOffset(-2);
        dottedline.setGap(2f);
        document.add(dottedline);
    }
    public void obtenerFactura ( int idInFactura){
        try {
            idFactura = idInFactura;
            modelo = new DefaultTableModel();
            table1.setModel(modelo);
            table1.setDefaultEditor(Object.class, null);
            table1.getTableHeader().setReorderingAllowed(false);

            Connection connection = conexionDB.ConexionLocal();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT PRODUCTOS.nombre AS nombre_producto, PRODUCTOS.precio, cantidad_p, PRODUCTOS.precio * cantidad_p AS SUBTOTAL\n" +
                    "FROM DETALLES\n" +
                    "JOIN PRODUCTOS ON DETALLES.id_p = PRODUCTOS.id_p\n" +
                    "WHERE id_v=?\n" +
                    "order by nombre_producto;");
            preparedStatement.setInt(1, idFactura);
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();

            int cantidadColumnas = resultSetMetaData.getColumnCount();

            modelo.addColumn("Nombre del Producto");
            modelo.addColumn("Precio Unitario");
            modelo.addColumn("Cantidad");
            modelo.addColumn("Subtotal");
            while (rs.next()) {
                Object[] filas = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    filas[i] = rs.getObject(i + 1);
                }
                modelo.addRow(filas);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "SQL ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException ignored) {
        }
    }
}

