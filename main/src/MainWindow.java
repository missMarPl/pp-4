/**
 * Created by Весёлый студент on 20.05.2015.
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MainWindow extends JFrame {

    private PicturePanel mPicture;
    private JList mList;
    private JTextArea mTextArea;
    private JTextField mSearchField;
    private JScrollPane mScrollerPane;
    Finder f = new Finder("info");

    public void showInfo(String country)
    {
        mTextArea.setText(null);
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream("info/" + country + "/info.txt"), StandardCharsets.UTF_8));
            boolean b = true;
            String line;
            while ((line = reader.readLine()) != null)
            {
                if (b)
                    b = false;
                else
                    mTextArea.append("\n");
                mTextArea.append(line);
            }
        }
        catch (IOException exp)
        {
            exp.printStackTrace();
        }
        finally
        {
            try
            {
                if (reader != null)
                    reader.close();
            }
            catch (IOException exp)
            {
                exp.printStackTrace();
            }
        }
    }

    public MainWindow() {
        setTitle("Справочник по странам");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());

        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BorderLayout());

        //здесь должен быть метод, который получает названия стран по названию файлов в директории
        // String[] data = {"example1", "example2", "example3"};

        String[] data = f.makeCountryList();

        mList = new JList(data); //data has type Object[]
        mList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mList.setLayoutOrientation(JList.VERTICAL_WRAP);
        mList.setVisibleRowCount(-1);

        JScrollPane listScroller = new JScrollPane(mList);
        listScroller.setPreferredSize(new Dimension(300, 80));

        mList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                if (evt.getValueIsAdjusting())
                    return;

                mPicture.setImageFile(new java.io.File("info/" + mList.getSelectedValue().toString() + "/flag.bmp"));
                showInfo(mList.getSelectedValue().toString());

            }
        });

        mSearchField = new JTextField();
        mSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchCountries();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchCountries();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchCountries();
            }

            private void searchCountries() {
                //здесь обновляется список стран в листе
                String[] findText = f.makeCountryList(mSearchField.getText());
                mList.removeAll();
                mList.setListData(findText);
            }
        });

        listPanel.add(BorderLayout.CENTER, listScroller);
        listPanel.add(BorderLayout.SOUTH, mSearchField);

        EmptyBorder border = new EmptyBorder(5,10,5,10);
        listPanel.setBorder(border);
        descriptionPanel.setBorder(border);

        mTextArea = new JTextArea();
        mTextArea.setColumns(20);
        mTextArea.setLineWrap(true);
        mTextArea.setRows(5);
        mTextArea.setWrapStyleWord(true);

        mScrollerPane = new JScrollPane(mTextArea);
        mScrollerPane.setPreferredSize(new Dimension(450, 260));
        showInfo(data[0]);

        mPicture = new PicturePanel();
        mPicture.setLayout(new java.awt.BorderLayout());
        mPicture.setPreferredSize(new Dimension(200, 200));
        mPicture.setImageFile(new java.io.File("info/" + data[0] + "/flag.bmp"));

        descriptionPanel.add(BorderLayout.NORTH, mPicture);
        descriptionPanel.add(BorderLayout.SOUTH, mScrollerPane);

        getContentPane().add(BorderLayout.WEST, listPanel);
        getContentPane().add(BorderLayout.EAST, descriptionPanel);

        setResizable(false);
        setSize(800, 500);
        setVisible(true);
    }

    public static void main(String[] args) {
        MainWindow w = new MainWindow();
    }
}