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
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    public String createShortUrl(ShortUrl url) {
        try {
            long startTime = System.currentTimeMillis();
            Optional<List<ShortUrl>> alreadyCreated = shortUrlRepository.findUrl(url.getLong_url());
            if (alreadyCreated.isPresent()) {
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
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getOriginalUrl(String url) {
        try {
            long startTime = System.currentTimeMillis();
            long id = conversionUrl.decodeUrl(url);
            Optional<ShortUrl> longUrl = shortUrlRepository.findById(id);
            ShortUrl shortUrl = longUrl.get();
            shortUrl.setAmount_of_clicks(shortUrl.getAmount_of_clicks() + 1);
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            shortUrl.setRetrieved_time(executionTime);
            shortUrlRepository.save(shortUrl);
            System.out.println("tiempo que tarde de obtención" + executionTime);
            return longUrl.get().getLong_url();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public int getAmountInactivesUrl() {
        try {
            return shortUrlRepository.findAllByIsInActive().size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getAmountActivesUrl() {
        try {
            return shortUrlRepository.findAllByIsActive().size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<ShortUrl> getAllActiveUrlsInfo() {
        try {
            return  shortUrlRepository.findAllByIsActive();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<ShortUrl> getAllInActiveUrlsInfo() {

        try {
            return  shortUrlRepository.findAllByIsInActive();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

    }

    public String logicDeleted(String url) {
        try {
            var id = conversionUrl.decodeUrl(url);
            Optional<ShortUrl> urlToFind = shortUrlRepository.findById(id);
            ShortUrl urlToDelete = urlToFind.get();
            urlToDelete.setDeleted_of_time(Date.valueOf(LocalDate.now()));
            urlToDelete.setIs_active(0);
            shortUrlRepository.save(urlToDelete);
            return "Borrado lógico realizado correctamente";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error en el borrado lógico";
        }
    }

    public List<ShortUrl> getAllUrlInfo() {
        try {
            return shortUrlRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}

