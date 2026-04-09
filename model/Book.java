package model;

public class Book {
    private final String id;
    private final String title;
    private final String author;
    private final String category;
    private boolean issued;
    private String borrowerName;

    public Book(String id, String title, String author, String category) {
        if (id == null || id.isBlank() || title == null || title.isBlank() || author == null || author.isBlank() || category == null || category.isBlank()) {
            throw new IllegalArgumentException("Book id, title, author, and category must not be empty.");
        }
        this.id = id.trim();
        this.title = title.trim();
        this.author = author.trim();
        this.category = category.trim();
        this.issued = false;
        this.borrowerName = "";
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public boolean isIssued() {
        return issued;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void issue(String borrowerName) {
        if (borrowerName == null || borrowerName.isBlank()) {
            throw new IllegalArgumentException("Borrower name must not be empty.");
        }
        this.issued = true;
        this.borrowerName = borrowerName.trim();
    }

    public void returned() {
        this.issued = false;
        this.borrowerName = "";
    }
}


