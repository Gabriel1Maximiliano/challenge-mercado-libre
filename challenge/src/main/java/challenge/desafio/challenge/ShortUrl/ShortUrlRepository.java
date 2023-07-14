package challenge.desafio.challenge.ShortUrl;

import challenge.desafio.challenge.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl,Long> {
    @Query("SELECT s FROM ShortUrl s WHERE s.is_active = 1")
    List<ShortUrl> findAllByIsActive();
    @Query("SELECT s FROM ShortUrl s WHERE s.is_active = 0")
    List<ShortUrl> findAllByIsInActive();
    @Query("SELECT MAX(s.id) FROM ShortUrl s")
    Long findMaxId();
    @Query("SELECT s FROM ShortUrl s WHERE s.long_url=?1")
    Optional<List<ShortUrl>> findUrl(String url);
}
