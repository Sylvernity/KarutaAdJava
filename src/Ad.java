// Imports packages to read from and write to a file, store user input, and create an expanding array
import java.awt.event.ActionEvent;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


public class Ad{
    private static List<Card> cards;

    public static void main(String[] args)
    {
        startGUI();
    }



    // ****************
    // File Read Method
    // ****************

    public static List<Card> read(String fileName) throws IOException{
        File file = new File(fileName);

        if(!file.exists())
            file.createNewFile();

        BufferedReader fr = new BufferedReader(new FileReader(fileName));
        BufferedReader count = new BufferedReader(new FileReader(fileName));

        int lines = 0;

        while (count.readLine() != null)
            lines ++;

        List<Card> cards = new ArrayList<Card>();

        for(int i = 0; i < lines; i ++){
            String line = fr.readLine();
            int pipe = line.indexOf(" | ");
            Card temp = new Card(line.substring(0, pipe));

            line = line.substring(pipe + 7);
            pipe = line.indexOf(" | ");
            temp.setWishList(line.substring(0, pipe));

            line = line.substring(pipe + 10);
            pipe = line.indexOf(" | ");
            temp.setAnime(line.substring(0, pipe));

            line = line.substring(pipe + 3);
            temp.setCardID(line);
            cards.add(temp);
        }
        return cards;
    }

/*

    // Displays the state of a card object specified by card name

    // Creates an ad consisting of all existing cards

    // Displays all commands
*/
    // Saves the ad to the file earlier specified
    public static void save(List<Card> cards) throws IOException{
        FileWriter fw = new FileWriter(FileAction.getFilePath());

        for (Card temp: cards) {
            fw.write(temp.toString() + "\n");
        }

        fw.close();
    }

    // *********
    // GUI
    // *********

    public static void startGUI(){
        JFrame frame = new JFrame("Ad Creator");

        MyTable table = new MyTable(createTable(FileAction.getFilePath()));

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();

        // File Menu Creation

        // File Header
        JMenu file = new JMenu("File");
        menuBar.add(file);

        // File Items
        JMenuItem open = new JMenuItem("Open");
        file.add(open);
        open.setAction(new FileAction("Open File", table));
        open.setToolTipText("Load a file to the Ad Creator");

        JMenuItem save = new JMenuItem("Save");
        file.add(save);
        save.setAction(new SaveAction("Save File", table));
        save.setToolTipText("Save the current ad");

        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);
        exit.setAction(new ExitAction("Exit File", table));
        exit.setToolTipText("Exit the current ad");

        // Card Menu Creation

        // Card Header
        JMenu card = new JMenu("Card");
        menuBar.add(card);

        // Card Items
        JMenuItem create = new JMenuItem();
        card.add(create);
        create.setAction(new CreateAction("Create Card", table));
        create.setToolTipText("Create a new card");

        JMenuItem modify = new JMenuItem();
        card.add(modify);
        modify.setAction(new ModifyAction("Modify Card", table));
        modify.setToolTipText("Modify the selected card");

        JMenuItem delete = new JMenuItem("Delete Card");
        card.add(delete);
        delete.setAction(new DeleteAction("Delete Card", table));
        delete.setToolTipText("Delete a card using the card ID");

        JMenuItem deleteAll = new JMenuItem("Delete All");
        card.add(deleteAll);
        deleteAll.setAction(new DeleteAllAction("Delete All", table));
        deleteAll.setToolTipText("Delete all cards");

        // Ad Menu Creation
        JMenu ad = new JMenu("Ad");
        menuBar.add(ad);

        // Ad Items

        JMenuItem viewAd = new JMenuItem("Preview Ad");
        ad.add(viewAd);
        viewAd.setAction(new AdViewAction("Preview Ad", table));
        viewAd.setToolTipText("Preview the Ad that will be saved");

        frame.setJMenuBar(menuBar);

        // Setting File Actions

