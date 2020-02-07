package me.druwa.be.domain.user.model;

import me.druwa.be.domain.auth.service.TokenProvider;
import me.druwa.be.global.exception.DruwaException;

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
        throw DruwaException.unauthorized();
    }

    @Override
    public void onUpdate() {
        throw DruwaException.unauthorized();
    }

    @Override
    public View.Create.Response toCreateResponse(final TokenProvider tokenProvider) {
        throw DruwaException.unauthorized();
    }

    @Override
    public View.Read.Response toReadResponse() {
        throw DruwaException.unauthorized();
    }
}
