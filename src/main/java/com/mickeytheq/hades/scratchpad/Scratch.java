package com.mickeytheq.hades.scratchpad;

import com.google.common.collect.Lists;
import com.mickeytheq.hades.core.view.common.PaintConstants;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.strangeeons.plugin.Bootstrapper;
import com.mickeytheq.hades.ui.JTagSelector;

import javax.swing.*;
import java.util.List;

public class Scratch {
    public static void main(String[] args) throws Exception {
        Bootstrapper.initaliseOutsideStrangeEons();

//        MigLayoutUtils.setDebug(true);

        List<String> keywords = Lists.newArrayList("Aloof", "Alert", "Peril", "Exceptional", "Retaliate", "Uses", "Myriad");


        JPanel panel = MigLayoutUtils.createDialogPanel();

//        JTag tag1 = new JTag("Exceptional", blackCloseIcon, redCloseIcon);
//        tag1.addActionListener(e -> System.out.println("Exceptional"));
//
//        JTag tag2 = new JTag("Peril", blackCloseIcon, redCloseIcon);
//        tag2.addActionListener(e -> System.out.println("Peril"));
//
//        JSelector<String> autoCompleteList = new JSelector<>(s -> keywords.stream().filter(o -> o.toLowerCase().contains(s.toLowerCase())).collect(Collectors.toList()));
//
//        panel.add(tag1);
//        panel.add(tag2);
//        panel.add(autoCompleteList, "wrap");


        JTagSelector<String> tagSelector = new JTagSelector<>(keywords, (s, s2) -> s.toLowerCase().contains(s2.toLowerCase()));
        tagSelector.setTagCloseIcon(PaintConstants.TAG_CLOSE_ICON);
        tagSelector.setTagCloseHoverIcon(PaintConstants.TAG_CLOSE_ICON_HOVER);

        panel.add(tagSelector);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private static class TestClass {
        private List<String> list;

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }
    }

}
