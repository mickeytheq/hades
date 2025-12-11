# Introduction

Hades is a plugin for [Strange Eons](https://cgjennings.ca/eons/). It allows users to create custom content for Arkham Horror: LCG.

Hades is inspired by and uses some elements of the [AHLCG plugin](https://github.com/tokeeto/StrangeEonsAHLCG/) created by Pilute23/JaqenZann and maintained by tokeeto. Initially, it is intended to be a re-implementation existing plugin with some key changes.

I am also the author of [Zoop](https://mickeytheq.github.io/ZoopDocs/) a plugin built on top of the existing AHLCG plugin.

# Features

Features in _italic_ are either not yet available or partially complete. Click through the respective links for more information and see Status below

- Performance - Hades performance is approximately 5-10 times faster than the existing AHLCG plugin. Ancedotally it is much snappier when opening/closing/editing/exporting files. See [performance](feature/performance.md) for more details
- High-resolution templates - Produces high quality images
- Card combinations - Hades supports (almost) any combination of card front and backs of the available [card faces](feature/cardfaces.md) instead of fixed pairing of front and back faces. See [creating a new card](ui/newcard.md)
- Arno Pro enforced - The preferred font for AHLCG cards is Arno Pro which is required. See [font setup](ui/fontsetup.md)
- _Project settings_ - A centralised place to specify [project configuration](ui/projectconfiguration.md) such as encounter sets and collections instead of the current options of global and per-card
- _Comprehensive card types_ - The card types have been simplified. For example Story Assets are just an Asset. More information on [card faces](feature/cardfaces.md)
- _Migration from the existing AHLCG plugin_ - Easy [migration](migration/ahlcgplugin.md) of projects built using the existing plugin to Hades
- _Feature parity with the existing AHLCG plugin_ - This will naturally be delivered as support for all card types and providing migration logic
- _Supported by Zoop_ - (Typically a future feature of Zoop) Supporting the existing key functions of Zoop such as TTS export, print and play and arkham.build with Hades created content

# Status

**Hades is a work in progress so this manual currently represents a set of planned features**

Hades is **not** ready for real-world use yet. Feel free to try it out by creating new cards, migrating existing projects however please bear in mind the following

- There will be issues. If you find any - whether it is crashes, rendering issues, usability concerns or anything else - please [leave feedback](feedback.md)
- For now, do not expect any content to survive to the next version of Hades. When a feature set is declared ready I will make that clear. From that point upgrading to a new version of Hades will be safe

## Objectives

The current plan is

### 1.0

- Deliver all player cards - Investigator, Asset, Event, Skill, maybe Treachery and Enemy to support signatures/weaknesses - **in progress**
- Hi-res templates - **in progress**
- Arno Pro enforced - **complete**
- Migration from the AHLCG plugin of the above - **in progress**
- Within the scope above, feature parity/compatibility with AHLCG plugin - **in progress**
- Fix any critical bugs, crashes
- Finalise key technical decisions such as storage formats of content (anything that will be expensive to change later)

### Next

In some order TBD

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
