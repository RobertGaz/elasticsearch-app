package my.model;

import java.util.List;

public class Employee {

    private String name;
    private String dob;
    private Address address;
    private String email;
    private List<String> skills;
    private int experience;
    private double rating;
    private String description;
    private boolean verified;
    private int salary;

    public Employee() {
    }

    public Employee(String name, String dob, Address address, String email, List<String> skills, int experience,
                  double rating, String description, boolean verified, int salary) {
        this.name = name;
        this.dob = dob;
        this.address = address;
        this.email = email;
        this.skills = skills;
        this.experience = experience;
        this.rating = rating;
        this.description = description;
        this.verified = verified;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public static class Address {
        private String country;
        private String town;

        public Address() {
        }

        public Address(String country, String town) {
            this.country = country;
            this.town = town;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }
    }
}
