import exception.BookAlreadyIssuedException;
import exception.BookNotFoundException;
import exception.BookNotIssuedException;
import exception.DuplicateBookException;
import model.Book;
import service.LibraryService;
 
import java.util.List;
import java.util.Scanner;
 
public class LM {
 
    private static final LibraryService library = new LibraryService();
    private static final Scanner scanner = new Scanner(System.in);
 
    public static void main(String[] args) {
 
        // Pre-load sample books
        seedSampleData();
 
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║      LIBRARY MANAGEMENT SYSTEM           ║");
        System.out.println("╚══════════════════════════════════════════╝");
 
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ", 0, 7);
 
            switch (choice) {
                case 1  -> addBook();
                case 2  -> viewAllBooks();
                case 3  -> searchBook();
                case 4  -> issueBook();
                case 5  -> returnBook();
                case 6  -> filterBooks();
                case 7  -> showStats();
                case 0  -> { running = false; System.out.println("\n  Goodbye! Thank you for using the Library System.\n"); }
            }
        }
        scanner.close();
    }
 
    // ─────────────────────────────────────────────
    //  MENU
    // ─────────────────────────────────────────────
    private static void printMenu() {
        System.out.println("\n┌─────────────────────────────────┐");
        System.out.println("│            MAIN MENU            │");
        System.out.println("├─────────────────────────────────┤");
        System.out.println("│  1. Add a Book                  │");
        System.out.println("│  2. View All Books              │");
        System.out.println("│  3. Search a Book               │");
        System.out.println("│  4. Issue a Book                │");
        System.out.println("│  5. Return a Book               │");
        System.out.println("│  6. Filter Books                │");
        System.out.println("│  7. Library Statistics          │");
        System.out.println("│  0. Exit                        │");
        System.out.println("└─────────────────────────────────┘");
    }
 
    // ─────────────────────────────────────────────
    //  1. ADD BOOK
    // ─────────────────────────────────────────────
    private static void addBook() {
        System.out.println("\n  ── Add New Book ──");
        try {
            String id       = readNonEmpty("  Enter Book ID (e.g. B010): ");
            String title    = readNonEmpty("  Enter Title: ");
            String author   = readNonEmpty("  Enter Author: ");
            String category = readNonEmpty("  Enter Category (e.g. Fiction, Science, Technology): ");
 
            Book book = new Book(id, title, author, category);
            library.addBook(book);
 
        } catch (DuplicateBookException e) {
            System.out.println("  X Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("  X Validation Error: " + e.getMessage());
        }
    }
 
// ───────────────────────────────────────────── 
    //  2. VIEW ALL BOOKS
// ─────────────────────────────────────────────
    private static void viewAllBooks() {
        System.out.println("\n  ── All Books ──");
        library.viewAllBooks();
    }
 
    // ─────────────────────────────────────────────
    //  3. SEARCH BOOK
    // ─────────────────────────────────────────────
    private static void searchBook() {
        System.out.println("\n  ── Search Book ──");
        System.out.println("  1. Search by ID");
        System.out.println("  2. Search by Title");
        int choice = readInt("  Choose: ", 1, 2);
 
        try {
            if (choice == 1) {
                String id = readNonEmpty("  Enter Book ID: ");
                Book book = library.searchById(id);
                library.displayBooks(List.of(book), "ID = " + id);
 
            } else if (choice == 2) {
                String title = readNonEmpty("  Enter Title (partial match): ");
                List<Book> results = library.searchByTitle(title);
                library.displayBooks(results, "Title contains '" + title + "'");
 
            } else {
                System.out.println("  X Invalid option.");
            }
        } catch (BookNotFoundException e) {
            System.out.println("  X " + e.getMessage());
        }
    }
 
    // ─────────────────────────────────────────────
    //  4. ISSUE BOOK
    // ─────────────────────────────────────────────
    private static void issueBook() {
        System.out.println("\n  ── Issue Book ──");
        try {
            String id           = readNonEmpty("  Enter Book ID to issue: ");
            String borrowerName = readNonEmpty("  Enter Borrower Name: ");
            library.issueBook(id, borrowerName);
 
        } catch (BookNotFoundException e) {
            System.out.println("  X " + e.getMessage());
        } catch (BookAlreadyIssuedException e) {
            System.out.println("  X " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("  X Validation Error: " + e.getMessage());
        }
    }
 
    // ─────────────────────────────────────────────
    //  5. RETURN BOOK
    // ─────────────────────────────────────────────
    private static void returnBook() {
        System.out.println("\n  ── Return Book ──");
        try {
            String id = readNonEmpty("  Enter Book ID to return: ");
            library.returnBook(id);
 
        } catch (BookNotFoundException e) {
            System.out.println("  X " + e.getMessage());
        } catch (BookNotIssuedException e) {
            System.out.println("  X " + e.getMessage());
        }
    }
 
    // ─────────────────────────────────────────────
    //  6. FILTER BOOKS
    // ─────────────────────────────────────────────
    private static void filterBooks() {
        System.out.println("\n  ── Filter Books ──");
        System.out.println("  1. Filter by Author");
        System.out.println("  2. Filter by Category");
        int choice = readInt("  Choose: ", 1, 2);
 
        if (choice == 1) {
            String author = readNonEmpty("  Enter Author name (partial match): ");
            List<Book> results = library.filterByAuthor(author);
            library.displayBooks(results, "Author contains '" + author + "'");
 
        } else if (choice == 2) {
            String category = readNonEmpty("  Enter Category: ");
            List<Book> results = library.filterByCategory(category);
            library.displayBooks(results, "Category = '" + category + "'");
 
        } else {
            System.out.println("  X Invalid option.");
        }
    }
 
    // ─────────────────────────────────────────────
    //  7. STATISTICS
    // ─────────────────────────────────────────────
    private static void showStats() {
        System.out.println("\n  ── Library Statistics ──");
        System.out.println("  Total Books   : " + library.getTotalBooks());
        System.out.println("  Available     : " + library.getAvailableCount());
        System.out.println("  Currently Out : " + library.getIssuedCount());
    }
 
    // ─────────────────────────────────────────────
    //  SEED DATA
    // ─────────────────────────────────────────────
    private static void seedSampleData() {
        System.out.println("\n  Loading sample books...");
        try {
            library.addBook(new Book("B001", "The Great Gatsby",      "F. Scott Fitzgerald", "Fiction"));
            library.addBook(new Book("B002", "A Brief History of Time","Stephen Hawking",      "Science"));
            library.addBook(new Book("B003", "Sapiens",               "Yuval Noah Harari",   "History"));
            library.addBook(new Book("B004", "Clean Code",            "Robert C. Martin",    "Technology"));
            library.addBook(new Book("B005", "Atomic Habits",         "James Clear",         "Self-help"));
            library.addBook(new Book("B006", "Wings of Fire",         "A.P.J. Abdul Kalam",  "Biography"));
            library.addBook(new Book("B007", "The Lean Startup",      "Eric Ries",           "Non-fiction"));
 
            
            library.issueBook("B003", "Arjun Mehta");
 
        } catch (Exception e) {
            System.out.println("  Seed error: " + e.getMessage());
        }
    }
 
    // ─────────────────────────────────────────────
    //  INPUT HELPERS
    // ─────────────────────────────────────────────
    private static String readNonEmpty(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) System.out.println("  X Invalid input. Please enter a valid input.");
        } while (input.isEmpty());
        return input;
    }
 
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String line = scanner.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("  X Invalid input. Please enter a valid number.");
            }
        }
    }
 
    private static int readInt(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value < min || value > max) {
                System.out.printf("  X Invalid input. Please enter a number between %d and %d.%n", min, max);
                continue;
            }
            return value;
        }
    }
}