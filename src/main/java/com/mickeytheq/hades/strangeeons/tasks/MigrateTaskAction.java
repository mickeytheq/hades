package com.mickeytheq.hades.strangeeons.tasks;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.project.Member;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.Migrator;
import com.mickeytheq.hades.ui.DialogWithButtons;
import com.mickeytheq.hades.ui.FileChooser;
import com.mickeytheq.hades.ui.LoggingLevel;
import com.mickeytheq.hades.ui.ProgressDialog;
import com.mickeytheq.hades.util.LoggerUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

public class MigrateTaskAction extends BaseTaskAction {
    private final static Logger logger = Logger.getLogger(MigrateTaskAction.class.getName());

    @Override
    public String getLabel() {
        return "Migrate AHLCG plugin content to Hades";
    }

    @Override
    public boolean appliesToSelection(Member[] members) {
        return true;
    }

    @Override
    public boolean performOnSelection(Member[] members) {
        new Migration(members).run();
        return true;
    }

    static class Migration {
        private final Member[] members;
        private ProgressDialog progressDialog;
        private MigrationOptions migrationOptions;

        public Migration(Member[] members) {
            this.members = members;
        }

        public void run() {
//            migrationOptions = new MigrationOptions();
//            if (!migrationOptions.showDialog(StrangeEons.getOpenProject().getFile()))
//                return;

            progressDialog = new ProgressDialog(LoggingLevel.Debug);

            progressDialog.runWithinProgressDialog(() -> {
                doMigration(members);
                return null;
            });
        }

        private void doMigration(Member[] members) {
            Path projectRoot = StrangeEons.getOpenProject().getFile().toPath();
            Path migrationRootDirectory = projectRoot.resolve("Hades-Migrated");

            for (Member member : members) {
                if (member.isFolder())
                    continue;

                Path sourceFile = member.getFile().toPath();
                Path targetFile = migrationRootDirectory.resolve(projectRoot.relativize(sourceFile));
                Path targetDirectory = targetFile.getParent();

                try {
                    Files.createDirectories(targetDirectory);
                } catch (IOException e) {
                    throw new RuntimeException("Error creating target directory '" + targetDirectory + "'", e);
                }

                try {
                    Migrator.migrate(sourceFile, targetFile);
                } catch (Exception e) {
                    logger.severe(LoggerUtils.toLoggable("Migration of '" + sourceFile + "' to '" + targetFile + "' failed", e));
                }
            }
        }
    }

    static class MigrationOptions {
        private FileChooser fileChooser;

        public boolean showDialog(File startDirectory) {
            fileChooser = new FileChooser();
            fileChooser.setSelectedFile(startDirectory);
            fileChooser.getFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.getTextField().setEnabled(false);

            JPanel panel = MigLayoutUtils.createTitledPanel("Options");
            MigLayoutUtils.addLabel(panel, "Target directory: ");
            panel.add(fileChooser);

            DialogWithButtons dialogWithButtons = new DialogWithButtons(StrangeEons.getWindow(), true);
            dialogWithButtons.setContent(panel);
            dialogWithButtons.setTitle("Migration options");
            dialogWithButtons.addOkCancelButtons();

            return dialogWithButtons.showDialog() == DialogWithButtons.OK_OPTION;
        }

        public FileChooser getFileChooser() {
            return fileChooser;
        }
    }

}
