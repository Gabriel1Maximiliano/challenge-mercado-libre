package challenge.desafio.challenge.ShortUrl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path="api/url")
public class ShortUrlController {

    private final ShortUrlServices shortUrlServices;

    public ShortUrlController(ShortUrlServices shortUrlServices) {
        this.shortUrlServices = shortUrlServices;
    }


    @GetMapping("/get-url-created")
    public ResponseEntity<List<ShortUrl>> getAllUrlCreated(){

        var resp = shortUrlServices.getAmountOfUrl();

        if (!resp.isEmpty()) {
            return ResponseEntity.ok(resp);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @GetMapping("/get-amount-url-created")
    public ResponseEntity<Integer> getAmountOfUrl(){

        var resp = shortUrlServices.getAmountOfUrl().size();

        if (resp >= 0) {
            return ResponseEntity.ok(resp);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/get-amount-url-inactives")
    public ResponseEntity<Integer> getAllInactivesUrl(){
        var resp = shortUrlServices.getAmountInactivesUrl();
        if (resp >= 0) {
            return ResponseEntity.ok(resp);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/get-amount-url-actives")
    public ResponseEntity<Integer> getAllActivesUrl() {
        var resp = shortUrlServices.getAmountActivesUrl();
        if (resp >= 0) {
            return ResponseEntity.ok(resp);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




    @PostMapping("/logic-deleted-url")
    public ResponseEntity<String> logicDeleted(@RequestBody String url){
        var resp = shortUrlServices.logicDeleted(url);
        if(resp != null ){
            return ResponseEntity.ok(resp);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/create-short-url")
    public ResponseEntity<String> createShortUrl(@RequestBody ShortUrl url){
        var resp = shortUrlServices.createShortUrl(url);

        if(resp != null ){
            return ResponseEntity.created(URI.create(resp)).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/{url}")
    public ResponseEntity<Void> getOriginalUrl(@PathVariable String url){
        var resp = shortUrlServices.getOriginalUrl(url);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(resp)).build();
    }
}
