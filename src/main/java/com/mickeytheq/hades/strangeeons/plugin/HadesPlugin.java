package com.mickeytheq.hades.strangeeons.plugin;

import ca.cgjennings.apps.arkham.*;
import ca.cgjennings.apps.arkham.plugins.AbstractPlugin;
import ca.cgjennings.apps.arkham.plugins.Plugin;
import ca.cgjennings.apps.arkham.plugins.PluginContext;
import ca.cgjennings.apps.arkham.project.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mickeytheq.hades.core.Cards;
import com.mickeytheq.hades.core.global.carddatabase.CardDatabase;
import com.mickeytheq.hades.core.global.carddatabase.CardDatabases;
import com.mickeytheq.hades.core.global.configuration.GlobalConfiguration;
import com.mickeytheq.hades.core.global.configuration.GlobalConfigurationProvider;
import com.mickeytheq.hades.core.global.configuration.GlobalConfigurations;
import com.mickeytheq.hades.core.global.ui.GlobalConfigurationDialog;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.StandardProjectContext;
import com.mickeytheq.hades.core.project.ui.ProjectConfigurationDialog;
import com.mickeytheq.hades.core.view.CardView;
import com.mickeytheq.hades.core.view.utils.ImageUtils;
import com.mickeytheq.hades.serialise.CardIO;
import com.mickeytheq.hades.strangeeons.gamecomponent.CardGameComponent;
import com.mickeytheq.hades.strangeeons.tasks.HadesActionTree;
import com.mickeytheq.hades.strangeeons.tasks.NewCard;
import com.mickeytheq.hades.strangeeons.tasks.ViewLog;
import com.mickeytheq.hades.strangeeons.ui.FontInstallManager;
import com.mickeytheq.hades.strangeeons.util.MemberUtils;
import com.mickeytheq.hades.ui.DialogEx;
import com.mickeytheq.hades.ui.Environment;
import com.mickeytheq.hades.ui.ErrorDialog;
import com.mickeytheq.hades.ui.quicksearch.QuickSearchDialog;
import com.mickeytheq.hades.util.JsonUtils;
import com.mickeytheq.hades.util.VersionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import resources.RawSettings;
import resources.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HadesPlugin extends AbstractPlugin {
    private static final Logger logger = LogManager.getLogger(HadesPlugin.class);

    public static float getVersion() {
        return VersionUtils.getVersion();
    }

    @Override
    public int getPluginType() {
        return Plugin.EXTENSION;
    }

    @Override
    public boolean initializePlugin(PluginContext context) {
        logger.info("Initialising Hades plugin version " + VersionUtils.getVersion());

        // TODO: do we need to register anything?
        //        Game.register("Hades", "Arkham Horror: LCG");
        try {
            Bootstrapper.initialise();

            if (!checkFonts())
                return false;

            installHadesFileType();

            installUiElements();

            installCardDatabaseUpdater();

            installKeyboardShortcuts();

            installGlobalConfiguration();

            forceViewQualityToHighWithNoAutomaticChanging();

            Actions.register(new HadesActionTree(), Actions.PRIORITY_IMPORT_EXPORT);
        } catch (Exception e) {
            logger.error("Failed to initialise Hades plugin", e);
            return false;
        }

        logger.info("Initialisation of Hades plugin complete");
        return true;
    }

    private boolean checkFonts() {
        return FontInstallManager.checkAndLaunchSetupIfRequired();
    }

    // strange eons has this 'interesting' mechanic that automatically adjusts the view quality as a card is rendered
    // multiple times - you can see this in the SheetViewer.QualityManager class. From what I can tell if SE determines
    // that the card is rendering 'reasonably fast' then it will increase the quality. Unfortunately there is a significant
    // drop in performance when going from HIGH to ULTRAHIGH which decreases the repainting responsiveness noticeably
    //
    // we could ask/remind users to manual change the view quality from auto -> HIGH in the SE UI - however people might
    // not remember to do this so we're going to force it here
    //
    // it would be slightly cleaner to reflectively call ViewQuality.set() but this results in a permanent change as it
    // updates the Strange Eons settings
    private void forceViewQualityToHighWithNoAutomaticChanging() {
        logger.info("Setting view quality to always be High...");

        try {
            Field field = ViewQuality.class.getDeclaredField("auto");
            field.setAccessible(true);
            field.set(null, false);

            field = ViewQuality.class.getDeclaredField("current");
            field.setAccessible(true);
            field.set(null, ViewQuality.HIGH);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("Failed to force view quality settings to desired values", e);
        }
    }

    private void installHadesFileType() {
        logger.info("Installing Hades file type...");

        // register a custom metadata source to describe the hades files
        // by default SE will try to run resources.ResourceKit.getGameComponentFromFile to get this information
        // but hades files are not .eon components so this needs overriding
        Member.registerMetadataSource(new MetadataSource(){
            @Override
            public boolean appliesTo(Member m) {
                return CardIO.HADES_FILE_EXTENSION.equals(FilenameUtils.getExtension(m.getFile().toPath().getFileName().toString()));
            }

            @Override
            public String getDescription(Member m) {
                return "Hades card file"; // TODO: i18n
            }

            @Override
            public Icon getIcon(Member m) {
                return ImageUtils.HADES_PURPLE_H_ICON;
            }

            // TODO: override the fillinmetadata method if we want
        });

        // register custom opener to handle opening/viewing the hades files
        // by default SE will try to run resources.ResourceKit.getGameComponentFromFile to load files
        // but hades files are not .eon components so this needs overriding
        Open.setOpenRule(CardIO.HADES_FILE_EXTENSION, Open.OpenRule.INTERNAL_OPEN);
        Open.registerOpener(new Open.InternalOpener() {
            @Override
            public boolean appliesTo(File file) {
                Path path = file.toPath();

                if (!Files.isRegularFile(path))
                    return false;

                String extension = FilenameUtils.getExtension(path.getFileName().toString());

                if (!CardIO.HADES_FILE_EXTENSION.equals(extension))
                    return false;

                return true;
            }

            @Override
            public void open(File file) throws Exception {
                try {
                    doOpen(file);
                } catch (Throwable t) {
                    ErrorDialog.error("Error opening file '" + file.getAbsolutePath() + "'", t);
                }
            }

            private void doOpen(File file) {
                // check if this file is already open
                for (StrangeEonsEditor editor : StrangeEons.getWindow().getEditors()) {
                    if (file.equals(editor.getFile())) {
                        editor.select();
                        return;
                    }
                }

                CardGameComponent gameComponent = readCardGameComponent(file.toPath());

                AbstractGameComponentEditor<CardGameComponent> editor = gameComponent.createDefaultEditor();

                editor.setFrameIcon(ImageUtils.HADES_PURPLE_H_ICON);

                RecentFiles.addRecentDocument(file);
                editor.handleOpenRequest(gameComponent, file);

                StrangeEons.getWindow().addEditor(editor);
            }

            private CardGameComponent readCardGameComponent(Path path) {
                ProjectContext projectContext = StandardProjectContext.getContextForContentPath(path);

                Card card = CardIO.readCard(path, projectContext);

                CardView cardView = Cards.createCardView(card, projectContext);

                return new CardGameComponent(cardView);
            }
        });
    }

    private void installUiElements() {
        Environment.setTopLevelWindow(StrangeEons.getWindow());
        DialogEx.setDefaultIconImage(ImageUtils.HADES_PURPLE_H_IMAGE);
    }

    private void installKeyboardShortcuts() {
        logger.info("Installing keyboard shortcuts...");

        installNewCardKeyboardShortcut();
        installQuickSearchKeyboardShortcut();
        installOpenLogKeyboardShortcut();
        installOpenProjectConfigurationKeyboardShortcut();
        installOpenGlobalConfigurationKeyboardShortcut();
    }

    private void installNewCardKeyboardShortcut() {
        StrangeEonsAppWindow appWindow = StrangeEons.getWindow();

        KeyStroke altN = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.ALT_DOWN_MASK);

        final String HADES_NEW_CARD_ACTION = "HadesNewCard";

        appWindow.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(altN, HADES_NEW_CARD_ACTION);
        appWindow.getRootPane().getActionMap().put(HADES_NEW_CARD_ACTION, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Project project = StrangeEons.getOpenProject();

                if (project == null)
                    return;

                Member[] selectedMembers = appWindow.getOpenProjectView().getSelectedMembers();

                if (selectedMembers.length > 1)
                    return;

                NewCard.newCard(selectedMembers.length == 0 ? null : selectedMembers[0]);
            }
        });
    }

    private void installQuickSearchKeyboardShortcut() {
        StrangeEonsAppWindow appWindow = StrangeEons.getWindow();

        KeyStroke altN = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK);

        final String HADES_QUICK_SEARCH_TITLE = "HadesQuickSearchTitle";

        appWindow.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(altN, HADES_QUICK_SEARCH_TITLE);
        appWindow.getRootPane().getActionMap().put(HADES_QUICK_SEARCH_TITLE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QuickSearchDialog quickSearchDialog = new QuickSearchDialog();
                quickSearchDialog.setLocationRelativeTo(appWindow);
                quickSearchDialog.setVisible(true);
            }
        });
    }

    private void installOpenLogKeyboardShortcut() {
        StrangeEonsAppWindow appWindow = StrangeEons.getWindow();

        KeyStroke altN = KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.ALT_DOWN_MASK);

        final String HADES_VIEW_LOG = "HadesViewLog";

        appWindow.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(altN, HADES_VIEW_LOG);
        appWindow.getRootPane().getActionMap().put(HADES_VIEW_LOG, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewLog.viewLog();
            }
        });
    }

    private void installOpenProjectConfigurationKeyboardShortcut() {
        StrangeEonsAppWindow appWindow = StrangeEons.getWindow();

        KeyStroke altN = KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.ALT_DOWN_MASK);

        final String COMMAND_KEY = "HadesOpenProjectConfiguration";

        appWindow.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(altN, COMMAND_KEY);
        appWindow.getRootPane().getActionMap().put(COMMAND_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Project project = StrangeEons.getOpenProject();

                if (project == null)
                    return;

                ProjectContext projectContext = StandardProjectContext.getContextForContentPath(project.getFile().toPath());
                ProjectConfigurationDialog.openDialog(appWindow, projectContext);
            }
        });
    }

    private void installOpenGlobalConfigurationKeyboardShortcut() {
        StrangeEonsAppWindow appWindow = StrangeEons.getWindow();

        KeyStroke altN = KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.ALT_DOWN_MASK);

        final String COMMAND_KEY = "HadesOpenGlobalConfiguration";

        appWindow.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(altN, COMMAND_KEY);
        appWindow.getRootPane().getActionMap().put(COMMAND_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Project project = StrangeEons.getOpenProject();

                if (project == null)
                    return;

                GlobalConfigurationDialog.openDialog(appWindow);
            }
        });
    }

    private void installCardDatabaseUpdater() {
        logger.info("Installing card database listeners...");

        StrangeEons.getWindow().addProjectEventListener(new CardDatabaseProjectEventListener());
    }

    static class CardDatabaseProjectEventListener implements StrangeEonsAppWindow.ProjectEventListener {
        private final CardDatabase cardDatabase = CardDatabases.getCardDatabase();

        @Override
        public void projectOpened(Project project) {
            Path projectPath = project.getFile().toPath();

            // look for the hades project context, if it doesn't exist skip
            Optional<ProjectContext> projectContext = StandardProjectContext.findContextForContentPath(projectPath);

            if (!projectContext.isPresent())
                return;

            // load all the cards in the project and register them with the card database
            try (Stream<Path> stream = Files.walk(projectPath)) {
                List<Path> paths = stream.filter(MemberUtils::isPathHadesFile).collect(Collectors.toList());

                logger.debug("Registering " + paths.size() + " cards in card database for project with root path '" + projectPath + "'");

                cardDatabase.register(cardDatabaseLoader -> {
                    for (Path path : paths) {
                        try {
                            Card card = CardIO.readCard(path, projectContext.get());
                            cardDatabaseLoader.registerCard(card, path);
                        } catch (RuntimeException e) {
                            logger.error("Error loading card from path '" + path + "' while building card database", e);
                        }
                    }
                }, project);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void projectClosing(Project project) {
            logger.debug("Unregistering all cards in card database for project with root path '" + project.getFile().toPath() + "'");

            cardDatabase.unregister(project);
        }
    }

    private void installGlobalConfiguration() {
        GlobalConfigurations.setProvider(new StrangeEonsGlobalConfigurationProvider());
    }

    static class StrangeEonsGlobalConfigurationProvider implements GlobalConfigurationProvider {
        private static final String SETTINGS_KEY = "HADES_GLOBAL_CONFIGURATION_JSON";

        private final Settings settings = Settings.getUser();
        private final ObjectMapper objectMapper = JsonUtils.createDefaultObjectMapper(false);

        @Override
        public GlobalConfiguration load() {
            String json = settings.get(SETTINGS_KEY);

            if (StringUtils.isEmpty(json))
                return new GlobalConfiguration();

            try {
                return objectMapper.readValue(new StringReader(json), GlobalConfiguration.class);
            } catch (IOException e) {
                logger.error("Error loading global configuration from Strange Eons settings. Resetting to defaults", e);
                return new GlobalConfiguration();
            }
        }

        @Override
        public void save(GlobalConfiguration globalConfiguration) {
            StringWriter stringWriter = new StringWriter();
            try {
                objectMapper.writeValue(stringWriter, globalConfiguration);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            settings.set(SETTINGS_KEY, stringWriter.toString());

            RawSettings.writeUserSettingsImmediately();
        }
    }
}
