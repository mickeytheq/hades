package com.mickeytheq.hades.core.view.common;

import com.mickeytheq.hades.core.model.common.CommonCardFieldsModel;
import com.mickeytheq.hades.core.view.utils.EditorUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.ui.*;
import com.mickeytheq.hades.ui.validation.Validators;
import org.apache.commons.lang3.Strings;
import resources.Language;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.stream.Collectors;

public class KeywordEditor extends JPanel {
    private final JSelector<String> selector;
    private final List<JTag> tags = new ArrayList<>();
    private final Map<String, JTag> tagMap = new HashMap<>();

    private final List<String> allItems;

    private final List<CommonCardFieldsModel.KeywordModel> keywordModels = new ArrayList<>();

    private final BufferedImage tagCloseIcon = PaintConstants.TAG_CLOSE_ICON;
    private final BufferedImage tagCloseHoverIcon = PaintConstants.TAG_CLOSE_ICON_HOVER;

    public KeywordEditor() {
        // use the language file to fetch all the possible keywords
        // TODO: add project custom keywords
        this.allItems = Language.getGame().keySet().stream()
                .filter(o -> o.startsWith(CommonCardFieldsModel.KeywordModel.KEYWORD_LANGUAGE_KEY_PREFIX))
                .map(o -> Strings.CS.removeStart(o, CommonCardFieldsModel.KeywordModel.KEYWORD_LANGUAGE_KEY_PREFIX))
                .sorted()
                .collect(Collectors.toList());

        setLayout(MigLayoutUtils.createOrganiserLayout());

        selector = new JSelector<>(s -> getUnselectedValues().stream().filter(o -> keywordMatcher(o, s)).collect(Collectors.toList()));
        selector.addSelectionListener(this::select);

        relayout();
    }

    protected boolean keywordMatcher(String keyword, String searchText) {
        return keyword.toLowerCase().contains(searchText.toLowerCase());
    }

    private List<String> getUnselectedValues() {
        return allItems.stream().filter(o -> !isKeywordPresent(o)).collect(Collectors.toList());
    }

    private boolean isKeywordPresent(String keyword) {
        return keywordModels.stream().anyMatch(o -> o.getKeyword().contains(keyword));
    }

    public List<CommonCardFieldsModel.KeywordModel> getKeywordModels() {
        return keywordModels;
    }

    public void setKeywordModels(List<CommonCardFieldsModel.KeywordModel> models) {
        keywordModels.clear();
        keywordModels.addAll(models);

        for (CommonCardFieldsModel.KeywordModel model : models) {
            addTag(model);
        }

        relayout();
    }

    private void select(String item) {
        if (isKeywordPresent(item))
            return;

        CommonCardFieldsModel.KeywordModel keywordModel = new CommonCardFieldsModel.KeywordModel();
        keywordModel.setKeyword(item);

        // if null then placeholder resolution failed/was cancelled
        if (!resolvePlaceholders(keywordModel))
            return;

        keywordModels.add(keywordModel);

        addTag(keywordModel);

        fireChanged();

        relayout();
    }

    private void addTag(CommonCardFieldsModel.KeywordModel keywordModel) {
        JTag tag = new JTag(keywordModel.resolveKeyword(), tagCloseIcon, tagCloseHoverIcon);
        tag.addActionListener(e -> {
            unselect(keywordModel.getKeyword());
        });
        tags.add(tag);
        tagMap.put(keywordModel.getKeyword(), tag);
    }

    private boolean resolvePlaceholders(CommonCardFieldsModel.KeywordModel keywordModel) {
        Map<String, String> placeholders = keywordModel.parsePlaceholders();

        // no placeholders, return the input string
        if (placeholders.isEmpty())
            return true;

        JPanel panel = MigLayoutUtils.createDialogPanel();

        Map<String, String> resolvedPlaceholders = new HashMap<>();

        // create panel for placeholder input
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String key = entry.getKey();
            String type = entry.getValue();

            // TODO: use type to make the editor more sophisticated

            JTextField textEditor = EditorUtils.createTextField(20);
            EditorUtils.bindTextComponent(textEditor, s -> resolvedPlaceholders.put(key, s));
            Validators.required(textEditor);

            MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, key, textEditor);
        }

        // show dialog
        DialogEx dialogEx = new DialogEx(Environment.getTopLevelWindow(), false);
        dialogEx.setTitle("Parameters");
        dialogEx.setContentComponent(panel);
        dialogEx.addOkCancelButtons();

        if (dialogEx.showDialog() != DialogEx.OK_OPTION)
            return false;

        for (Map.Entry<String, String> entry : resolvedPlaceholders.entrySet()) {
            String placeholderKey = entry.getKey();

            CommonCardFieldsModel.KeywordParameterModel parameterModel = new CommonCardFieldsModel.KeywordParameterModel();
            parameterModel.setKey(placeholderKey);
            parameterModel.setValue(resolvedPlaceholders.get(placeholderKey));

            keywordModel.getParameters().add(parameterModel);
        }

        return true;
    }

    private void unselect(String item) {
        Optional<CommonCardFieldsModel.KeywordModel> unselected = keywordModels.stream().filter(o -> o.getKeyword().contains(item)).findFirst();

        if (!unselected.isPresent())
            return;

        keywordModels.remove(unselected.get());

        JTag tag = tagMap.remove(item);

        if (tag != null) {
            tags.remove(tag);
        }

        fireChanged();

        relayout();
    }

    private void relayout() {
        removeAll();

        for (JTag tag : tags) {
            add(tag, "width pref!");
        }

        add(selector, "pushx, growx");

        revalidate();
    }

    private void fireChanged() {
        for (KeywordEditorListener listener : listeners) {
            listener.keywordModelsChanged();
        }
    }

    private List<KeywordEditorListener> listeners = new ArrayList<>();

    public void addListener(KeywordEditorListener listener) {
        listeners.add(listener);
    }

    public interface KeywordEditorListener {
        void keywordModelsChanged();
    }
}
