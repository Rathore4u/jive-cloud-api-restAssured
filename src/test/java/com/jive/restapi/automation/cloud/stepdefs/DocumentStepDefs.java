package com.jive.restapi.automation.cloud.stepdefs;

import static com.xo.restapi.automation.configs.CloudCommonUtils.getBaseUrl;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertTrue;

import com.google.common.html.HtmlEscapers;
import com.jive.restapi.automation.cloud.data.EntityConstants;
import com.jive.restapi.automation.cloud.data.NumberConstants;
import com.jive.restapi.automation.cloud.data.SearchFilterConstants;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.data.ContentConsts;
import com.jive.restapi.automation.utilities.CommentUtils;
import com.jive.restapi.automation.utilities.ContentConstants;
import com.jive.restapi.automation.utilities.DocumentConstants;
import com.jive.restapi.automation.utilities.DocumentUtils;
import com.jive.restapi.automation.utilities.FilterBuilder;
import com.jive.restapi.automation.utilities.GroupUtils;
import com.jive.restapi.automation.utilities.OutcomeConstants;
import com.jive.restapi.automation.utilities.PeopleUtils;
import com.jive.restapi.automation.utilities.PlacesUtils;
import com.jive.restapi.automation.utilities.SearchUtils;
import com.jive.restapi.automation.utilities.StandardOutcomeTypes;
import com.jive.restapi.automation.utilities.v3.ContentUtil;
import com.aurea.automation.openapi.common.Options;
import com.jive.restapi.automation.utilities.v3.OutcomesUtil;
import com.jive.restapi.generated.person.models.Comment;
import com.jive.restapi.generated.person.models.CommentEntities;
import com.jive.restapi.generated.person.models.Content;
import com.jive.restapi.generated.person.models.Content.StatusEnum;
import com.jive.restapi.generated.person.models.ContentBody;
import com.jive.restapi.generated.person.models.ContentEntities;
import com.jive.restapi.generated.person.models.Document;
import com.jive.restapi.generated.person.models.DocumentEntities;
import com.jive.restapi.generated.person.models.Group;
import com.jive.restapi.generated.person.models.Image;
import com.jive.restapi.generated.person.models.Outcome;
import com.jive.restapi.generated.person.models.Person;
import com.jive.restapi.generated.person.models.Place;
import com.xo.restapi.automation.configs.CloudCommonUtils;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.val;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.Assert;

public class DocumentStepDefs {

    private Response createDocumentResponse;
    private Response searchResponse;
    private Response commentCreationResponse;
    private Response documentOutcomeResponse;
    private final String umlauts = "für alle & jeden zum testen ä ö ü";
    private Response contentOutcomeResponse;
    private Content documentData;

    @When("^Logged in user requests to create document (.*) with tags (.*)$")
    public void userCreatesADocumentWithTags(String documentTag, String tag) {
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        Document documentData = ContentConstants.getDefaultDocumentData();
        documentData.setSubject(documentData.getSubject() + RandomStringUtils.randomAlphanumeric(10));
        documentData.setTags(tagList);
        createDocumentResponse = DocumentUtils.createDocumentToReturnResponse(documentData);
        FeatureRegistry.getCurrentFeature()
                .setData(Document.class, documentTag, createDocumentResponse.as(Document.class));
        FeatureRegistry.getCurrentFeature()
                .setData(Content.class, documentTag, createDocumentResponse.as(Content.class));
    }

    @Then("^Document (.*) is created successfully$")
    public void documentIsCreatedSuccessfully(String documentTag) {
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        createDocumentResponse = DocumentUtils.getResponseOfDocumentById(documentData.getContentID());
        createDocumentResponse.then().assertThat().statusCode(SC_OK);
    }

    @When("^Logged in user requests to update document (.*) with tags (.*)$")
    public void userUpdatesADocument(String documentTag, String tag) {
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        documentData.setTags(tagList);
        createDocumentResponse = DocumentUtils.updateDocumentToReturnResponse(documentData);
        FeatureRegistry.getCurrentFeature()
                .setData(Document.class, documentTag, createDocumentResponse.as(Document.class));
    }

    @Then("^Document (.*) is updated with tags (.*) successfully$")
    public void documentIsUpdatedSuccessfully(String documentTag, String tag) {
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        Assert.assertTrue(documentData.getTags().contains(tag));
    }

