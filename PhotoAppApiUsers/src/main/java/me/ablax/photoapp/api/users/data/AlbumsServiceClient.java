package me.ablax.photoapp.api.users.data;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import me.ablax.photoapp.api.users.ui.model.AlbumResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;

@FeignClient(name = "albums-ws")
public interface AlbumsServiceClient {

    @GetMapping("/users/{id}/albums")
    @Retry(name = "albums-ws")
    @CircuitBreaker(name = "albums-ws", fallbackMethod = "getAlbumsFallback")
    List<AlbumResponseModel> getAlbums(@PathVariable String id);

    default List<AlbumResponseModel> getAlbumsFallback(final String id, final Throwable e){
        System.out.println("Fallback method called " + id);
        return Collections.emptyList();
    }

}
