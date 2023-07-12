package challenge.desafio.challenge.ShortUrl;

import challenge.desafio.challenge.Utils.ConversionUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
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

    public List<ShortUrl> getAmountOfUrl(){
        var resp = shortUrlRepository.findAll();

        return resp;

    }
    public String createShortUrl(ShortUrl url){
        url.setIs_active(1);
        url.setDateOfCreate(Date.valueOf(LocalDate.now()));
        var data = shortUrlRepository.save(url);
        var resp = conversionUrl.encodeUrl(data.getId());
        return "creado en base de datos " + resp;
    }
    public String getOriginalUrl(String url) {

        var id = conversionUrl.decodeUrl(url);
        Optional<ShortUrl> longUrl = shortUrlRepository.findById(id);
        if (longUrl.isPresent()) {
            ShortUrl shortUrl = longUrl.get();
            shortUrl.setAmount_of_clicks(shortUrl.getAmount_of_clicks() + 1);
            shortUrlRepository.save(shortUrl);
            Optional<ShortUrl> originalUrl = shortUrlRepository.findById(id);
            return originalUrl.get().getLong_url();
        }
        return null;
    }
    public int getAmountInactivesUrl(){
        var resp = shortUrlRepository.findAllByIsActive();
        return resp.size();
    }
    public int getAmountActivesUrl(){
        var resp = shortUrlRepository.findAllByIsInActive();
        return resp.size();
    }
    public String logicDeleted(String url){

        var id = conversionUrl.decodeUrl(url);
        Optional<ShortUrl> urlToFind = shortUrlRepository.findById(id);
        ShortUrl urlToDelete = urlToFind.get();
        urlToDelete.setIs_active(0);
        shortUrlRepository.save(urlToDelete);

        return "Borrado lógico realizado correctamente";

    }
    public List<ShortUrl> getAllUrlInfo(){
        return  shortUrlRepository.findAll();
    }

}
