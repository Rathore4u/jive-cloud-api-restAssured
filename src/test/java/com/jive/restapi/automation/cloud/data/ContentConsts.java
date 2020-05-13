package com.jive.restapi.automation.cloud.data;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ContentConsts {
    public final int TWO_THOUSAND = 2000;
    public final String STATUS_UPDATE_TEXT_WITH_LINK_SNIPPET = "<div class=\"jive-rendered-content\">"
            + "<span>%s</span>"
            + "<a class=\"generated-link\" href=\"%sexternal-link.jspa?url=%s\" rel=\"nofollow\" target=\"_blank\"> some link</a>"
            + "</div>";
    public final String STATUS_UPDATE_TEXT_WITH_PERSON_MENTION_SNIPPET = "<div class=\"jive-rendered-content\">"
            + "<span>%s</span>"
            + "<a class=\"jive-link-profile-small jiveTT-hover-user\" data-containerId=\"-1\" data-containerType=\"-1\" "
                + "data-objectId=\"2007\" data-objectType=\"3\" href=\"%speople/%s\">%s</a>"
            + "</div>";
    public final String STATUS_UPDATE_TEXT_WITH_LANGUAGE_TAG_SNIPPET = "<div class=\"jive-rendered-content\">"
            + "<a class=\"jive-link-tag-small\" href=\"%stags/#/?tags=%s\">%s</a>"
            + "</div>";
    public final String DEFAULT_LINK = "http://www.google.com";
    public final String ONE_SPACE_STRING = " ";
    public final String EMPTY_STRING = "";
    public final String CONTENT_TYPE_PNG = "image/png";
    public final String FILE_RESOURCE_TYPE = "file";
    public final String DONT_WORD = "dont";
    public final String SOME_JAPANESE_WORD = "こんにちは";
    public final String SOME_CHINESE_WORD = "你好";
    public final String SOME_THAI_WORD = "สวัสดี";
    public final String JAPANESE_LANGUAGE_WORD = "Japanese";
    public final String CHINESE_LANGUAGE_WORD = "Chinese";
    public final String THAI_LANGUAGE_WORD = "Thai";
    public final String APPROVERS_KEY_NAME = "approvers";
}