        JScrollPane js = new JScrollPane(table);
        frame.add(js);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(600, 200, 600, 400);
        frame.setVisible(true);
    }

    public static TableModel createTable(String fileName){
        try{
            cards = read(fileName);
        }
        catch(IOException e){
            cards = new ArrayList<Card>();
        }

        String[] cNames = {"Card Name", "Anime", "Wish List", "Card ID"};

        String[][] rowData = new String[cards.size()][4];

        for (int i = 0; i < cards.size(); i++) {
            rowData[i][0] = cards.get(i).getCardName();
            rowData[i][1] = cards.get(i).getAnime();
            rowData[i][2] = cards.get(i).getWishList();
            rowData[i][3] = cards.get(i).getCardID();
        }

        return new DefaultTableModel(rowData, cNames);
    }

    public static void emptyCards(){
        cards = new ArrayList<Card>();
    }
    
}

class MyTable extends JTable{

    public MyTable(TableModel tableModel){
        super(tableModel);
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }
}

// File Action Classes

class FileAction extends AbstractAction{
    private static String filePath = "";
    private MyTable table;

    public FileAction(String text, MyTable table){
        super(text);
        this.table = table;
    }

    public void actionPerformed(ActionEvent e){
        JFrame frame = new JFrame("File Explorer");
        JFileChooser chooser = new JFileChooser();

        File dir = new File("C:\\Users\\jadac\\Desktop");
        chooser.setCurrentDirectory(dir);

        frame.add(chooser);

        filePath = chooser.getSelectedFile().getPath();
        table.setModel(Ad.createTable(filePath));
        SaveAction.setSaved(true);
    }
    
    public static String getFilePath(){
        return filePath;
    }

    public static void setFilePath(String path){
        filePath = path;
    }
}

class SaveAction extends AbstractAction{
    private MyTable table;
    private static boolean saved;

    public SaveAction(String text, MyTable table){
        super(text);
        this.table = table;
        saved = true;
    }

    public void actionPerformed(ActionEvent e){
        if(FileAction.getFilePath().equals("")){
            JFrame frame = new JFrame("File Explorer");
            JFileChooser chooser = new JFileChooser();

            File dir = new File("C:\\Users\\jadac\\Desktop");
            chooser.setCurrentDirectory(dir);

            frame.add(chooser);
            frame.setVisible(true);

            String filePath = chooser.getSelectedFile().getPath();
            FileAction.setFilePath(filePath);
        }
        try{
            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            List<Card> tempCards = new ArrayList<Card>();
            for(int i = 0; i < tableModel.getRowCount(); i ++){
                Card temp = new Card();
                temp.setCardName((String) tableModel.getValueAt(i, 0));
                temp.setAnime((String) tableModel.getValueAt(i, 1));
                temp.setWishList((String) tableModel.getValueAt(i, 2));
                temp.setCardID((String) tableModel.getValueAt(i, 3));
                tempCards.add(temp);
            }
            Ad.save(tempCards);
            saved = true;
        }
        catch(IOException f){
            System.out.println("YOUR AD CANNOT BE SAVED. PLEASE COPY YOUR WORK.");
        }
    }

    public static boolean getSaved(){
        return saved;
    }

    public static void setSaved(boolean save){
        saved = save;
    }
}

class ExitAction extends AbstractAction{
    private MyTable table;

    public ExitAction(String text, MyTable table){
        super(text);
        this.table = table;
    }

    public void actionPerformed(ActionEvent e){
        if(SaveAction.getSaved()) {
            Ad.emptyCards();
            FileAction.setFilePath("");
            table.setModel(Ad.createTable(""));
        }
        else{
           JFrame frame = new JFrame();
           JOptionPane pane = new JOptionPane("WARNING: You are exiting this dataset without saving. " +
                   "\n Choosing not to save will delete your dataset forever.");

           JButton save = new JButton(new SaveAction("Save File", table));
           save.setText("Save");
           save.addActionListener(f -> {
               frame.dispose();
           });

           JButton exit = new JButton(new DeleteAllAction("Exit File", table));
           exit.setText("Exit Dataset");
           exit.addActionListener(f -> {
               frame.dispose();
           });

           Object[] options = {save, exit};
           pane.setOptions(options);

           frame.add(pane);
           frame.setBounds(630, 230, 350, 200);
           frame.setVisible(true);
        }
    }
}

