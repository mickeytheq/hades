package com.mickeytheq.hades.core.view.cardfaces;

import com.google.common.collect.Lists;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.cardfaces.EncounterCardBack;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.view.utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.util.List;

@View(interfaceLanguageKey = InterfaceConstants.ENCOUNTERCARDBACK)
public class EncounterCardBackView extends BaseCardFaceView<EncounterCardBack> {
    @Override
    public void initialiseView() {
        // nothing to do
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    protected List<TemplateInfo> getAvailableTemplateInfos() {
        return Lists.newArrayList(TemplateInfos.createStandard300("/templates/backs/encounter_card_back.png", CardFaceOrientation.Portrait));
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        // nothing to do
    }

    @Override
    public void paint(PaintContext paintContext) {
        // TODO: can we cache this instead of drawing it each time?
        // TODO: although may not be worth it. for bulk operations the sheet must be created a drawn from scratch so
        // TODO: this would only improve subsequent paints of the same sheet which aren't that high frequency
        paintContext.getGraphics().drawImage(paintContext.getTemplateInfo().getTemplateImage(), 0, 0, null);
    }
}
