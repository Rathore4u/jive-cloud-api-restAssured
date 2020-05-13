# jive-cloud-api-restAssured
Repo for jive cloud API based E2E Tests built with JAVA + RestAssured + Allure

### Getting started
Before starting on repo:
1. Check your settings.xml under .m2 , you can rename existing settings.xml to say "settings1.xml" so that you have no settings.xml of your own. Other Alternative is to delete it.
2. If you do step 1, make sure to restart System as well as IDE.

After cloning:
1. Run commnad `mvn clean install` if you want install and execute all tests.
2. Run commnad `mvn install -DskipTests` if you want to install without tests execution.
And see if you get `BUILD SUCCESS`.

### Running only a specific test
- Step 1: Add a unique tag (say @sample) before the scenario you want to run/test
- Step 2: run below command
```mvn -U clean test -o -Dcucumber.options="--tags @sample"```

### Writing Utils
#### Method 1: Creating restapi-common jar in IDEA cache (.m2)
Here we need to work simultaneously on two repos. 1. restapi-common 2.cdm-rest-assured
- Step 1: Switch to IDE instance where restapi-common repo is opened.
- Step 2: Write utils in respective util files as one needs.
- Step 3: Change the package version to SNAPSHOT -

```html
    <groupId>com.jive.restapi.automation.utils</groupId>
    <artifactId>restapi.automation.utils</artifactId>
    <version>0.9.18-SNAPSHOT</version>
    <packaging>jar</packaging>
```

- Step 4: Run ```mvn install -DskipTests=ture``` - This must Build successfully.
- Step 5: Switch to IDE instance where cdm-rest-assured repo is opened.
- Step 6: Update the version of restapi-common dependency

```html
    <jive.restapi.utils.version>0.9.18-SNAPSHOT</jive.restapi.utils.version>
```
- Step 7: Right click on pom.xml of cdm-rest-assured > maven > reimport
- Step 8: run ```mvn -U clean package```. You should be able to use new utils.
- Step 9: once you now utils are working as expected, create a PR for restapi-common by updating its release version -

```html
    <groupId>com.jive.restapi.automation.utils</groupId>
    <artifactId>restapi.automation.utils</artifactId>
    <version>0.9.19</version>
    <packaging>jar</packaging>
```
- Step 10: Once PR is merged, reviewer will publish the new jar to nexus. once that is done. you can newer version in cdm-rest-assured

```html
    <jive.restapi.utils.version>0.9.19</jive.restapi.utils.version>
```

#### Method 2: importing external module to current project. (Preferable)
Here we need to work on only one instance of IDEA.
- Step 1: Switch to IDE instance where cloud.api.utils repo is opened.
- Step 2: File Menu > New > Module from Existing Sources > Navigate to restapi-common directory to select pom.xml > finish import
- Step 3: Now you can write utils in the imported source and use it immediately.
- Follow Step 9-10 from Method 1

### Debugging
Step 1: Top Right section of IDEA > Click on add configuration
Step 2: On pop up fill-in the following details:

| Option | Value |
| ------------- | ------------- |
| Name | OfYourChoice |
| Main Class | `cucumber.api.cli.Main` |
| Glue | `com.jive.restapi.automation.cloud.stepdefs` |
| Feature or folder path | browse to folder where all features exisits : `*/src/test/resources/com/jive/restapi/automation/cloud/features` |
| VM Option | `-Dcucumber.options="--tags @sample"` (@sample will only execute scenarios which has @sample tag added to it) |
| Program Arguments | Blank |
| Working Directory | It should be auto-populated, if not then browse your working directory |
| Use ClassPath of Module | It should be auto-populated, if not select ```cloud.api.utils``` |
| JRE | Default Selected (1.8) |
| Shorten Command Line | Default Selected |
Step 3: Apply > you should be able to debug/add break points

### Logging (for old utilities under `/src/main/java/com/jive/restapi/automation/utilities`)
#### Logging Request:
There may be occurrences where you need to see request details. For this just add below line in your util method before
you call .execute()
```javascript
    .reqSpec(req -> req.log(LogDetail.ALL))
```

For Example:

