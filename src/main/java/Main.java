import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;

public class Main {


    public static final String URI = "https://api.nasa.gov/planetary/apod?api_key=wyvnIDTmbwTQZf88oIHVZ5MQ2xt1QEUYrQMPuPWP";

    public static void main(String[] args) {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
            .setDefaultRequestConfig(RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(30000)
                .setRedirectsEnabled(false)
                .build())
            .build();

        HttpGet request = new HttpGet(URI);

        try {
            CloseableHttpResponse response = httpClient.execute(request);

            ObjectMapper mapper = new ObjectMapper();
            NasaResponse nasaResponse = mapper.readValue(response.getEntity().getContent(), new TypeReference<NasaResponse>() {});
            String url = nasaResponse.getUrl();
            String fileName = url.substring(url.lastIndexOf("/") + 1);

            HttpGet nasaRequest = new HttpGet(url);
            CloseableHttpResponse imageResponse = httpClient.execute(nasaRequest);

            try(FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName))){
                imageResponse.getEntity().writeTo(fileOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}
