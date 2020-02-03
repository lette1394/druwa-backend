package me.druwa.be.domain.user.model;

import me.druwa.be.domain.auth.service.TokenProvider;
import me.druwa.be.global.exception.UnauthorizedException;

public class PublicUser extends User {
    private static final PublicUser instance = new PublicUser();

    public static PublicUser getInstance() {
        return instance;
    }

    private PublicUser() {
        super();
        setUserId(-99999L);
        setAuthorities(Authorities.of(Authority.PUBLIC));
        setEmail("public@user.email");
    }

    @Override
    public void onCreate() {
        throw new UnauthorizedException();
    }

    @Override
    public void onUpdate() {
        throw new UnauthorizedException();
    }

    @Override
    public View.Create.Response toCreateResponse(final TokenProvider tokenProvider) {
        throw new UnauthorizedException();
    }

    @Override
    public View.Read.Response toReadResponse() {
        throw new UnauthorizedException();
    }
}
