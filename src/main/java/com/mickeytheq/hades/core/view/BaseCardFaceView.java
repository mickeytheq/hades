package com.mickeytheq.hades.core.view;

import com.mickeytheq.hades.core.model.CardFaceModel;

import java.lang.reflect.ParameterizedType;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public abstract class BaseCardFaceView<M extends CardFaceModel> implements CardFaceView {
    private CardView cardView;
    private CardFaceSide cardFaceSide;
    private M cardFaceModel;

    @Override
    public final void initialiseView(CardView cardView, CardFaceSide cardFaceSide, CardFaceModel cardFaceModel) {
        this.cardView = cardView;
        this.cardFaceSide = cardFaceSide;

        // validate the model matches the type defined by the generic 'M'
        Class<M> declaredModelClass = (Class<M>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        if (!declaredModelClass.isInstance(cardFaceModel))
            throw new RuntimeException("Implementation of " + getClass().getSimpleName() + " declares " + declaredModelClass.getName() + " as required model type but a model of class " + cardFaceModel.getClass().getName() + " was provided");

        this.cardFaceModel = declaredModelClass.cast(cardFaceModel);

        initialiseView();
    }

    // perform any card specific initialisation
    public abstract void initialiseView();

    public CardView getCardView() {
        return cardView;
    }

    public CardFaceSide getCardFaceSide() {
        return cardFaceSide;
    }

    public M getModel() {
        return cardFaceModel;
    }

    @Override
    public Optional<TemplateInfo> getCompatibleTemplateInfo(int desiredResolutionInPpi) {
        List<TemplateInfo> availableTemplates = getAvailableTemplateInfos();

        // try to find a template with the exact resolution requested
        Optional<TemplateInfo> exactResolutionTemplate = availableTemplates.stream().filter(o -> o.getResolutionInPixelsPerInch() == desiredResolutionInPpi).findAny();

        if (exactResolutionTemplate.isPresent())
            return exactResolutionTemplate;

        // find a template that has the closer higher resolution than requested
        Optional<TemplateInfo> nextHighestTemplate = availableTemplates.stream()
                .filter(o -> o.getResolutionInPixelsPerInch() > desiredResolutionInPpi)
                .min(Comparator.comparingInt(TemplateInfo::getResolutionInPixelsPerInch));

        if (nextHighestTemplate.isPresent())
            return nextHighestTemplate.map(templateInfo -> templateInfo.scaleToResolution(desiredResolutionInPpi));

        // find a template that has the closest lower resolution than requested
        Optional<TemplateInfo> nextLowestTemplate = availableTemplates.stream()
                .filter(o -> o.getResolutionInPixelsPerInch() < desiredResolutionInPpi)
                .max(Comparator.comparingInt(TemplateInfo::getResolutionInPixelsPerInch));

        if (nextLowestTemplate.isPresent())
            return nextLowestTemplate.map(templateInfo -> templateInfo.scaleToResolution(desiredResolutionInPpi));

        return Optional.empty();
    }

    protected abstract List<TemplateInfo> getAvailableTemplateInfos();

    @Override
    public String getBriefDisplayString() {
        return getCardFaceSide().name() + " - " + getTitle() + " (type: " + getClass().getSimpleName() + ")";
    }
}