```javascript
    public static Response searchPeople(String[] filters, String origin) {
        return JiveApiUtils.getApiClient()
                .search()
                .searchPeople()
                .filterQuery(filters)
                .originQuery(origin)
                .reqSpec(req -> req.log(LogDetail.ALL))
                .execute(Function.identity());
    }
```

#### Logging Response:
There may be occurrences where you need to see Response details. For this just add below line in your stepfDefc
after request is made
```javascript
    searchResponse.then().log().all();
```

For Example:

```javascript
    searchResponse = SearchUtils.searchPeople(
            FilterBuilder
                    .builder()
                    .addCondition("search", personData.getEmails().get(0).getValue())
                    .buildList(), origin);
    searchResponse.then().log().all();
```
### Logging (for new utilities under `/src/main/java/com/jive/restapi/automation/utilities/v3`)
#### Logging Request:
There may be occurrences where you need to see request details. For this just add below line in your util method as a parameter 
```javascript
    Options.logRequest()
```

For Example:

```javascript
    @Then("^Document (.*) is created successfully$")
    public void documentIsCreatedSuccessfully(String documentTag) {
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        createDocumentResponse = ContentUtil.getContent(
                documentData.getContentID(),
                Options.logRequest()
        );
        createDocumentResponse.then().assertThat().statusCode(SC_OK);
    }
```
#### Logging Response:
There may be occurrences where you need to see request details. For this just add below line in your util method as a parameter 
```javascript
    Options.logRequest()
```

For Example:

```javascript
    @Then("^Document (.*) is created successfully$")
    public void documentIsCreatedSuccessfully(String documentTag) {
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        createDocumentResponse = ContentUtil.getContent(
                documentData.getContentID(),
                Options.logResponse()
        );
        createDocumentResponse.then().assertThat().statusCode(SC_OK);
    }
```
#### Logging Request and Response:
There may be occurrences where you need to see both request and response details. For this just add below line in your util method as a parameters
```javascript
    Options.logRequest(), Options.logResponse()
```

For Example:

```javascript
    @Then("^Document (.*) is created successfully$")
    public void documentIsCreatedSuccessfully(String documentTag) {
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        createDocumentResponse = ContentUtil.getContent(
                documentData.getContentID(),
                Options.logRequest(),
                Options.logResponse()
        );
        createDocumentResponse.then().assertThat().statusCode(SC_OK);
    }
```
### Extra parameters
 If you need special optional parameters to be passed you can add below line in your util method
 ```javascript
    Options.custom(op -> op.mySpecialParameter("value")
 ```
 
 For example you want to add a comment to document using endpoint `POST /contents/{contentID}/comments`.
 You call this function:
 
 ```javascript
    ContentUtil.createComment(
        document.getContentID(),
        ContentConstants.getDefaultCommentData()
    );
 ```
Then you want to add an author comment to document using endpoint `POST contents/{contentID}/comments?author=true`.
For this case you should use extra parameter:
```javascript
    ContentUtil.createComment(
        document.getContentID(),
        ContentConstants.getDefaultCommentData(),
        Options.custom(op -> op.authorQuery("true"))
    );
```
Try intellisense on `op` object and it will give you all available options. These options have been generated from documentation. Any request parameter `xyz` will correspond to `xyzQuery` method. The same way a header will correspond to `xyzHeader` method.

All available parameters you can find in corresponding `Oper` class. For example, for `createComment` method you can find all available parameters in `ContentApi.CreateCommentOper` class.

### Object extraction from response
#### Response contains one object
For example you get response using this code:
```javascript
    Response commentCreationResponse = ContentUtil.createComment(
            document.getContentID(),
            ContentConstants.getDefaultCommentData()
    );
```
Then you can extract `Comment` object using this code:
```javascript
    Comment comment = commentCreationResponse.as(Comment.class);
```
#### Response contains a list of objects
For example you get response using this code:
```javascript
    Response commentsResponse = ContentUtil.comments(
            document.getContentID()
    );
```
Then you can extract collection of `Comment` objects using this code:
```javascript
    CommentEntities commentEntities = commentsResponse.as(CommentEntities.class);
```
Then you can get Java `List<Comment>` object form this collection using this code:
```javascript
    val commentList = commentEntities.getList();
```

Generally list for `Something` are handled by `SomethingEntities`.
For example for `Document` there is `DocumentEntities` class.
