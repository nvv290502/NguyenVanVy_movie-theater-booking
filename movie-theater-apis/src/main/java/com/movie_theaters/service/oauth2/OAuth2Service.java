package com.movie_theaters.service.oauth2;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie_theaters.dto.UserDTO;
import com.movie_theaters.entity.User;
import com.movie_theaters.exception.TokenException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class OAuth2Service {

    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";
    private static final String TOKEN_INFO_URL = "https://www.googleapis.com/oauth2/v3/tokeninfo";
    private static final String CLIENT_ID = "60153713652-fep0gqchpvvahggi9q6v5l7qbdiic1du.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "http://localhost:3000/home";
    public String getAccessToken(String code) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = "code=" + code +
                "&client_id="+CLIENT_ID+
                "&client_secret=" +CLIENT_SECRET+
                "&redirect_uri=" +REDIRECT_URI+
                "&grant_type=authorization_code";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, entity, String.class);
        String responseBody = response.getBody();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

//    public String getRefreshToken(String code) throws JsonProcessingException {
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        String requestBody = "code=" + code +
//                "&client_id=" +CLIENT_ID+
//                "&client_secret=" +CLIENT_SECRET+
//                "&redirect_uri=" +REDIRECT_URI+
//                "&grant_type=authorization_code";
//
//        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, entity, String.class);
//        String responseBody = response.getBody();
//
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode jsonNode = mapper.readTree(responseBody);
//        return jsonNode.get("refresh_token").asText();
//    }

    public UserDTO getUserInfo(String accessToken) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Gửi yêu cầu GET để lấy thông tin người dùng
            ResponseEntity<String> response = restTemplate.exchange(USER_INFO_URL, HttpMethod.GET, entity, String.class);
            String userInfoJson = response.getBody();

            // Phân tích JSON để lấy thông tin người dùng
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(userInfoJson);

            String email = jsonNode.get("email").asText();
            String name = jsonNode.get("name").asText();
            String picture = jsonNode.get("picture").asText();

            UserDTO user = new UserDTO();
            user.setEmail(email);
            user.setFullName(name);
            user.setAvatarUrl(picture);

            return user;

        } catch (HttpClientErrorException e) {
            throw new TokenException("Token không hợp lệ hoặc đã hết hạn");
        } catch (HttpServerErrorException e) {
            throw new TokenException("Lỗi từ phía server khi xác thực token");
        } catch (JsonProcessingException e) {
            throw new TokenException("Lỗi khi phân tích thông tin người dùng");
        } catch (Exception e) {
            throw new TokenException("Lỗi không xác định khi xác thực token");
        }
    }

    public boolean isValidToken(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // Gửi yêu cầu GET đến endpoint token info để xác minh tính hợp lệ của token
        ResponseEntity<String> response = restTemplate.exchange(TOKEN_INFO_URL + "?access_token=" + accessToken, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        return response.getStatusCode() == HttpStatus.OK;
    }
}
