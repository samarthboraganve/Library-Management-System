package service;

import exception.BookAlreadyIssuedException;
import exception.BookNotFoundException;
import exception.BookNotIssuedException;
import exception.DuplicateBookException;
import model.Book;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class LibraryService {
    private final Map<String, Book> bookIndex = new TreeMap<>();

    public void addBook(Book book) throws DuplicateBookException {
        if (bookIndex.containsKey(book.getId())) {
            throw new DuplicateBookException("A book with ID " + book.getId() + " already exists.");
        }
        bookIndex.put(book.getId(), book);
        System.out.println("  ✓ Book added: " + book.getTitle());
    }

    public void viewAllBooks() {
        displayBooks(new ArrayList<>(bookIndex.values()), "All Books");
    }

    public Book searchById(String id) throws BookNotFoundException {
        Book book = bookIndex.get(id);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + id + " was not found.");
        }
        return book;
    }

    public List<Book> searchByTitle(String title) {
        String normalized = title.toLowerCase();
        return bookIndex.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(normalized))
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toList());
    }

    public void issueBook(String id, String borrowerName) throws BookNotFoundException, BookAlreadyIssuedException {
        Book book = searchById(id);
        if (book.isIssued()) {
            throw new BookAlreadyIssuedException("Book " + id + " is already issued to " + book.getBorrowerName() + ".");
        }
        book.issue(borrowerName);
        System.out.println("  ✓ Book issued successfully.");
    }

    public void returnBook(String id) throws BookNotFoundException, BookNotIssuedException {
        Book book = searchById(id);
        if (!book.isIssued()) {
            throw new BookNotIssuedException("Book " + id + " is not currently issued.");
        }
        book.returned();
        System.out.println("  ✓ Book returned successfully.");
    }

    public List<Book> filterByAuthor(String author) {
        String normalized = author.toLowerCase();
        return bookIndex.values().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(normalized))
                .sorted(Comparator.comparing(Book::getAuthor))
                .collect(Collectors.toList());
    }

    public List<Book> filterByCategory(String category) {
        String normalized = category.toLowerCase();
        return bookIndex.values().stream()
                .filter(book -> book.getCategory().toLowerCase().contains(normalized))
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toList());
    }

    public int getTotalBooks() {
        return bookIndex.size();
    }

    public int getAvailableCount() {
        return (int) bookIndex.values().stream().filter(book -> !book.isIssued()).count();
    }

    public int getIssuedCount() {
        return (int) bookIndex.values().stream().filter(Book::isIssued).count();
    }

    public void displayBooks(List<Book> books, String caption) {
        if (books == null || books.isEmpty()) {
            System.out.println("  No books found for " + caption + ".");
            return;
        }

        System.out.println("\n  " + caption + ":");
        System.out.println("  ---------------------------------------------------------------");
        System.out.printf("  %-6s | %-25s | %-18s | %-12s | %-8s\n", "ID", "Title", "Author", "Category", "Status");
        System.out.println("  ---------------------------------------------------------------");
        for (Book book : books) {
            String status = book.isIssued() ? "Issued" : "Available";
            System.out.printf("  %-6s | %-25s | %-18s | %-12s | %-8s\n",
                    book.getId(), abbreviate(book.getTitle(), 25), abbreviate(book.getAuthor(), 18), abbreviate(book.getCategory(), 12), status);
        }
        System.out.println("  ---------------------------------------------------------------");
    }

    private String abbreviate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
}
