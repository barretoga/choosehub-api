package com.barretoga.choosehub.book;

import com.barretoga.choosehub.badge.Badge;
import com.barretoga.choosehub.badge.BadgeRepository;
import com.barretoga.choosehub.book.dto.BookDTO;
import com.barretoga.choosehub.user.User;
import com.barretoga.choosehub.user.UserRepository;
import com.barretoga.choosehub.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;

    public List<BookDTO> listBooksByUser() {
        String userEmail = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return bookRepository.findByUser(user).stream()
                .map(book -> new BookDTO(
                        book.getId(),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getGenre(),
                        new UserDTO(user.getId(), user.getFirstname())
                ))
                .toList();
    }

    public Optional<BookDTO> getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        User user = book.getUser();

        return Optional.of(new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getGenre(),
                new UserDTO(user.getId(), user.getFirstname())
        ));
    }

    public Book saveBook(Book book) {
        String userEmail = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        book.setUser(user);

        Book savedBook = bookRepository.save(book);

        updateUserBadge(user);

        return savedBook;
    }

    public void removeBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        User user = book.getUser();

        bookRepository.deleteById(id);

        updateUserBadge(user);
    }

    private void updateUserBadge(User user) {
        long totalBooks = bookRepository.countByUser(user);

        Badge badge = badgeRepository.findTopByMinBooksLessThanEqualOrderByMinBooksDesc((int) totalBooks);

        if (badge != null && !user.getBadges().contains(badge)) {
            user.getBadges().add(badge);
            userRepository.save(user);
        }
    }
}
