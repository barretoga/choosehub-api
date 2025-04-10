package com.barretoga.choosehub.book;

import com.barretoga.choosehub.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByUser(User user);
    long countByUser(User user);
}
