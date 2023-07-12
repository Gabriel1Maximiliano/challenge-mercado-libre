package challenge.desafio.challenge.ShortUrl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShortUrlRepository extends JpaRepository<ShortUrl,Long> {
    @Query("SELECT s FROM ShortUrl s WHERE s.is_active = 1")
    List<ShortUrl> findAllByIsActive();
    @Query("SELECT s FROM ShortUrl s WHERE s.is_active = 0")
    List<ShortUrl> findAllByIsInActive();
}
