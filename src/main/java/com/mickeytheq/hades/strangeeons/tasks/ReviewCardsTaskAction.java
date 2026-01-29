package com.mickeytheq.hades.strangeeons.tasks;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.project.Member;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.strangeeons.util.MemberUtils;
import com.mickeytheq.hades.ui.cardreviewer.CardReviewDialog;
import com.mickeytheq.hades.ui.cardreviewer.ItemSource;
import com.mickeytheq.hades.ui.cardreviewer.ItemSources;
import com.mickeytheq.hades.ui.cardreviewer.ListItemSource;

import java.nio.file.Path;
import java.util.List;

public class ReviewCardsTaskAction extends BaseTaskAction {
    @Override
    public String getLabel() {
        return "Review card images";
    }

    @Override
    public boolean appliesToSelection(Member[] members) {
        return members.length > 0;
    }

    @Override
    public boolean performOnSelection(Member[] members) {
        List<Path> hadesFiles = MemberUtils.getAllHadesPathDescendants(Lists.newArrayList(members));

        ItemSource<Path> cardReviewSource = new ListItemSource<>(hadesFiles);

        new CardReviewDialog(StrangeEons.getWindow(), ItemSources.cardViewFromPath(cardReviewSource));

        return true;
    }
}
