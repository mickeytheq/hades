package com.mickeytheq.hades.strangeeons.plugin;

import ca.cgjennings.apps.arkham.*;
import ca.cgjennings.apps.arkham.plugins.AbstractPlugin;
import ca.cgjennings.apps.arkham.plugins.Plugin;
import ca.cgjennings.apps.arkham.plugins.PluginContext;
import ca.cgjennings.apps.arkham.project.Actions;
import ca.cgjennings.apps.arkham.project.Member;
import ca.cgjennings.apps.arkham.project.MetadataSource;
import ca.cgjennings.apps.arkham.project.Open;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.hades.core.CardFaces;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.project.ProjectConfigurationProviderJson;
import com.mickeytheq.hades.core.project.ProjectConfigurations;
import com.mickeytheq.hades.core.view.CardView;
import com.mickeytheq.hades.core.view.utils.ImageUtils;
import com.mickeytheq.hades.serialise.CardIO;
import com.mickeytheq.hades.serialise.JsonCardSerialiser;
import com.mickeytheq.hades.serialise.RawJsonSerialiser;
import com.mickeytheq.hades.strangeeons.gamecomponent.CardGameComponent;
import com.mickeytheq.hades.strangeeons.tasks.HadesActionTree;
import com.mickeytheq.hades.strangeeons.ui.FontInstallManager;
import com.mickeytheq.hades.util.VersionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

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
        // TODO: do we need to register anything?
        //        Game.register("Hades", "Arkham Horror: LCG");

        try {
            Bootstrapper.initialise();

            if (!checkFonts())
                return false;

            installHadesFileType();

            forceViewQualityToHighWithNoAutomaticChanging();

            // StrangeEons.getOpenProject is not resolvable until after the plugin has finished loading and StrangeEons has
            // finished starting. Therefore we use a Supplier of the Path so the loading of the config and path resolution can be deferred to first access
            ProjectConfigurations.setDefaultProvider(new ProjectConfigurationProviderJson(() -> StrangeEons.getOpenProject().getFile().toPath().resolve(ProjectConfigurationProviderJson.DEFAULT_FILENAME)));

            Actions.register(new HadesActionTree(), Actions.PRIORITY_IMPORT_EXPORT);

            return true;
        } catch (Exception e) {
            logger.fatal("Failed to initialise Hades plugin", e);
            throw e;
        }
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
                Card card = CardIO.readCard(path);

                CardView cardView = CardFaces.createCardView(card);

                return new CardGameComponent(cardView);
            }
        });
    }
}
