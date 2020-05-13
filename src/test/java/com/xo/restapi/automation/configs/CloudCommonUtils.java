package com.xo.restapi.automation.configs;

import com.jive.restapi.automation.cloud.data.UrlConsts;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.utilities.CommonApiUtils;
import com.jive.restapi.automation.utilities.GroupUtils;
import com.jive.restapi.generated.person.models.Blog;
import com.jive.restapi.generated.person.models.Content;
import com.jive.restapi.generated.person.models.Discussion;
import com.jive.restapi.generated.person.models.Document;
import com.jive.restapi.generated.person.models.FileModel;
import com.jive.restapi.generated.person.models.Group;
import com.jive.restapi.generated.person.models.Person;
import com.jive.restapi.generated.person.models.Place;
import com.jive.restapi.generated.person.models.Poll;
import com.jive.restapi.generated.person.models.Post;
import java.util.concurrent.TimeUnit;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CloudCommonUtils {

    private static final String DISCUSSION = "discussion";
    private static final String DOCUMENT = "document";
    private static final String BLOG_POST = "blog post";
    private static final String BLOG = "blog";
    private static final String FILE = "file";
    private static final String VIDEO = "video";
    private static final String POLL = "poll";

    public static String getBaseUrl() {
        return CommonUtils.getBaseUrl();
    }

    public static String getParentPlaceUri(Place place) {
        return getBaseUrl() + UrlConsts.CORE_API + "/places/" + place.getPlaceID();
    }

    public static String getPersonUri(Person person) {
        return getBaseUrl() + UrlConsts.CORE_API + "/people/" + person.getId();
    }

    public static String getContentUri(Content content) {
        return getBaseUrl() + UrlConsts.CORE_API + "/contents/" + content.getContentID();
    }

    public static <T> T getContentData(String contentType, String contentTag) {
        Object content = null;
        switch (contentType.toLowerCase()) {
            case DISCUSSION:
                content = FeatureRegistry.getCurrentFeature().getData(Discussion.class, contentTag);
                break;
            case DOCUMENT:
                content = FeatureRegistry.getCurrentFeature().getData(Document.class, contentTag);
                break;
            case BLOG_POST:
                content = FeatureRegistry.getCurrentFeature().getData(Post.class, contentTag);
                break;
            case BLOG:
                content = FeatureRegistry.getCurrentFeature().getData(Blog.class, contentTag);
                break;
            case VIDEO:
            case FILE:
                content = FeatureRegistry.getCurrentFeature().getData(FileModel.class, contentTag);
                break;
            case POLL:
                content = FeatureRegistry.getCurrentFeature().getData(Poll.class, contentTag);
                break;
            default:
                content = FeatureRegistry.getCurrentFeature().getData(Content.class, contentTag);
                break;
        }
        return (T) content;
    }

    public static void waitToProceed(Integer milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static String getParentId(String parent) {
        String[] parentElements = parent.split("/");
        return parentElements[parentElements.length - 1];
    }

    public static Group retrieveGroup(String groupTag) {
        if (groupTag != null) {
            return FeatureRegistry.getCurrentFeature().getData(Group.class, groupTag);
        } else {
            return GroupUtils.createOpenGroup().as(Group.class);
        }
    }
}