    @When("User requests to get document (.*) by outcome (.*) from place (.*)")
    public void getDocumentByOutcomeFromPlace(String contentTag, String outcomeTag, String placeTag)
            throws InterruptedException {
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, contentTag);
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        for (int counter = NumberConstants.zero; counter < NumberConstants.fifteen; counter++) {
            searchResponse = PlacesUtils.getContentWithFilters(place.getPlaceID(),
                    FilterBuilder
                            .builder()
                            .addCondition(SearchFilterConstants.searchFilterKey, documentData.getSubject())
                            .addCondition(SearchFilterConstants.outcomeTypeFilterKey, "\"" + outcomeTag + "\"")
                            .buildList());
            if (searchResponse.as(ContentEntities.class).getList().size() != NumberConstants.zero) {
                break;
            }
            // Added 2 sec sleep for content to index
            Thread.sleep(NumberConstants.twoThousand);
        }
    }

    @Then("^Document (.*) is searched successfully$")
    public void documentIsSearched(String documentTag) {
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        Assert.assertTrue(searchResponse.as(DocumentEntities.class)
                .getList()
                .stream()
                .anyMatch(c -> c.getSubject().equals(documentData.getSubject())));
    }

    @When("User request to delete document (.*)")
    public void deleteDocument(String documentTag) {
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        searchResponse = DocumentUtils.deleteDocument(documentData.getContentID());
    }

    @Then("^Document (.*) is deleted successfully$")
    public void documentIsDeleted(String documentTag) {
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        searchResponse.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("^Request to create a document (.*) under place(?: )?(\\S+)?$")
    public void userCreatesADocumentUnderPlace(String documentName, String groupTag) {
        Group documentGroup;
        documentGroup = CloudCommonUtils.retrieveGroup(groupTag);
        Document documentData = ContentConstants.getDefaultDocumentDataUnderPlace(documentGroup);
        documentData.setSubject(documentName);
        createDocumentResponse = DocumentUtils.createDocumentToReturnResponse(documentData);
        FeatureRegistry.getCurrentFeature()
                .setData(Document.class, documentName, createDocumentResponse.as(Document.class));
    }

    @When("^user creates a umlauts Document (.*) with tags (.*)$")
    public void userCreatesUmlautsDocument(String doc, String tag) {
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        Document documentData = ContentConstants.getDefaultDocumentData();
        documentData.content(new ContentBody().text(HtmlEscapers.htmlEscaper().escape(umlauts))).subject(umlauts);
        documentData.setSubject(documentData.getSubject() + RandomStringUtils.randomAlphanumeric(10));
        documentData.setTags(tagList);
        createDocumentResponse = DocumentUtils.createDocumentToReturnResponse(documentData);
        FeatureRegistry.getCurrentFeature().setData(Document.class, doc, createDocumentResponse.as(Document.class));
    }

    @When("^user updates a Document (.*) with Umlauts$")
    public void userUpdatesDocumentWithUmlauts(String doc) {
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, doc);
        String subject = umlauts + RandomStringUtils.randomAlphanumeric(10);
        documentData.content(new ContentBody().text(HtmlEscapers.htmlEscaper().escape(umlauts))).subject(subject);
        createDocumentResponse = DocumentUtils.updateDocumentToReturnResponse(documentData);
        FeatureRegistry.getCurrentFeature().setData(Document.class, doc, createDocumentResponse.as(Document.class));
    }

    @Then("^Document (.*) is updated with umlauts successfully$")
    public void documentIsUpdatedWithUmlautsSuccessfully(String doc) {
        String umlautsedit = "jeden zum testen";
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, doc);
        Assert.assertTrue(documentData.getSubject().contains(umlautsedit));
        Assert.assertTrue(documentData.getContent().getText().contains(umlautsedit));
    }

    @Then("^Logged In User Add (.*) User to Document (.*)")
    public void addAuthorToContent(String userTag, String documentTag) throws Throwable {
        Person person = FeatureRegistry.getCurrentFeature().getData(Person.class, userTag);
        Response documentGroup = GroupUtils.createOpenGroup();
        Document documentData = ContentConstants
                .getDefaultDocumentDatawithExtendedAuthors(documentGroup.as(Place.class), person);
        Response res = DocumentUtils.createDocumentToReturnResponse(documentData);
        res.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(Document.class, documentTag, res.as(Document.class));
    }

    @And("^User (.*) is created$")
    public void userBZUIsCreated(String userTag) throws Throwable {
        Person newPerson = PeopleUtils.newPerson();
        Response resp = PeopleUtils.createPerson(newPerson);
        resp.then().statusCode(HttpStatus.SC_CREATED);
        newPerson.setId(resp.as(Person.class).getId());
        FeatureRegistry.getCurrentFeature().setData(Person.class, userTag, newPerson);
    }

    @When("^(?:admin|user)?(?: )?Request to create a document (\\S+)$")
    public void userCreatesADocument(String documentTag) {
        Document documentData = ContentConstants.getDefaultDocumentData();
        documentData.setSubject(documentData.getSubject() + RandomStringUtils.randomAlphanumeric(10));
        createDocumentResponse = DocumentUtils.createDocumentToReturnResponse(documentData);
        FeatureRegistry.getCurrentFeature()
                .setData(Document.class, documentTag, createDocumentResponse.as(Document.class));
    }

    @When("^(?:admin|user) request to search document (.*)$")
    public void userRequestSearchDocument(String documentTag) {
        Document document = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        Content searchedDocument = DocumentUtils.getDocumentById(document.getContentID());
    }

    @When("^User request to create a document (\\S+)$")
    public void userCreateDocument(String documentTag) {
        Document documentData = ContentConstants.getDefaultDocumentData();
        documentData.setSubject(documentData.getSubject() + RandomStringUtils.randomAlphanumeric(10));
        createDocumentResponse = DocumentUtils.createDocumentToReturnResponse(documentData);
        FeatureRegistry.getCurrentFeature()
                .setData(Document.class, documentTag, createDocumentResponse.as(Document.class));
    }

    @Then("^It should returns document (.*)$")
    public void userVerifySearchedDocument(String documentTag) {
        Document document = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        Assert.assertTrue(document.getSubject().equals(document.getSubject()));
    }

    @When("^Add a comment (.*) to the document (.*)$")
    public void addCommentToDocument(String commentTag, String documentTag) {
        Document document = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        commentCreationResponse = CommentUtils.createComment(ContentConstants.getDefaultCommentData(), document);
        FeatureRegistry.getCurrentFeature()
                .setData(Comment.class, commentTag, commentCreationResponse.as(Comment.class));
    }

    @Then("^Comment has been added to the document successfully$")
    public void commentAddedSuccessfully() {
        commentCreationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("^Get comments from the document (.*) to the list (.*)$")
    public void getCommentsFromDocument(String documentTag, String listTag) {
        Document document = FeatureRegistry.getCurrentFeature().getData(
                Document.class,
                documentTag);

        Response commentsResponse = ContentUtil.comments(
                document.getContentID());

        FeatureRegistry.getCurrentFeature().setData(
                CommentEntities.class,
                listTag,
                commentsResponse.as(CommentEntities.class));
    }

    @When("^Logged in user requests to create Document (.*) with description (.*) under place$")
    public void userCreatesADocumentWithDescriptionUnderPlace(String documentTag, String description) {
        Response openGroupCreationResponse = GroupUtils.createOpenGroup();
        Document documentData = ContentConstants.getDefaultDocumentData();
        documentData.content((new ContentBody()).text(description));
        documentData.setParent(CloudCommonUtils.getParentPlaceUri(openGroupCreationResponse.as(Place.class)));
        documentData.setVisibility(Content.VisibilityEnum.PLACE);
        documentData.setSubject(documentData.getSubject() + RandomStringUtils.randomAlphanumeric(10));
        createDocumentResponse = DocumentUtils.createDocumentToReturnResponse(documentData);
        FeatureRegistry.getCurrentFeature()
                .setData(Document.class, documentTag, createDocumentResponse.as(Document.class));
    }

    @When("^Logged in user requests to create Document (.*) with description (.*) in community$")
    public void userCreatesADocumentWithDescription(String documentTag, String description) {
        Document documentData = ContentConstants.getDefaultDocumentData();
        documentData.content((new ContentBody()).text(description));
        documentData.setSubject(documentData.getSubject() + RandomStringUtils.randomAlphanumeric(10));
        createDocumentResponse = DocumentUtils.createDocumentToReturnResponse(documentData);
        FeatureRegistry.getCurrentFeature()
                .setData(Document.class, documentTag, createDocumentResponse.as(Document.class));
    }

    @Then("outcome (.*) should be created in document (.*)")
    public void officialOutcomeShouldBeCreatedInDocument(String officialTag, String contentTag) {
        Outcome officialData = FeatureRegistry.getCurrentFeature().getData(Outcome.class, officialTag);
        Document contentData = FeatureRegistry.getCurrentFeature().getData(Document.class, contentTag);
        Assert.assertTrue(contentData.getOutcomeTypes().contains(officialData.getOutcomeType()));
    }

    @When("user request to mark document (.*) as Official (.*)")
    public void markDocumentAsOfficialOutcome(String documentTag, String officialTag) {
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        Outcome outcomeData = (Outcome) new Outcome()
                .outcomeType(StandardOutcomeTypes.OFFICIAL.getOutcomeType());
        documentOutcomeResponse = ContentUtil.createOutcome(documentData.getContentID(), outcomeData);
        documentOutcomeResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature()
                .setData(Outcome.class, officialTag, documentOutcomeResponse.as(Outcome.class));
    }

    @Then("outcome (.*) should be created in (.*)")
    public void officialOutcomeShouldBeCreated(String officialTag, String documentTag) {
        Outcome officialData = FeatureRegistry.getCurrentFeature().getData(Outcome.class, officialTag);
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        Assert.assertTrue(documentData.getOutcomeTypes().contains(officialData.getOutcomeType()));
    }

    @Then("outcome (.*) should be removed in (.*)")
    public void officialOutcomeShouldBeRemoved(String officialTag, String documentTag) {
        Outcome officialData = FeatureRegistry.getCurrentFeature().getData(Outcome.class, officialTag);
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        Assert.assertFalse(documentData.getOutcomeTypes().contains(officialData.getOutcomeType()));
    }

    @When("^Logged in user requests to update document (.*) with title (.*)$")
    public void userUpdatesADocumentTitle(String documentTag, String titleTag) {
        String title = RandomStringUtils.randomAlphanumeric(10);
        FeatureRegistry.getCurrentFeature().setData(String.class, titleTag, title);
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        documentData.setSubject(title);
        createDocumentResponse = DocumentUtils.updateDocumentToReturnResponse(documentData);
        FeatureRegistry.getCurrentFeature()
                .setData(Document.class, documentTag, createDocumentResponse.as(Document.class));
    }

    @Then("^Document (.*) is updated with title (.*) successfully$")
    public void documentIsUpdatedWithTitleSuccessfully(String documentTag, String tag) {
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        String updatedTitle = FeatureRegistry.getCurrentFeature().getData(String.class, tag);
        Assert.assertTrue(documentData.getSubject().contains(updatedTitle));
    }

    @When("user request to mark document (.*) as Outdated (.*)")
    public void markDocumentAsOutdatedOutcome(String documentTag, String outdatedTag) {
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        Outcome outcomeData = (Outcome) new Outcome()
                .outcomeType(StandardOutcomeTypes.OUTDATED.getOutcomeType());
        documentOutcomeResponse = ContentUtil.createOutcome(documentData.getContentID(), outcomeData);
        documentOutcomeResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature()
                .setData(Outcome.class, outdatedTag, documentOutcomeResponse.as(Outcome.class));
    }

    @When("User request to search document (.*) by outcome (.*) from (.*)")
    public void searchByDocumentOutcome(String documentTag, String outcomeTag, String origin)
            throws InterruptedException {
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        for (int counter = NumberConstants.zero; counter < NumberConstants.fifteen; counter++) {
            searchResponse = SearchUtils.searchContent(
                    FilterBuilder
                            .builder()
                            .addCondition(SearchFilterConstants.searchFilterKey, documentData.getSubject())
                            .addCondition(SearchFilterConstants.outcomeTypeFilterKey, outcomeTag)
                            .buildList(), origin);
            if (searchResponse.as(DocumentEntities.class).getList().size() != NumberConstants.zero) {
                break;
            }
            // Added 2 sec sleep for content to index
            Thread.sleep(NumberConstants.twoThousand);
        }
    }

    @When("^User (.*) mark content document (.*) as Mark for Action (.*)$")
    public void userMarkForAction(String userName, String documentTag, String outcomeName) {
        Document document = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        Outcome outcomeData = (Outcome) new Outcome()
                .outcomeType(StandardOutcomeTypes.PENDING.getOutcomeType());
        contentOutcomeResponse = ContentUtil.createOutcome(document.getContentID(), outcomeData);
        FeatureRegistry.getCurrentFeature()
                .setData(Outcome.class, outcomeName, contentOutcomeResponse.as(Outcome.class));
    }

    @Then("Verify content document is Mark for action (.*)$")
    public void verifyUserMarkForAction(String outcomeName) {
        contentOutcomeResponse.then().assertThat().statusCode(SC_CREATED);
        Assert.assertEquals(contentOutcomeResponse.as(Outcome.class).getOutcomeType().getName(),
                StandardOutcomeTypes.PENDING.getOutcomeType().getName());
    }

    @When("^User (.*) mark content (.*) resolved, which is marked for action (.*)$")
    public void userMarkContentResolved(String userName, String documentTag, String outcomeName) {
        Outcome outcome = FeatureRegistry.getCurrentFeature().getData(Outcome.class, outcomeName);
        Outcome outcomeData = (Outcome) new Outcome()
                .outcomeType(StandardOutcomeTypes.RESOLVED.getOutcomeType());
        contentOutcomeResponse = OutcomesUtil.createOutcome(outcome.getId(), outcomeData, Options.logResponse());
        FeatureRegistry.getCurrentFeature()
                .setData(Outcome.class, outcomeName, contentOutcomeResponse.as(Outcome.class));
    }

    @Then("Verify content is marked as resolved (.*)$")
    public void verifyUserMarkContentResolved(String outcomeName) {
        contentOutcomeResponse.then().assertThat().statusCode(SC_CREATED);
        Assert.assertEquals(contentOutcomeResponse.as(Outcome.class).getOutcomeType().getName(),
                StandardOutcomeTypes.RESOLVED.getOutcomeType().getName());
    }

    @When("Logged in user Like document (.*)")
    public void likeDocument(String documentTag) {
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        Response like = ContentUtil.contentLikes(documentData.getContentID());
        like.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("Logged in user remove Like added on document (.*)")
    public void removeLikeOnDocument(String documentTag) {
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        Response like = ContentUtil.deleteContentLike(documentData.getContentID());
        like.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("^User requests to create a draft Document (\\S+)$")
    public void createDraftDocument(String documentTag) {
        Document documentData = ContentConstants.getDefaultDocumentData();
        documentData.setStatus(Content.StatusEnum.INCOMPLETE);
        createDocumentResponse = DocumentUtils.createDocumentToReturnResponse(documentData);
        FeatureRegistry.getCurrentFeature()
                .setData(Document.class, documentTag, createDocumentResponse.as(Document.class));
    }

    @When("^Mark document (.*) as \"Final\" and get outcome (.*)$")
    public void markDocumentAsFinal(String documentTag, String outcomeTag) {
        Document document = FeatureRegistry.getCurrentFeature().getData(
                Document.class,
                documentTag);

        documentOutcomeResponse = ContentUtil.createOutcome(
                document.getContentID(),
                OutcomeConstants.getDefaultOutcomeDataWithType(StandardOutcomeTypes.FINALIZED));

        FeatureRegistry.getCurrentFeature().setData(
                Outcome.class,
                outcomeTag,
                documentOutcomeResponse.as(Outcome.class));
    }

    @Then("^Outcome (.*) is created for document (.*)$")
    public void verifyOutcomeIsCreatedForDocument(String outcomeTag, String documentTag) {
        Outcome outcome = FeatureRegistry.getCurrentFeature().getData(
                Outcome.class,
                outcomeTag);
        Document document = FeatureRegistry.getCurrentFeature().getData(
                Document.class,
                documentTag);

        assertTrue(document.getOutcomeTypes().contains(outcome.getOutcomeType()));
    }

    @When("^User requests to create a Document (\\S+) with (\\S+) mention and (\\S+) mention$")
    public void createDocumentWithMentions(String documentTag, String groupTag, String personTag) {
        Group groupData = FeatureRegistry.getCurrentFeature().getData(Group.class, groupTag);
        Person personData = FeatureRegistry.getCurrentFeature().getData(Person.class, personTag);
        String groupMention = PlacesUtils.createPlaceMacro(SearchFilterConstants.GROUP_MACRO, groupData);
        String personMention = PeopleUtils.createUserMention(personData);
        Content documentData = ContentConstants
                .getDefaultContentDataWithTagMention(Content.TypeEnum.DISCUSSION, groupMention + " " + personMention);
        createDocumentResponse = DocumentUtils.createDocumentToReturnResponse(documentData);
        FeatureRegistry.getCurrentFeature()
                .setData(Document.class, documentTag, createDocumentResponse.as(Document.class));
    }

    @When("^Create a document (.*) with \"Specific People\" publish location:$")
    public void createDocumentWithSpecificPeople(String documentTag, List<String> usersTags) {
        val users = new ArrayList<Person>();
        for (String userTag : usersTags) {
            Person user = FeatureRegistry.getCurrentFeature().getData(Person.class, userTag);
            users.add(user);
        }

        createDocumentResponse = ContentUtil.createContent(
                DocumentConstants.getDefaultDocumentDataWithSpecificPeople(users),
                null);

        FeatureRegistry.getCurrentFeature().setData(
                Document.class,
                documentTag,
                createDocumentResponse.as(Document.class));
    }

    @When("^Create a document (.*) with \"Specific People\" publish location and authors:$")
    public void createDocumentWithSpecificPeopleAndAuthors(String documentTag, List<String> usersTags) {
        val users = new ArrayList<Person>();
        for (String userTag : usersTags) {
            Person user = FeatureRegistry.getCurrentFeature().getData(Person.class, userTag);
            users.add(user);
        }

        createDocumentResponse = ContentUtil.createContent(
                DocumentConstants.getDefaultDocumentDataWithSpecificPeopleAndAuthors(users),
                null, Options.logResponse(),Options.logRequest());

        createDocumentResponse.then().assertThat().statusCode(SC_CREATED);

        FeatureRegistry.getCurrentFeature().setData(
                Document.class,
                documentTag,
                createDocumentResponse.as(Document.class));
    }

    @When("^Create a draft document (.*) with \"Specific People\" publish location:$")
    public void createDraftDocumentWithSpecificPeople(String documentTag, List<String> usersTags) {
        val users = new ArrayList<Person>();
        for (String userTag : usersTags) {
            Person user = FeatureRegistry.getCurrentFeature().getData(Person.class, userTag);
            users.add(user);
        }

        val documentData = DocumentConstants.getDefaultDocumentDataWithSpecificPeople(users);
        documentData.setStatus(StatusEnum.INCOMPLETE);
        createDocumentResponse = ContentUtil.createContent(
                documentData,
                null
        );
        FeatureRegistry.getCurrentFeature().setData(
                Document.class,
                documentTag,
                createDocumentResponse.as(Document.class)
        );
        FeatureRegistry.getCurrentFeature().setData(
                Content.class,
                documentTag,
                createDocumentResponse.as(Content.class)
        );
    }

    @When("^Create a draft document (.*) with \"Specific People\" publish location and authors:$")
    public void createDraftDocumentWithSpecificPeopleAndAuthors(String documentTag, List<String> usersTags) {
        val users = new ArrayList<Person>();
        for (String userTag : usersTags) {
            Person user = FeatureRegistry.getCurrentFeature().getData(Person.class, userTag);
            users.add(user);
        }

        val documentData = DocumentConstants.getDefaultDocumentDataWithSpecificPeopleAndAuthors(users);
        documentData.setStatus(StatusEnum.INCOMPLETE);
        createDocumentResponse = ContentUtil.createContent(
                documentData,
                null
        );
        FeatureRegistry.getCurrentFeature().setData(
                Document.class,
                documentTag,
                createDocumentResponse.as(Document.class)
        );
        FeatureRegistry.getCurrentFeature().setData(
                Content.class,
                documentTag,
                createDocumentResponse.as(Content.class)
        );
    }

    @When("^Add an author comment (.*) to the document (.*)$")
    public void addAuthorCommentToDocument(String commentTag, String documentTag) {
        Document document = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);

        commentCreationResponse = ContentUtil.createComment(
                document.getContentID(),
                ContentConstants.getDefaultCommentData(),
                Options.custom(op -> op.authorQuery("true")));

        commentCreationResponse.then().assertThat().statusCode(SC_CREATED);

        FeatureRegistry.getCurrentFeature().setData(
                Comment.class,
                commentTag,
                commentCreationResponse.as(Comment.class));
    }

    @When("^Get author comments from the document (.*) to the list (.*)$")
    public void getAuthorCommentsFromDocument(String documentTag, String listTag) {
        Document document = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);

        Response commentsResponse = ContentUtil.comments(
                document.getContentID(),
                Options.custom(op -> op.authorQuery("true")));

        FeatureRegistry.getCurrentFeature().setData(
                CommentEntities.class,
                listTag,
                commentsResponse.as(CommentEntities.class));
    }

    @When("^Search document (.*) by subject from (.*) using (.*) attempts$")
    public void searchByDocumentSubject(String documentTag, String origin, String attemptsQty)
            throws InterruptedException {
        Document documentData = FeatureRegistry.getCurrentFeature().getData(
                Document.class,
                documentTag);

        for (int counter = 0; counter < Integer.parseInt(attemptsQty); counter++) {
            searchResponse = SearchUtils.searchContent(
                    FilterBuilder
                            .builder()
                            .addCondition(SearchFilterConstants.searchFilterKey, documentData.getSubject())
                            .buildList(), origin);
            if (!searchResponse.as(DocumentEntities.class).getList().isEmpty()) {
                break;
            }
            Thread.sleep(2000);
        }
    }

    @Then("^Document (.*) is not found$")
    public void documentNotFound(String documentTag) {
        Document document = FeatureRegistry.getCurrentFeature().getData(
                Document.class,
                documentTag);

        searchResponse.then().assertThat().statusCode(SC_OK);
        assertTrue(String.format(ValidationConstants.DOCUMENT_NOT_FOUND_VALIDATION, document.getSubject()),
                searchResponse.as(DocumentEntities.class).getList().isEmpty());
    }

    @When("^Update a document (.*) by new title (.*) and save as a draft with \"Specific People\" publish location:$")
    public void updateDocumentByTitleWithSpecificPeople(String documentTag, String titleTag, List<String> usersTags) {
        val users = usersTags
                .stream()
                .map(userTag -> FeatureRegistry.getCurrentFeature().getData(Person.class, userTag))
                .collect(Collectors.toList());
        Document documentData = FeatureRegistry.getCurrentFeature().getData(
                Document.class,
                documentTag);
        String newTitle = documentData.getSubject() + DocumentConstants.UPDATED_SUBJECT_POSTFIX;
        documentData
                .subject(newTitle)
                .users(users)
                .setStatus(StatusEnum.INCOMPLETE);

        createDocumentResponse = ContentUtil.updateContent(
                documentData.getContentID(),
                documentData);

        FeatureRegistry.getCurrentFeature().setData(
                String.class,
                titleTag,
                newTitle);
        FeatureRegistry.getCurrentFeature().setData(
                Document.class,
                documentTag,
                createDocumentResponse.as(Document.class));
    }

    @When("^Update a document (.*) by new \"Specific People\" publish location list:$")
    public void updateDocumentByNewSpecificPeopleList(String documentTag, List<String> usersTags) {
        val users = usersTags
                .stream()
                .map(userTag -> FeatureRegistry.getCurrentFeature().getData(Person.class, userTag))
                .collect(Collectors.toList());
        Document documentData = FeatureRegistry.getCurrentFeature().getData(
                Document.class,
                documentTag);
        documentData.users(users);

        createDocumentResponse = ContentUtil.updateContent(
                documentData.getContentID(),
                documentData);

        FeatureRegistry.getCurrentFeature().setData(
                Document.class,
                documentTag,
                createDocumentResponse.as(Document.class));
    }

    @Then("^Document (.*) doesn't contain following users:$")
    public void documentDoesntContainUsers(String documentTag, List<String> usersTags) {
        Document documentData = FeatureRegistry.getCurrentFeature().getData(
                Document.class,
                documentTag);

        boolean documentDoesntContainUsers = true;
        val documentUsersList = documentData.getUsers();
        if (CollectionUtils.isNotEmpty(documentUsersList)) {
            documentDoesntContainUsers = usersTags
                    .stream()
                    .noneMatch(user -> documentUsersList.contains(user));
        }
        assertTrue(ValidationConstants.DOCUMENT_DOESNT_CONTAIN_USERS_VALIDATION,
                documentDoesntContainUsers);
    }

    @When("^User move document (\\S+) to group (\\S+)$")
    public void userMoveDocumentToGroup(String documentTag, String groupTag) {
        Document document = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        Group group = FeatureRegistry.getCurrentFeature().getData(Group.class, groupTag);

        document.setParent(CloudCommonUtils.getParentPlaceUri(group));
        document.setVisibility(Content.VisibilityEnum.PLACE);
        createDocumentResponse = ContentUtil.updateContent(document.getContentID(), document);
        FeatureRegistry.getCurrentFeature().setData(Document.class, documentTag, document);
    }

    @Then("^Document (\\S+) moved successfully$")
    public void verifyDocumentMovedSuccessfully(String documentTag) {
        createDocumentResponse.then().assertThat().statusCode(SC_OK);
        Document document = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        Document documentUpdated = createDocumentResponse.as(Document.class);
        assertTrue(
                ValidationConstants.getErrorMessage(EntityConstants.DOCUMENT, ValidationConstants.UPDATED),
                document.getParent().equals(documentUpdated.getParent())
        );
    }

    @When("^Create (.*) with Image (.*) in RTE In Place (.*)$")
    public void createDocumentWithImageInRTE(String contentTag, String imageTag, String groupTag) throws Throwable {
        Place group = FeatureRegistry.getCurrentFeature().getData(Place.class, groupTag);
        Image imageData = FeatureRegistry.getCurrentFeature().getData(Image.class, imageTag);

        val imageURL1 = String.format(ContentConsts.SHOW_IMAGE_SERVER_URL,
                getBaseUrl(), imageData.getId());
        val imageURL2 = String.format(ContentConsts.DOWNLOAD_IMAGE_SERVER_URL,
                getBaseUrl(), imageData.getId());

        Content documentData = ContentConstants.getDefaultContentDataWithImageInRTE
                (Content.TypeEnum.DOCUMENT, imageURL1, imageURL2);
        documentData.setParent(CloudCommonUtils.getParentPlaceUri(group));
        documentData.setVisibility(Content.VisibilityEnum.PLACE);
        createDocumentResponse = ContentUtil.createContent(documentData, null);
        FeatureRegistry.getCurrentFeature()
                .setData(Content.class, contentTag, createDocumentResponse.as(Content.class));
    }

    @When("^Create Content (.*) with Image (.*) in RTE In Community$")
    public void createDocumentWithRTEImageInCommunity(String contentTag, String imageTag) throws Throwable {
        getImageInContentRTE(imageTag);
        createDocumentResponse = ContentUtil.createContent(documentData, null);
        FeatureRegistry.getCurrentFeature()
                .setData(Content.class, contentTag, createDocumentResponse.as(Content.class));
    }

    @When("^Update a document (.*) with Image (.*) In RTE$")
    public void updateSpecificPeopleDocumentWithImageInRTE(String contentTag, String imageTag) throws Throwable {
        Content document = FeatureRegistry.getCurrentFeature().getData(Content.class, contentTag);
        getImageInContentRTE(imageTag);
        createDocumentResponse = ContentUtil.updateContent(document.getContentID(), documentData);
        FeatureRegistry.getCurrentFeature().setData(
                Content.class,
                contentTag,
                createDocumentResponse.as(Content.class)
        );
    }

    @When("^Create Content (.*) with Image (.*) in Hidden Publish Location$")
    public void createDocumentAsHiddenWithImageInRTE(String contentTag, String imageTag) throws Throwable {
        getImageInContentRTE(imageTag);
        documentData.setVisibility(Content.VisibilityEnum.HIDDEN);
        createDocumentResponse = ContentUtil.createContent(documentData, null);
        FeatureRegistry.getCurrentFeature()
                .setData(Content.class, contentTag, createDocumentResponse.as(Content.class));
    }

    private void getImageInContentRTE(String imageTag) {
        Image imageData = FeatureRegistry.getCurrentFeature().getData(Image.class, imageTag);

        val imageURL1 = String.format(ContentConsts.SHOW_IMAGE_SERVER_URL,
                getBaseUrl(), imageData.getId());
        val imageURL2 = String.format(ContentConsts.DOWNLOAD_IMAGE_SERVER_URL,
                getBaseUrl(), imageData.getId());

        documentData = ContentConstants.getDefaultContentDataWithImageInRTE
                (Content.TypeEnum.DOCUMENT, imageURL1, imageURL2);
    }
}
