package com.takeaway.automation.tests.components;

public class Result {
    String id, user_id, title, first_name, last_name, gender, dob, email, phone,
            website, address, status, album_id, url, thumbnail, post_id, name, body, message;
    Links _links;

    public Links getLinks(){
        return _links;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", title='" + title + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", gender='" + gender + '\'' +
                ", dob='" + dob + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                ", album_id='" + album_id + '\'' +
                ", url='" + url + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", post_id='" + post_id + '\'' +
                ", name='" + name + '\'' +
                ", body='" + body + '\'' +
                ", links=" + _links +
                '}';
    }

    public String getMessage(){
        return message;
    }

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getPost_id() {
        return post_id;
    }

    public String getName() {
        return name;
    }

    public String getBody() {
        return body;
    }

    public static class Links {
        Link self, edit, avatar;

        public Link getSelf() {
            return self;
        }

        public Link getEdit() {
            return edit;
        }

        public Link getAvatar() {
            return avatar;
        }

        @Override
        public String toString() {
            return "Links{" +
                    "self=" + self +
                    ", edit=" + edit +
                    ", avatar=" + avatar +
                    '}';
        }

        public static class Link {
            String href;

            public String getHref() {
                return href;
            }

            @Override
            public String toString() {
                return "Link{" +
                        "href='" + href + '\'' +
                        '}';
            }
        }
    }
}