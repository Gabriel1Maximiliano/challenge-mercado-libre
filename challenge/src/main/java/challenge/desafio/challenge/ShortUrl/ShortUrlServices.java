package challenge.desafio.challenge.ShortUrl;

import challenge.desafio.challenge.Utils.ConversionUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class ShortUrlServices {

    private final ShortUrlRepository shortUrlRepository;
    private ConversionUrl conversionUrl = new ConversionUrl();

    private static final Logger logger = LogManager.getLogger(ShortUrl.class);

    @Autowired
    public ShortUrlServices(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    public List<ShortUrl> getAmountOfUrl() {
        try {
            return shortUrlRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error en getAmountOfUrl: " + e);
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public String createShortUrl(ShortUrl url) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando creación de Url");
        try {
            boolean isWordInUrl1 = url.getLong_url().toLowerCase().contains("mercadolibre");
            if (isWordInUrl1) {
                Optional<ShortUrl> alreadyCreated = shortUrlRepository.findUrl(url.getLong_url());
                if (!alreadyCreated.isPresent()) {
                    url.setDateOfCreate(Date.valueOf(LocalDate.now()));
                    ShortUrl data = shortUrlRepository.save(url);
                    String resp = conversionUrl.encodeUrl(data.getId());
                    long endTime = System.currentTimeMillis();
                    long executionTime = endTime - startTime;
                    data.setCreation_time(executionTime);
                    shortUrlRepository.save(data);
                    logger.info("Finalizando creación de Url");
                    return "https://me.li/" + resp;
                } else {
                    return "La url ya está almacenada en la base de datos";
                }
            } else {
                return "Url no permitida, debe pertenecer al dominio 'MercadoLibre'";
            }
        } catch (Exception e) {
            logger.error("Error en createShortUrl: ", e);
            e.printStackTrace();
            throw new ShortUrlException("Error en createShortUrl");
        }
    }

    public String getOriginalUrl(String url) {
        logger.info("Obteniendo url original");
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
            logger.info("Finalizado con éxito");
            return longUrl.get().getLong_url();
        } catch (Exception e) {
            logger.error("Error en getOriginalUrl: ", e);
            e.printStackTrace();
            throw new ShortUrlException("Error en getOriginalUrl: " + e.getMessage());
        }
    }

    public int getAmountInactivesUrl() {
        try {
            return shortUrlRepository.findAllByIsInActive().size();
        } catch (Exception e) {
            logger.error("Error en getAmountInactivesUrl: ", e);
            e.printStackTrace();
            throw new ShortUrlException("Error en getAmountInactivesUrl: " + e.getMessage());
        }
    }

    public int getAmountActivesUrl() {
        try {
            List<ShortUrl> activeUrls = shortUrlRepository.findAllByIsActive();
            return activeUrls.size();
        } catch (Exception e) {
            logger.error("Error en getAmountActivesUrl: ", e);
            e.printStackTrace();
            throw new ShortUrlException("Error en getAmountActivesUrl: " + e.getMessage());
        }
    }

    public List<ShortUrl> getAllActiveUrlsInfo() {
        try {
            return shortUrlRepository.findAllByIsActive();
        } catch (Exception e) {
            logger.error("Error en getAllActiveUrlsInfo: ", e);
            e.printStackTrace();
            throw new ShortUrlException("Error en getAllActiveUrlsInfo: " + e.getMessage());
        }
    }

    public List<ShortUrl> getAllInActiveUrlsInfo() {
        try {
            return shortUrlRepository.findAllByIsInActive();
        } catch (Exception e) {
            logger.error("Error en getAllInActiveUrlsInfo: ", e);
            e.printStackTrace();
            throw new ShortUrlException("Error en getAllInActiveUrlsInfo: " + e.getMessage());
        }
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
            } else {
                throw new ShortUrlException("URL no encontrada con el ID proporcionado");
            }
        } catch (Exception e) {
            logger.error("Error en logicDeleted: ", e);
            e.printStackTrace();
            throw new ShortUrlException("Error en logicDeleted: " + e.getMessage());
        }
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
            } else {
                throw new ShortUrlException("Error, URL no encontrada");
            }
        } catch (Exception e) {
            logger.error("Error en restoreUrl: ", e);
            e.printStackTrace();
            throw new ShortUrlException("Error en restoreUrl: " + e.getMessage());
        }
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
            throw new ShortUrlException("URL no encontrada con el ID proporcionado: " + id);
        } catch (Exception e) {
            logger.error("Error en getShortUrl: ", e);
            e.printStackTrace();
            throw new ShortUrlException("Error en getShortUrl: " + e.getMessage());
        }
    }

    public String getSeed() {
        try {
            long startTime = System.currentTimeMillis();

            String[] popularUrls = {
                    "https://www.mercadolibre.com.ar/interruptor-miniatura-para-riel-din-chint-nxb-2-63-40a/p/MLA13661808#reco_item_pos=1&reco_backend=machinalis-homes-pdp-boos&reco_backend_type=function&reco_client=home_navigation-recommendations&reco_id=522b7a0c-798e-4954-8279-87dc98143dcf",
                    "https://listado.mercadolibre.com.ar/mancuernas#D[A:mancuernas,L:undefined]",
                    "https://www.mercadolibre.com.ar/ofertas/movetelibre23#DEAL_ID=MLA33037&S=MKT&V=1&T=TSB&L=MKTPLACE_PPS_CROSS_MOVETE_LIBRE&me.flow=-1&me.bu=3&me.audience=all&me.content_id=PPS13_MOVETELIBRE_banner_search&me.component_id=exhibitors_ml&me.logic=user_journey&me.position=0&me.bu_line=26",
                    "https://www.mercadolibre.com.ar/interruptor-sica-limit-782225/p/MLA11289920#reco_item_pos=3&reco_backend=machinalis-homes-pdp-boos&reco_backend_type=function&reco_client=home_navigation-recommendations&reco_id=1e37c5f9-4224-44b9-b8a3-f967e4708a18",
                    "https://articulo.mercadolibre.com.ar/MLA-1317236701-modulo-sensor-microonda-proximidad-presencia-radar-rcwl0516-_JM#reco_item_pos=1&reco_backend=machinalis-homes-pdp-boos&reco_backend_type=function&reco_client=home_navigation-trend-recommendations&reco_id=405fcf1d-03bc-4b6b-b8a6-d6af6665f77c",
                    "https://articulo.mercadolibre.com.ar/MLA-1420686792-sensor-ultrasonido-medidor-distancia-arduino-hc-sr04-_JM#reco_item_pos=1&reco_backend=adv_hybrid_L2_brothers_cruella&reco_backend_type=low_level&reco_client=vip-pads-up&reco_id=62732d36-55fd-4ea6-aa9e-c8873466a401&is_advertising=true&ad_domain=VIPDESKTOP_UP&ad_position=2&ad_click_id=ZjFlNzQ4Y2ItMTQ5NS00NzE2LWFkNjAtM2JkZTk1ZmIxMTg4",
            };

            for (String url : popularUrls) {
                ShortUrl shortUrl = new ShortUrl();
                shortUrl.setLong_url(url);
                shortUrlRepository.save(shortUrl);
            }

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            return "Ok:Seed ejecutado correctamente " + executionTime + "ms";
        } catch (Exception e) {
            logger.error("Error en getSeed: ", e);
            e.printStackTrace();
            throw new ShortUrlException("Error en getSeed: " + e.getMessage());
        }
    }
}
