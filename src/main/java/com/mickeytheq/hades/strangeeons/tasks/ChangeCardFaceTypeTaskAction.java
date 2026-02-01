package com.mickeytheq.hades.strangeeons.tasks;

import ca.cgjennings.apps.arkham.project.Member;
import com.mickeytheq.hades.strangeeons.util.MemberUtils;
import com.mickeytheq.hades.ui.changecardfacetype.ChangeCardFaceType;

import java.nio.file.Path;

public class ChangeCardFaceTypeTaskAction extends BaseTaskAction {
    @Override
    public String getLabel() {
        return "Change card face type";
    }

    @Override
    public boolean appliesToSelection(Member[] members) {
        return members.length == 1 && MemberUtils.isMemberHadesFile(members[0]);
    }

    @Override
    public boolean performOnSelection(Member[] members) {
        Member member = members[0];

        new ChangeCardFaceType().change(member.getFile().toPath());

        return true;
    }
}
