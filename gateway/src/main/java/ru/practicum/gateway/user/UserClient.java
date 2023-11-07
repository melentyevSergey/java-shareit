package ru.practicum.gateway.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.gateway.client.BaseClient;

import static ru.practicum.gateway.utility.GatewayAppRequestParams.API_PREFIX_USERS;

@Service
public class UserClient extends BaseClient {

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX_USERS))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> saveUser(UserDto requestDto) {
        return post("", requestDto);
    }

    public ResponseEntity<Object> update(Integer userId, UserDto requestDto) {
        return patch("/" + userId, requestDto);
    }

    public ResponseEntity<Object> removeById(Integer userId) {
        return delete("/" + userId);
    }

    public ResponseEntity<Object> getById(Integer userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> getAll() {
        return get("");
    }
}