// Card Action Classes

// Creating Cards
class CreateAction extends AbstractAction{
    private JTextField[] newCardData;
    private MyTable table;

    public CreateAction(String text, MyTable table){
        super(text);
        this.table = table;
        newCardData = new JTextField[4];
    }

    public void actionPerformed(ActionEvent e){
        JFrame frame = new JFrame("Card Creator");
        JPanel panel = new JPanel();

        JLabel name = new JLabel("Card Name: ");
        panel.add(name);
        JTextField nameF = new JTextField(25);
        panel.add(nameF);
        newCardData[0] = nameF;

        JLabel anime = new JLabel("Anime: ");
        panel.add(anime);
        JTextField animeF = new JTextField(25);
        panel.add(animeF);
        newCardData[1] = animeF;

        JLabel wishList = new JLabel("Wish List: ");
        panel.add(wishList);
        JTextField wishListF = new JTextField(25);
        panel.add(wishListF);
        newCardData[2] = wishListF;

        JLabel cardID = new JLabel("Card ID: ");
        panel.add(cardID);
        JTextField cardIDF = new JTextField(25);
        panel.add(cardIDF);
        newCardData[3] = cardIDF;

        JButton add = new JButton("Create!");
        panel.add(add);
        add.setAction(new AddAction("Create!", table, newCardData));
        add.addActionListener(f -> {
            frame.dispose();
        });

        frame.add(panel);
        frame.setBounds(800, 280, 400, 400);
        frame.setVisible(true);
        SaveAction.setSaved(false);
    }

}

class AddAction extends AbstractAction{
    private MyTable table;
    private JTextField[] newCardData;

    public AddAction(String text, MyTable table, JTextField[] cards){
        super(text);
        this.table = table;
        newCardData = cards;
    }

    public void actionPerformed(ActionEvent e){
        Object[] newCard = new String[4];

        newCard[0] = newCardData[0].getText();
        newCard[1] = newCardData[1].getText();
        newCard[2] = newCardData[2].getText();
        newCard[3] = newCardData[3].getText();

        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.addRow(newCard);
        table.setModel(tableModel);
        SaveAction.setSaved(false);
    }
}

// Modifying Cards
class ModifyAction extends AbstractAction{
    private String[] oldCardData;
    private JTextField[] newCardData;
    private MyTable table;
    private int row;

    public ModifyAction(String text, MyTable newTable){
        super(text);
        table = newTable;
        newCardData = new JTextField[4];
        oldCardData = new String[table.getColumnCount()];
        row = 0;
    }

    public void actionPerformed(ActionEvent e){
        row = table.getSelectedRow();
        for(int c = 0; c < table.getColumnCount(); c++){
            oldCardData[c] = (String) table.getValueAt(row, c);
        }

        JFrame frame = new JFrame("Card Modifier");
        JPanel panel = new JPanel();

        JLabel name = new JLabel("Card Name: ");
        panel.add(name);
        JTextField nameF = new JTextField(oldCardData[0], 25);
        panel.add(nameF);
        newCardData[0] = nameF;

        JLabel anime = new JLabel("Anime: ");
        panel.add(anime);
        JTextField animeF = new JTextField(oldCardData[1], 25);
        panel.add(animeF);
        newCardData[1] = animeF;

        JLabel wishList = new JLabel("Wish List: ");
        panel.add(wishList);
        JTextField wishListF = new JTextField(oldCardData[2], 25);
        panel.add(wishListF);
        newCardData[2] = wishListF;

        JLabel cardID = new JLabel("Card ID: ");
        panel.add(cardID);
        JTextField cardIDF = new JTextField(oldCardData[3], 25);
        panel.add(cardIDF);
        newCardData[3] = cardIDF;

        JButton add = new JButton("Modify!");
        panel.add(add);
        add.setAction(new ChangeAction("Modify!", table, newCardData, row));
        add.addActionListener(f -> {
            frame.dispose();
        });

        frame.add(panel);
        frame.setBounds(800, 280, 400, 400);
        frame.setVisible(true);
    }

}

