package sample;

public class Book {

    private String bookName;
    private String bookTitle;
    private String bookGenre;

    public Book(String bookName, String bookTitle, String bookGenre) {
        this.bookName = bookName;
        this.bookTitle = bookTitle;
        this.bookGenre = bookGenre.toUpperCase();
    }

    public Book() {

    }

    /**
     * @return the bookName
     */
    public String getBookName() {

        return bookName;
    }

    /**
     * @param bookName the bookName to set
     */
    public void setBookName(String bookName) {

        this.bookName = bookName;
    }

    /**
     * @return the bookTitle
     */
    public String getBookTitle() {

        return bookTitle;
    }

    /**
     * @param bookTitle the bookTitle to set
     */
    public void setBookTitle(String bookTitle) {

        this.bookTitle = bookTitle;
    }

    /**
     * @return the bookGenre
     */
    public String getBookGenre() {

        return bookGenre;
    }

    /**
     * @param bookGenre the bookGenre to set
     */
    public void setBookGenre(String bookGenre) {

        this.bookGenre = bookGenre;
    }

    @Override
    public String toString() {

        return getBookName() + " - " + getBookTitle() + " - " + getBookGenre();
    }

}
