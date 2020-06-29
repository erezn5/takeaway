package com.takeaway.automation.tests.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.takeaway.automation.framework.conf.EnvConf;
import com.takeaway.automation.framework.utils.JsonHandler;
import com.takeaway.automation.framework.utils.SimpleHttpClient;
import com.takeaway.automation.tests.BaseTest;
import com.takeaway.automation.tests.components.GoRest;
import com.takeaway.automation.tests.components.Result;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import static com.takeaway.automation.framework.logger.LoggerFactory.LOG;

public class HTTPTester extends BaseTest {

    private HashMap<String, String> headersMap = new HashMap<>();
    private final static String BASE_URL = EnvConf.getProperty("basic.url");
    private String email = String.format(EnvConf.getProperty("user.email"), randSuffix("mailer"));
    private SimpleHttpClient simpleHttpClient;
    private String userId;
    private GoRest gorest = new GoRest();


    @BeforeMethod
    private void initSimpleHttpClient() {
        simpleHttpClient = new SimpleHttpClient();
        // headers are constant thus headers assigned in the beginning of the test
        headersMap.put("Authorization", "Bearer iiO3ZDQpM81BIqSgW79UA2c-iCa_V0otGWZU");
        headersMap.put("Content-Type", "application/json");
    }


    @Test(priority = 10, dataProvider = "newUserDataProvider")
    public void createNewUser(String url, String body) throws IOException {
        String res = simpleHttpClient.sendPostRequest(url, body, headersMap);
        JsonObject js = JsonHandler.asListObject(res, "_meta");
        gorest = JsonHandler.getJsonAsClassObject(js.toString(), GoRest.class);

        JsonObject resultObj = JsonHandler.asListObject(res, "result");
        Result result = JsonHandler.getJsonAsClassObject(resultObj.toString(), Result.class);
        String email = result.getEmail();
        String userName = result.getFirst_name();
        String lastName = result.getLast_name();

        userId = result.getId();
        Assert.assertEquals(email, this.email, String.format("Email is wrong not as expected, expected mail is: =[%s]", this.email));
        Assert.assertEquals(userName, "John", "Wrong first name");
        Assert.assertEquals(lastName, "Doe", "Wrong last name");

        Assert.assertEquals(gorest.getMessage(), "OK. Everything worked as expected.");
    }

    @DataProvider(name = "newUserDataProvider")
    public Object[][] newUserDataProvider(Method m) {
        switch (m.getName()) {
            case "createNewUser":
                return new Object[][]{
                        {BASE_URL.concat("/users"), "{\"first_name\":\"John\",\"last_name\":\"Doe\"," +
                                "\"gender\":\"male\",\"email\":" +
                                "\"" + email + "\"" +
                                ",\"status\":\"active\"}"}
                };
            case "updateExistedUser":
                return new Object[][]{
                        {BASE_URL.concat(String.format("/users/%s", userId)), "{\"first_name\":\"Jon\",\"last_name\":\"Doel\"," +
                                "\"gender\":\"male\",\"email\":" +
                                "\"" + email + "\"" +
                                ",\"status\":\"active\"}"}
                };
        }
        return null;
    }


    @Test(priority = 20)
    public void getCreatedUserDetails() throws IOException {
        String res = simpleHttpClient.sendGetRequest(BASE_URL.concat(String.format("/users/%s", userId)), headersMap);
        Result result = getResult(res);
        Assert.assertEquals(result.getEmail(), this.email, String.format("Email is wrong not as expected, expected mail is: =[%s]", this.email));
        Assert.assertEquals(result.getFirst_name(), "John", "Wrong first name");
        Assert.assertEquals(result.getLast_name(), "Doe", "Wrong last name");
    }

    @Test(priority = 25, dataProvider = "newUserDataProvider")
    public void updateExistedUser(String path, String jsonBody) throws IOException {
        String res = simpleHttpClient.sendGetRequest(path, headersMap);
        Result resultGet = getResult(res);
        simpleHttpClient = new SimpleHttpClient();
        res = simpleHttpClient.sendPutRequest(BASE_URL.concat(String.format("/users/%s", userId)), jsonBody, headersMap);
        Result result = getResult(res);
        compareUpdatedUserDetails(resultGet, result);
        Assert.assertTrue(compareUpdatedUserDetails(resultGet, result));

    }

    private boolean compareUpdatedUserDetails(Result getResult, Result updateResult) {
        return updateResult.getId().equals(userId) &&
                !getResult.getFirst_name().equals(updateResult.getFirst_name()) &&
                !getResult.getLast_name().equals(updateResult.getLast_name());
    }

    @Test(priority = 30)
    public void deleteUser() throws IOException {
        String res = simpleHttpClient.sendDeleteRequest(BASE_URL.concat(String.format("/users/%s", userId)), headersMap);
        gorest = getMeta(res);
        Assert.assertEquals("The request was handled successfully and the response contains no body content.", gorest.getMessage());
    }

