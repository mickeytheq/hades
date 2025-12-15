# Introduction

Hades is a plugin for [Strange Eons](https://cgjennings.ca/eons/). It allows users to create custom content for Arkham Horror: LCG.

Hades is inspired by and uses some elements of the [AHLCG plugin](https://github.com/tokeeto/StrangeEonsAHLCG/) created by Pilute23/JaqenZann and maintained by tokeeto. Initially, it is intended to be a re-implementation of the existing plugin with some key changes.

I am also the author of [Zoop](https://mickeytheq.github.io/ZoopDocs/) a plugin built on top of the existing AHLCG plugin.

See the [features](#features) for more information.

# Getting started

Firstly install the Hades plugin

1. Firstly download the Hades plugin file [here](https://www.dropbox.com/scl/fi/ukumf5tgtyfxuecugrfck/hades-0.1.seext?rlkey=s9lbp0rn5rm1my4c9t51fw6jb&st=ruzs25mq&dl=0).
2. Find the Strange Eons plugin folder. If you don't already know where this is you can open Strange Eons, go to the `Toolbox` menu, select `Manage Plug-ins` and click `Open Plug-in Folder`
3. Copy the Hades plugin file into the plugin folder
4. Restart Strange Eons
5. At this point Hades may prompt you to install the Arno Pro font files. Follow the instructions in [font setup](ui/fontsetup.md)
6. To confirm setup is complete either create a new project or open an existing project and r-click on any directory, project or file and you should see the `Hades` option in the context menu

## Creating a new card

Most Hades functionality is accessed via the r-click menu under `Hades`. To create a new card do r-click `Hades -> New card`. Hades cards are not created via the normal Strange Eons `New Game Component` option as that mechanism is not compatible with the flexibility Hades offers on creating cards. See [card faces](feature/cardfaces.md) for more information.

## Migrating existing AHLCG projects

Hades can migrate projects in from the existing plugin format. See [migration](migration/ahlcgplugin.md) for more details.

# Features

Hades is intended to have feature parity with the existing AHLCG plugin and feel familiar to that plugin. However it aims to improve in the following ways

- Performance - Hades performance is approximately 5-10 times faster than the existing AHLCG plugin. Ancedotally it is much snappier when opening/closing/editing/exporting files. See [performance](feature/performance.md) for more details
- High-resolution templates - Produces high quality images
- Card combinations - Hades supports (almost) any combination of card front and backs of the available [card faces](feature/cardfaces.md) instead of fixed pairing of front and back faces. See [creating a new card](#creating-a-new-card)
- Arno Pro enforced - The preferred font for AHLCG cards is Arno Pro which is required. See [font setup](ui/fontsetup.md)
- Project settings - A centralised place to specify [project configuration](ui/projectconfiguration.md) such as encounter sets and collections instead of the current options of global and per-card
- Comprehensive card types - The card types have been simplified. For example Story Assets are just an Asset. More information on [card faces](feature/cardfaces.md)
- Migration from the existing AHLCG plugin - Easy [migration](migration/ahlcgplugin.md) of projects built using the existing plugin to Hades
- Supported by Zoop - (Not started yet) Supporting the existing key functions of Zoop such as TTS export, print and play and arkham.build with Hades created content

# Status

**Hades is a work in progress so this manual currently represents a set of planned features**

Hades is **not** ready for real-world use yet. Feel free to try it out by creating new cards, migrating existing projects however please bear in mind the following

- There will be issues. If you find any - whether it is crashes, rendering issues, usability concerns or anything else - please [leave feedback](feedback.md)
- For now, do not expect any content to survive to the next version of Hades. When a feature set is declared ready I will make that clear. From that point upgrading to a new version of Hades will be safe

## Objectives

The current plan is below

### 1.0

- Deliver all player cards - Investigator, Asset, Event, Skill, maybe Treachery and Enemy to support signatures/weaknesses - **in progress**
- Hi-res templates - **in progress**
- Arno Pro enforced - **complete**
- Migration from the AHLCG plugin of the above - **in progress**
- Within the scope above, feature parity/compatibility with AHLCG plugin - **in progress**
- Fix any critical bugs, crashes
- Finalise key technical decisions such as storage formats of content (anything that will be expensive to change later)

### Next

In some order TBD. These are musings rather than a firm roadmap

- Deliver the rest of the card types in some order
- Usability - user feedback on making it more pleasent to use
- Improvements - add more high value options, e.g. more sophisticated layering of card art - based on user feedback
- Port some Zoop utilities into the plugin, e.g. collection numbering
- Performance - continue to look for performance gains

# Thanks to

- Tokeeto for maintaining the AHLCG plugin
- Pilute23/JaqenZann for creating the AHLCG plugin
- Chris Jennings for creating Strange Eons
- Fantasy Flight for creating Arkham Horror: LCG
