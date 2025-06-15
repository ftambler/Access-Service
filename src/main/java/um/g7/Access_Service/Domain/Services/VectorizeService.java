package um.g7.Access_Service.Domain.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import um.g7.Access_Service.Domain.Entities.VectorAndPicture;

@Service
public class VectorizeService {
    
    private final RestTemplate restTemplate;

    @Value("${services.vectorize-service-uri}")
    private String vectorizeURI;


    public VectorizeService() {
        this.restTemplate = new RestTemplate();
    }

    public VectorAndPicture getVectorAndPicture(String base64String) {
        return restTemplate.postForEntity(vectorizeURI, new HttpEntity<String>(base64String), VectorAndPicture.class).getBody();
    }
    

}