class ChangeAction extends AbstractAction{
    private MyTable table;
    private JTextField[] newCardData;
    private int row;

    public ChangeAction(String text, MyTable table, JTextField[] cards, int row){
        super(text);
        this.table = table;
        newCardData = cards;
        this.row = row;
    }

    public void actionPerformed(ActionEvent e){
        Object[] newCard = new String[4];

        newCard[0] = newCardData[0].getText();
        newCard[1] = newCardData[1].getText();
        newCard[2] = newCardData[2].getText();
        newCard[3] = newCardData[3].getText();

        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.removeRow(row);
        tableModel.insertRow(row, newCard);
        table.setModel(tableModel);
        SaveAction.setSaved(false);
    }
}

// Deleting Cards
class DeleteAction extends AbstractAction{
    private MyTable table;
    private static JTextField remove;

    public DeleteAction(String text, MyTable table){
        super(text);
        this.table = table;
        remove = new JTextField(25);
    }

    public void actionPerformed(ActionEvent e){
        JFrame frame = new JFrame("Card Creator");
        JPanel panel = new JPanel();

        JLabel cardID = new JLabel("Card ID: ");
        panel.add(cardID);
        panel.add(remove);

        JButton delete = new JButton("Delete");
        panel.add(delete);
        delete.setAction(new RemoveAction("Delete", table));
        delete.addActionListener(f -> {
            frame.dispose();
        });

        frame.add(panel);
        frame.setBounds(800, 280, 400, 400);
        frame.setVisible(true);
        SaveAction.setSaved(false);
    }

    public static JTextField getRemove(){
        return remove;
    }
}

class RemoveAction extends AbstractAction{
    private MyTable table;

    public RemoveAction(String text, MyTable table){
        super(text);
        this.table = table;
    }

    public void actionPerformed(ActionEvent e){
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Object remove = DeleteAction.getRemove().getText();

        String temp = "";

        for(int i = 0; i < table.getRowCount(); i ++){
            temp = (String) table.getValueAt(i, 3);
            if(temp.equals(remove)){
                model.removeRow(i);
            }
        }
        table.setModel(model);
    }
}

class DeleteAllAction extends AbstractAction{
    private MyTable table;

    public DeleteAllAction(String text, MyTable table){
        super(text);
        this.table = table;
    }

    public void actionPerformed(ActionEvent e){
        Ad.emptyCards();
        table.setModel(Ad.createTable(""));
        SaveAction.setSaved(false);
    }
}

// Ad Action Classes

class AdViewAction extends AbstractAction{
    private MyTable table;

    public AdViewAction(String text, MyTable table){
        super(text);
        this.table = table;
    }

    public void actionPerformed(ActionEvent e){
        JFrame frame = new JFrame("Ad Viewer");
        JTextArea area = new JTextArea(table.getRowCount(), 50);
        frame.add(area);

        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        List<Card> tempCards = new ArrayList<Card>();
        for(int i = 0; i < tableModel.getRowCount(); i ++){
            Card temp = new Card();
            temp.setCardName((String) tableModel.getValueAt(i, 0));
            temp.setAnime((String) tableModel.getValueAt(i, 1));
            temp.setWishList((String) tableModel.getValueAt(i, 2));
            temp.setCardID((String) tableModel.getValueAt(i, 3));
            tempCards.add(temp);
        }

        String text = "";

        for (Card temp: tempCards) {
            text += temp.toString() + "\n";
            System.out.println(temp.toString());
        }

        area.setText(text);

        frame.setBounds(650, 225, 300, 100);
        frame.setVisible(true);
    }
}