package ch.ffhs.blogrest.appuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public AppUser create(AppUser appUser) {
        return appUserRepository.save(appUser);
    }
}
