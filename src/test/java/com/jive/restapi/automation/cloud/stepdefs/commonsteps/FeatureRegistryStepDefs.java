package com.jive.restapi.automation.cloud.stepdefs.commonsteps;

import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.cloud.data.PlaceTypeEnum;
import com.jive.restapi.generated.person.models.Content;
import com.jive.restapi.generated.person.models.Content.TypeEnum;
import com.jive.restapi.generated.person.models.Document;
import com.jive.restapi.generated.person.models.FileModel;
import com.jive.restapi.generated.person.models.Group;
import com.jive.restapi.generated.person.models.Place;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FeatureRegistryStepDefs {

    public Place getPlaceData(String placeTag) {
        Place placeData = null;

        if (placeTag.toLowerCase().contains(PlaceTypeEnum.SPACE.toString().toLowerCase())) {
            placeData = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        } else if (placeTag.toLowerCase().contains(PlaceTypeEnum.GROUP.toString().toLowerCase())) {
            placeData = FeatureRegistry.getCurrentFeature().getData(Group.class, placeTag);
        }

        return placeData;
    }

    public Content getContentData(String contentTag) {
        Content contentData = null;

        if (contentTag.toLowerCase().contains(TypeEnum.FILE.getValue().toLowerCase())) {
            contentData = FeatureRegistry.getCurrentFeature().getData(FileModel.class, contentTag);
        }
        if (contentTag.toLowerCase().contains(TypeEnum.DOCUMENT.toString().toLowerCase())) {
            contentData = FeatureRegistry.getCurrentFeature().getData(Document.class, contentTag);
        }

        return contentData;
    }
}