    private GoRest getMeta(String res) {
        JsonObject jo = JsonHandler.asListObject(res, "_meta");
        return JsonHandler.getJsonAsClassObject(jo.toString(), GoRest.class);
    }

    private Result getResult(String res) {
        JsonObject jo = JsonHandler.asListObject(res, "result");
        return JsonHandler.getJsonAsClassObject(jo.toString(), Result.class);
    }

    @Test(priority = 40)
    public void getUsers() throws IOException {
        String res = simpleHttpClient.sendGetRequest(BASE_URL.concat("/users"), headersMap);
        JsonArray result = JsonHandler.asList(res, "result");
        List<Result> resultList = JsonHandler.getJsonAsClassObjectList(result.toString(), Result[].class);
    }

    @Test(priority = 50)
    public void getSpecificPage() throws IOException {
        String res = simpleHttpClient.sendGetRequest(BASE_URL.concat("/users?page=5"), headersMap);
        gorest = getMeta(res);
        Assert.assertEquals(gorest.getCurrentPage(), 5);

    }

    @Test(priority = 60)
    public void veirfyAuthenticationFailure() throws IOException {
        HashMap<String, String> map = new HashMap<>();
        map.put("Authorization", "Bearer iiO3ZDQpM81BIqSgW79UA2c-iCa_V0otGWZUg");
        map.put("Content-Type", "application/json");
        String res = simpleHttpClient.sendGetRequest(BASE_URL.concat("/users"), map);
        gorest = getMeta(res);
        Assert.assertEquals(gorest.getCode(), 401, "Error, it should be status code 401, not authorized");
    }

    @Test(priority = 70)
    public void getAllPosts() throws IOException {
        List<Result> resultList = getResults("/posts");
        Assert.assertTrue(resultList.size() != 0);
        for (Result result : resultList) {
            Assert.assertTrue(result.getLinks().getSelf().toString().contains("posts"));
        }
    }

    private List<Result> getResults(String path) throws IOException {
        String res = simpleHttpClient.sendGetRequest(BASE_URL.concat(path), headersMap);
        JsonArray jsonArrayResult = JsonHandler.asList(res, "result");
        gorest = getMeta(res);
        return JsonHandler.getJsonAsClassObjectList(jsonArrayResult.toString(), Result[].class);
    }

    @Test(priority = 80)
    public void getSpecificPost() throws IOException {
        List<Result> resultList = getResults("/posts");
        for (Result result : resultList) {
            String id = result.getId();
            String title = result.getTitle();
            String url = BASE_URL.concat(String.format("/posts/%s", id));
            simpleHttpClient = new SimpleHttpClient();
            String res = simpleHttpClient.sendGetRequest(url, headersMap);
            Result resultObj = getResult(res);
            Assert.assertEquals(resultObj.getTitle(), title);
            LOG.info("Success!");
        }
    }

    @Test(priority = 90)
    public void deletePost() throws IOException {
        List<Result> resultList = getResults("/posts");
        String id = resultList.get(0).getId();
        String url = BASE_URL.concat(String.format("/posts/%s", id));
        simpleHttpClient = new SimpleHttpClient();
        String res = simpleHttpClient.sendDeleteRequest(url, headersMap);
        gorest = getMeta(res);
        Assert.assertEquals(gorest.getMessage(), "The request was handled successfully and the response contains no body content.");
        Assert.assertEquals(gorest.getCode(), 204);
        simpleHttpClient = new SimpleHttpClient();
        res = simpleHttpClient.sendGetRequest(url, headersMap);
        gorest = getMeta(res);
        Result result = getResult(res);

        Assert.assertEquals(gorest.getMessage(), "The requested resource does not exist.");
        Assert.assertEquals(result.getMessage(), String.format("Object not found: %s", id));

    }

    public void getResponseAsList(String path) throws IOException {
        List<Result> resultList = getResults(path);
        for (Result result : resultList) {
            String id = result.getId();
            String body = result.getBody();
            String url = BASE_URL.concat(String.format("%s/%s", path, id));
            simpleHttpClient = new SimpleHttpClient();
            String res = simpleHttpClient.sendGetRequest(url, headersMap);
            Result resultObj = getResult(res);
            Assert.assertEquals(resultObj.getBody(), body);
            LOG.info("Success!");
        }
    }

    @Test(priority = 100)
    public void getAllCommentsAndCompareOneByOne() throws IOException {
        getResponseAsList("/comments");
    }

    @Test(priority = 110)
    public void getAllAlbumsAndCompareOneByOne() throws IOException {
        getResponseAsList("/albums");
    }

    @Test(priority = 120)
    public void getAllPhotoAndCompareOneByOne() throws IOException {
        getResponseAsList("/photos");
    }

}
