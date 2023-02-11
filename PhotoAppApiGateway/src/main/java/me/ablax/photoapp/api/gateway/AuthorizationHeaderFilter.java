package me.ablax.photoapp.api.gateway;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    final Environment environment;

    public AuthorizationHeaderFilter(final Environment environment) {
        super(Config.class);
        this.environment = environment;
    }

    @Override
    public GatewayFilter apply(final Config config) {
        return (exchange, chain) -> {
            final ServerHttpRequest request = exchange.getRequest();

            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return onError(exchange, "No authorization header!", HttpStatus.UNAUTHORIZED);
            }

            final String bearerToken = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            final String jwtToken = bearerToken.replace("Bearer", "").trim();

            if(!isJwtValid(jwtToken)){
                return onError(exchange, "Jwt token is not valid!", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(final ServerWebExchange exchange, final String err, final HttpStatus httpStatus) {
        final ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean isJwtValid(final String jwtToken) {
        boolean isValid = true;

        try{
            final String subject = Jwts.parser()
                    .setSigningKey(environment.getProperty("token.secret"))
                    .parseClaimsJws(jwtToken)
                    .getBody()
                    .getSubject();

            if(subject == null || subject.isEmpty()){
                isValid = false;
            }
        }catch (JwtException exception){
            isValid = false;
        }


        return isValid;
    }

    public static class Config {
        //put config props here!
    }

}
