package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Model {

    private final ObservableList<Book> booksCollection = FXCollections.observableArrayList();
    private BufferedReader readBooksFromFile;
    private BufferedReader input;
    private PrintStream writeBooksToFile;
    private File booksFile;
    private Book book;
    private Stage stage;
    private String stageTitle = "Ratusca's Totally Awesome Books Library";
    private int nrOfBooksFromFile = 0;


    public void addBook(Book b) {
        booksCollection.add(b);
    }

    // add book method
    @SuppressWarnings("Convert2Lambda")
    public void addBook(Book b, TextField txtBookAuthor, TextField txtBookTitle, TextField txtBookGenre, Label lblAddMessage, String emptyFields, TextArea txtBooksDisplay) {
        String bookName = txtBookAuthor.getText();
        String bookTitle = txtBookTitle.getText();
        String bookGenre = txtBookGenre.getText();

        if (isTextFieldEmpty(txtBookAuthor) || isTextFieldEmpty(txtBookTitle) || isTextFieldEmpty(txtBookGenre)) {
            lblAddMessage.setTextFill(Color.RED);
            lblAddMessage.setText(emptyFields);
        } else {

            //add book to collection
            book = new Book(bookName, bookTitle, bookGenre);
            if (!isBookPresentInLibrary(book)) {
                lblAddMessage.setText(book.toString() + " added to library.");
                booksCollection.add(book);
            } else {
                lblAddMessage.setText(book.toString() + " is already in library.");
            }

            // clears the fields after adding a book
            txtBookAuthor.setText("");
            txtBookTitle.setText("");
            txtBookGenre.setText("");
            txtBookAuthor.requestFocus();
        }
    }

    public String getLabelText(Label label) {

        return label.getText();
    }

    public int getNumberOfBooks() {

        return booksCollection.size();
    }

    public int getNumberOfBooksFromFile() {

        return nrOfBooksFromFile;
    }

    public boolean isTextFieldEmpty(TextField txtField) {

        return txtField.getText().isEmpty();
    }

    public boolean isBookPresentInLibrary(Book c) {

        for (Book b : booksCollection) {
            if (b.toString().toLowerCase().equalsIgnoreCase(c.toString())) {
                return true;
            }
        }
        return false;
    }

    public void showLibrary(TextArea txtBooksDisplay, Label lblInfo) {
        sortLibrary();
        txtBooksDisplay.clear(); // clears the text area to display the updated list
        int bookIndex = 1;
        if (booksCollection.isEmpty()) {
            txtBooksDisplay.setText("No books to be displayed");
        } else {
            txtBooksDisplay.clear();
            for (Book b : booksCollection) {
                txtBooksDisplay.appendText(bookIndex + ". " + b.toString() + "\n");
                bookIndex++;
            }
        }
        lblInfo.setText("There are " + getNumberOfBooks() + " books in your library.");

    }

    public void searchLibrary(TextArea txtSearchDisplay, TextField txtSearch, Label lblInfo) {
        txtSearchDisplay.clear(); // clears the text area to display new results
        String search = txtSearch.getText().toLowerCase();
        int numberOfBooksFound = 0;

        if (search.length() >= 3) {
            for (Book b : booksCollection) {
                if (b.toString().toLowerCase().contains(search)) {
                    lblInfo.setText("Something that you might've searched for was found.");
                    txtSearchDisplay.appendText(b.toString() + "\n");
                    numberOfBooksFound++;
                } else if (numberOfBooksFound <= 0) {
                    lblInfo.setText("Nothing was found");
                }
            }
        } else if (search.length() == 0) {
            lblInfo.setText("You will have to type something in the 'Search' field");
        } else if (search.length() >= 0 && search.length() < 3) {
            lblInfo.setText("For a more precise result please enter a word with a length bigger than 3 letters");
        }
        txtSearch.clear(); // clears search  field
        txtSearch.requestFocus(); // sets focus to search field 
    }

    // imports any existing library
    @SuppressWarnings("CallToPrintStackTrace")
    public void importLibrary(String fileLocation, Label lblInfo) {


        booksCollection.clear();

        // location of file to be imported
        booksFile = new File(fileLocation);
        boolean filePresent = true;

        // create a file if one doesnt exist
        if (!booksFile.exists()) {
            filePresent = false;
            try {
                booksFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // reads file data and stores it in list
        try {
            readBooksFromFile = new BufferedReader(new InputStreamReader(new FileInputStream(booksFile)));

            @SuppressWarnings("UnusedAssignment")
            String line = "";

            while ((line = readBooksFromFile.readLine()) != null) {
                String[] s = line.split(" - ");
                String bookAuthor = s[0];
                String bookTitle = s[1];
                String bookGenre = s[2];
                book = new Book(bookAuthor, bookTitle, bookGenre);
                addBook(book);
                nrOfBooksFromFile++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (filePresent == true) {
            lblInfo.setText("A number of " + getNumberOfBooksFromFile() + " books have been added to your library.");
        } else {
            lblInfo.setText("No local library was found, so we will create one for you instead. You can find it on your Desktop with name books.txt");
        }
    }

    // sort library alphabetically
    @SuppressWarnings("Convert2Lambda")
    public void sortLibrary() {
        Collections.sort(booksCollection, new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {

                return o1.toString().compareToIgnoreCase(o2.toString());
            }
        });
    }

    // export context of list to file
    @SuppressWarnings("CallToPrintStackTrace")
    public void exportLibrary() {
        booksFile.delete();
        try {
            booksFile.createNewFile();
            writeBooksToFile = new PrintStream(booksFile);
            for (Book b : booksCollection) {
                writeBooksToFile.println(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // checks if number of books in file equals number of books in list
    public boolean isLibraryUpToDate() {

        int amountOfBooksInCollection = getNumberOfBooks();
        int amountOfBooksInFile = 0;
        try {
            readBooksFromFile = new BufferedReader(new InputStreamReader(new FileInputStream(booksFile)));
            while (readBooksFromFile.readLine() != null) {
                amountOfBooksInFile++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return amountOfBooksInCollection == amountOfBooksInFile;
    }

    // prompt if isLibraryUpToDate() == false
    public void exitLibrary() {

        if (isLibraryUpToDate()) {
            Platform.exit();
        } else {
            closeDialog();
            ;
        }
    }

    public String getStageTitle() {

        return stageTitle;
    }

    public void closeDialog() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("You have not exported your library and anything you added will be lost.");
        alert.setContentText("You sure you want to exit ?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    public void deleteBook() {

        // choice dropdown
        ChoiceDialog<Book> dialog = new ChoiceDialog("", booksCollection);
        dialog.setTitle("Delete prompt");
        dialog.setContentText("Choose books to delete");
        Optional<Book> result = dialog.showAndWait();

        if (result.isPresent()) {
            // alert to verify delete
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Book " + result.get() + " will be deleted.");
            alert.setContentText("You sure you want to purge it ?");
            Optional<ButtonType> result2 = alert.showAndWait();
            if (result2.get() == ButtonType.OK) {
                booksCollection.remove(result.get());
            }
        }
    }
}
