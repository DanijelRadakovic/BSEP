package megatravel.com.pki.domain.DTO;

public class SubjectDTO {
    private String commonName;
    private String surname;
    private String givenName;
    private String gender;
    private String country;
    private String email;
    private String organization;
    private String organizationUnit;
    private String placeOfBirth;
    private String street;
    private String localityName;
    private String postalCode;
    private String countryOfCitizenship;
    private String countryOfResidence;

    public SubjectDTO() {
    }

    public SubjectDTO(String commonName, String surname, String givenName, String gender, String country, String email,
                      String organization, String organizationUnit, String localityName) {
        this.commonName = commonName;
        this.surname = surname;
        this.givenName = givenName;
        this.gender = gender;
        this.country = country;
        this.email = email;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
        this.localityName = localityName;
    }

    public SubjectDTO(String commonName, String surname, String givenName, String gender, String country, String email,
                      String organization, String organizationUnit, String placeOfBirth,
                      String street, String localityName, String postalCode, String countryOfCitizenship,
                      String countryOfResidence) {
        this.commonName = commonName;
        this.surname = surname;
        this.givenName = givenName;
        this.gender = gender;
        this.country = country;
        this.email = email;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
        this.placeOfBirth = placeOfBirth;
        this.street = street;
        this.localityName = localityName;
        this.postalCode = postalCode;
        this.countryOfCitizenship = countryOfCitizenship;
        this.countryOfResidence = countryOfResidence;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountryOfCitizenship() {
        return countryOfCitizenship;
    }

    public void setCountryOfCitizenship(String countryOfCitizenship) {
        this.countryOfCitizenship = countryOfCitizenship;
    }

    public String getCountryOfResidence() {
        return countryOfResidence;
    }

    public void setCountryOfResidence(String countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }
}
