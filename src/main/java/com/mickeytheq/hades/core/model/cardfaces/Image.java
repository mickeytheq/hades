package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.CardDimensions;
import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.CardModelUtils;
import com.mickeytheq.hades.core.model.common.HasPortraitModel;
import com.mickeytheq.hades.core.model.common.PortraitModel;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.util.shape.DimensionEx;
import resources.Language;

@Model(typeCode = "Image", version = 1)
public class Image extends BaseCardFaceModel implements HasPortraitModel {
    private final PortraitModel portraitModel;
    private final ImageFieldsModel imageFieldsModel;

    public Image() {
        this.portraitModel = new PortraitModel();
        this.imageFieldsModel = new ImageFieldsModel();
    }

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        imageFieldsModel.setImageType(ImageType.StandardPortrait);
        imageFieldsModel.setCustomDimension(CardDimensions.STANDARD_PORTRAIT_INCHES);
    }

    @Property(CardModelUtils.ART_PORTRAIT)
    public PortraitModel getPortraitModel() {
        return portraitModel;
    }

    @Property("ImageProperties")
    public ImageFieldsModel getImageFieldsModel() {
        return imageFieldsModel;
    }

    public enum ImageFilter {
        Greyscale
    }

    public enum ImageType {
        StandardPortrait(CardDimensions.STANDARD_PORTRAIT_INCHES, InterfaceConstants.STANDARD_PORTRAIT),
        StandardLandscape(CardDimensions.STANDARD_LANDSCAPE_INCHES, InterfaceConstants.STANDARD_LANDSCAPE),
        MiniCardPortrait(CardDimensions.MINI_CARD_PORTRAIT_INCHES, InterfaceConstants.MINI_CARD_PORTRAIT),
        MiniCardLandscape(CardDimensions.MINI_CARD_LANDSCAPE_INCHES, InterfaceConstants.MINI_CARD_LANDSCAPE),
        Custom(null, InterfaceConstants.CUSTOM_CARD);

        private final DimensionEx dimension;
        private final String languageKey;

        ImageType(DimensionEx dimension, String languageKey) {
            this.dimension = dimension;
            this.languageKey = languageKey;
        }

        public DimensionEx getDimension() {
            return dimension;
        }

        @Override
        public String toString() {
            return Language.string(languageKey);
        }
    }

    public static class ImageFieldsModel {
        private ImageType imageType;
        private DimensionEx customDimension;
        private ImageFilter filter;

        @Property("ImageType")
        public ImageType getImageType() {
            return imageType;
        }

        public void setImageType(ImageType imageType) {
            this.imageType = imageType;
        }

        @Property("CustomDimension")
        public DimensionEx getCustomDimension() {
            return customDimension;
        }

        public void setCustomDimension(DimensionEx customDimension) {
            this.customDimension = customDimension;
        }

        @Property("Filter")
        public ImageFilter getFilter() {
            return filter;
        }

        public void setFilter(ImageFilter filter) {
            this.filter = filter;
        }
    }
}
