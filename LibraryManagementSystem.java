import java.util.*;

class Book {
    private String title;
    private String author;
    private String isbn;
    private boolean isAvailable;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isAvailable = true;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public boolean isAvailable() { return isAvailable; }

    public void borrowBook() { isAvailable = false; }
    public void returnBook() { isAvailable = true; }

    @Override
    public String toString() {
        return "Book{" + "title='" + title + "', author='" + author + "', ISBN='" + isbn + "', Available=" + isAvailable + "}";
    }
}

class Member {
    private String name;
    private int memberId;
    private List<Book> borrowedBooks;

    public Member(String name, int memberId) {
        this.name = name;
        this.memberId = memberId;
        this.borrowedBooks = new ArrayList<>();
    }

    public String getName() { return name; }
    public int getMemberId() { return memberId; }
    public List<Book> getBorrowedBooks() { return borrowedBooks; }

    public boolean borrowBook(Book book) {
        if (borrowedBooks.size() < 5 && book.isAvailable()) {
            book.borrowBook();
            borrowedBooks.add(book);
            return true;
        }
        return false;
    }

    public boolean returnBook(Book book) {
        if (borrowedBooks.remove(book)) {
            book.returnBook();
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Member{" + "name='" + name + "', memberId=" + memberId + ", borrowedBooks=" + borrowedBooks + "}";
    }
}

class Library {
    private List<Book> books;
    private List<Member> members;

    public Library() {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
    }

    public void addBook(String title, String author, String isbn) {
        books.add(new Book(title, author, isbn));
    }

    public void removeBook(String isbn) {
        books.removeIf(book -> book.getIsbn().equals(isbn));
    }

    public Book searchBook(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) return book;
        }
        return null;
    }

    public void addMember(String name, int memberId) {
        members.add(new Member(name, memberId));
    }

    public Member getMember(int memberId) {
        for (Member member : members) {
            if (member.getMemberId() == memberId) return member;
        }
        return null;
    }

    public boolean borrowBook(int memberId, String isbn) {
        Member member = getMember(memberId);
        Book book = searchBook(isbn);
        if (member != null && book != null) {
            return member.borrowBook(book);
        }
        return false;
    }

    public boolean returnBook(int memberId, String isbn) {
        Member member = getMember(memberId);
        Book book = searchBook(isbn);
        if (member != null && book != null) {
            return member.returnBook(book);
        }
        return false;
    }

    public void displayBooks() {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    public void displayMembers() {
        for (Member member : members) {
            System.out.println(member);
        }
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library();
        
        // Adding books
        library.addBook("The Great Gatsby", "F. Scott Fitzgerald", "123456");
        library.addBook("To Kill a Mockingbird", "Harper Lee", "789101");

        // Adding members
        library.addMember("Alice", 1);
        library.addMember("Bob", 2);

        // Borrowing a book
        System.out.println("Borrowing book: " + library.borrowBook(1, "123456"));

        // Displaying books
        System.out.println("Library Books:");
        library.displayBooks();

        // Returning a book
        System.out.println("Returning book: " + library.returnBook(1, "123456"));

        // Displaying members
        System.out.println("Library Members:");
        library.displayMembers();
    }
}
