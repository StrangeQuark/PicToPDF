import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;

public class PicToPDF extends JFrame
{
    JFileChooser fileChooser = new JFileChooser();
    JFileChooser fileSaver = new JFileChooser();
    File selectedFile;
    int fileCount;
    String path = "";
    String pdfName = "";
    JButton okButton = new JButton("OK");
    JButton cwButton = new JButton("Rotate CW");
    JButton ccwButton = new JButton("Rotate CCW");
    JButton fullRotateButton = new JButton("Rotate 180 degrees");
    ImageIcon icon;
    Object[] options = {ccwButton, fullRotateButton, cwButton, okButton};
    JOptionPane optionPane;
    Image image;
    int i = 1;

    public PicToPDF()
    {
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator")+ "Desktop"));
        fileSaver.setCurrentDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator")+ "Desktop"));

        
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnVal = fileChooser.showOpenDialog(fileChooser);

        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            selectedFile = fileChooser.getSelectedFile();
            path = fileChooser.getSelectedFile().getPath();
            fileCount = (new File(path).list().length);
        }

        if (selectedFile == null) {
            JOptionPane.showMessageDialog(null, "There was no directory selected!","ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        returnVal = fileSaver.showSaveDialog(fileSaver);

        selectedFile = fileSaver.getSelectedFile();
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(null, "There was no destination file selected!","ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            pdfName = fileSaver.getSelectedFile() + ".pdf";
            recursivePicToPDF(pdfName, path, fileCount);
        }

        JOptionPane.showMessageDialog(null, "Files successfully converted to pdf","Success", JOptionPane.PLAIN_MESSAGE);

        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    public void recursivePicToPDF(String pdfName, String path, int fileCount)
    {
        try
        {
            String destination = pdfName;
            PdfWriter writer = new PdfWriter(destination);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            File folder = new File(path);
            for(File fileEntry : folder.listFiles())
            {
                try
                {
                    String imageFile = fileEntry.getPath();
                    ImageData data = ImageDataFactory.create(imageFile);
                    image = new Image(data);

                    icon = scaleImage(new ImageIcon(fileEntry.getPath()), 1200, 1200);

                    okButton.addMouseListener(new MouseAdapter() {

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            System.out.println("OK was clicked");
                            optionPane.getRootFrame().dispose();
                        }
                    });
                    cwButton.addMouseListener(new MouseAdapter() {

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            System.out.println("cw was clicked");
                            image.setRotationAngle(-Math.PI / 2);

                            optionPane.getRootFrame().dispose();
                        }
                    });
                    ccwButton.addMouseListener(new MouseAdapter() {

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            System.out.println("ccw was clicked");
                            image.setRotationAngle(Math.PI / 2);

                            optionPane.getRootFrame().dispose();
                        }
                    });
                    fullRotateButton.addMouseListener(new MouseAdapter() {

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            System.out.println("full rotate was clicked");
                            image.setRotationAngle(Math.PI);

                            optionPane.getRootFrame().dispose();
                        }
                    });

                    optionPane.showOptionDialog(null, "", "Rotate?",JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, icon, options, options[0]);

                    image.setAutoScale(true);
                    document.add(image);
                }
                catch(Exception e)
                {
                    JOptionPane.showMessageDialog(null, "One or more of your files is not an image","ERROR", JOptionPane.ERROR_MESSAGE);
                    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                }

                if(i != fileCount)
                    document.add(new AreaBreak());
                i++;
            }
            document.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public ImageIcon scaleImage(ImageIcon icon, int w, int h)
    {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        if(icon.getIconWidth() > w)
        {
            nw = w;
            nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        if(nh > h)
        {
            nh = h;
            nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }

        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, java.awt.Image.SCALE_DEFAULT));
    }

    public static void main(String[] args)
    {
        PicToPDF gui = new PicToPDF();
    }
}
