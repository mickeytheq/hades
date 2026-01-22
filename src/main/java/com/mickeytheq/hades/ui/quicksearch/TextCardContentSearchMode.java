package com.mickeytheq.hades.ui.quicksearch;

import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.global.CardDatabase;
import com.mickeytheq.hades.core.global.CardDatabases;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.model.entity.AnnotatedEntityMetadataBuilder;
import com.mickeytheq.hades.core.model.entity.EntityMetadata;
import com.mickeytheq.hades.core.model.entity.PropertyMetadata;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.strangeeons.util.StrangeEonsAppWindowUtils;
import com.mickeytheq.hades.util.SwingUtils;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public class TextCardContentSearchMode implements QuickSearchMode {
    private final CardDatabase cardDatabase = CardDatabases.getCardDatabase();

    @Override
    public String getDisplay() {
        return Language.string(InterfaceConstants.QUICKSEARCHMODE_TEXT);
    }

    @Override
    public void performSearch(String searchText, DefaultListModel<Object> listModel) {
        if (StringUtils.isEmpty(searchText))
            return;

        for (Card card : cardDatabase.getCards()) {
            EntityMetadata entityMetadata = AnnotatedEntityMetadataBuilder.build(card.getFrontFaceModel().getClass());

            for (PropertyMetadata propertyMetadata : entityMetadata.getProperties()) {
                if (propertyMetadata.isValue()) {
                    if (!propertyMetadata.getPropertyClass().equals(String.class))
                        continue;

                    Object value = propertyMetadata.getPropertyValue(card.getFrontFaceModel());

                    if (value == null)
                        continue;

                    if (value.toString().contains(searchText)) {
                        listModel.addElement(new SearchItem(cardDatabase.getSourcePathForCard(card), CardFaceSide.Front, value.toString(), propertyMetadata.getName()));
                    }
                }
            }
        }
    }

    public ListCellRenderer<Object> getRenderer() {
        return new ListCellRendererImpl();
    }

    static class ListCellRendererImpl implements ListCellRenderer<Object> {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            SearchItem searchItem = (SearchItem)value;

            MigLayout layout = new MigLayout(MigLayoutUtils.createDefaultLayoutConstraints().insets("2"));
            JPanel panel = new JPanel(layout);

            JLabel primaryLabel = new JLabel(searchItem.getTitle() + " (" + searchItem.getDescription() + ")", SwingConstants.LEFT);
            JLabel secondaryLabel = new JLabel(searchItem.getPath().toString(), SwingConstants.RIGHT);

            Color foreground;
            Color background;

            if (isSelected) {
                // copying the color here works around the color returned from the getSelectionForeground/Background method being a UIResource
                // that gets auto-converted to normal un-selected colors due to how the Synth L&F works
                //
                // the SynthLabelUI/SynthListUI with the SynthStyle uses some sort of flagging to mark the labels as 'selected' (even though they have no concept)
                // which allows the L&F to pick up the selected status correctly
                // when creating our own labels this flagging is absent so the color gets transformed to a standard text foreground instead
                //
                // the workaround is to prevent a UIResource 'color' being used which is achieved by cloning the Color objects and copying the
                // raw RGB values. this strips the L&F container so the Color is used as-is
                // TODO: investigate further why this is and see if there's a better solution
                foreground = SwingUtils.copyColor(list.getSelectionForeground());
                background = SwingUtils.copyColor(list.getSelectionBackground());
            } else {
                foreground = list.getForeground();
                background = list.getBackground();
            }

            primaryLabel.setFont(list.getFont());
            secondaryLabel.setFont(list.getFont());

            // need to set the background of the panel container. JLabels don't paint their background by default
            panel.setBackground(background);

            primaryLabel.setForeground(foreground);
            secondaryLabel.setForeground(foreground);

            panel.add(primaryLabel, "growx, pushx");
            panel.add(secondaryLabel);

            return panel;
        }
    }

    @Override
    public void selected(Object selectedItem) {
        SearchItem searchItem = (SearchItem)selectedItem;
        StrangeEonsAppWindowUtils.navigateTo(searchItem.getPath(), searchItem.getCardFaceSide());
    }

    static class SearchItem {
        private final Path path;
        private final CardFaceSide cardFaceSide;
        private final String title;
        private final String description;

        public SearchItem(Path path, CardFaceSide cardFaceSide, String title, String description) {
            this.path = path;
            this.cardFaceSide = cardFaceSide;
            this.title = title;
            this.description = description;
        }

        public Path getPath() {
            return path;
        }

        public CardFaceSide getCardFaceSide() {
            return cardFaceSide;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
    }
}
