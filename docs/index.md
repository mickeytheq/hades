# Introduction

Hades is a plugin for [Strange Eons](https://cgjennings.ca/eons/). It allows users to create custom content for Arkham Horror: LCG.

Hades is inspired by and uses some elements of the [AHLCG plugin](https://github.com/tokeeto/StrangeEonsAHLCG/) created by Pilute23/JaqenZann and maintained by tokeeto. It is intended to be very familiar to users of the existing plugin with some key changes.

I decided to explore this project due to some frustrations with the existing AHLCG plugin and having too much free time!

I am also the author of [Zoop](https://mickeytheq.github.io/ZoopDocs/) a plugin built on top of the existing AHLCG plugin.

# Features

- Performance - Hades performance is approximately 5-10 times faster than the existing AHLCG plugin. See [performance](feature/performance.md) for more details
- Card combinations - Hades supports (almost) any combination of card front and backs of the available [card faces](feature/cardfaces.md). See [creating a new card](ui/newcard.md)
- Arno Pro enforced - The preferred font for AHLCG cards is Arno Pro which is required. See [font setup](ui/fontsetup.md)
- Simplified card types - The card types have been simplified. For example Story Assets are just an Asset. More information on [card faces](feature/cardfaces.md)
- Project settings - A centralised place to specify (project configuration)[ui/projectconfiguration.md] such as encounter sets and collections instead of the current options of global and per-card
- Migration from the existing AHLCG plugin - Easy [migration](migration/ahlcgplugin.md) of projects built using the existing plugin to Hades
- Supported by Zoop - Allowing the existing key functions of Zoop such as TTS export, print and play and arkham.build

# Thanks to

- Tokeeto for maintaining the AHLCG plugin
- Pilute23/JaqenZann for creating the AHLCG plugin
- Chris Jennings for creating Strange Eons
- Fantasy Flight for creating Arkham Horror: LCG
