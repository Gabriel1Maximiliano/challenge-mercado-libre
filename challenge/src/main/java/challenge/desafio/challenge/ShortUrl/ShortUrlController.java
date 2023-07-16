package challenge.desafio.challenge.ShortUrl;

import com.hazelcast.jet.json.JsonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="api/url")
public class ShortUrlController {

    private final ShortUrlServices shortUrlServices;

    public ShortUrlController(ShortUrlServices shortUrlServices) {
        this.shortUrlServices = shortUrlServices;
    }


    @GetMapping("/get-created")
    public ResponseEntity<List<ShortUrl>> getAllUrlCreated(){

         List<ShortUrl> resp = shortUrlServices.getAmountOfUrl();

        if (!resp.isEmpty()) {
            return ResponseEntity.ok(resp);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @GetMapping("/get-amount-created")
    public ResponseEntity<Integer> getAmountOfUrl(){

        int resp = shortUrlServices.getAmountOfUrl().size();

        if (resp >= 0) {
            return ResponseEntity.ok(resp);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/get-amount-inactives")
    public ResponseEntity<Integer> getAllInactivesUrl(){
        int resp = shortUrlServices.getAmountInactivesUrl();
        if (resp >= 0) {
            return ResponseEntity.ok(resp);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/get-amount-actives")
    public ResponseEntity<Integer> getAllActivesUrl() {
        var resp = shortUrlServices.getAmountActivesUrl();
        if (resp >= 0) {
            return ResponseEntity.ok(resp);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




    @PutMapping("/{url}/delete")
    public ResponseEntity<ShortUrl> logicDeleted(@PathVariable String url){
        ShortUrl resp = shortUrlServices.logicDeleted(url);
        if(resp != null ){
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/create-short-url")
    public ResponseEntity<String> createShortUrl(@RequestBody ShortUrl url){

        String resp = shortUrlServices.createShortUrl(url);
        if(resp != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }


    }
    @GetMapping("/{url}/get")
    public ResponseEntity<Void> getOriginalUrl(@PathVariable String url){

        var resp = shortUrlServices.getOriginalUrl(url);
        if(resp != null){
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(resp)).build();
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
    @PutMapping("/{url}/restore")
    public ResponseEntity<ShortUrl> restoreUrl(@PathVariable String url){
        var resp = shortUrlServices.restoreUrl(url);
        if(resp != null ){
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/active-info")
    public ResponseEntity<List<ShortUrl>> getAllInActiveUrlsInfo(){
        var resp = shortUrlServices.getAllInActiveUrlsInfo();
        if(resp != null ){
            return ResponseEntity.status(HttpStatus.OK).body(resp);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/inactive-info")
    public ResponseEntity<List<ShortUrl>> getAllActiveUrlsInfo(){
        var resp = shortUrlServices.getAllActiveUrlsInfo();
        if(resp != null ){
            return ResponseEntity.status(HttpStatus.OK).body(resp);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("get-short-url")
    public String getShortUrl(@RequestBody RequestBodyDto id){
        long res = id.getId();
           return shortUrlServices.getShortUrl(res);

    }

    @GetMapping("seed")
    public String getSeed(){
var resp = shortUrlServices.getSeed();
        return resp;

    }
}
