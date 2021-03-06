package traveler.module.domain.entity;

import java.io.File;

public class CardInfo {
    private final UserEntity user;
    private final String city;
    private final String country;
    private final String fullDescription;
    private final String shortDescription;
    private final String address;
    private final String pathToPhoto;
    private final String hashtag;
    private File file;

    public CardInfo(
            UserEntity user,
            String city,
            String country,
            String fullDescription,
            String shortDescription,
            String address,
            String pathToPhoto,
            String hashtag
    ) {
        this.user = user;
        this.city = city;
        this.country = country;
        this.fullDescription = fullDescription;
        this.shortDescription = shortDescription;
        this.address = address;
        this.pathToPhoto = pathToPhoto;
        this.hashtag = hashtag;
    }

    public UserEntity getUser() {
        return user;
    }

    public String getCity() {
        return city;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getAddress() {
        return address;
    }

    public String getPathToPhoto() {
        return pathToPhoto;
    }

    public String getCountry() {
        return country;
    }

    public String getHashtag() {
        return hashtag;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
