package com.mickeytheq.hades.strangeeons.tasks;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.project.Member;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.strangeeons.util.MemberUtils;
import com.mickeytheq.hades.ui.cardreviewer.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CompareCardsTaskAction extends BaseTaskAction {
    @Override
    public String getLabel() {
        return "Compare card images";
    }

    @Override
    public boolean appliesToSelection(Member[] members) {
        return members.length > 0;
    }

    @Override
    public boolean performOnSelection(Member[] members) {
        Path hadesRootPath = Paths.get("D:\\Temp\\Buffy Investigator Cards SE-Hades\\Buffy Investigators Set One\\1 Investigator and Signature");
        Path ahlcgRootPath = Paths.get("D:\\Temp\\Buffy Investigator Cards SE\\Buffy Investigators Set One\\1 Investigator and Signature");

        new CardCompareDialog(StrangeEons.getWindow(), hadesRootPath, ahlcgRootPath);

        return true;
    }
}
