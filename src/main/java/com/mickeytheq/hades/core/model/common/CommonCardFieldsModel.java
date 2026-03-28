package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.serialise.discriminator.BooleanEmptyWhenFalseDiscriminator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import resources.Language;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommonCardFieldsModel {
    private String title;
    private String subtitle;
    private boolean copyOtherFaceTitles = false;
    private boolean unique = false;
    private String traits;
    private Distance afterTraitsSpacing = Distance.createZeroPoint();
    private List<KeywordModel> keywords = new ArrayList<>();
    private Distance afterKeywordsSpacing = Distance.createZeroPoint();
    private String rules;
    private Distance afterRulesSpacing = Distance.createZeroPoint();
    private String flavourText;
    private Distance afterFlavourTextSpacing = Distance.createZeroPoint();
    private String victory;
    private String copyright;


    @Property("Title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Property("Subtitle")
    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @Property(value = CardModelUtils.COPY_OTHER_FACE + "Titles", discriminator = BooleanEmptyWhenFalseDiscriminator.class)
    public boolean getCopyOtherFaceTitles() {
        return copyOtherFaceTitles;
    }

    public void setCopyOtherFaceTitles(boolean copyOtherFaceTitles) {
        this.copyOtherFaceTitles = copyOtherFaceTitles;
    }

    @Property(value = "Unique", discriminator = BooleanEmptyWhenFalseDiscriminator.class)
    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    @Property("Traits")
    public String getTraits() {
        return traits;
    }

    public void setTraits(String traits) {
        this.traits = traits;
    }

    @Property("Keywords")
    public List<KeywordModel> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<KeywordModel> keywords) {
        this.keywords = keywords;
    }

    @Property("Rules")
    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    @Property("Flavor")
    public String getFlavourText() {
        return flavourText;
    }

    public void setFlavourText(String flavourText) {
        this.flavourText = flavourText;
    }

    @Property("Victory")
    public String getVictory() {
        return victory;
    }

    public void setVictory(String victory) {
        this.victory = victory;
    }

    @Property("Copyright")
    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    @Property(value = "AfterTraitsSpacing")
    public Distance getAfterTraitsSpacing() {
        return afterTraitsSpacing;
    }

    public void setAfterTraitsSpacing(Distance afterTraitsSpacing) {
        this.afterTraitsSpacing = afterTraitsSpacing;
    }

    @Property(value = "AfterKeywordsSpacing")
    public Distance getAfterKeywordsSpacing() {
        return afterKeywordsSpacing;
    }

    public void setAfterKeywordsSpacing(Distance afterKeywordsSpacing) {
        this.afterKeywordsSpacing = afterKeywordsSpacing;
    }

    @Property(value = "AfterRulesSpacing")
    public Distance getAfterRulesSpacing() {
        return afterRulesSpacing;
    }

    public void setAfterRulesSpacing(Distance afterRulesSpacing) {
        this.afterRulesSpacing = afterRulesSpacing;
    }

    @Property(value = "AfterFlavourTextSpacing")
    public Distance getAfterFlavourTextSpacing() {
        return afterFlavourTextSpacing;
    }

    public void setAfterFlavourTextSpacing(Distance afterFlavourTextSpacing) {
        this.afterFlavourTextSpacing = afterFlavourTextSpacing;
    }

    public static class KeywordModel {
        private String keyword;
        private List<KeywordParameterModel> parameters = new ArrayList<>();

        @Property("Keyword")
        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        @Property("Parameters")
        public List<KeywordParameterModel> getParameters() {
            return parameters;
        }

        public void setParameters(List<KeywordParameterModel> parameters) {
            this.parameters = parameters;
        }

        public static final String KEYWORD_LANGUAGE_KEY_PREFIX = "Hades-Keyword-";
        public static final String KEYWORD_LANGUAGE_KEY_FORMAT_SUFFIX = "-Format";

        // TODO: make the placeholder 3-part {key:type:translation} where key and type should not change but translation can be altered
        // TODO: and maybe drop type entirely - although would be useful for having a translatable drop-down for uses types
        private static final Pattern PLACEHOLDER = Pattern.compile("\\{(\\w+):(\\w+)}");

        private static final List<String> ALL_KEYWORDS;
        private static final Map<String, String> KEYWORD_TRANSLATIONS = new HashMap<>();
        private static final Map<String, String> KEYWORD_FORMATS = new HashMap<>();

        static {
            ALL_KEYWORDS = Language.getGame().keySet().stream()
                    .filter(o -> o.startsWith(KEYWORD_LANGUAGE_KEY_PREFIX))
                    .map(o -> Strings.CS.removeStart(o, KEYWORD_LANGUAGE_KEY_PREFIX))
                    .filter(o -> !o.contains("-"))
                    .sorted()
                    .collect(Collectors.toList());

            for (String keyword : ALL_KEYWORDS) {
                String translation = Language.gstring(KEYWORD_LANGUAGE_KEY_PREFIX + keyword);
                KEYWORD_TRANSLATIONS.put(keyword, translation);

                String formatKey = KEYWORD_LANGUAGE_KEY_PREFIX + keyword + KEYWORD_LANGUAGE_KEY_FORMAT_SUFFIX;

                String format;

                // if there's no format just use the raw translated string
                if (!Language.getGame().keySet().contains(formatKey))
                    format = translation;
                else
                    format = Language.gstring(formatKey);

                KEYWORD_FORMATS.put(keyword, format);
            }
        }

        // returns all keywords in their immutable values (no translation)
        public static List<String> getAllKeywords() {
            return Collections.unmodifiableList(ALL_KEYWORDS);
        }

        // return a user displayable string in the current language for the given keyword
        public static String getKeywordTranslation(String keyword) {
            return KEYWORD_TRANSLATIONS.get(keyword);
        }

        // returns a format string in the current language for the given keyword
        public static String getKeywordFormat(String keyword) {
            return KEYWORD_FORMATS.get(keyword);
        }

        public Map<String, String> parsePlaceholders() {
            String format = getKeywordFormat(keyword);

            Map<String, String> placeholders = new LinkedHashMap<>();

            Matcher matcher = PLACEHOLDER.matcher(format);

            while (matcher.find()) {
                placeholders.put(matcher.group(1), matcher.group(2));
            }

            return placeholders;
        }

        public String resolveKeyword() {
            String format = getKeywordFormat(keyword);

            // map placeholder results back to the input string
            Matcher matcher = PLACEHOLDER.matcher(format);
            StringBuffer sb = new StringBuffer();

            while (matcher.find()) {
                String placeholderKey = matcher.group(1);

                Optional<KeywordParameterModel> parameterModelOptional = getParameters().stream().filter(o -> placeholderKey.equals(o.getKey())).findFirst();

                if (parameterModelOptional.isPresent()) {
                    matcher.appendReplacement(sb, parameterModelOptional.get().getValue());
                }
                else {
                    matcher.appendReplacement(sb, "<Missing placeholder>");
                }
            }

            matcher.appendTail(sb);

            return sb.toString();
        }
    }

    public static class KeywordParameterModel {
        private String key;
        private String value;

        @Property("Key")
        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @Property("Value")
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
