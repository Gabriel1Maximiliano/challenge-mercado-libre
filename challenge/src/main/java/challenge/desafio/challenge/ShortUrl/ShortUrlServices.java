package challenge.desafio.challenge.ShortUrl;

import challenge.desafio.challenge.Utils.ConversionUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ShortUrlServices {

    private final ShortUrlRepository shortUrlRepository;
    private ConversionUrl conversionUrl = new ConversionUrl();

    @Autowired
    public ShortUrlServices(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    public List<ShortUrl> getAmountOfUrl() {
        try {
            return shortUrlRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error en getAmountOfUrl: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public String createShortUrl(ShortUrl url) {
        long startTime = System.currentTimeMillis();
        try {
            Optional<ShortUrl> alreadyCreated = shortUrlRepository.findUrl(url.getLong_url());
            System.out.println("soy already created"+alreadyCreated);
            if (!alreadyCreated.isPresent() ) {
                url.setDateOfCreate(Date.valueOf(LocalDate.now()));
                ShortUrl data = shortUrlRepository.save(url);
                String resp = conversionUrl.encodeUrl(data.getId());
                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;
                data.setCreation_time(executionTime);
                shortUrlRepository.save(data);
                System.out.println("Tiempo de creación: " + executionTime);
                return resp;
            } else {
                return "La url ya está almacenada en la base de datos";
            }
        } catch (Exception e) {
            System.err.println("Error en createShortUrl: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String getOriginalUrl(String url) {
        try {
            long startTime = System.currentTimeMillis();
            long id = conversionUrl.decodeUrl(url);
            Optional<ShortUrl> longUrl = shortUrlRepository.findById(id);
            if (longUrl.isPresent() && longUrl.get().getIs_active() == 1) {
                return "Url inactiva";
            }
            ShortUrl shortUrl = longUrl.get();
            shortUrl.setAmount_of_clicks(shortUrl.getAmount_of_clicks() + 1);
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            shortUrl.setRetrieved_time(executionTime);
            shortUrlRepository.save(shortUrl);
            System.out.println("Tiempo que tardó de obtención: " + executionTime);
            return longUrl.get().getLong_url();
        } catch (Exception e) {
            System.err.println("Error en getOriginalUrl: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public int getAmountInactivesUrl() {
        try {
            return shortUrlRepository.findAllByIsInActive().size();
        } catch (Exception e) {
            System.err.println("Error en getAmountInactivesUrl: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public int getAmountActivesUrl() {
        try {
            List<ShortUrl> activeUrls = shortUrlRepository.findAllByIsActive();
            return activeUrls.size();
        } catch (Exception e) {
            System.err.println("Error en getAmountActivesUrl: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public List<ShortUrl> getAllActiveUrlsInfo() {
        try {
            return shortUrlRepository.findAllByIsActive();
        } catch (Exception e) {
            System.err.println("Error en getAllActiveUrlsInfo: " + e.getMessage());
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<ShortUrl> getAllInActiveUrlsInfo() {
        try {
            return shortUrlRepository.findAllByIsInActive();
        } catch (Exception e) {
            System.err.println("Error en getAllInActiveUrlsInfo: " + e.getMessage());
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public ShortUrl logicDeleted(String url) {
        try {
            long id = conversionUrl.decodeUrl(url);
            Optional<ShortUrl> urlToFind = shortUrlRepository.findById(id);
            if (urlToFind.isPresent()) {
                ShortUrl urlToDelete = urlToFind.get();
                urlToDelete.setDeleted_of_time(Date.valueOf(LocalDate.now()));
                urlToDelete.setIs_active(1);
                ShortUrl deletedUrl = shortUrlRepository.save(urlToDelete);
                return deletedUrl;
            }
        } catch (Exception e) {
            System.err.println("Error en logicDeleted: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public ShortUrl restoreUrl(String url) {
        try {
            long id = conversionUrl.decodeUrl(url);
            Optional<ShortUrl> urlToFind = shortUrlRepository.findById(id);
            if (urlToFind.isPresent()) {
                ShortUrl urlToRestore = urlToFind.get();
                urlToRestore.setIs_active(0);
                ShortUrl restoredUrl = shortUrlRepository.save(urlToRestore);
                return restoredUrl;
            }
        } catch (Exception e) {
            System.err.println("Error en restoreUrl: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String getShortUrl(long id) {


        try {
            Optional<ShortUrl> originalUrl = shortUrlRepository.findById(id);
            if (originalUrl.isPresent()) {
                String shortUrl = conversionUrl.encodeUrl(originalUrl.get().getId());
                if (shortUrl != null) {
                    return shortUrl;
                }
            }
        } catch (Exception e) {
            System.err.println("Error en getShortUrl: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String getSeed() {
        String[] urls = {
                "https://www.google.com",
                "https://www.youtube.com",
                "https://www.facebook.com",
                "https://www.amazon.com",
                "https://www.twitter.com",
                "https://www.instagram.com",
                "https://www.linkedin.com",
                "https://www.netflix.com",
                "https://www.wikipedia.org",
                "https://www.github.com"
        };

        for (String url : urls) {
            ShortUrl shortUrl = new ShortUrl();
            shortUrl.setLong_url(url);
            shortUrlRepository.save(shortUrl);
        }

        return "Ok:Seed ejecutado correctamente";
    }}
